package tianlinz_CS201L_assignment5;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

public class MainWindow extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabs;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem newItem;
	private JMenuItem openItem;
	private JMenuItem saveItem;
	private JMenuItem closeItem;
	private JMenu editMenu;
	private JMenuItem undoItem;
	private JMenuItem redoItem;
	private JMenuItem cutItem;
	private JMenuItem copyItem;
	private JMenuItem pasteItem;
	private JMenuItem selectAllItem;
	private JMenu spellMenu;
	private JMenuItem runItem;
	private JMenuItem configureItem;
	private JMenu usersMenu;
	private JMenuItem addMenuItem;
	private JMenuItem removeMenuItem;
	
	private String username = "";
	private MyClient client;
	private OnlineDirectoryChooser onlineDirectoryChooser;
		
	public MainWindow(MyClient client){
		super("CSCI201 Text Editer");
		this.instantiateComponents();
		this.createGUI();
		this.addAccelerator();
		this.addListeners();
		this.client = client;
	}
		
	private void instantiateComponents(){
		tabs = new HelperClasses.MyTabbedPane();
		
        menuBar = new JMenuBar() {
			private static final long serialVersionUID = 1L;
			@Override
            public void paintComponent(Graphics g) {
                g.drawImage(Toolkit.getDefaultToolkit().getImage("resources/img/menu/menuBar.jpg"),0,0,this);
            }
        };		fileMenu = new JMenu("File");
		newItem = new JMenuItem("New");
		openItem = new JMenuItem("Open");
		saveItem = new JMenuItem("Save");
		closeItem = new JMenuItem("Close");
		editMenu = new JMenu("Edit");
		undoItem = new JMenuItem("Undo");
		redoItem = new JMenuItem("Redo");
		cutItem = new JMenuItem("Cut");
		copyItem = new JMenuItem("Copy"); 
		pasteItem = new JMenuItem("Paste");
		selectAllItem = new JMenuItem("Select All");
		spellMenu = new JMenu("SpellCheck");
		runItem = new JMenuItem("Run");
		configureItem = new JMenuItem("Configure");
		usersMenu = new JMenu("Users");
		addMenuItem = new JMenuItem("Add");
		removeMenuItem = new JMenuItem("Remove");
	}
	
	private void createGUI(){
		this.setResizable(false);
		this.setSize(900, 500);
		this.setLocation(400, 100);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setJMenuBar(menuBar);
		this.add(tabs);
		fileMenu.add(newItem);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(closeItem);
		menuBar.add(fileMenu);
		editMenu.add(undoItem);
		editMenu.add(redoItem);
		editMenu.addSeparator();
		editMenu.add(cutItem);
		editMenu.add(copyItem);
		editMenu.add(pasteItem);
		editMenu.addSeparator();
		editMenu.add(selectAllItem);
		menuBar.add(editMenu);
		spellMenu.add(runItem);
		spellMenu.add(configureItem);
		menuBar.add(spellMenu);
		usersMenu.add(addMenuItem);
		usersMenu.add(removeMenuItem);
		menuBar.add(usersMenu);
		editMenu.setVisible(false);
		spellMenu.setVisible(false);
		usersMenu.setVisible(false);
		
		customizeMainWindow();
	}
	
	private void customizeMainWindow(){
		//Background Logo - MyTabbedPane
		
		//Program Icon: Currently there is no windows program icon
		if(System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0){
			Class<?> applicationClass;
			try {
				applicationClass = Class.forName("com.apple.eawt.Application");
				Method getApplicationMethod = applicationClass.getMethod("getApplication");
				Method setDockIconMethod = applicationClass.getMethod("setDockIconImage", java.awt.Image.class);
				Object macOSXApplication = getApplicationMethod.invoke(null);
				setDockIconMethod.invoke(macOSXApplication, ImageIO.read(new File("resources/img/program/programIcon.png")));
			
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//Cursor
			this.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon("resources/img/icon/cursor.png").getImage(),
				new Point(0,0),"custom cursor"));
		
		//Font
			HelperMethods.customizeFonts(this);
			//myTab/customizeTab()
		
		//MenuBar
			//Done in menu bar instantiation
		
		//MenuItems
			newItem.setIcon(new ImageIcon("resources/img/menuitems/new.png"));
			openItem.setIcon(new ImageIcon("resources/img/menuitems/open.png"));
			saveItem.setIcon(new ImageIcon("resources/img/menuitems/save.png"));
			closeItem.setIcon(new ImageIcon("resources/img/menuitems/close.png"));
			undoItem.setIcon(new ImageIcon("resources/img/menuitems/undo.png"));
			redoItem.setIcon(new ImageIcon("resources/img/menuitems/redo.png"));
			cutItem.setIcon(new ImageIcon("resources/img/menuitems/cut.png"));
			copyItem.setIcon(new ImageIcon("resources/img/menuitems/copy.png"));
			pasteItem.setIcon(new ImageIcon("resources/img/menuitems/paste.png"));
			selectAllItem.setIcon(new ImageIcon("resources/img/menuitems/select.png"));
			runItem.setIcon(new ImageIcon("resources/img/menuitems/run.png"));
			configureItem.setIcon(new ImageIcon("resources/img/menuitems/configure.png"));
		
		//Scroll Bar
			//myTab/customizeTab()
			
		//Tabs - MyTabUI
			tabs.setUI(new HelperClasses.MyTabUI());
						
		//Selection
			//myTab/customizeTab()
	
		//Buttons & Combo box arrow
			//myTab/customizeTab()
			
		//Button mouse over
			//myTab/customizeTab()
			
		//Configuration background
			//myTab/customizeTab()
			
		//Spellcheck background
			//myTab/customizeTab()
						
	}
	
	private void addAccelerator(){
		int master = InputEvent.CTRL_DOWN_MASK;
		
		if(System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0)
			master = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, master));
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, master));
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, master));
		closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, master));
		undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, master));
		redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, master | InputEvent.SHIFT_DOWN_MASK));
		cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, master));
		copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, master));
		pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, master));
		selectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, master));
		runItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, master));
		configureItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, master | InputEvent.SHIFT_DOWN_MASK));
	}
	
	private void addListeners(){
		newItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newFile("");
			}
		});;
		openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});
		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}
		});
		closeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeFile();
			}
		});
		undoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MyTab currentTab = (MyTab) tabs.getSelectedComponent();
				undo(currentTab.getUndoManager());
			}
		});
		redoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MyTab currentTab = (MyTab) tabs.getSelectedComponent();
				redo(currentTab.getUndoManager());
			}
		});
		cutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cut();
			}
		});
		copyItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				copy();
			}
		});
		pasteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paste();
			}
		});
		selectAllItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectAll();
			}
		});
		runItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				runSpellCheck();
			}
		});
		configureItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				configureSpellCheck();
			}
		});
		addMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addUserPermission();
			}
		});
		removeMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeCLicked();
			}
		});
		tabs.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				tabsChanged();
			}
		});
	}
	
	private void tabsChanged(){
		if(tabs.getTabCount() == 0){
			usersMenu.setVisible(false);
			return;
		}
		MyTab currentTab = (MyTab) tabs.getSelectedComponent();
		if(!currentTab.getFileID().equals("EMPTY")){
			if(currentTab.isOwner()){
				usersMenu.setVisible(true);
			}
		}
		else{
			usersMenu.setVisible(false);
		}
	}
	
	public void setUsername(String un){
		username = un;
	}

	public JTabbedPane getJTabbedPane(){
		return tabs;
	}

	private void toggleEditingAndSpellingMenus(){
		if(editMenu.isVisible())
			editMenu.setVisible(false);
		else
			editMenu.setVisible(true);
		if(spellMenu.isVisible())
			spellMenu.setVisible(false);
		else
			spellMenu.setVisible(true);	
	}
	
	private void undo(UndoManager manager){
//		if(tabs.getTabCount() == 0)
//			return;
//		
//		if(manager.canUndo()){
//			manager.undo();
//		}
	}
	
	private void redo(UndoManager manager){
//		if(tabs.getTabCount() == 0)
//			return;
//		
//		if(manager.canRedo()){
//			manager.redo();
//		}
	}
	
	private void cut(){
		if(tabs.getTabCount() == 0)
			return;
		MyTab currentTab = (MyTab) tabs.getSelectedComponent();
		currentTab.cut();	
	}
	
	private void copy(){
		if(tabs.getTabCount() == 0)
			return;
		
		MyTab currentTab = (MyTab) tabs.getSelectedComponent();
		currentTab.copy();
	}
	
	private void paste(){
		if(tabs.getTabCount() == 0)
			return;

		MyTab currentTab = (MyTab) tabs.getSelectedComponent();
		currentTab.paste();
	}
	
	private void selectAll(){
		if(tabs.getTabCount() == 0)
			return;
		
		MyTab currentTab = (MyTab) tabs.getSelectedComponent();
		currentTab.selectAll();
	}

	private void runSpellCheck(){
		if(tabs.getTabCount() == 0)
			return;
		
		MyTab currentTab = (MyTab) tabs.getSelectedComponent();
		currentTab.runSpellCheck();
	}
	
	private void configureSpellCheck(){
		if(tabs.getTabCount() == 0)
			return;
		
		MyTab currentTab = (MyTab) tabs.getSelectedComponent();
		currentTab.showConfigurationWindow();
	}
	
	private void newFile(String fn){
		//New tab
		MyTab newTab = new MyTab(this);
		
		//Name
		if(fn.equals(""))
			newTab.setName("*Untitled");
		else
			newTab.setName(fn);
		
		//File path
		newTab.setFilePath("");
		
		//Undo and Redo
		UndoManager manager = new UndoManager();
		newTab.setUndoManager(manager);
		newTab.getDocument().addUndoableEditListener(new UndoableEditListener() {
			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				manager.addEdit(e.getEdit());
			}
		});
		newTab.getActionMap().put("Undo", new AbstractAction("Undo") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent evt) {
				undo(manager);
			}
		});
		newTab.getActionMap().put("Redo", new AbstractAction("Redo") {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent evt) {
				redo(manager);
	        }
	    });
				
		tabs.add(newTab);
		tabs.setSelectedComponent(newTab);
		
		//Edit & Spell Correction Menu
		if(!editMenu.isVisible())
			toggleEditingAndSpellingMenus();
	}
	
	private void closeFile(){
		if(tabs.getTabCount() == 0)
			return;
		
		MyTab currentTab = (MyTab) tabs.getSelectedComponent();
		
		//Not saved
		if(!currentTab.isSaved()){
			int result = JOptionPane.showOptionDialog(this, 
					"This file is not saved, would you like to save it?", 
					"Save", 
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE, 
					null, 
					null, 
					JOptionPane.DEFAULT_OPTION);
			if(result == 2 )
				return;
			else if(result == 1){
				currentTab.disconnect(false);
				tabs.remove(tabs.getSelectedComponent());
			}
			else if(result == 0){
				saveFile();
			}
		}
		else{
			currentTab.disconnect(false);
			tabs.remove(tabs.getSelectedComponent());
		}
		
		if(tabs.getTabCount() == 0)
			toggleEditingAndSpellingMenus();
	}

	private void openFile(){
		boolean openOnline = false;
		
		//If the username is "", then the user is not logged in, so only offline files are available
		if(!username.equals("")){
			Object[] options = {"Online", "Offline"};
			int result = JOptionPane.showOptionDialog(this, 
					"Where would you like to open the file?", 
					"Save...", 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE, 
					null, options, null);
			if(result==0){
				openOnline = true;
			}
		}
		
		if(openOnline){
			client.queryFilenamesAndIDs("_OPEN");
		}
		else {
			openFileOffline();
		}
	
	}
	
	public void openFileOnline(Vector<String> currentUserFilenames, Vector<String> currentUserFileIDs, Vector<String> ownerFileID, Vector<String> ownerUsernames){
		onlineDirectoryChooser = new OnlineDirectoryChooser(client, this);
		onlineDirectoryChooser.parsePermissions(ownerFileID, ownerUsernames, currentUserFileIDs);
		//My files should only display files where I'm the owner
		for(int i = 0; i < currentUserFileIDs.size(); i++){
			for(int j = 0; j <ownerFileID.size(); j++){
				if(ownerFileID.get(j).equals(currentUserFileIDs.get(i))){
					if(!ownerUsernames.get(j).equals(username)){
						currentUserFilenames.remove(i);
						currentUserFileIDs.remove(i);
					}
				}
			}
		}
		onlineDirectoryChooser.getMyFileChooser().populateFileNames(currentUserFilenames);
		onlineDirectoryChooser.getMyFileChooser().setFileIDs(currentUserFileIDs);
		onlineDirectoryChooser.setVisible(true);
	}
	
	public void populateOtherUsersFiles(Vector<String> othersFilenames, Vector<String> othersFileIDs, Vector<String> selectedFileIDs){
		onlineDirectoryChooser.finishSelectingUser(othersFilenames, othersFileIDs, selectedFileIDs);
	}
		
	public void finishOpeningOnline(String filename, String fileID, String fileContent){
		newFile(filename);
		MyTab currentTab = (MyTab) tabs.getComponentAt(((tabs.indexOfTab(filename))));
		if(currentTab == null)
			return;
		currentTab.setFilePath("ONLINE/"+filename);
		
		//Load the text of the file.
		currentTab.setText(fileContent);
		
		//Set file ID
		currentTab.setFileID(fileID);
		
		client.checkIsOwner(username, fileID);
	}
	
	public void setIsOwnerInDatabase(boolean isOwner){
		MyTab currentTab = (MyTab) tabs.getComponentAt(tabs.getSelectedIndex());
		currentTab.setIsOwner(isOwner);
		currentTab.startSaveTimer();
		if(!isOwner)
			currentTab.startPermissionTimer();
		tabsChanged();
	}
	
	private void openFileOffline(){
		File openPath;
		JFileChooser fileChooser = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Text files","txt"); 
		fileChooser.setFileFilter(filter);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(getParent());
		if (result == JFileChooser.APPROVE_OPTION) {
			openPath = fileChooser.getSelectedFile();

			//Check if the file is already open
			for(int i = 0; i < tabs.getTabCount(); i++ ){
				if(  ((MyTab)tabs.getComponentAt(i)).getFilePath().equals(openPath.getAbsolutePath())){
					JOptionPane.showMessageDialog(this, "The file: " + openPath.getName() + " is already open.");
					tabs.setSelectedIndex(i);
					return;
				}
			}
			
			newFile(openPath.getName());
			MyTab currentTab = (MyTab) tabs.getComponentAt(((tabs.indexOfTab(openPath.getName()))));
			
			if(currentTab == null)
				return;
			
			currentTab.setFilePath(openPath.getAbsolutePath());
			
			//Load the text of the file.
			Scanner scnr;
			try {
				scnr = new Scanner(openPath);
				
				if(scnr.hasNextLine()){
					String content = scnr.nextLine();
					while(scnr.hasNextLine()){
						content = content + "\n"+ scnr.nextLine();
					}
					currentTab.setText(content);
				}
				else{
					currentTab.setText("");
				}
				
				
				scnr.close();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this, "Failed to open file: " + openPath.getName());
			}
		}
	}
		
	private void saveFile(){
		if(tabs.getTabCount() == 0)
			return;	
		
		
		//Not owner = save locally
		MyTab currentTab = ((MyTab) tabs.getSelectedComponent());
		if((!currentTab.getFileID().equals("EMPTY")) && (!currentTab.isOwner())){
			saveFileOffline();
			return;
		}
		
		boolean saveOnline = false;
		
		//If the username is "", then the user is not logged in, so only offline saving is available
		if(!username.equals("")){
			Object[] options = {"Online", "Offline"};
			int result = JOptionPane.showOptionDialog(this, 
					"Where would you like to save the file?", 
					"Save...", 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE, 
					null, options, null);
			if(result==0){
				saveOnline = true;
			}
		}
		
		if(saveOnline){
			client.queryFilenamesAndIDs("_SAVE");
		}
		else {
			saveFileOffline();
		}
	}
	
	public void saveFileOnline(Vector<String> files, Vector<String> fileIDs){
		MyTab currentTab = ((MyTab) tabs.getSelectedComponent());
		currentTab.setEnabled(false);
		MyFileChooser fileChooser = new MyFileChooser(client, currentTab.getText(), this, "Save");
		fileChooser.populateFileNames(files);
		fileChooser.setFileIDs(fileIDs);
		fileChooser.setVisible(true);
	}
	
	public void finishSavingOnline(String fileID, String filename){
		JOptionPane.showMessageDialog(this, Constants.onlineSaveSuccessful, "File saved", JOptionPane.INFORMATION_MESSAGE);
		MyTab currentTab = ((MyTab) tabs.getSelectedComponent());
		currentTab.setEnabled(true);
		currentTab.setName(filename);
		currentTab.setFilePath("ONLINE/"+filename);
		currentTab.setFileID(fileID);
		currentTab.setIsOwner(true);
		tabs.setTitleAt(tabs.getSelectedIndex(), filename);
		tabsChanged();
	}
	
	private void saveFileOffline(){
		File savePath;
		JFileChooser fileChooser = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Text files","txt"); 
		fileChooser.setFileFilter(filter);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		MyTab currentTab = ((MyTab) tabs.getSelectedComponent());
		if(currentTab == null)
			return;
		
		if(currentTab.getFilePath().equals("")){
			
		}
		else if(currentTab.getFilePath().substring(0,6).equals("ONLINE")){
			
		}
		else{
			fileChooser.setSelectedFile(new File(currentTab.getFilePath()));
		}
			
		boolean done = false;
		while(!done){
			int result = fileChooser.showSaveDialog(getParent());
			if (result == JFileChooser.APPROVE_OPTION) {

				savePath = fileChooser.getSelectedFile();

				//File already exists
				if(new File(savePath.getAbsolutePath()).exists()){
					int userInput = JOptionPane.showConfirmDialog(getParent(), 
							savePath.getName()+" already exists as a file, do you want to replace it?", 
							"Confirm save as",
							JOptionPane.YES_NO_OPTION , 
							JOptionPane.WARNING_MESSAGE);
					if(userInput == 1){//User answered no
						continue;
					}
					else{
						done = true;
					}
				}

				String content = currentTab.getText();

				try (Writer writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(savePath.getAbsolutePath()), "utf-8"))) {
					writer.write(content);
					writer.flush();
					currentTab.setName(savePath.getName());
					currentTab.setFilePath(savePath.getAbsolutePath());
					tabs.setTitleAt(tabs.getSelectedIndex(), savePath.getName());
					done = true;
					writer.close();
				}
				catch(Exception e){
					JOptionPane.showMessageDialog(this, "Failed to open file: " + savePath.getName());
				}	
			}
			else{
				done = true;
			}
		}	
	}
	
	private void addUserPermission(){
		MyTab currentTab = (MyTab) tabs.getSelectedComponent();
		AddUserGUI addUserGUI = new AddUserGUI(client, currentTab);
		addUserGUI.setVisible(true);		
	}
	
	public void finishAddingUserPermission(boolean valid, String enteredUsername){
		if(!valid){
			JOptionPane.showMessageDialog(this, enteredUsername, "Failed to share file", JOptionPane.WARNING_MESSAGE);
			return;
		}		
	}
	
	private void removeCLicked(){
		MyTab currentTab = (MyTab) tabs.getSelectedComponent();
		client.getAllUsersWithPermission(currentTab.getFileID());
	}
	
	public void removeUserPermission(Vector<String> allUsernames){
		MyTab currentTab = (MyTab) tabs.getSelectedComponent();
		RemoveUserGUI removeUserGUI = new RemoveUserGUI(client, currentTab);
		removeUserGUI.populateUsernames(allUsernames);
		removeUserGUI.setVisible(true);
	}
	
	public HashMap<String, String> getAllOnlineTabsIDsAndContent(){
		HashMap<String, String> fileIDToContent = new HashMap<String, String>();
		for(int i = 0; i < tabs.getTabCount(); i++){
			MyTab tab = (MyTab)tabs.getComponent(i);
			if(!tab.getFileID().equals("EMPTY")){
				fileIDToContent.put(tab.getFileID(), tab.getText());
			}
		}
		return fileIDToContent;
	}
	
	public void updateAllContents(HashMap<String, String> map){
		for(HashMap.Entry<String, String> entry: map.entrySet()){
			for(int i = 0; i < tabs.getTabCount(); i++){
				MyTab tab = (MyTab)tabs.getComponent(i);
				if(tab.getFileID().equals(entry.getKey())){
					tab.setText(entry.getValue());
					int len = tab.getDocument().getLength();
					tab.setCaretPosition(len);
				}
			}
		}		
	}
	
	public void autoSaveCurrentTab(){
		MyTab currentTab = (MyTab) tabs.getSelectedComponent();
		client.saveFileContent(currentTab.getFileID(), currentTab.getName(), currentTab.getText());
	}
	
	public void checkPermissionTab(MyTab tab){
		client.checkPermission(tab.getFileID());
	}
	
	public void disconnectTab(String fileID){
		for(int i = 0 ; i < tabs.getTabCount(); i++){
			MyTab tab = ((MyTab)tabs.getComponent(i));
			if(tab.getFileID().equals(fileID)){
				tab.disconnect(true);
			}
		}
	}
}

//TODO 1. online file closed = stop auto-saving & remove from thread's needed files
//TODO 2. Save file = change the current version
//TODO 3. Close file = remove it from current version
