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

public class RemoveUserGUI extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private JPanel mainPanel;
	private JLabel topLabel;
	private JScrollPane scrollPane;
	private JList<String> list;
	private JPanel userPanel;
	private JLabel usernameLabel;
	private JTextField usernameTextField;
	private JPanel buttonPanel;
	private JButton removeButton;
	private JButton cancelButton;
	
	private MyClient client;
	private MyTab tab;
	
	public RemoveUserGUI(MyClient client, MyTab tab){
		super("Removing permission from user");
		this.client = client;
		this.tab = tab;
		instantiateComponents();
		createGUI();
		addActions();
	}
	
	private void instantiateComponents(){
		mainPanel = new JPanel();
		topLabel = new JLabel("Select a user:");
		scrollPane = new JScrollPane();
		list = new JList<String>();
		userPanel = new JPanel();
		usernameLabel = new JLabel("Username");
		usernameTextField = new JTextField(50);
		buttonPanel = new JPanel();
		removeButton = new JButton("Remove");
		cancelButton = new JButton("Cancel");
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
		buttonPanel.add(removeButton);
		buttonPanel.add(cancelButton);
	}
	
	private void addActions(){
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeUser();
				setVisible(false);
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
	
			
	//Receive all of the user's files from the server-client.run()-mainwindow-here 
	public void populateUsernames(Vector<String> usernames){
		list.setListData(usernames.toArray(new String[usernames.size()]));
	}
	
	private void removeUser(){
		String username = usernameTextField.getText();
		client.removeUserPermission(tab.getFileID(), username);
		this.setVisible(false);		
	}
}
