import java.util.StringTokenizer;

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Comanda touch
 */
public class Touch implements ICommand {

	private FileSystem fileSystem;
	private String path;

	public Touch(FileSystem fileSystem, StringTokenizer path) {
		this.fileSystem = fileSystem;
		this.path = path.nextToken();

	}

	@Override
	public int execute() {
		UserSystem userSys = fileSystem.getUserSystem();
		String command = "touch " + path;
		String fileToCreate = "";
		Directory pathToPutIn;

		if (!path.contains("/")) {// trebuie facut in directoru curent
			fileToCreate = path;
			pathToPutIn = fileSystem.getCurrentDirectory();
		} else {
			int index = path.lastIndexOf("/") + 1;
			fileToCreate = path.substring(index);
			path = path.substring(0, index);
			pathToPutIn = fileSystem.getToPath(path, userSys.getActiveUser(),
					command);
		}

		if (pathToPutIn == null) {// daca nu e buna calea
			return -100;
		}
		boolean check = Check(pathToPutIn, fileToCreate, command);
		if (!check) {
			return -100;
		} else {// exista si avem permisiuni
				// cream si adaugam noul fisier
			File newFile = new File(fileToCreate);
			newFile.setOwner(userSys.getActiveUser());
			newFile.setParent(pathToPutIn);
			pathToPutIn.addFile(newFile);

		}

		return 0;

	}

	boolean Check(Directory current, String toPutIn, String command) {
		IFile check = current.findFileInCurrentDirectory(toPutIn);

		// daca fisierul nu exista
		User currrentUser = fileSystem.getUserSystem().getActiveUser();
		// daca nu e root poate sa n-aibe drepturi
		if (!currrentUser.getName().equals("root")) {
			// daca e owner si bou
			if (current.getOwner().getName().equals(currrentUser.getName())) {
				// daca n-are drept de Write si e OWNER
				if (!current.ownerPermissions.getWriteStatus()) {
					ErrorHandler.reportError(-5, command);
					return false;
				}
			}
			// daca nu e owner
			if (!current.getOwner().getName().equals(currrentUser.getName())) {
				// daca n-are drept de Write si nu e OWNER
				if (!current.othersPermissions.getWriteStatus()) {
					ErrorHandler.reportError(-5, command);
					return false;
				}
			}
		}
		if (check != null) {// daca fisierul exista
			if (check.getType().equals("d")) {
				ErrorHandler.reportError(-1, command);
				return false;
			}
			if (check.getType().equals("f")) {
				ErrorHandler.reportError(-7, command);
				return false;
			}
		}

		return true;
	}

}
