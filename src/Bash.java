import java.util.StringTokenizer;

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Clasa "interfata" pentru comenzile ce se bazeaza pe
 *         API(FileSystem+UserSystem)
 */
public class Bash extends AbstractBash {

	public Bash(IOHandler handler) {
		super();
		this.handler = handler;
		commandParser();
	}

	/*
	 * @author Ionut-Cristian Bucur, 323CA
	 * 
	 * Clasa "interfata" pentru comenzile ce se bazeaza pe
	 * API(FileSystem+UserSystem)
	 */
	private void commandParser() {

		String currentLine;
		currentLine = handler.getNextLine();
		// sarim peste mkdir / intrucat ne-am ocupat de el in initializarea
		// API-ului
		currentLine = handler.getNextLine();

		while (currentLine != null) {
			StringTokenizer linieComanda = new StringTokenizer(currentLine,
					" ");

			String command = linieComanda.nextToken();

			switch (command) {

			case "adduser":
				addTask(fileSystem, linieComanda, typeOfCommand.adduser);
				break;
			case "deluser":
				addTask(fileSystem, linieComanda, typeOfCommand.deluser);
				break;
			case "chuser":
				addTask(fileSystem, linieComanda, typeOfCommand.chuser);
				break;
			case "cd":
				addTask(fileSystem, linieComanda, typeOfCommand.cd);
				break;
			case "mkdir":
				addTask(fileSystem, linieComanda, typeOfCommand.mkdir);
				break;
			case "ls":
				addTask(fileSystem, linieComanda, typeOfCommand.ls);
				break;
			case "chmod":
				addTask(fileSystem, linieComanda, typeOfCommand.chmod);
				break;
			case "touch":
				addTask(fileSystem, linieComanda, typeOfCommand.touch);
				break;
			case "rm":
				addTask(fileSystem, linieComanda, typeOfCommand.rm);
				break;
			case "rmdir":
				addTask(fileSystem, linieComanda, typeOfCommand.rmdir);
				break;
			case "writetofile":
				addTask(fileSystem, linieComanda, typeOfCommand.writetofile);
				break;
			case "cat":
				addTask(fileSystem, linieComanda, typeOfCommand.cat);
				break;
			}

			currentLine = handler.getNextLine();
		}

	}

}
