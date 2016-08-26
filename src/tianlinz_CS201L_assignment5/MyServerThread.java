package tianlinz_CS201L_assignment5;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;

public class MyServerThread extends Thread{

	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private MyServer server;
	private Socket socket;
	private Vector<String> neededFileIDs;
	
	private int threadID;
		
	public MyServerThread(Socket s, MyServer ms) {
		try {
			socket = s;
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			server = ms;
			this.start();
		} catch (IOException ioe) {
			sendResult(new ReturnParcel("ERROR", false));
			ioe.printStackTrace();
		}
		
		neededFileIDs = new Vector<String>();
	}

	private void sendResult(ReturnParcel result) {
		//Send to client
		try {
			oos.writeObject(result);
			oos.flush();
		} catch (IOException ioe) {
			server.getGUI().fatalServerError();
			ioe.printStackTrace();		
		}
	}
		
	public void closeSocket(){
		try {
			socket.close();
		} catch (IOException e) {
			server.getGUI().fatalServerError();
			e.printStackTrace();
		}
	}
	
	//Check if the given username is available
	private void signUpAvailability(Parcel parcel, ResultSet rSet){
		//Check if it is taken
		server.getGUI().logMessage("Sign-up attempt User:"
				+parcel.getContent1()
				+" Password:"
				+parcel.getContent2());
		boolean taken = false;
		try {
			while (rSet.next()){
				if(parcel.getContent1().equals(rSet.getString("username"))){
					taken = true;
					break;
				}
			}
		} catch (SQLException e) {
			sendResult(new ReturnParcel("ERROR", false));
			e.printStackTrace();
		}
		
		//Log the message 
		if(taken){
			server.getGUI().logMessage("Sign-up Failure User:"
					+parcel.getContent1());	
		}
		else{
			server.getGUI().logMessage("Sign-up Success User:"
					+parcel.getContent1());							
		}
		
		//Send result to client
		sendResult(new ReturnParcel(parcel.getOperation(), taken));
	}
	
	//Query to check if the entered password matches the one in the database for a given username
	private void loginAuthentication(Parcel parcel, ResultSet rSet){
		//print to the serverGUI's log
		server.getGUI().logMessage("Login attempt User:"
				+parcel.getContent1()
				+" Password:"
				+parcel.getContent2());
		
		String pswd = "";
		try {
			if(rSet.next()){
				pswd = rSet.getString("password");
			}
		} catch (SQLException e) {
			sendResult(new ReturnParcel("ERROR", false));
			e.printStackTrace();
		}
		
		//Check for a match
		boolean match = pswd.equals(parcel.getContent2());
		if(match){
			server.getGUI().logMessage("Login Success User:"
					+parcel.getContent1());	
		}
		else{
			server.getGUI().logMessage("Login Failure User:"
					+parcel.getContent1());	
		}
		
		//Send result to client
		sendResult(new ReturnParcel("AUTHENTICATION", match));
	}
	
	//Query for all the files for a given user
	private void getFilenamesAndIDs(Parcel parcel, ResultSet rSet){
		Vector<String> filenames = new Vector<String>();
		Vector<String> fileIDs = new Vector<String>();
		
		try {
			while(rSet.next()){
				filenames.add(rSet.getString("filename"));
				fileIDs.add(rSet.getString("fileID"));
			}
		} catch (SQLException e) {
			sendResult(new ReturnParcel("ERROR", false));
			e.printStackTrace();
		}

		//Send the hashmap to client
		sendResult(new ReturnParcel(parcel.getOperation(), filenames, fileIDs, null, null));
	}
	
	//Open file
	private void getUserFileContent(Parcel parcel, ResultSet rSet){
		String fileContent = "";
		try {
			if(rSet.next())
				fileContent = rSet.getString("fileContent");
		} catch (SQLException e) {
			sendResult(new ReturnParcel("ERROR", false));
			e.printStackTrace();
		}
		
		if(fileContent.equals("")){
			server.getGUI().logMessage("File failed to open User:"+ parcel.getContent3());	
		}
		else{
			server.getGUI().logMessage("File opened User:" +parcel.getContent3()+" File:"+parcel.getContent1());
		}
		
		server.receiveBaseFile(parcel.getContent2(), fileContent);
		
		sendResult(new ReturnParcel(parcel.getOperation(), parcel.getContent1(), parcel.getContent2(), fileContent, null));
	}
	
