import java.util.StringTokenizer;

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Comanda cd
 */
public class Cd implements ICommand {

	private String path;
	private FileSystem fileSystem;

	public Cd(FileSystem fileSystem, StringTokenizer path) {

		this.path = path.nextToken();
		this.fileSystem = fileSystem;

	}

	@Override
	public int execute() {

		UserSystem userSys = fileSystem.getUserSystem();
		// incercam sa scoatem directorul unde vrem sa ajungem
		Directory newCurrentDirectory = fileSystem.getToPath(path,
				userSys.getActiveUser(), "cd " + path);
		// daca directorul exista,schimbam calea curenta
		if (newCurrentDirectory != null) {
			fileSystem.setCurrentDirectory(newCurrentDirectory);
		}
		return 0;
	}

}
