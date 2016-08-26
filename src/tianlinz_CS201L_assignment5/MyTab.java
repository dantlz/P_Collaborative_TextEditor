package tianlinz_CS201L_assignment5;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.undo.UndoManager;

import tianlinz_CS201L_assignment1.Assignment1;

public class MyTab extends JPanel {

	private static final long serialVersionUID = 1L;
	private UndoManager undoManager;
	
	//Spellcheck
	private static String keyboardFile = "NA";
	private static String wordListFile = "NA";
	private String currentError;
	private int spellCheckStart = 0;
	private Vector<Vector<String>> spellCheckResults;
	private Vector<String> errorsToIgnore;

	//A static vector of all tabs including "this." Use this to update other tabs' labels
	private static Vector<MyTab> allTabs = new Vector<MyTab>();
	
	//For Google Doc functionality
	private String filePath;
	private String fileID;
	private boolean isOwner;
	
	private MyFileSaveTimer myFileSaveTimer;
	private PermissionTimer permissionTimer;
	private MainWindow mainWindow;
	
	JTextArea textArea;
	JScrollPane scrollPane;
	HelperClasses.MyPanel spellCheckWindow;
		JPanel mainGroup;
			JLabel spellCheckLabel;
			JPanel buttonGroup;
				HelperClasses.MyButton ignoreButton;
				HelperClasses.MyButton addButton;
				JComboBox<String> suggestionsCB;
				HelperClasses.MyButton changeButton;
				HelperClasses.MyButton spellCheckCloseButton;
	HelperClasses.MyPanel configurationWindow;
		JPanel configGroup;
			JLabel wordListLabel;
			HelperClasses.MyButton wordListButton;
			JLabel keyboardLabel;
			HelperClasses.MyButton keyboardButton;
			HelperClasses.MyButton configCloseButton;
			
	public MyTab(MainWindow main){
		instantiateComponents();
		createGUI();
		addActions();
		this.setVisible(true);
		allTabs.add(this);
		mainWindow = main;
	}
	
	private void instantiateComponents(){
		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea,
	            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		spellCheckWindow = new HelperClasses.MyPanel();
			mainGroup = new JPanel();
				spellCheckLabel = new JLabel("Spelling: ");
				buttonGroup = new JPanel();
					ignoreButton = new HelperClasses.MyButton("Ignore", "resources/img/buttons/button.png");
					addButton = new HelperClasses.MyButton("Add", "resources/img/buttons/button.png");
					suggestionsCB = new JComboBox<String>();
					changeButton = new HelperClasses.MyButton("Change", "resources/img/buttons/button.png");
			spellCheckCloseButton = new HelperClasses.MyButton("Close", "resources/img/buttons/button.png");
		configurationWindow = new HelperClasses.MyPanel();
			configGroup = new JPanel();
				wordListLabel = new JLabel(" ");
				wordListButton = new HelperClasses.MyButton("Select Word List", "resources/img/buttons/button.png");
				keyboardLabel = new JLabel(" ");
				keyboardButton = new HelperClasses.MyButton("Select Keyboard", "resources/img/buttons/button.png");
				configCloseButton = new HelperClasses.MyButton("Close", "resources/img/buttons/button.png");
				
		fileID = "EMPTY";
		isOwner = false;
	}
	
