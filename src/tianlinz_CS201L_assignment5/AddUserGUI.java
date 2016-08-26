package tianlinz_CS201L_assignment5;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class AddUserGUI extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private JPanel mainPanel;
	private JLabel topLabel;
	private JTextField usernameTextField;
	private JPanel buttonPanel;
	private JButton addButton;
	private JButton cancelButton;
	
	private MyClient client;
	private MyTab currentTab;
			
	public AddUserGUI(MyClient client, MyTab tab){
		super("Sharing file");
		instantiateComponents();
		createGUI();
		addActions();
		this.client = client;
		this.currentTab = tab;
	}
	
	private void instantiateComponents(){
		mainPanel = new JPanel();
		topLabel = new JLabel("Add User:");
		usernameTextField = new JTextField(50);
		buttonPanel = new JPanel();
		cancelButton = new JButton("Cancel");
		addButton = new JButton("Add");
	}
	
	private void createGUI(){
		this.setResizable(false);
		this.setSize(300, 100);
		this.setLocation(400, 100);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.add(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.add(topLabel);
		topLabel.setAlignmentX(SwingConstants.LEFT);
		mainPanel.add(usernameTextField);
		mainPanel.add(buttonPanel);
		buttonPanel.add(addButton);
		buttonPanel.add(cancelButton);
	}
	
	private void addActions(){
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addClicked();
			}
		});
	}
	
	private void addClicked(){
		String enteredUsername = usernameTextField.getText();
		//Check if the user exists
		client.permissionValidity1(enteredUsername, currentTab.getFileID());
		this.setVisible(false);
	}
}
