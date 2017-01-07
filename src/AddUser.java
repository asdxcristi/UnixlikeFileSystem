import java.util.StringTokenizer;

public class AddUser implements ICommand {

	private String userName;
	private FileSystem fileSystem;

	public AddUser(FileSystem fileSystem, StringTokenizer userName) {

		this.userName = userName.nextToken();
		this.fileSystem = fileSystem;

	}

	@Override
	public int execute() {
		UserSystem userSys = fileSystem.getUserSystem();

		if (!userSys.getActiveUser().getName().equals("root")) {
			// daca nu e root
			ErrorHandler.reportError(-10, "adduser " + userName);
		} else {

			int ok = userSys.addUser(userName); // incercam sa adaugam userul

			if (ok == -9) { //
				ErrorHandler.reportError(-9, "adduser " + userName);
			} else {// ii facem si setam si homedirectory-ul daca am reusit sa-l
					// adaugam
				Directory homeDir = fileSystem.addHomeDirectoryToRoot(userName,
						userSys.getUser(userName));

				userSys.getUser(userName).setHomeDirectoryPath(homeDir);
			}

		}
		return 0;
	}

}
