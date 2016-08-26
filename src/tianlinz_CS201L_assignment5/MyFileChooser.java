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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MyFileChooser extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel mainPanel;
	private JLabel topLabel;
	private JScrollPane scrollPane;
	private JList<String> list;
	private JPanel filePanel;
	private JLabel fileLabel;
	private JTextField fileNameTextField;
	private JPanel buttonPanel;
	private JButton cancelButton;
	private JButton saveOrOpenButton;
	
	private MyClient client;
	private String content;
	private MainWindow mainWindow;
	private String operation;
	
	private Vector<String> filenames;
	private Vector<String> fileIDs;
	
	public MyFileChooser(MyClient client, String content, MainWindow main, String operation){
		super("File Chooser");
		this.client = client;
		this.content = content;
		this.mainWindow = main;
		this.operation = operation;
		instantiateComponents();
		createGUI();
		addActions();
	}
	
	private void instantiateComponents(){
		mainPanel = new JPanel();
		topLabel = new JLabel("Select a file:");
		scrollPane = new JScrollPane();
		list = new JList<String>();
		filePanel = new JPanel();
		fileLabel = new JLabel("File");
		fileNameTextField = new JTextField(50);
		buttonPanel = new JPanel();
		cancelButton = new JButton("Cancel");
		saveOrOpenButton = new JButton(operation);
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
		mainPanel.add(filePanel);
		filePanel.add(fileLabel);
		if(operation.equals("Open")){
			fileNameTextField.setEnabled(false);
		}
		if(operation.equals("Save")){
			list.setEnabled(false);
		}
		filePanel.add(fileNameTextField);
		filePanel.add(buttonPanel);
		buttonPanel.add(cancelButton);
		buttonPanel.add(saveOrOpenButton);
	}
	
	private void addActions(){
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		saveOrOpenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(operation.equals("Save")){
					save();
				}
				else{
					open();
				}
			}
		});
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				fileNameTextField.setText(list.getSelectedValue());
			}
		});
	}
		
	private void save(){
		//Check if the filename already exists
		boolean taken = false;
		for(String entry : filenames){
			if(entry.equals(fileNameTextField.getText())){
				taken = true;
			}
		}
		
		//Filename taken
		if(taken){
			JOptionPane.showMessageDialog(this, Constants.filenameTaken, "Save file online failed", JOptionPane.WARNING_MESSAGE);
			return;
		}
		//Filename invalid
		else if(fileNameTextField.getText().equals("")){
			JOptionPane.showMessageDialog(this, Constants.invalidFilename, "Save file online failed", JOptionPane.WARNING_MESSAGE);
			return;
		}
		//Save file through client
		else{
			String selectedFile = fileNameTextField.getText();
			MyTab currentTab = ((MyTab) mainWindow.getJTabbedPane().getSelectedComponent());
			String fileID;
			if(currentTab.getFileID().equals("EMPTY")){
				fileID = Integer.toString(-1);
			}
			else{
				fileID = currentTab.getFileID();
			}

			client.saveFileContent(fileID, selectedFile, content);
			this.setVisible(false);
		}
		
	}
	
	private void open(){
		//No file selected
		if(fileNameTextField.getText().equals("")){
			JOptionPane.showMessageDialog(this, Constants.noFileChosen, "Open file online failed", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		//Open file from online
		else{
			String selectedFile = fileNameTextField.getText();			
			int index = 0;
			for(int i = 0; i < filenames.size(); i++){
				if(filenames.get(i).equals(selectedFile)){
					index = i;
					break;
				}
			}
			client.openFile(selectedFile, fileIDs.get(index));
			this.setVisible(false);
		}
	}
	
	//Receive all of the user's files from the server-client.run()-mainwindow-here 
	public void populateFileNames(Vector<String> filenames){
		this.filenames = filenames;
		list.setListData(filenames.toArray(new String[filenames.size()]));
	}
	
	public void setFileIDs(Vector<String> fileIDs){
		this.fileIDs = fileIDs;
	}
}
