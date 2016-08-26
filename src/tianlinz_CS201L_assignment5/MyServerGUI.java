package tianlinz_CS201L_assignment5;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MyServerGUI extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private boolean serverStarted = false;
	private int port;
	private int interval;
	private MyServer server;
	
	private JTextArea logArea;
	private JScrollPane scrollPane;
	private JButton startStopButton;
	
	public MyServerGUI(){
		super("TextEdit Server");
		instantiateComponents();
		createGUI();
		addActions();
		readConfigFile();
	}
	
	private void instantiateComponents(){
		logArea = new JTextArea();
		scrollPane = new JScrollPane();
		startStopButton = new JButton("Start");
	}
	
	private void createGUI(){
		this.setResizable(false);
		this.setSize(700, 350);
		this.setLocation(0, 100);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(false);
		
		logArea.setEditable(false);
		logArea.setFont(Constants.avenir);
		scrollPane.add(logArea);
		scrollPane.setViewportView(logArea);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(startStopButton, BorderLayout.SOUTH);
	}
	
	private void addActions(){
		startStopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleServer();
			}
		});
	}
	
	//Read configuration file from drive
	private void readConfigFile(){
		Scanner scnr;
		try {
			scnr = new Scanner(new File("resources/serverConfig.txt"));
			//Set port
			if(scnr.hasNext()){
				port = scnr.nextInt();
			}
			//Set update interval
			if(scnr.hasNext()){
				interval = scnr.nextInt();
			}
			scnr.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, Constants.invalidConfigFile, "Fatal Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
			e.printStackTrace();
		}
	}
	
	
	//Start and stop server
	private void toggleServer(){
		//Start server
		if(!serverStarted){
			server = new MyServer(port, this, interval);
			logMessage("Server started on port: " + port);
			startStopButton.setText("Stop");
			serverStarted = true;
		}
		//Stop server
		else{
			server.stopServer();
			logMessage("Server stopped.");
			startStopButton.setText("Start");
			serverStarted = false;
		}
	}
	
	//Append a message to the log textarea
	public void logMessage(String message){
		if(logArea.getText().equals("")){
			logArea.append(message);
		}
		else{
			logArea.append(System.lineSeparator() + message);
		}	
	}
	
	public void fatalServerError(){
		JOptionPane.showMessageDialog(this, Constants.fatalServerError, "Fatal Server Error", JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}
}
