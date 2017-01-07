import java.util.StringTokenizer;

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Comanda chmod
 */
public class Chmod implements ICommand {

	private FileSystem fileSystem;
	private String permissions;
	private String path;

	public Chmod(FileSystem fileSystem, StringTokenizer path) {
		this.fileSystem = fileSystem;
		this.permissions = path.nextToken();
		this.path = path.nextToken();

	}

	@Override
	public int execute() {
		String command = "chmod " + permissions + " " + path;
		UserSystem userSys = fileSystem.getUserSystem();
		String fileToChange = "";

		Directory pathToPutIn;
		if (!path.contains("/")) { // daca se intampla in folderu curent
			pathToPutIn = fileSystem.getCurrentDirectory();
			fileToChange = path;
		} else {// trebuie sa ajungem la folderu' respectiv
			int index = path.lastIndexOf("/") + 1;
			// verificam daca pathul se termina cu "/"
			if (index == path.length()) {
				// stergem / din final
				path = path.substring(0, path.length() - 1);
				index = path.lastIndexOf("/") + 1;
			}
			// scoatem numele fisierului din finalul pathului
			fileToChange = path.substring(index);
			path = path.substring(0, index);

			// incercam sa ajungem la calea unde sa introducem fisierul
			pathToPutIn = fileSystem.getToPath(path, userSys.getActiveUser(),
					command);
		}
		if (pathToPutIn == null) {// daca nu e buna calea
			return -100;
		}
		// daca exista si respecta si permisiunile caracteristici
		boolean check = Check(pathToPutIn, fileToChange, command);

		if (!check) {
			return -100;
		} else {// exista si avem permisiuni
			IFile file = pathToPutIn.findFileInCurrentDirectory(fileToChange);

			// schimbam permisiunile fisierului
			((FileEntity) file).ownerPermissions
					.setAll(permissions.substring(0, 1));
			((FileEntity) file).othersPermissions
					.setAll(permissions.substring(1, 2));
		}
		return 0;
	}

	private boolean Check(Directory current, String toPutIn, String command) {
		IFile check = current.findFileInCurrentDirectory(toPutIn);
		if (check == null) {// daca nu exista fisierul
			ErrorHandler.reportError(-12, command);
			return false;
		}

		User currrentUser = fileSystem.getUserSystem().getActiveUser();
		// daca nu e root poate sa n-aibe drepturi
		if (!currrentUser.getName().equals("root")) {
			// daca e owner si bou
			if (((FileEntity) check).getOwner().getName()
					.equals(currrentUser.getName())) {
				// daca n-are drept de Write si e OWNER
				if (!((FileEntity) check).ownerPermissions.getWriteStatus()) {
					ErrorHandler.reportError(-5, command);
					return false;
				}
			}
			// daca nu e owner
			if (!((FileEntity) check).getOwner().getName()
					.equals(currrentUser.getName())) {
				// daca n-are drept de Write si nu e OWNER
				if (!((FileEntity) check).othersPermissions.getWriteStatus()) {
					ErrorHandler.reportError(-5, command);
					return false;
				}
			}
		}
		return true;
	}

}
