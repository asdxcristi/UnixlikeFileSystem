import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Interfata folosita in gestionarea scheletului fisierelor/folderelor
 */
interface IFile {

	public void print(int i);

	public String getType();

	public String getName();

	public User getOwner();

	public void print();

	public Directory getParent();

	public void killOwner(String user, User newOwner);

	/**
	 * @author Ionut-Cristian Bucur, 323CA
	 * 
	 *         Implementeaza sistemul de permisiuni din cadrul fisierelor
	 */
	class Permission {
		private boolean read;
		private boolean write;
		private boolean execute;

		public Permission() {
			read = false;
			write = false;
			execute = false;

		}

		/**
		 * Seteaza toate permisiunile dupa un string
		 * 
		 * @param perm
		 *            permisiunile sub forma numerica(0-7)
		 */
		public void setAll(String perm) {
			switch (perm) {
			case "0":
				setExecuteInactive();
				setWriteInactive();
				setReadInactive();
				return;
			case "1":
				setExecuteActive();
				setWriteInactive();
				setReadInactive();
				break;
			case "2":
				setWriteActive();
				setReadInactive();
				setExecuteInactive();
				break;
			case "3":
				setExecuteActive();
				setWriteActive();
				setReadInactive();
				break;
			case "4":
				setReadActive();
				break;
			case "5":
				setReadActive();
				setExecuteActive();
				setWriteInactive();
				break;
			case "6":
				setReadActive();
				setWriteActive();
				setExecuteInactive();
				break;
			case "7":
				setReadActive();
				setWriteActive();
				setExecuteActive();
				break;
			}
		}

		/**
		 * Scoate permisiunile frumos
		 * 
		 * @return scoate toate permisiunile sub forma numerica "rwx"
		 */
		public String getAll() {
			String res = "";

			if (read) {
				res = res + "r";
			} else {
				res = res + "-";
			}
			if (write) {
				res = res + "w";
			} else {
				res = res + "-";
			}
			if (execute) {
				res = res + "x";
			} else {
				res = res + "-";
			}
			return res;
		}

		public void setReadActive() {
			read = true;
		}

		public void setReadInactive() {
			read = false;
		}

		public boolean getReadStatus() {
			return read;
		}

		public void setWriteActive() {
			write = true;
		}

		public void setWriteInactive() {
			write = false;
		}

		public boolean getWriteStatus() {
			return write;
		}

		public void setExecuteActive() {
			execute = true;
		}

		public void setExecuteInactive() {
			execute = false;
		}

		public boolean getExecuteStatus() {
			return execute;
		}
	}

	public void setParent(Directory directory);
}

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Parintele comun al File si Directory
 */
abstract class FileEntity implements IFile {
	/**
	 * 
	 * Numele entitatii
	 */
	protected String name;
	/**
	 * 
	 * Ownerul entitatii
	 */
	protected User owner;

	protected Directory parent;
	/**
	 * 
	 * Tipul entitatii(f/d)-file/directory
	 */
	protected String type;
	/**
	 * 
	 * Permisiunile pentru owner
	 */
	protected Permission ownerPermissions;
	/**
	 * 
	 * Permisiunile pentru ceilalti
	 */
	protected Permission othersPermissions;

	public User getOwner() {
		return owner;
	}

	@Override
	public void setParent(Directory directory) {
		parent = directory;

	}

	public Directory getParent() {
		return parent;
	}

	public String getType() {
		return type;
	}

	/**
	 * Inlocuieste ownerul actual al fisierului Folosit la setergerea
	 * utilizatorilor din sistem
	 * 
	 * @param user
	 *            userul ce se vrea sters
	 * @param noul
	 *            owner al fisierului
	 */
	public void killOwner(String user, User newOwner) {
		if (owner.getName().equals(user)) { // verifica daca chiar el e ownerul
			setOwner(newOwner);
		}
	}

	public FileEntity(String name) {
		this.name = name;
		ownerPermissions = new Permission();
		othersPermissions = new Permission();
		parent = null;
	}

