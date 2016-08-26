package tianlinz_CS201L_assignment5;

import java.awt.Font;

//This class contains constant variables or values that look too messy otherwise
public class Constants {
	public final static Font avenir = new Font("Avenir", Font.BOLD, 15);
	public final static String filenameTaken = "Filename is already taken, please try a different one.";
	public final static String onlineSaveSuccessful = "File successfully saved online";
	public final static String invalidFilename = "Please enter a valid filename";
	public final static String noFileChosen = "Please choose a file";
	public final static String serverUnreachable = "Server could not be reached. Continuing in offline mode";
	public final static String passwordRepeatMismatch = "Password and repeat are not the same";
	public final static String passwordRequirements = "Password must contain at least one number and one upper-case letter.";
	public final static String usernameTaken = "Username already taken, please try a different one.";
	public final static String invalidCombination = "Username and/or password is invalid";
	public final static String invalidConfigFile = "The designated configuration file could not be loaded.";
	public final static String fatalServerError = "There was an error with the server. Exiting";
	public final static String cannotReadFromServer = "Failed to read from server";
}
