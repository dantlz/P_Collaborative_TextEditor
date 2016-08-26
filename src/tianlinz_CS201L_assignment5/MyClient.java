package tianlinz_CS201L_assignment5;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MyClient extends javax.swing.JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private HelperClasses.MyBackgroundPanel mainPanel;
	
	private JPanel introComponents;
	private HelperClasses.MyButton loginOptionButton;
	private HelperClasses.MyButton signUpOptionButton;
	private HelperClasses.MyButton offlineButton;
	
	private JPanel signUpComponents;
	private JLabel usernameLabel;
	private JTextField signUp_UsernameField;
	private JLabel passwordLabel;
	private JPasswordField signUp_PasswordField;
	private JLabel repeatLabel;
	private JPasswordField signUp_RepeatField;
	private HelperClasses.MyButton signUp_BackButton;
	private HelperClasses.MyButton signUpButton;
	
	private JPanel loginComponents;
	private JLabel login_UsernameLabel;
	private JTextField login_UsernameField;
	private JLabel login_PasswordLabel;
	private JPasswordField login_PasswordField;
	private HelperClasses.MyButton login_BackButton;
	private HelperClasses.MyButton loginButton;
	
	private MainWindow mainWindow;
		
	private int port;
	private String host;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	private String username;
	private String password;
		
	
	public MyClient(){
		super("TextEdit Login");
		instantiateComponents();
		createGUI();
		addActions();
		readConfigFile();
	}

	private void instantiateComponents(){
		mainPanel = new HelperClasses.MyBackgroundPanel();
		introComponents = new JPanel();
		loginOptionButton = new HelperClasses.MyButton("Login", "resources/img/buttons/button.png");
		signUpOptionButton = new HelperClasses.MyButton("Sign up", "resources/img/buttons/button.png");
		offlineButton = new HelperClasses.MyButton("Offline", "resources/img/buttons/button.png");
		
		signUpComponents= new JPanel();
		usernameLabel = new JLabel("Username: ");
		signUp_UsernameField = new JTextField(20);
		passwordLabel = new JLabel("Password: ");
		signUp_PasswordField = new JPasswordField(20);
		repeatLabel = new JLabel("Repeat: ");
		signUp_RepeatField = new JPasswordField(20);
		signUpButton = new HelperClasses.MyButton("Sign up", "resources/img/buttons/button.png");
		signUp_BackButton = new HelperClasses.MyButton("Back", "resources/img/buttons/button.png");

		loginComponents = new JPanel();
		login_UsernameLabel = new JLabel("Username: ");
		login_UsernameField = new JTextField(20);
		login_PasswordLabel = new JLabel("Password: ");
		login_PasswordField = new JPasswordField(20);		
		loginButton = new HelperClasses.MyButton("Login", "resources/img/buttons/button.png");
		login_BackButton = new HelperClasses.MyButton("Back", "resources/img/buttons/button.png");
		mainWindow = new MainWindow(this);		
	}
	
	private void createGUI(){
		this.setResizable(false);
		this.setSize(900, 500);
		this.setLocation(700, 100);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.add(mainPanel);
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.add(introComponents);
		introComponents.setLayout(new GridBagLayout());
		introComponents.add(loginOptionButton);
		introComponents.add(signUpOptionButton);
		introComponents.add(offlineButton);	
		introComponents.setVisible(true);
		
		mainPanel.add(signUpComponents);
		signUpComponents.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.gridx = 0;
		usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		usernameLabel.setPreferredSize(new Dimension(80, 30));
		signUpComponents.add(usernameLabel, gbc);
		gbc.gridx = 1;
		signUp_UsernameField.setHorizontalAlignment(SwingConstants.LEFT);
		signUp_UsernameField.setPreferredSize(new Dimension(150, 30));
		signUpComponents.add(signUp_UsernameField, gbc);
		gbc.gridy = 1;
		gbc.gridx = 0;
		passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		passwordLabel.setPreferredSize(new Dimension(80, 30));
		signUpComponents.add(passwordLabel,gbc);
		gbc.gridx = 1;
		signUp_PasswordField.setHorizontalAlignment(SwingConstants.LEFT);
		signUp_PasswordField.setPreferredSize(new Dimension(150, 30));
		signUpComponents.add(signUp_PasswordField,gbc);		
		gbc.gridy = 2;
		gbc.gridx = 0;
		repeatLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		repeatLabel.setPreferredSize(new Dimension(80, 30));
		signUpComponents.add(repeatLabel,gbc);
		gbc.gridx = 1;
		signUp_RepeatField.setHorizontalAlignment(SwingConstants.LEFT);
		signUp_RepeatField.setPreferredSize(new Dimension(150, 30));
		signUpComponents.add(signUp_RepeatField,gbc);
		gbc.gridy = 3;
		gbc.gridx = 0;
		signUpComponents.add(signUp_BackButton, gbc);
		gbc.gridx = 1;
		signUpComponents.add(signUpButton,gbc);
		signUpComponents.setVisible(false);
		
		
		mainPanel.add(loginComponents);
		loginComponents.setLayout(new GridBagLayout());
		gbc.gridy = 0;
		gbc.gridx = 0;
		login_UsernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		login_UsernameLabel.setPreferredSize(new Dimension(80, 30));
		loginComponents.add(login_UsernameLabel, gbc);
		gbc.gridx = 1;
		login_UsernameField.setHorizontalAlignment(SwingConstants.LEFT);
		login_UsernameField.setPreferredSize(new Dimension(150, 30));
		loginComponents.add(login_UsernameField, gbc);
		gbc.gridy = 1;
		gbc.gridx = 0;
		login_PasswordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		login_PasswordLabel.setPreferredSize(new Dimension(80, 30));
		loginComponents.add(login_PasswordLabel,gbc);
		gbc.gridx = 1;
		login_PasswordField.setHorizontalAlignment(SwingConstants.LEFT);
		login_PasswordField.setPreferredSize(new Dimension(150, 30));
		loginComponents.add(login_PasswordField,gbc);		
		gbc.gridy = 2;
		gbc.gridx = 0;
		loginComponents.add(login_BackButton, gbc);
		gbc.gridx = 1;
		loginComponents.add(loginButton, gbc);
		loginComponents.setVisible(false);	
	}
	
	private void addActions(){
		loginOptionButton.addMouseListener(new HelperClasses.MyMouseAdapter());
		signUpOptionButton.addMouseListener(new HelperClasses.MyMouseAdapter());
		offlineButton.addMouseListener(new HelperClasses.MyMouseAdapter());
		signUp_BackButton.addMouseListener(new HelperClasses.MyMouseAdapter());
		signUpButton.addMouseListener(new HelperClasses.MyMouseAdapter());
		login_BackButton.addMouseListener(new HelperClasses.MyMouseAdapter());
		loginButton.addMouseListener(new HelperClasses.MyMouseAdapter());

		
		loginOptionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				goToLogin();
			}
		});
		signUpOptionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				goToSignUp();
			}
		});
		offlineButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				goToOfflineMode();
			}
		});
		signUp_BackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleIntroButtons();
			}
		});
		signUpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				signUp();
			}
		});
		login_BackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleIntroButtons();
			}
		});
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
	}
	
	private void toggleIntroButtons(){
		if(introComponents.isVisible()){
			introComponents.setVisible(false);
		}
		else{
			introComponents.setVisible(true);
		}
		loginComponents.setVisible(false);
		signUpComponents.setVisible(false);
		clearAllFields();
	}
	
	private void goToLogin(){
		connectToServer();
		toggleIntroButtons();
		loginComponents.setVisible(true);
	}
	
	private void goToSignUp(){
		connectToServer();
		toggleIntroButtons();
		signUpComponents.setVisible(true);
	}
	
	private void goToOfflineMode(){
		mainWindow.setUsername("");
		mainWindow.setVisible(true);
		this.setVisible(false);
		clearAllFields();
	}
	
	private void readConfigFile(){
		Scanner scnr;
		try {
			scnr = new Scanner(new File("resources/clientConfig.txt"));
			if(scnr.hasNext()){
				port = scnr.nextInt();
			}
			if(scnr.hasNext()){
				host = scnr.next();
			}
			scnr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void connectToServer(){
		Socket s = null;
		try {
			s = new Socket(host, port);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			new Thread(this).start();	
		}
		catch (ConnectException ce){
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Client Connection failed", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
		}
		catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Client Connection failed", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
		} 
	}
	
	private void signUp(){
		//Get data
		username = signUp_UsernameField.getText();
		password = HelperMethods.encryptPassword(new String(signUp_PasswordField.getPassword()));
		String repeat = HelperMethods.encryptPassword(new String(signUp_RepeatField.getPassword()));		

		//Check validity of data
		if(!password.equals(repeat)){
			JOptionPane.showMessageDialog(this, Constants.passwordRepeatMismatch, "Sign up failed", JOptionPane.WARNING_MESSAGE);
			return;
		}	
		boolean hasUpperCase = false; boolean hasNum = false;
		for(char entry: signUp_PasswordField.getPassword()){
			if(Character.isDigit(entry)){
				hasNum = true;
			}
			else if(Character.isUpperCase(entry)){
				hasUpperCase = true;
			}
		}
		if((!hasUpperCase)||(!hasNum)){
			JOptionPane.showMessageDialog(this, Constants.passwordRequirements, "Sign up failed", JOptionPane.WARNING_MESSAGE);
			return;
		}
		clearAllFields();
		
		//Query server to check for validity
		try {
			Parcel parcel = new Parcel(true, "AVAILABLE", MySQLCommands.getAllUsernames, username, password, null, null);
			oos.writeObject(parcel);
			oos.flush();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Sign up failed", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
			return;
		}
	}
	
	private void finishSignUp(ReturnParcel result){
		
		//Result being true = username IS taken
		if(result.getResult()){
			JOptionPane.showMessageDialog(this, Constants.usernameTaken, "Sign up failed", JOptionPane.WARNING_MESSAGE);
			return;
		}
		else{
			//Username and password are valid and not taken, Insert combination into server
			try {
				Parcel parcel = new Parcel(false,"INSERT",MySQLCommands.addToUsers(username, password),  null, null, null, null);
				oos.writeObject(parcel);
				oos.flush();
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Sign up failed", JOptionPane.WARNING_MESSAGE);
				goToOfflineMode();
			}
			
			//Pass the user through
			this.setVisible(false);
			mainWindow.setUsername(username);
			mainWindow.setVisible(true);
		}
	}
	
	private void login(){
		//Get inputs
		username = login_UsernameField.getText();
		password = HelperMethods.encryptPassword(new String(login_PasswordField.getPassword()));
		clearAllFields();

		//Query server for authentication
		try {
			Parcel parcel = new Parcel(true, "AUTHENTICATION", MySQLCommands.allUsersWithUsername(username), username, password, null, null);
			oos.writeObject(parcel);	
			oos.flush();
		} 
		catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Login failed", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
			return;
		}
	}
	
	private void finishLogin(ReturnParcel result){
		//Authenticated, login
		if(result.getResult()){
			this.setVisible(false);
			mainWindow.setUsername(username);
			mainWindow.setVisible(true);
		}
		else{
			JOptionPane.showMessageDialog(this, Constants.invalidCombination, "Login failed", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	private void clearAllFields(){
		login_UsernameField.setText("");
		login_PasswordField.setText("");
		signUp_UsernameField.setText("");
		signUp_PasswordField.setText("");
		signUp_RepeatField.setText("");
	}

	public void queryFilenamesAndIDs(String purpose){
		//Query the server to check for all files under the given username
		try {
			Parcel parcel = new Parcel(true, "GETFILES"+purpose, MySQLCommands.allFilenamesAndIDsWithUsername(username), null, null, null, null);
			oos.writeObject(parcel);
			oos.flush();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Could not get files", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
			return;
		}
	}
	
	public void openFile(String filename, String fileID){
		//Notify server to log file "downloading"
		try {
			Parcel parcel = new Parcel(true, "OPENFILE", MySQLCommands.openFileContent(fileID, filename), filename, fileID, username, null);
			oos.writeObject(parcel);
			oos.flush();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Could not open file", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
			return;
		}
	}

	public void saveFileContent(String fileID, String filename, String fileContent){
		//Save a file content to server
		try {
			Parcel parcel = new Parcel(false, "SAVECONTENT", MySQLCommands.saveFileContent(fileID, filename, fileContent), fileID, username, filename, fileContent);
			oos.writeObject(parcel);
			oos.flush();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Save file online failed", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
			return;
		}
	}
	
	public void getNewFileID(String filename, String fileContent, String fileID, String username){
		//Save file permission to server
		try {
			Parcel parcel = new Parcel(true, "GETFILEID", MySQLCommands.getFileID(filename, fileContent), username,filename, null, null);
			oos.writeObject(parcel);
			oos.flush();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Save file online failed", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
			return;
		}
	}
		
	public void saveFilePermission(String fileID, String username, String filename){
		//Save file permission to server
		try {
			Parcel parcel = new Parcel(false, "SAVEPERMISSION", MySQLCommands.saveFilePermission(fileID, username, "true"), fileID, filename, null, null);
			oos.writeObject(parcel);
			oos.flush();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Save file online failed", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
			return;
		}
	}
	
	public void permissionValidity1(String enteredUser, String fileID){
		//Query server to check for validity
		try {
			Parcel parcel = new Parcel(true, "ADDVALID1", MySQLCommands.getAllUsernames, enteredUser, fileID, null, null);
			oos.writeObject(parcel);
			oos.flush();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Add user failed", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
			return;
		}
	}
	
	public void permissionValidity2(String enteredUser, String fileID){
		//Query server to check for validity
		try {
			Parcel parcel = new Parcel(true, "ADDVALID2", MySQLCommands.userAlreadyHavePermission(enteredUser), enteredUser, fileID, null, null);
			oos.writeObject(parcel);
			oos.flush();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Add user failed", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
			return;
		}
	}

	public void addUserPermission(String fileID, String username){
		//Save file permission to server
		try {
			Parcel parcel = new Parcel(false, "ADDPERMISSION", MySQLCommands.saveFilePermission(fileID, username, "false"), null, null, null, null);
			oos.writeObject(parcel);
			oos.flush();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Save file online failed", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
			return;
		}
	}
	
	public void checkIsOwner(String username, String fileID){
		//Check if username is the owner of fileID
		try {
			Parcel parcel = new Parcel(true, "OWNERSHIPCHECK", MySQLCommands.ownershipCheck(username, fileID), null, null, null, null);
			oos.writeObject(parcel);
			oos.flush();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Querying ownership failed", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
			return;
		}
	}
	
	public void getAllUsersWithPermission(String fileID){
		try {
			Parcel parcel = new Parcel(true, "ALLUSERSWITHPERMISSION", MySQLCommands.allUsersWithPermission(fileID), null, null, null, null);
			oos.writeObject(parcel);
			oos.flush();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Query all usernames failed.", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
			return;
		}
	}
	
	public void removeUserPermission(String fileID, String userToRemove){
		try {
			Parcel parcel = new Parcel(false, "REMOVEPERMISSION", MySQLCommands.removePermission(fileID, userToRemove), null, null, null, null);
			oos.writeObject(parcel);
			oos.flush();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Remove user permission failed.", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
			return;
		}
	}
	
	public void getAllPermissions(Vector<String> filenames, Vector<String> fileIDs){
		try {
			Parcel parcel = new Parcel(true, "GETALLPERMISSIONS", MySQLCommands.getAllPermissions(username), filenames, fileIDs);
			oos.writeObject(parcel);
			oos.flush();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Querying all permissions failed.", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
			return;
		}
	}
	
	public void queryOtherUserFilenamesAndFileIDs(String username, Vector<String> fileIDs){
		//Query the server to check for all files under the given username
		try {
			Parcel parcel = new Parcel(true, "OTHERSFILES", MySQLCommands.allFilenamesAndIDsWithUsername(username), fileIDs, null);
			oos.writeObject(parcel);
			oos.flush();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Could not get " + username + "'s files", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
			return;
		}
	}
	
	public void sendOnlineTabs(HashMap<String, String> fileIDToContent){
		try {
			Parcel parcel = new Parcel(false, "UPDATE", fileIDToContent);
			oos.writeObject(parcel);
			oos.flush();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Could not send updates to serverthrea", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
			return;
		}
	}
	
	public void checkPermission(String fileID){
		try {
			Parcel parcel = new Parcel(true, "CHECKPERMISSION", MySQLCommands.checkPermission(username, fileID), username, fileID, null, null);
			oos.writeObject(parcel);
			oos.flush();
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Could not check user permission", JOptionPane.WARNING_MESSAGE);
			goToOfflineMode();
			return;
		}
	}
	
	public void run() {
		while(true) {
			//Read from server
			try {
				Object received = ois.readObject();
				if(received == null){
					//Server cannot be reached
				}
				else{
					ReturnParcel result = (ReturnParcel)received;
					String operation = result.getOperation();
					switch (operation) {
					case "AVAILABLE":
						finishSignUp(result);
						break;
					case "AUTHENTICATION":
						finishLogin(result);
						break;
					case "GETFILES_SAVE":
						mainWindow.saveFileOnline(result.getVector1(), result.getVector2());
						break;
					case "GETFILES_OPEN":
						Vector<String> filenames = result.getVector1();
						Vector<String> fileIDs = result.getVector2();
						getAllPermissions(filenames, fileIDs);
						break;
					case "GETALLPERMISSIONS":
						Vector<String> currentUserFilenames = result.getVector1();
						Vector<String> currentUserFileIDs = result.getVector2();
						Vector<String> ownerFileIDs = result.getVector3();
						Vector<String> ownerUsernames = result.getVector4();
						mainWindow.openFileOnline(currentUserFilenames, currentUserFileIDs, ownerFileIDs, ownerUsernames);
						break;
					case "OPENFILE":
						mainWindow.finishOpeningOnline(result.getContent1(), result.getContent2(), result.getContent3());
						break;
					case "ERROR":
						JOptionPane.showMessageDialog(this, Constants.serverUnreachable, "Connection error", JOptionPane.WARNING_MESSAGE);
						goToOfflineMode();
						break;
					case "SAVECONTENT":
						//This parcel was sent by save content, now it's time to save file permission.
						String fileID = result.getContent1();
						String username = result.getContent2();
						String filename = result.getContent3();
						String fileContent = result.getContent4();
						//If file is new, save permission
						//If the current user already has permission, fileID != -1 don't save permision. 
						//If the file is old, then user already has permission. fileID != -1 don't save permission
						if(Integer.valueOf(fileID) == -1 ){
							getNewFileID(filename, fileContent, fileID, username);
						}
						break;
					case "GETFILEID":
						fileID = result.getContent1();
						username = result.getContent2();
						filename = result.getContent3();
						saveFilePermission(fileID, username, filename);						
						break;
					case "SAVEPERMISSION":
						fileID = result.getContent1();
						filename = result.getContent2();
						mainWindow.finishSavingOnline(fileID, filename);
						break;
					case "ADDVALID1": 
						boolean exists = Boolean.valueOf(result.getContent1());
						if(exists){//User exists
							permissionValidity2(result.getContent2(), result.getContent3());
						}
						else{
							mainWindow.finishAddingUserPermission(exists, "The user does not exist.");
						}
						break;
					case "ADDVALID2": //This can either return user
						boolean hasPermission = Boolean.valueOf(result.getContent1());
						boolean doesNotHavePermissionYet = !hasPermission;
						if(hasPermission){
							mainWindow.finishAddingUserPermission(doesNotHavePermissionYet, "The user already has permission to the given file.");
						}
						else{
							addUserPermission(result.getContent3(), result.getContent2());
						}
						break;
					case "OWNERSHIPCHECK":
						boolean isOwner = result.getResult();
						mainWindow.setIsOwnerInDatabase(isOwner);
						break;
					case "ALLUSERSWITHPERMISSION":
						Vector<String> allUsernames = result.getVector1();
						mainWindow.removeUserPermission(allUsernames);
						break;
					case "OTHERSFILES":
						Vector<String> allFilenames = result.getVector1();
						Vector<String> allFileIDs = result.getVector2();
						Vector<String> selectedFileIDs = result.getVector3();
						mainWindow.populateOtherUsersFiles(allFilenames, allFileIDs, selectedFileIDs);
						break;
					case "UPDATE":
						HashMap<String, String> fileIDToContent = mainWindow.getAllOnlineTabsIDsAndContent();
						sendOnlineTabs(fileIDToContent);
						break;
					case "MERGED":
						mainWindow.updateAllContents(result.getMap());
						break;
					case "CHECKPERMISSION":
						if(!(Boolean.valueOf(result.getContent1()))){
							mainWindow.disconnectTab(result.getContent2());
						}
						break;
					}
				}
			} 
			catch (ClassNotFoundException | IOException e) {
				JOptionPane.showMessageDialog(this, Constants.cannotReadFromServer, "Connection error", JOptionPane.WARNING_MESSAGE);
				mainWindow.setUsername("");
				break;
			}
		}
	}
}
