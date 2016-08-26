package tianlinz_CS201L_assignment5;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class OnlineDirectoryChooser extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private JPanel mainPanel;
	private JLabel topLabel;
	private JScrollPane scrollPane;
	private JList<String> list;
	private JPanel userPanel;
	private JLabel usernameLabel;
	private JTextField usernameTextField;
	private JPanel buttonPanel;
	private JButton myFilesButton;
	private JButton selectUserButton;
	private JButton cancelButton;
	
	private MyClient client;
	private MainWindow main;
	private MyFileChooser fileChooser;
	private Vector<String> ownerFileIDs;
	private Vector<String> ownerUsernames;

	public OnlineDirectoryChooser(MyClient client, MainWindow main){
		super("Choosing which online directory to open from");
		this.client = client;
		this.main = main;
		instantiateComponents();
		createGUI();
		addActions();
	}
	
	private void instantiateComponents(){
		mainPanel = new JPanel();
		topLabel = new JLabel("Select a user who has shared files with you:");
		scrollPane = new JScrollPane();
		list = new JList<String>();
		userPanel = new JPanel();
		usernameLabel = new JLabel("Username");
		usernameTextField = new JTextField(50);
		buttonPanel = new JPanel();
		myFilesButton = new JButton("My Files");
		selectUserButton = new JButton("Select User");
		cancelButton = new JButton("Cancel");
		fileChooser = new MyFileChooser(client, "", main, "open");
	}
	
	private void createGUI(){
		this.setResizable(false);
		this.setSize(700, 350);
		this.setLocation(400, 100);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(topLabel);
		mainPanel.add(scrollPane);
		scrollPane.setPreferredSize(new Dimension(300, 100));
		scrollPane.add(list);
		scrollPane.setViewportView(list);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		mainPanel.add(userPanel);
		userPanel.add(usernameLabel);
		userPanel.add(usernameTextField);
		usernameTextField.setEditable(false);
		userPanel.add(buttonPanel);
		buttonPanel.add(myFilesButton);
		buttonPanel.add(selectUserButton);
		buttonPanel.add(cancelButton);
	}
	
	private void addActions(){
		myFilesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectFromMyFiles();
				setVisible(false);
			}
		});
		selectUserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectUser();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				usernameTextField.setText(list.getSelectedValue());
			}
		});
	}
	
	public void parsePermissions(Vector<String> ownerIDs, Vector<String> ownerUsernames, Vector<String> currentIDs){
		Vector<String> allRelevantOwners = new Vector<String>();
		Vector<String> allRelevantIDs = new Vector<String>();

		for(String currentUserID: currentIDs){
			for(int i = 0; i < ownerIDs.size(); i ++){
				if(currentUserID.equals(ownerIDs.get(i))){
					allRelevantIDs.add(ownerIDs.get(i));
					allRelevantOwners.add(ownerUsernames.get(i));
				}
			}
		}

		this.ownerUsernames = allRelevantOwners;
		this.ownerFileIDs = allRelevantIDs;
		
		list.setListData(allRelevantOwners.toArray(new String[allRelevantOwners.size()]));
	}
	
	public MyFileChooser getMyFileChooser(){
		return fileChooser;
	}
	
	private void selectFromMyFiles(){
		fileChooser.setVisible(true);
		setVisible(false);
	}
	
	private void selectUser(){
		String username = usernameTextField.getText();
		Vector<Integer> indexes = new Vector<Integer>();
		
		for(int i = 0; i < ownerUsernames.size(); i++){
			if(username.equals(ownerUsernames.get(i))){
				indexes.add(i);
			}
		}
		
		Vector<String> selectedFileIDs = new Vector<String>();
		for(int i: indexes){
			selectedFileIDs.add(ownerFileIDs.get(i));
		}
		
		fileChooser.setFileIDs(ownerFileIDs);
		client.queryOtherUserFilenamesAndFileIDs(username, selectedFileIDs);
	}
	
	public void finishSelectingUser(Vector<String> othersFilenames, Vector<String> othersFileIDs, Vector<String> selectedFileIDs){
		Vector<String> selectedFileNames = new Vector<String>();
		
		for(int i = 0; i < selectedFileIDs.size(); i++){
			for(int j = 0; j < othersFileIDs.size(); j++){
				if(selectedFileIDs.get(i).equals(othersFileIDs.get(j))){
					selectedFileNames.add(othersFilenames.get(j));
				}
			}
		}
		
		fileChooser.populateFileNames(selectedFileNames);
		this.setVisible(false);
		fileChooser.setVisible(true);
	}
}