	public void setOwner(User newOwner) {
		owner = newOwner;
		ownerPermissions.setExecuteActive();
		ownerPermissions.setReadActive();
		ownerPermissions.setWriteActive();
	}

}

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Clasa pentru fisiere(frunze,cum sunt numite in Composite Pattern)
 */
class File extends FileEntity {

	private String content;

	public File(String name) {
		super(name);

		type = "f";
	}

	public String getName() {
		return name;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	/**
	 * Functie de afisare cu identare
	 */
	public void print(int i) {
		String indentShit = "";
		for (int j = 0; j < i; j++) {
			indentShit += "\t";
		}
		System.out.println(
				indentShit + name + " " + getType() + ownerPermissions.getAll()
						+ othersPermissions.getAll() + " " + owner.getName());
	}

	@Override
	public void print() {
		System.out.println(name + " " + getType() + ownerPermissions.getAll()
				+ othersPermissions.getAll() + " " + owner.getName());

	}

}

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Clasa pentru directory aka composite
 */
class Directory extends FileEntity {

	private ArrayList<IFile> childFiles = new ArrayList<IFile>();

	public Directory(String name) {
		super(name);
		type = "d";
	}

	public boolean isEmpty() {
		return childFiles.isEmpty();
	}

	/**
	 * @return lista de subfisere ale directorului
	 */
	public ArrayList<IFile> getChildFiles() {
		return childFiles;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Cauta in directorul curent un fisier
	 * 
	 * @return referinta la fisierul cautat sau null daca nu a fost gasit
	 */
	public IFile findFileInCurrentDirectory(String fileToFind) {

		if (fileToFind.equals(".")) {
			return this;
		}
		if (fileToFind.equals("..")) {
			if (name.equals("/")) {// e batman
				return this;
			} else {
				return this.parent;
			}
		}

		for (IFile tree : childFiles) {

			if (fileToFind.equals(tree.getName())) {

				return tree;
			}
		}

		return null;
	}

	/**
	 * Inlocuieste ownerul actual al fisierului Folosit la setergerea
	 * utilizatorilor din sistem
	 * 
	 * @param user
	 *            userul ce se vrea sters
	 * @param noul
	 *            owner al fisierului
	 */
	public void killOwner(String user, User newOwner) {
		if (owner.getName().equals(user)) {
			setOwner(newOwner);
		}
		for (IFile tree : childFiles) {
			tree.killOwner(user, newOwner);

		}
	}

	/**
	 * Functie de afisare cu identare
	 */
	public void print(int i) {
		String indentShit = "";
		for (int j = 0; j < i; j++) {
			indentShit += "\t";
		}
		System.out.println(
				indentShit + name + " " + getType() + ownerPermissions.getAll()
						+ othersPermissions.getAll() + " " + owner.getName());
		i++;
		for (IFile tree : childFiles) {
			tree.print(i);
		}
	}

	/**
	 * Verifica daca fisierul pe care vrem sa-l stergem se afla in calea
	 * directorului curent
	 * 
	 * @param toBeKilled
	 *            fisierul pe care vrem sa-l stergem
	 */
	public boolean isAncestorInDanger(String toBeKilled) {
		Directory aux = this;
		while (!aux.getName().equals("/")) { // cat timp nu suntem pe root
			if (aux.getName().equals(toBeKilled)) {
				return true;
			}
			aux = aux.getParent();
		}
		return false;
	}

	/**
	 * Functie de afisare fara identare
	 */
	public void print() {
		System.out.println(name + " " + getType() + ownerPermissions.getAll()
				+ othersPermissions.getAll() + " " + owner.getName());

	}

	public void addFile(IFile newFile) {
		newFile.setParent(this);
		childFiles.add(newFile);
	}

	public void remove(String fileToRemove) {
		IFile file = findFileInCurrentDirectory(fileToRemove);
		childFiles.remove(file);
	}

}

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Sistem de gestionarea a fisierelor
 */
public class FileSystem {

	public Directory tree;
	private Directory currentDirectory;
	private UserSystem userSystem;

