import java.util.StringTokenizer;

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Comanda mkdir
 */
public class Mkdir implements ICommand {

	private FileSystem fileSystem;
	private String path;

	public Mkdir(FileSystem fileSystem, StringTokenizer path) {
		this.fileSystem = fileSystem;
		this.path = path.nextToken();

	}

	@Override
	public int execute() {
		String backPath = path;
		UserSystem userSys = fileSystem.getUserSystem();
		String command = "mkdir " + backPath;
		String folderToCreate;
		Directory pathToPutIn;

		if (backPath.equals("/")) { // deja am initializat
			ErrorHandler.reportError(-1, command);
			return 0;
		}

		if (!path.contains("/")) {// trebuie facut in directoru curent
			folderToCreate = path;
			pathToPutIn = fileSystem.getCurrentDirectory();
		} else { // e cale absoluta
			int index = path.lastIndexOf("/") + 1;
			if (index == path.length()) {
				path = path.substring(0, path.length() - 1);
				index = path.lastIndexOf("/") + 1;
			}
			folderToCreate = path.substring(index);
			path = path.substring(0, index);
			// incercam sa scoatem calea la care vrem sa facem noul folder
			pathToPutIn = fileSystem.getToPath(path, userSys.getActiveUser(),
					command);
		}

		if (pathToPutIn == null) {
			return -100;
		}
		boolean check = Check(pathToPutIn, folderToCreate, command);

		if (!check) {
			return -100;
		} else {// e ok si punem folderu in cale pathToPutIn
			// pregatim folderu' si il adaugam
			Directory newDir = new Directory(folderToCreate);
			newDir.setOwner(userSys.getActiveUser());
			newDir.setParent(pathToPutIn);
			pathToPutIn.addFile(newDir);

			return 0;
		}

	}

	boolean Check(Directory current, String toPutIn, String command) {
		IFile check = current.findFileInCurrentDirectory(toPutIn);

		User currrentUser = fileSystem.getUserSystem().getActiveUser();

		// daca nu e root poate sa nu aiba drepturi
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
		if (check == null) {// daca nu exista deja un fisier cu numele asta
			return true;
		}
		if (check.getType().equals("d")) {
			ErrorHandler.reportError(-1, command);
			return false;
		}
		if (check.getType().equals("f")) {
			ErrorHandler.reportError(-3, command);
			return false;
		}

		return true;
	}

}
