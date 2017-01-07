import java.util.StringTokenizer;

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Comanda rm(rm -r)
 */
public class Rm implements ICommand {
	private FileSystem fileSystem;
	private StringTokenizer pathToFileToDel;
	boolean recursive;

	public Rm(FileSystem fileSystem, StringTokenizer path) {
		this.fileSystem = fileSystem;
		this.pathToFileToDel = path;

	}

	@Override
	public int execute() {
		UserSystem userSys = fileSystem.getUserSystem();
		String path = "";
		String backPath;
		String fileToDel = "";
		String command;
		Directory pathToDelFrom;

		// verificam daca e rm sau rm-r si setam pathul corespunzator
		if (pathToFileToDel.countTokens() == 1) {
			path = pathToFileToDel.nextToken();
			backPath = path;
			command = "rm " + path;
			recursive = false;
		} else {
			pathToFileToDel.nextToken();
			path = pathToFileToDel.nextToken();
			backPath = path;
			command = "rm -r " + path;
			recursive = true;
		}

		if (!path.contains("/")) { // daca se intampla in folderu curent
			pathToDelFrom = fileSystem.getCurrentDirectory();
			fileToDel = path;
		} else {// trebuie sa ajungem la folderu' respectiv
			int index = path.lastIndexOf("/") + 1;
			fileToDel = path.substring(index);
			path = path.substring(0, index);

			pathToDelFrom = fileSystem.getToPath(path, userSys.getActiveUser(),
					command);
		}
		if (pathToDelFrom == null) {// daca nu e buna calea
			return -100;
		}

		IFile file = pathToDelFrom.findFileInCurrentDirectory(fileToDel);
		if (!recursive) {// directoru nu e gol
			boolean check = Check(pathToDelFrom, fileToDel, command);
			if (!check) {
				return -100;
			} else {
				file.getParent().remove(fileToDel);
				return 0;
			}
		}
		if (recursive) {// rm -r
			if (backPath.equals("/")) {// nope,DENIED
				ErrorHandler.reportError(-13, command);
				return -100;
			}
			if (backPath.equals(".")) {// nope,DENIED
				ErrorHandler.reportError(-13, command);
				return -100;
			}
			if (backPath.equals("..")) {// nope,DENIED
				ErrorHandler.reportError(-13, command);
				return -100;
			}
			boolean check = CheckRec(pathToDelFrom, fileToDel, command);
			if (!check) {
				return -100;
			} else {
				if (!file.getName().equals("/")) {
					// stergem folderu
					((IFile) file).getParent().remove(fileToDel);
					return 0;
				} else {// e la root si root n-are parinte
					fileSystem.tree.remove(fileToDel);
				}
			}
		}

		return 0;
	}

	boolean Check(Directory current, String toPutIn, String command) {
		IFile check = current.findFileInCurrentDirectory(toPutIn);

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
		if (check != null) {
			if (check.getType().equals("d")) { // exista si e director
				ErrorHandler.reportError(-1, command);
				return false;
			}

		} else { // nu exista fisier acolo
			ErrorHandler.reportError(-11, command);
			return false;
		}

		return true;
	}

	boolean CheckRec(Directory current, String toPutIn, String command) {
		IFile check = current.findFileInCurrentDirectory(toPutIn);

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
		if (check == null) {
			ErrorHandler.reportError(-12, command);
			return false;
		}
		if (fileSystem.getCurrentDirectory().isAncestorInDanger(toPutIn)) {
			ErrorHandler.reportError(-13, command);
			return false;
		}

		return true;
	}
}