	private void createGUI(){
		//Set up this
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(scrollPane);
		this.add(spellCheckWindow);
		this.add(configurationWindow);
		
		//Set up text area
		scrollPane.setVisible(true);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
				
		//Set up spell check window
		spellCheckWindow.setVisible(false);
		spellCheckWindow.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.anchor = GridBagConstraints.PAGE_START;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weighty = 1.0;   //request any extra vertical space
		spellCheckWindow.add(mainGroup, gridBagConstraints);
		gridBagConstraints.anchor = GridBagConstraints.PAGE_END; //bottom of space
		spellCheckWindow.add(spellCheckCloseButton, gridBagConstraints);
			mainGroup.setBorder(BorderFactory.createTitledBorder(getBorder(), "Spell Check", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, Constants.avenir));
			mainGroup.setLayout(new GridBagLayout());
			mainGroup.setOpaque(false);
			gridBagConstraints.anchor = GridBagConstraints.PAGE_START;
			mainGroup.add(spellCheckLabel, gridBagConstraints);
			gridBagConstraints.anchor = GridBagConstraints.PAGE_END;
			mainGroup.add(buttonGroup, gridBagConstraints);
				buttonGroup.setOpaque(false);
				buttonGroup.setLayout(new GridLayout(2, 2));
				buttonGroup.add(ignoreButton);
				buttonGroup.add(addButton);
				buttonGroup.add(suggestionsCB);
				buttonGroup.add(changeButton);
				
		//Set up configuration window
		configurationWindow.setVisible(false);
		configurationWindow.setLayout(new GridBagLayout());
		configurationWindow.setBorder(BorderFactory.createTitledBorder(getBorder(), "Configure", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, Constants.avenir));
		gridBagConstraints.anchor = GridBagConstraints.PAGE_START;
		configurationWindow.add(configGroup,gridBagConstraints);
			configGroup.setLayout(new BoxLayout(configGroup, BoxLayout.Y_AXIS));
			configGroup.setSize(new Dimension(configurationWindow.getWidth(), 0));
			configGroup.setOpaque(false);
			configGroup.add(wordListLabel);
			configGroup.add(wordListButton);
			configGroup.add(Box.createRigidArea(new Dimension(0,25)));
			configGroup.add(keyboardLabel);
			configGroup.add(keyboardButton);
		gridBagConstraints.anchor = GridBagConstraints.PAGE_END;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		configurationWindow.add(configCloseButton, gridBagConstraints);
		
		customizeTab();
	}
	
	private void customizeTab(){
		//Font
			HelperMethods.customizeFonts(this);
			//createGUI / borderFont
		
		//Scroll bar - MyScrollbarUI
			scrollPane.getVerticalScrollBar().setUI(new HelperClasses.MyScrollbarUI());
			
		//Selection
			textArea.setSelectionColor(Color.getHSBColor((float)0.5, (float)0.96, (float)0.63));
			
		//Buttons - MyButton
			
		//Combobox - MyComboBoxUI
			suggestionsCB.setUI(new HelperClasses.MyComboBoxUI());
			
		//Button Mouse Over - MyMouseAdapter		
			ignoreButton.addMouseListener(new HelperClasses.MyMouseAdapter());
			addButton.addMouseListener(new HelperClasses.MyMouseAdapter());
			changeButton.addMouseListener(new HelperClasses.MyMouseAdapter());
			spellCheckCloseButton.addMouseListener(new HelperClasses.MyMouseAdapter());
			wordListButton.addMouseListener(new HelperClasses.MyMouseAdapter());
			keyboardButton.addMouseListener(new HelperClasses.MyMouseAdapter());
			configCloseButton.addMouseListener(new HelperClasses.MyMouseAdapter());
			
		//Configure Window Background - MyPanel
		
		//Spell Check Window Background - MyPanel
	}
	
