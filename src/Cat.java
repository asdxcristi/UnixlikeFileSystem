import java.util.StringTokenizer;

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Comanda cat
 */
public class Cat implements ICommand {

	private FileSystem fileSystem;
	private String path;

	public Cat(FileSystem fileSystem, StringTokenizer path) {
		this.fileSystem = fileSystem;
		this.path = path.nextToken();
	}

	@Override
	public int execute() {
		String command = "cat " + path + " ";
		UserSystem userSys = fileSystem.getUserSystem();
		String fileToDel = "";
		Directory pathToCatFrom;

		if (!path.contains("/")) { // daca se intampla in folderu curent
			pathToCatFrom = fileSystem.getCurrentDirectory();
			fileToDel = path;
		} else {// trebuie sa ajungem la folderu' respectiv
			int index = path.lastIndexOf("/") + 1;
			fileToDel = path.substring(index);
			path = path.substring(0, index);

			pathToCatFrom = fileSystem.getToPath(path, userSys.getActiveUser(),
					command); // incercam sa scoatem calea de unde sa afisam
		}
		if (pathToCatFrom == null) {// daca nu e buna calea
			return -100;
		}

		// verificam si restul conditiilor specifice
		boolean check = Check(pathToCatFrom, fileToDel, command);
		if (!check) {
			return -100;
		} else {
			// scoatem o refereinta la fisierul cautat
			IFile file = pathToCatFrom.findFileInCurrentDirectory(fileToDel);
			// scoatem si afisam contentul
			String content = ((File) file).getContent();
			System.out.println(content);
			return 0;
		}
	}

	boolean Check(Directory current, String toPutIn, String command) {
		IFile check = current.findFileInCurrentDirectory(toPutIn);
		if (check != null) {
			if (check.getType().equals("d")) { // exista si e director
				ErrorHandler.reportError(-1, command);
				return false;
			}

		} else { // nu exista fisier acolo
			ErrorHandler.reportError(-11, command);
			return false;
		}

		User currrentUser = fileSystem.getUserSystem().getActiveUser();
		// daca nu e root poate sa n-aibe drepturi
		if (!currrentUser.getName().equals("root")) {
			// daca e owner si bou
			if (((FileEntity) check).getOwner().getName()
					.equals(currrentUser.getName())) {
				// daca n-are drept de Write si e OWNER
				if (!((FileEntity) check).ownerPermissions.getReadStatus()) {
					ErrorHandler.reportError(-4, command);
					return false;
				}
			}
			// daca nu e owner
			if (!((FileEntity) check).getOwner().getName()
					.equals(currrentUser.getName())) {
				// daca n-are drept de Write si nu e OWNER
				if (!((FileEntity) check).othersPermissions.getReadStatus()) {
					ErrorHandler.reportError(-4, command);
					return false;
				}
			}
		}

		return true;
	}
}
