package tianlinz_CS201L_assignment5;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import tianlinz_CS201L_assignment5.diff_match_patch.Patch;

public class MyServer extends Thread{
	
	private int port;
	private MyServerGUI gui;
	private ServerSocket ss;
	private MyServerTimer timer;
	
	private Vector<MyServerThread> serverThreads;
	private HashMap<String, String> currentFiles;
	private Vector<HashMap<String, String>> allUpdates;
	
//	private Vector<Integer> serverThreadIDs;
//	private Vector<Integer> receivedServerThreadIDs;
//	private int latestServerThreadID;

	public MyServer(int p, MyServerGUI g, int interval){
		serverThreads = new Vector<MyServerThread>();
		allUpdates = new Vector<HashMap<String, String>>();
		currentFiles = new HashMap<String, String>();
		port = p;
		gui = g;
		this.start();
		timer = new MyServerTimer(this, interval);
		timer.start();
		
//		serverThreadIDs = new Vector<Integer>();
//		receivedServerThreadIDs = new Vector<Integer>();
//		latestServerThreadID = 0;
	}
		
	//Interrupt server and all serverthreads
	public void stopServer(){
		for(MyServerThread mst: serverThreads){
			mst.closeSocket();
		}
		if(ss != null){
			try {
				ss.close();
			} catch (IOException e) {
				gui.fatalServerError();
				e.printStackTrace();
			}
		}
		
	}
	
	//Return the server GUI to update the log message
	public MyServerGUI getGUI(){
		return gui;
	}
	
	synchronized public void receiveBaseFile(String fileID, String content){
		currentFiles.put(fileID, content);
	}
	
	public void initiateClientUpdates(){
		allUpdates.clear();
				
		for(MyServerThread thread: serverThreads){
			thread.requestUpdateFromClient();
		}
	}
	
	//No need to match updates to threads/clients since they all receive the same thing.
	synchronized public void receiveUpdate(HashMap<String, String> update, int threadID){
		allUpdates.add(update);
		
		if(allUpdates.size() != 0){
			if(allUpdates.size() == serverThreads.size()){
				startMerge();
			}
		}
	}
	
	private void startMerge(){
		diff_match_patch dmp = new diff_match_patch();
		HashMap<String, String> newMap = new HashMap<String, String>();
		
		for(HashMap.Entry<String, String> entry: currentFiles.entrySet()){
			
			Vector<String> allContents = new Vector<String>();
			
			for(HashMap<String, String> update: allUpdates){
				if(update.containsKey(entry.getKey())){
					allContents.add(update.get(entry.getKey()));
				}
			}
			
			String merged = "";
			for(String content: allContents){
				LinkedList<Patch> patch = dmp.patch_make(entry.getValue(), content);
				if(merged.equals("")){
					merged = (String)(dmp.patch_apply(patch, entry.getValue())[0]);		
				}
				else{
					merged = (String)(dmp.patch_apply(patch, merged)[0]);		
				}
			}
						
			if(merged.equals("")){
				newMap.put(entry.getKey(), entry.getValue());
			}
			else{
				newMap.put(entry.getKey(), merged);
			}
		}
		
		currentFiles = newMap;
		
		sendBackMergedContent();
	}
	
	private void sendBackMergedContent(){
		for(MyServerThread thread: serverThreads){
			thread.receiveMergedContents(currentFiles);
		}
	}
	
	public void run(){
		//Create new server socket and add a new serverthread to the vector of threads whenever a new client connects
		ss = null;
		try {
			ss = new ServerSocket(port);
			//Continuously listens for new clients
			while (true) {
				Socket s = ss.accept();
				MyServerThread st = new MyServerThread(s, this);
				serverThreads.add(st);
				
//				st.setID(latestServerThreadID);
//				serverThreadIDs.add(latestServerThreadID);
//				latestServerThreadID++;
			}
		} 
		catch (SocketException se){
			System.out.println("Server socket closed");
		}
		catch (IOException ioe) {
			gui.fatalServerError();
			ioe.printStackTrace();
		} 
		finally {
			if (ss != null) {
				try {
					ss.close();
				} 
				catch (IOException ioe) {
					gui.fatalServerError();
					ioe.printStackTrace();	
				}
			}
		}

	}
}