	private void permissionValidity1(Parcel parcel, ResultSet rSet){
		boolean exists = false;
		try {
			while (rSet.next()){
				if(parcel.getContent1().equals(rSet.getString("username"))){
					exists = true;
					break;
				}
			}
		} catch (SQLException e) {
			sendResult(new ReturnParcel("ERROR", false));
			e.printStackTrace();
		}
		
		String enteredUser = parcel.getContent1();
		String fileID = parcel.getContent2();
		//If user exists, we will make sure user doensn't have permission already
		//If user doesn't exist, let client know.
		sendResult(new ReturnParcel(parcel.getOperation(), Boolean.toString(exists), enteredUser, fileID, null));
	}
	
	private void permissionValidity2(Parcel parcel, ResultSet rSet){
		boolean hasPermission = false;

		try {
			while (rSet.next()){
				if(parcel.getContent2().equals(rSet.getString("fileID"))){
					hasPermission = true;
					break;
				}
			}
		} catch (SQLException e) {
			sendResult(new ReturnParcel("ERROR", false));
			e.printStackTrace();
		}
		
		String enteredUser = parcel.getContent1();
		String fileID = parcel.getContent2();
		sendResult(new ReturnParcel(parcel.getOperation(), Boolean.toString(hasPermission), enteredUser, fileID, null));
	}
	
