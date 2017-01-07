/**
 * @author Ionut-Cristian Bucur, 323CA
 * 
 *         Sistem de gestionarea globala a erorilor si implict a mesajelor
 *         acestora
 */
public final class ErrorHandler {

	private ErrorHandler() { // din motive evidente

	}

	/**
	 * Se ocupa cu gestionarea globala a erorilor
	 * 
	 * @param numberOfError
	 *            numarul corespunzator acelui tip de eroare
	 * @param command
	 *            comanda+argumentul acesteia care a cauzat eroarea
	 */
	public static void reportError(int numberOfError, String command) {
		switch (numberOfError) {
		case -1:
			System.out.println(
					numberOfError + ": " + command + ": Is a directory");
			break;
		case -2:
			System.out.println(
					numberOfError + ": " + command + ": No such directory");
			break;
		case -3:
			System.out.println(
					numberOfError + ": " + command + ": Not a directory");
			break;
		case -4:
			System.out.println(
					numberOfError + ": " + command + ": No rights to read");
			break;
		case -5:
			System.out.println(
					numberOfError + ": " + command + ": No rights to write");
			break;
		case -6:
			System.out.println(
					numberOfError + ": " + command + ": No rights to execute");
			break;
		case -7:
			System.out.println(
					numberOfError + ": " + command + ": File already exists");
			break;
		case -8:
			System.out.println(
					numberOfError + ": " + command + ": User does not exist");
			break;
		case -9:
			System.out.println(
					numberOfError + ": " + command + ": User already exists");
			break;
		case -10:
			System.out.println(numberOfError + ": " + command
					+ ": No rights to change user status");
			break;
		case -11:
			System.out
					.println(numberOfError + ": " + command + ": No such file");
			break;
		case -12:
			System.out.println(numberOfError + ": " + command
					+ ": No such file or directory");
			break;
		case -13:
			System.out.println(numberOfError + ": " + command
					+ ": Cannot delete parent or current directory");
			break;
		case -14:
			System.out.println(
					numberOfError + ": " + command + ": Non empty directory");
			break;

		}
	}
}
