import java.util.StringTokenizer;

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Comanda rmdir
 */
public class Rmdir implements ICommand {
	private FileSystem fileSystem;
	private String path;

	public Rmdir(FileSystem fileSystem, StringTokenizer path) {
		this.fileSystem = fileSystem;
		this.path = path.nextToken();

	}

	@Override
	public int execute() {
		String command = "rmdir " + path;
		UserSystem userSys = fileSystem.getUserSystem();
		String dirToDel = "";

		Directory pathToDelFrom;
		if (!path.contains("/")) { // daca se intampla in folderu curent
			pathToDelFrom = fileSystem.getCurrentDirectory();
			dirToDel = path;
		} else {// trebuie sa ajungem la folderu' respectiv
			int index = path.lastIndexOf("/") + 1;
			if (index != 1) {
				dirToDel = path.substring(index);
				path = path.substring(0, index);

				pathToDelFrom = fileSystem.getToPath(path,
						userSys.getActiveUser(), command);
			} else {
				path = path.substring(1, path.length());
				pathToDelFrom = fileSystem.getCurrentDirectory();
				dirToDel = path;
			}
		}
		if (pathToDelFrom == null) {// daca nu e buna calea
			return -100;
		}

		if (path.equals("/")) {// nope,DENIED

			ErrorHandler.reportError(-13, command);
			return -100;
		}
		if (path.equals(".")) {// nope,DENIED

			ErrorHandler.reportError(-13, command);
			return -100;
		}
		if (path.equals("..")) {// nope,DENIED

			ErrorHandler.reportError(-13, command);
			return -100;
		}
		if (path.equals("/../")) {// nope,DENIED
			ErrorHandler.reportError(-13, command);
			return -100;
		}
		if (path.equals("/./")) {// nope,DENIED
			ErrorHandler.reportError(-13, command);
			return -100;
		}
		boolean check = Check(pathToDelFrom, dirToDel, command);

		if (!check) {
			return -100;
		} else {// exista si avem permisiuni
			IFile file = pathToDelFrom.findFileInCurrentDirectory(dirToDel);
			if (!((Directory) file).isEmpty()) {// directoru nu e gol
				return -100;
			}
			((Directory) file).getParent().remove(dirToDel); // stergem folderu
		}
		return 0;
	}

	boolean Check(Directory current, String toPutIn, String command) {
		IFile check = current.findFileInCurrentDirectory(toPutIn);
		if (check == null) {
			ErrorHandler.reportError(-2, command); // nu exista directoru
			return false;
		}

		User currrentUser = fileSystem.getUserSystem().getActiveUser();
		// daca nu e root poate sa n-aibe drepturi
		if (!currrentUser.getName().equals("root")) {
			// daca e owner si bou
			if (current.getOwner().getName().equals(currrentUser.getName())) {
				// daca n-are drept de Write si e OWNER
				if (!((FileEntity) check).ownerPermissions.getWriteStatus()) {
					ErrorHandler.reportError(-5, command);
					return false;
				}
			}
			// daca nu e owner
			if (!current.getOwner().getName().equals(currrentUser.getName())) {
				// daca n-are drept de Write si nu e OWNER
				if (!((FileEntity) check).othersPermissions.getWriteStatus()) {
					ErrorHandler.reportError(-5, command);
					return false;
				}
			}
		}

		if (check.getType().equals("f")) { // e fisier
			ErrorHandler.reportError(-3, command);
			return false;
		}
		if (!((Directory) check).isEmpty()) { // nu e gol
			ErrorHandler.reportError(-14, command);
			return false;
		}
		if (fileSystem.getCurrentDirectory().isAncestorInDanger(toPutIn)) {
			ErrorHandler.reportError(-13, command);
			return false;
		}

		return true;
	}

}
