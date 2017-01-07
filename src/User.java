/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Implementarea pentru un user ce retine intern numele si
 *         homeDirectoryul acestuia
 */
public class User {

	private String name;
	private Directory homeDirectoryPath;

	public User(final String name) {
		this.name = name;
		homeDirectoryPath = null;
	}

	public void setHomeDirectoryPath(final Directory path) {
		homeDirectoryPath = path;
	}

	public Directory getHomeDirectoryPath() {
		return homeDirectoryPath;
	}

	public String getName() {
		return name;
	}
}