	private void addActions(){
		ignoreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ignoreError();
			}
		});;
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addToWordList();
			}
		});;
		suggestionsCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});;		
		changeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fixError();
			}
		});;
		spellCheckCloseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hideSpellCheckWIndow();
			}
		});;
		wordListButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseFile("wl");
			}
		});;		
		keyboardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseFile("kb");
			}
		});;		
		configCloseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				hideConfigurationWindow();
			}
		});;
	}

	public void runSpellCheck(){
		//Reset everything at the start of each spell check.
		spellCheckResults = null;
		spellCheckStart = 0;
		currentError = null;
		errorsToIgnore = new Vector<String>();
		
		//If the configuration files are ready
		if((!keyboardFile.equals("NA")) && (!wordListFile.equals("NA"))){
						
			textArea.setEditable(false);
			Assignment1 spellChecker= new Assignment1(textArea.getText(), keyboardFile, wordListFile);
			//The way .spellCheck() works is that for each element, which is a vector of strings: 
			//The first string is the error. The following strings, if any, are the suggestions
			spellCheckResults = spellChecker.spellCheck();
			
			//No errors
			if(spellCheckResults == null || spellCheckResults.size() == 0){
				JOptionPane.showMessageDialog(this, "Spell check complete. There were no errors in the text.");
				hideConfigurationWindow();
				textArea.setEditable(true);
				return;
			}
			
			//Start spell check
			populateSpellingSuggestions();
			showSpellCheckWindow();
		}
		else{
			JOptionPane.showMessageDialog(this, "Please finish configuring a keyboard file and a word list file first.");
			showConfigurationWindow();
		}	
	}

	private void populateSpellingSuggestions(){
		//Renable all GUI components
		suggestionsCB.setEnabled(true);
		suggestionsCB.removeAllItems();
		changeButton.setEnabled(true);
		
		//Take out errors to ignore added from the add button
		for(String entry: errorsToIgnore){
			for(int i = 0; i < spellCheckResults.size() ;i ++){
				if(spellCheckResults.get(i).get(0).equals(entry)){
					spellCheckResults.remove(i);
				}
			}
		}
		
		//No more errors
		if(spellCheckResults.size() == 0){
			JOptionPane.showMessageDialog(this, "Spell check complete. There are no more errors in the text.");
			hideSpellCheckWIndow();
			return;
		}
		
		//Populate the label
		currentError = spellCheckResults.get(0).get(0);
		spellCheckLabel.setText("Spelling: " + currentError);
		spellCheckResults.get(0).remove(0);
		
		//Find the word in text
		Vector<Integer> indexes = findIndexOfWord(currentError);
		
		//NEVER LET THIS HAPPEN
		if(indexes.size() < 2 || indexes.get(1) == 0){
			System.err.println("FATAL ERROR: " + currentError + " cound not be found.");
			JOptionPane.showMessageDialog(this, "FATAL ERROR: " + currentError + " cound not be found.");
			return;
		}
		
		//Highlight the word in text
		textArea.select(indexes.get(0), indexes.get(1));
		Highlighter highlighter = textArea.getHighlighter();

		highlighter.removeAllHighlights();
		try {
			highlighter.addHighlight(indexes.get(0), indexes.get(1), new DefaultHighlightPainter(Color.pink));
		} 
		catch (BadLocationException e) {
			JOptionPane.showMessageDialog(this, "Something went wrong while highlighting.");
		}
		
		//index[0] is start index[1] is space or text.length
		spellCheckStart = indexes.get(1)+1;
			
		//Suggestions is empty
		if(spellCheckResults.get(0).size() == 0){
			suggestionsCB.addItem("No suggestions");	
			suggestionsCB.setEnabled(false);
			changeButton.setEnabled(false);
		}
		//Suggestions not empty
		else{
			for(String suggestion: spellCheckResults.get(0)){
				suggestionsCB.addItem(suggestion);
			}
		}
	}
	
	private Vector<Integer> findIndexOfWord(String word){
		String text = HelperMethods.normalizeText(textArea.getText());
			
		Vector<Integer> result = new Vector<>();
		int startingIndex = spellCheckStart;
		
		for(int i = spellCheckStart; i <= text.length(); i++){
	
			if(i == text.length() || Character.isSpaceChar(text.charAt(i))){
				
				String temp = HelperMethods.removeNonAlpha(text.substring(startingIndex, i));
				
				if(temp.equals(word)){
					result.add(startingIndex);
					result.add(i);
					break;
				}
				
				startingIndex = i+1;
			}
		}

		return result;
	}

	private void ignoreError(){
		spellCheckResults.remove(0);
		populateSpellingSuggestions();
	}
	
	private void addToWordList(){
		//Since I don't examine the errors as I discover them, I just write to word list and add the word to add to a vector of words to result.
		//This way, the next time spell check is run it goes by the word list with the word added
		try (Writer writer = new BufferedWriter( new FileWriter(new File(wordListFile),true))) { //It only appends
			writer.write("\n" + currentError);
			writer.close();
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(this, "Could not add " + currentError + " to the word list, but future cases will be ignored");
		}	
		errorsToIgnore.add(currentError);
		spellCheckResults.remove(0);
		populateSpellingSuggestions();
	}
	
	private void fixError(){
		//Upper/Lower case and punctuation doesn't matter		
		String originalWord = textArea.getSelectedText();
		String newWord = (String)suggestionsCB.getSelectedItem();
		int difference = originalWord.length() - newWord.length();
		spellCheckStart = spellCheckStart-difference;
		
		textArea.replaceRange((String) suggestionsCB.getSelectedItem(), textArea.getSelectionStart(), textArea.getSelectionEnd());
		spellCheckResults.remove(0);
		populateSpellingSuggestions();
	}
		
	private void showSpellCheckWindow(){
		configurationWindow.setVisible(false);
		spellCheckWindow.setVisible(true);
	}
	
	private void hideSpellCheckWIndow(){
		//Reset GUI upon closing spell check window. 
		spellCheckWindow.setVisible(false);
		textArea.setEditable(true);
		textArea.getHighlighter().removeAllHighlights();
	}

	public void showConfigurationWindow(){
		spellCheckWindow.setVisible(false);
		configurationWindow.setVisible(true);
	}

	private void hideConfigurationWindow(){
		configurationWindow.setVisible(false);
	}
	
	private void chooseFile(String type){
		File openPath;
		JFileChooser fileChooser = new JFileChooser();
		FileFilter filter;
		boolean notDone = true;
		
		if(type == "wl")
			filter = new FileNameExtensionFilter("Word List File","wl"); 
		else //if(type == "kb")
			filter = new FileNameExtensionFilter("Keyboard File", "kb");
		
		fileChooser.setFileFilter(filter);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		
		while(notDone){
			int result = fileChooser.showOpenDialog(getParent());
			if (result == JFileChooser.APPROVE_OPTION) {
				openPath = fileChooser.getSelectedFile();
				
				//CHECK THE FILE EXTENSION JUST TO BE SURE
				if(openPath.exists() && openPath.isFile() && openPath.canRead()){
					if(type == "wl"){
						wordListFile = openPath.getAbsolutePath();
						for(MyTab sibling: allTabs){
							sibling.wordListLabel.setText(openPath.getName());
						}
					}
					else if(type == "kb"){
						keyboardFile = openPath.getAbsolutePath();
						for(MyTab sibling: allTabs){
							sibling.keyboardLabel.setText(openPath.getName());
						}
					}
					notDone = false;
				}
			}
			else if (result ==JFileChooser.CANCEL_OPTION) {
				notDone = false;
			}
		}		
	}
	
	private boolean modified(){
		boolean result = false;
		
		if(getFilePath() == "")
			return true;
		
		File currentFile = new File(getFilePath());
		if(currentFile.isFile() && currentFile.canRead()){	
			Scanner scnr;
			try {
				scnr = new Scanner(currentFile);
				String content = "";
				while(scnr.hasNextLine()){
					content = content + scnr.nextLine();
				}
				if(!textArea.getText().equals(content)){
					result = true;
				}
			} 
			catch (FileNotFoundException e) {
				result = true;
			}
		}
		else{
			result = true;
		}
		
		return result;
	}
	
	public void setUndoManager(UndoManager manager){
		undoManager = manager;
	}
	
	public UndoManager getUndoManager(){
		return undoManager;
	}
		
	public void setFilePath(String fp){
		filePath = fp;
	}

	public String getFilePath(){
		return filePath;
	}

	public boolean isSaved(){
		if( getFilePath() == "")
			return false;
		else if(modified())
			return false;
		//TODO IRRELEVANT Make it so that saving online also checks for modified and stuff
		
		return true;
	}

	public Document getDocument(){
		return textArea.getDocument();
	}
	
	public void setText(String text){
		textArea.setText(text);
	}

	public String getText(){
		return textArea.getText();
	}
		
	public void cut(){
		textArea.cut();
	}
	
	public void copy(){
		textArea.copy();
	}
	
	public void paste(){
		textArea.paste();
	}
	
	public void selectAll(){
		textArea.selectAll();
	}
	
	public void setCaretPosition(int pos){
		textArea.setCaretPosition(pos);
	}

	public void setFileID(String id){
		fileID = id;
	}
	
	public String getFileID(){
		return fileID;
	}

	public boolean isOwner(){
		return isOwner;
	}
	
	public void setIsOwner(boolean isOwner){
		this.isOwner = isOwner; 
	}
	
	public void startSaveTimer(){
		myFileSaveTimer = new MyFileSaveTimer(this, 5000);
		myFileSaveTimer.start();
	}

	public void autoSave(){
		mainWindow.autoSaveCurrentTab();
	}
	
	public void startPermissionTimer(){
		permissionTimer = new PermissionTimer(this, 500);
		permissionTimer.start();
	}
	
	public void checkPermission(){
		mainWindow.checkPermissionTab(this);
	}
	
	public void disconnect(boolean permissionStripped){
		if(myFileSaveTimer != null)
			myFileSaveTimer.terminate();
		if(permissionTimer != null)
			permissionTimer.terminate();
		filePath = "";
		fileID = "EMPTY";
		if(permissionStripped)
			//Create a notification on the removed user
			JOptionPane.showMessageDialog(this, "You have been removed.", "Permission Withdrew", JOptionPane.WARNING_MESSAGE);
	}
}
