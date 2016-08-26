package tianlinz_CS201L_assignment5;

public class MySQLCommands {
	//AVAILABILITY
	//Old - final static String getAllUsernames = "SELECT * FROM Users";
	final static String getAllUsernames = "SELECT username FROM Users";
	
	//AVAILABLE
	static String addToUsers(String username, String password){
		return "INSERT INTO Users (username, password) VALUES ('"+username+"','"+password+"')";
	}
	
	//AUTHENTICATION
	static String allUsersWithUsername(String username){
		return "SELECT * from Users WHERE username='"+username+"'"; 
	}
	
	//GETFILES
	static String allFilenamesAndIDsWithUsername(String username){
		//Old - return "SELECT filename from Files WHERE username='"+username+"'";
		//Wrong - return "SELECT f.filename FROM Files f, Permission p WHERE p.username = '" + username + "' AND f.fileID = p.fileID";
		return 	"SELECT f.filename, f.fileID FROM Files f, Permissions p WHERE p.username = '" + username + "' AND f.fileID = p.fileID";
	}
	
	//OPENFILE
	static String openFileContent(String fileID, String filename){
		return "SELECT fileContent FROM Files WHERE fileID='"
				+fileID+"' AND filename = '"+filename+"'";
		//Doesn't seem to need updating
	}
	
	//These are used to save file
		//SAVECONTENT
	static String saveFileContent(String fileID, String filename, String fileContent){
		//Old - return "INSERT INTO Files(username,filename,fileContent)" + "VALUES('" +username+ "','"+filename+"','"+fileContent+"')";
		
		//New file
		if(Integer.valueOf(fileID)==-1){
			return "INSERT INTO Files(filename,fileContent)"
					+ "VALUES('" +filename+ "','"+fileContent+"')";
		}
		//File already exists, just update it
		else{
			//WRONG return "INSERT INTO Files(fileID, filename,fileContent)" + "VALUES('" + fileID + "','" +filename+ "','"+fileContent+"')";
			return "UPDATE Files SET fileContent = '" + fileContent + "' WHERE fileID = '" + fileID + "'";
		}
		//fileID will auto increment in Files table
	}
		//SAVEPERMISSION
	static String saveFilePermission(String fileID, String username, String isOwner){
		return "INSERT INTO Permissions(fileID, username, isOwner)"
				+ "VALUES('" + fileID + "','" + username + "', " + isOwner + ")";
		//fileID does not auto increment here
	}
	
	//These are used to add user permission
	//This combined with query all users determines if user exist and does not already have permission
	static String userAlreadyHavePermission(String username){
		return "SELECT fileID FROM Permissions WHERE username = '" + username +"'";
	}
	
	static String ownershipCheck(String username, String fileID){
		return "SELECT * FROM Permissions WHERE fileID = '" + fileID + "' AND username = '"+ username + "'";
	}
	
 	//Get FileID from filename and fileContent
	static String getFileID(String filename, String fileContent){
		return "SELECT fileID FROM Files WHERE filename = '"+filename+"' AND fileContent = '" + fileContent + "'";
	}
	
	static String removePermission(String fileID, String username){
		return "DELETE FROM Permissions WHERE fileID = '" + fileID + "' AND username = '" + username + "'";
	}
	
	static String getAllPermissions(String username){
		return "SELECT * FROM Permissions WHERE isOwner = " + "true AND username != '"+username+"'";
	}
	
	static String checkPermission(String username, String fileID){
		return "SELECT username FROM Permissions WHERE fileID = '" + fileID + "'AND username = '"+username+"'";
	}
	
	static String allUsersWithPermission(String fileID){
		return "SELECT username FROM Permissions WHERE fileID = '" + fileID+"'";
	}
}
