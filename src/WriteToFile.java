import java.util.StringTokenizer;

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Comanda writetofile
 */
public class WriteToFile implements ICommand {

	private FileSystem fileSystem;
	private String path;
	private String content;

	public WriteToFile(FileSystem fileSystem, StringTokenizer path) {
		this.fileSystem = fileSystem;
		this.path = path.nextToken();
		content = "";
		while (path.countTokens() > 0) {
			content += " " + path.nextToken();
		}

	}

	@Override
	public int execute() {
		String command = "writetofile " + path + content;
		UserSystem userSys = fileSystem.getUserSystem();
		String fileToDel = "";
		Directory fileToWriteIt;

		if (!path.contains("/")) { // daca se intampla in folderu curent
			fileToWriteIt = fileSystem.getCurrentDirectory();
			fileToDel = path;
		} else {// trebuie sa ajungem la folderu' respectiv
			int index = path.lastIndexOf("/") + 1;
			fileToDel = path.substring(index);
			path = path.substring(0, index);

			fileToWriteIt = fileSystem.getToPath(path, userSys.getActiveUser(),
					command);
		}
		if (fileToWriteIt == null) {// daca nu e buna calea
			return -100;
		}

		boolean check = Check(fileToWriteIt, fileToDel, command);
		if (!check) {
			return -100;
		} else {// exista fisierul in care sa scriem
			// scriem in el
			IFile file = fileToWriteIt.findFileInCurrentDirectory(fileToDel);
			((File) file).setContent(content);
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
