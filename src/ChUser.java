import java.util.StringTokenizer;

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Comana chuser
 */
public class ChUser implements ICommand {

	private String userName;
	private FileSystem fileSystem;

	public ChUser(FileSystem fileSystem, StringTokenizer userName) {

		this.userName = userName.nextToken();
		this.fileSystem = fileSystem;

	}

	@Override
	public int execute() {

		UserSystem userSys = fileSystem.getUserSystem();
		// incercam sa schimbam userul activ
		int ok = userSys.setActiveUser(userName);

		if (ok == -8) { // nu exista userul
			ErrorHandler.reportError(-8, "chuser " + userName);
			return -8;

		} else { // exista userul
			// schimbam pathul curent la homedirectoryul userului
			fileSystem.setCurrentDirectory(
					userSys.getActiveUser().getHomeDirectoryPath());
		}

		return 0;
	}

}
