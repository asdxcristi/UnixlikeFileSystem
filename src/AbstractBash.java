import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Da o gestionare mai usoara a clasei Bash
 */
public abstract class AbstractBash {

	protected IOHandler handler;
	protected Factory factory;

	protected FileSystem fileSystem;
	protected UserSystem userSystem;

	ArrayList<ICommand> container;

	public AbstractBash() {
		factory = new Factory();
		userSystem = new UserSystem();
		fileSystem = new FileSystem(userSystem);
		container = new ArrayList<ICommand>();
	}

	/**
	 * Implementeaza adaugarea comenzilor in containerul intern
	 * 
	 * @param fileSystem
	 *            Sistemul de fisiere utilizat
	 * @param path
	 *            Argumentul comenzii(in cazul lui rm -r,acesta va contine si
	 *            -r)
	 * @param typeOfCommand
	 *            tipul comenzii ce se doreste a fi adaugata
	 */
	public void addTask(FileSystem fileSystem, StringTokenizer path,
			typeOfCommand task) {
		container.add(factory.createCommand(fileSystem, path, task));
	}

	/**
	 * Implementeaza executarea comenzilor din containerul intern
	 */
	public void executeAll() {
		while (!container.isEmpty()) {
			container.remove(0).execute();

		}
		fileSystem.print(); // afiseaza arborele de fisiere la finalul executiei
							// comenzilor
	}
}