	public FileSystem(UserSystem userSystem) {
		this.userSystem = userSystem;

		tree = new Directory("/");
		tree.setOwner(userSystem.getUser("root"));
		tree.othersPermissions.setReadActive();
		tree.othersPermissions.setExecuteActive();
		userSystem.getUser("root").setHomeDirectoryPath(tree);
		setCurrentDirectory(tree);

	}

	public UserSystem getUserSystem() {
		return userSystem;
	}

	public Directory getCurrentDirectory() {
		return currentDirectory;
	}

	public void setCurrentDirectory(Directory newDir) {
		currentDirectory = newDir;
	}

	/**
	 * Printeaza arborele de fisiere(folosit la finalul executarii comenzilor)
	 */
	public void print() {
		tree.print(0);
	}

	/**
	 * Adauga un nou home directory pentru un utilizator eventual nou
	 * 
	 * @param dirName
	 *            numele directorlui
	 * @param owner
	 *            userul caruia ii va apartine
	 */
	public Directory addHomeDirectoryToRoot(String dirName, User owner) {
		Directory newDir = new Directory(dirName);
		newDir.setOwner(owner);

		tree.addFile(newDir);
		return newDir;
	}

	/**
	 * Incearca sa ajunga in calea dorita
	 * 
	 * @param path
	 *            calea in care vrem sa ajungem
	 * @param owner
	 *            utilizatorul owner al folderului
	 * @param command
	 *            comanda care a cerut aceasta metoda
	 */
	public Directory getToPath(String path, User currentUser, String command) {

		Directory curva = null;

		if (path.startsWith("/")) {// calea e absoluta

			curva = tree;
		} else {// calea e relativa

			curva = getCurrentDirectory();
		}

		StringTokenizer meh = new StringTokenizer(path, "/");

		String folder;

		while (meh.hasMoreTokens()) {

			folder = meh.nextToken();

			if (folder.equals(".")) {
				continue;
			}
			if (folder.equals("..")) {
				if (curva.getName().equals("/")) {// root n-are parinte
					continue;
				}

				curva = curva.getParent();
				continue;
			}

			if ((curva.findFileInCurrentDirectory(folder)) == null) {

				ErrorHandler.reportError(-2, command);
				return null;

			}

			if (curva.findFileInCurrentDirectory(folder).getType()
					.equals("f")) {
				ErrorHandler.reportError(-3, command);
				return null;
			}
			curva = (Directory) curva.findFileInCurrentDirectory(folder);

		}
		if (curva.getType().equals("f")) {// daca e fisier nu mai are rost,mai
			// dam si eroare
			ErrorHandler.reportError(-3, command);
			return null;
		} else {// daca e folder
			int ok = Check(currentUser, (Directory) curva, command);
			if (ok == 0) {// si totu e ok in lume
				return (Directory) curva;
			}
			// continua
		}

		return null;

	}

	/**
	 * Verifica permisiunile unui utilizator asupra unu director si implicit
	 * returneaza erorile aferente Folosit de getToPath
	 * 
	 * @param dir
	 *            folderul pe care vrem sa il verificam
	 * @param currentUser
	 *            utilizatorul curent
	 * @param command
	 *            comanda care a cerut aceasta metoda
	 */
	public int Check(User currentUser, Directory dir, String command) {
		if (dir.getType().equals("f")) {
			ErrorHandler.reportError(-3, command);
			return -3;
		}
		User owner = dir.getOwner();

		if (currentUser.getName().equals("root")) {
			return 0;
		}

		if (owner.getName().equals(currentUser.getName())) { // daca e
			// owner,poate e
			// idiot si si-a
			// scos executeu'
			if (!dir.ownerPermissions.getExecuteStatus()) {
				ErrorHandler.reportError(-6, command);
				return -6;
			}
		} else {// daca nu e owner
			if (!dir.othersPermissions.getExecuteStatus()) {// si nu are
															// perimisiuni
				ErrorHandler.reportError(-6, command);
				return -6;
			}
		}
		return 0;
	}

	public void removeUserTrace(String userToDel) {

		tree.killOwner(userToDel, userSystem.getReplacement());
	}

}
