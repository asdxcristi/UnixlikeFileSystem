import java.util.StringTokenizer;

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Interfata folosita in gestionarea scheletului Factory-ul
 */
public interface IFactory {

	public ICommand createCommand(FileSystem fileSystem, StringTokenizer path,
			typeOfCommand type);
}
