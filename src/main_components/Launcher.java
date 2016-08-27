package main_components;

import javax.swing.UIManager;

import server_components.MyServerGUI;

public class Launcher {
	public static void main(String[] args) {
		try{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		}
		catch(Exception e){
			System.out.println("Warning! Cross-platform L&F not used!");
		}
	
		MyClient loginWindow1 = new MyClient();
		MyClient loginWindow2 = new MyClient();
		MyServerGUI serverGUI = new MyServerGUI();
		loginWindow1.setVisible(true);
		loginWindow2.setVisible(true);
		serverGUI.setVisible(true);
	}
}
