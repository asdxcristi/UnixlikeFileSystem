import java.util.ArrayList;

/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Implementarea pentru gestionarea sistemului de useri Contine o lista
 *         cu useri si userul activ
 */
public class UserSystem {

	private ArrayList<User> userList;
	private User activeUser;

	public UserSystem() {
		userList = new ArrayList<User>();
		activeUser = null;
		initialize();
	}

	/*
	 * Adauga utilizatorul root in sistem
	 */
	private final void initialize() {
		String root = "root";
		addUser(root);
		setActiveUser(root);
	}

	public User getActiveUser() {
		return activeUser;
	}

	public int setActiveUser(String activeUser) {
		int index = findUserIndex(activeUser);

		if (index == -1) { // verifica daca userul exista

			return -8; // userul nu exista
		} else {
			this.activeUser = userList.get(index);
			return 0;
		}
	}

	/**
	 * Folosita pentru inlocuirea ownerului fisierelor ce apartineau unui
	 * utilizator ce tocmai a fost sters
	 * 
	 * @return noul owner
	 */
	public User getReplacement() {
		return userList.get(1);
	}

	/**
	 * Sterge utilizatorul din sistem
	 */
	public int removeUser(String userToDel) {
		int index = findUserIndex(userToDel);

		if (index == -1) { // verifica daca userul exista
			return -8; // userul nu exista
		}
		User del = userList.get(index);
		userList.remove(del);
		return 0;
	}

	/**
	 * Adauga un utilizator in sistem
	 * 
	 * @param newUser
	 *            numele noului utilizator
	 */
	public int addUser(String newUser) {
		if (findUserIndex(newUser) == -1) { // userul nu exista
			userList.add(new User(newUser));
			return 0;
		} else // userul exista
			return -9;
	}

	/**
	 * Scoate pozitia unui utilizator in sistem
	 * 
	 * @param userToFind
	 *            utilizator a carui pozitie o cautam
	 */
	public int findUserIndex(String userToFind) {
		int i = 0;
		for (User it : userList) {
			if (it.getName().equals(userToFind)) {
				return i;
			}
			i++;
		}
		return -1;
	}

	public User getUser(String userName) {
		int index = findUserIndex(userName);
		return userList.get(index);
	}
}
