import java.util.StringTokenizer;

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Comanda deluser
 */
public class DelUser implements ICommand {

	private String userToDel;
	private FileSystem fileSystem;

	public DelUser(FileSystem fileSystem, StringTokenizer userToDel) {

		this.userToDel = userToDel.nextToken();
		this.fileSystem = fileSystem;

	}

	@Override
	public int execute() {
		UserSystem userSys = fileSystem.getUserSystem();
		// daca nu e root
		if (!userSys.getActiveUser().getName().equals("root")) {

			ErrorHandler.reportError(-10, "deluser " + userToDel);
			return -10;
		}
		// daca e root si nu s-a gasit userul
		if (userSys.findUserIndex(userToDel) == -1) {

			ErrorHandler.reportError(-8, "deluser " + userToDel);
			return -8;
		}

		userSys.removeUser(userToDel);
		fileSystem.removeUserTrace(userToDel);

		return 0;
	}

}
