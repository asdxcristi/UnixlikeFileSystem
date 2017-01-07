import java.util.StringTokenizer;

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Comanda ls
 */
public class Ls implements ICommand {

	private String path;
	private FileSystem fileSystem;

	public Ls(FileSystem fileSystem, StringTokenizer path) {

		this.path = path.nextToken();
		this.fileSystem = fileSystem;

	}

	@Override
	public int execute() {
		UserSystem userSys = fileSystem.getUserSystem();
		String command = "ls " + path;
		String fileToList = "";
		Directory pathToListFrom = null;

		if (!path.contains("/")) {// trebuie facut in directoru curent

			if (path.equals(".")) {// daca listam directorul curent
				Directory curDir = fileSystem.getCurrentDirectory();
				for (IFile it : curDir.getChildFiles()) {
					it.print();
				}
				return 0;
			}
		}

		// nu e in folderu curent
		if (path.equals("/")) {// daca trebuie sa listam din radacina
			Directory curDir = fileSystem.tree;
			for (IFile it : curDir.getChildFiles()) {
				it.print();
			}
			return 0;
		}
		if (!path.contains("/")) {// trebuie facut in directoru curent
			fileToList = path;
			IFile toBeFound = fileSystem.getCurrentDirectory()
					.findFileInCurrentDirectory(fileToList);

			if (toBeFound == null) {
				ErrorHandler.reportError(-12, command);
				return -100;
			}
			toBeFound.print();
			return 0;
		} else {// are / in el

			int index = path.lastIndexOf("/") + 1;

			if (index == path.length()) {
				path = path.substring(0, path.length() - 1);
				index = path.lastIndexOf("/") + 1;

			}
			fileToList = path.substring(index);

			path = path.substring(0, index);

			pathToListFrom = fileSystem.getToPath(path, userSys.getActiveUser(),
					command);
		}

		if (pathToListFrom == null) {
			return -100;
		}

		boolean check = Check(pathToListFrom, fileToList, command);

		if (!check) {
			return -100;
		} else {// e totu ok avem permisiuni
			IFile checkS = pathToListFrom
					.findFileInCurrentDirectory(fileToList);
			// daca este fisier il afisam singurel
			if (checkS.getType().equals("f")) {
				checkS.print();
				return 0;
			}
			// daca e folder ii afisam toti copchii
			for (IFile it : ((Directory) checkS).getChildFiles()) {
				it.print();
			}
			return 0;
		}

	}

	boolean Check(Directory pathToListFrom, String fileToList, String command) {
		IFile check = pathToListFrom.findFileInCurrentDirectory(fileToList);
		if (check == null) {
			ErrorHandler.reportError(-12, command);
			return false;
		}
		User currrentUser = fileSystem.getUserSystem().getActiveUser();

		// daca nu e root poate sa n-aibe drepturi
		if (!currrentUser.getName().equals("root")) {
			// daca e owner si bou
			if (check.getOwner().getName().equals(currrentUser.getName())) {
				// daca n-are drept de Write si e OWNER
				if (!((FileEntity) check).ownerPermissions.getExecuteStatus()) {
					ErrorHandler.reportError(-6, command);
					return false;
				}
			}
			// daca nu e owner
			if (!check.getOwner().getName().equals(currrentUser.getName())) {
				// daca n-are drept de Write si nu e OWNER
				if (!((FileEntity) check).othersPermissions
						.getExecuteStatus()) {
					ErrorHandler.reportError(-6, command);
					return false;
				}
			}
		}
		// daca nu e root poate sa n-aibe drepturi
		if (!currrentUser.getName().equals("root")) {
			// daca e owner si bou
			if (check.getOwner().getName().equals(currrentUser.getName())) {
				// daca n-are drept de Write si e OWNER
				if (!((FileEntity) check).ownerPermissions.getReadStatus()) {
					ErrorHandler.reportError(-4, command);
					return false;
				}
			}
			// daca nu e owner
			if (!check.getOwner().getName().equals(currrentUser.getName())) {
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