	private void parsePermissions(Parcel parcel, ResultSet rSet){
		Vector<String> fileIDs = new Vector<String>();
		Vector<String> usernames = new Vector<String>();
		try {
			while (rSet.next()){
				fileIDs.add(rSet.getString("fileID"));
				usernames.add(rSet.getString("username"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		sendResult(new ReturnParcel(parcel.getOperation(), parcel.getVector1(), parcel.getVector2(), fileIDs, usernames));
	}
	
	private void getOthersFilenamesAndIDs(Parcel parcel, ResultSet rSet){
		Vector<String> filenames = new Vector<String>();
		Vector<String> fileIDs = new Vector<String>();
		
		try {
			while(rSet.next()){
				filenames.add(rSet.getString("filename"));
				fileIDs.add(rSet.getString("fileID"));
			}
		} catch (SQLException e) {
			sendResult(new ReturnParcel("ERROR", false));
			e.printStackTrace();
		}
		
		Vector<String> selectedFileIDs = parcel.getVector1();
		//Send the hashmap to client
		sendResult(new ReturnParcel(parcel.getOperation(), filenames, fileIDs, selectedFileIDs, null));
	}
	
	public void requestUpdateFromClient(){
		sendResult(new ReturnParcel("UPDATE", "", null, null, null));
	}
		
	synchronized public void receiveMergedContents(HashMap<String, String> updatedContent){
		for(HashMap.Entry<String, String> entry: updatedContent.entrySet()){
			boolean remove = true;
			for(String neededFileID: neededFileIDs){
				if(entry.getKey().equals(neededFileID)){
					remove = false;
				}
			}
			if(remove){
				updatedContent.remove(entry.getKey());
			}
		}
				
		sendResult(new ReturnParcel("MERGED", updatedContent));
	}
	
	public void setID(int id){
		threadID = id;
	}
	
	public int getID(){
		return threadID;
	}
	
	public void run(){
		while(true) {
			try {
				//Read from client
				Parcel parcel = (Parcel)ois.readObject();
				Class.forName("com.mysql.jdbc.Driver");
				
				if(!parcel.isQuery()){
					if(parcel.getMap() != null){
						HashMap<String, String> fileIDToContent = parcel.getMap();
						neededFileIDs.clear();
						for(HashMap.Entry<String, String> entry : fileIDToContent.entrySet()){
							neededFileIDs.add(entry.getKey());
						}
						server.receiveUpdate(fileIDToContent, getID());
						continue;
					}
				}
				
				//TODO IMPORTANT Change string p to your password
				String p = "YOUR PASSWORD";
				
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/tianlinz?useSSL=false&user=root&password="+p);
				Statement st = conn.createStatement();
				PreparedStatement ps = conn.prepareStatement(parcel.getCommand());
				
				//Query
				if(parcel.isQuery()){	
					ResultSet rSet = ps.executeQuery();

					//Signup username availability
					if(parcel.getOperation().equals("AVAILABLE")){
						signUpAvailability(parcel, rSet);
					}
					//Login Authentication
					else if(parcel.getOperation().equals("AUTHENTICATION")){
						loginAuthentication(parcel, rSet);
					}
					//Get all of a user's files
					else if(parcel.getOperation().substring(0, 8).equals("GETFILES")){
						getFilenamesAndIDs(parcel, rSet);
					}
					else if(parcel.getOperation().equals("OPENFILE")){
						getUserFileContent(parcel, rSet);
					}
					else if(parcel.getOperation().equals("ADDVALID1")){
						permissionValidity1(parcel, rSet);
					}
					else if(parcel.getOperation().equals("ADDVALID2")){
						permissionValidity2(parcel, rSet);
					}
					else if(parcel.getOperation().equals("OWNERSHIPCHECK")){
						boolean isOwner = false;
						if(rSet.next()){
							isOwner = rSet.getBoolean("isOwner");
						}
						sendResult(new ReturnParcel(parcel.getOperation(), isOwner));
					}
					else if(parcel.getOperation().equals("GETFILEID")){
						if(rSet.next()){
							String fileID = rSet.getString("fileID");
							String username = parcel.getContent1();
							String filename = parcel.getContent2();
							sendResult(new ReturnParcel(parcel.getOperation(), fileID, username, filename, null));
						}
					}
					else if(parcel.getOperation().equals("ALLUSERSWITHPERMISSION")){
						Vector<String> allUsernames = new Vector<String>();
						while(rSet.next()){
							allUsernames.add(rSet.getString("username"));
						}
						sendResult(new ReturnParcel(parcel.getOperation(), allUsernames, null, null, null));
					}
					else if(parcel.getOperation().equals("GETALLPERMISSIONS")){
						parsePermissions(parcel, rSet);
					}
					else if(parcel.getOperation().equals("OTHERSFILES")){
						getOthersFilenamesAndIDs(parcel, rSet);
					}
					else if(parcel.getOperation().equals("CHECKPERMISSION")){
						boolean hasPermission = false;
						if(rSet.next()){
							if(rSet.getString("username").equals(parcel.getContent1())){
								hasPermission = true;
							}
						}
						String fileID = parcel.getContent2();
						
						sendResult(new ReturnParcel(parcel.getOperation(), Boolean.toString(hasPermission), fileID, null, null));
					}
				}
				//Not Query
				else{
					ps.execute();
					//Print to Server GUI Log (getPassword() returns the filename in this case)
					if(parcel.getOperation().equals("SAVECONTENT")){
						//Set permissions with this fileID
						String fileID = parcel.getContent1();
						String username = parcel.getContent2();
						String filename = parcel.getContent3();
						String fileContent = parcel.getContent4();
						sendResult(new ReturnParcel(parcel.getOperation(), fileID, username, filename, fileContent));
					}
					else if(parcel.getOperation().equals("SAVEPERMISSION")){
						String filename = parcel.getContent2();
						String fileID = parcel.getContent1();
						sendResult(new ReturnParcel(parcel.getOperation(), fileID, filename, null, null));
						server.getGUI().logMessage("File saved User:"
								+parcel.getContent1()+" File:"+parcel.getContent2());
					}
					else if(parcel.getOperation().equals("ADDPERMISSION")){
						//Do nothing
					}
					else if(parcel.getOperation().equals("INSERT")){
						//Do nothing
					}
					else if(parcel.getOperation().equals("REMOVEPERMISSION")){
						///Do nothing
					}
				}
				st.close();
				conn.close();
			} 
			catch (SocketException se){
				System.out.println("Severthread Socket closed");
				this.closeSocket();
				break;
			}
			catch (SQLException sqle) {
				sendResult(new ReturnParcel("ERROR", false));
				sqle.printStackTrace();
			} 
			catch (ClassNotFoundException cnfe) {
				sendResult(new ReturnParcel("ERROR", false));
				cnfe.printStackTrace();
			} 
			catch (IOException e) {
				sendResult(new ReturnParcel("ERROR", false));
				e.printStackTrace();
			}
		}
	}
}
