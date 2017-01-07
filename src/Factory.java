import java.util.StringTokenizer;

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Factory Pattern folosind in creeare de obiecte noi pentru comenzi
 */
public final class Factory implements IFactory {

	@Override
	public ICommand createCommand(FileSystem fileSystem, StringTokenizer arg,
			typeOfCommand type) {

		switch (type) {

		case adduser:
			return new AddUser(fileSystem, arg);
		case deluser:
			return new DelUser(fileSystem, arg);
		case chuser:
			return new ChUser(fileSystem, arg);
		case cd:
			return new Cd(fileSystem, arg);
		case mkdir:
			return new Mkdir(fileSystem, arg);
		case ls:
			return new Ls(fileSystem, arg);
		case chmod:
			return new Chmod(fileSystem, arg);
		case touch:
			return new Touch(fileSystem, arg);
		case rm:
			return new Rm(fileSystem, arg);
		case rmdir:
			return new Rmdir(fileSystem, arg);
		case writetofile:
			return new WriteToFile(fileSystem, arg);
		case cat:
			return new Cat(fileSystem, arg);

		}
		return null;
	}

}
