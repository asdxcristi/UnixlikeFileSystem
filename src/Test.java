/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Clasa pentru teste asupra sistemului de fisiere+useri implementat
 */
public class Test {

	public static void main(String[] args) {
		IOHandler asd = new IOHandler(args[0]);
		Bash bash = new Bash(asd);
		bash.executeAll();

	}

}
