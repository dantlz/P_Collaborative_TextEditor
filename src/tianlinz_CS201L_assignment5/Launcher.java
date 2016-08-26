package tianlinz_CS201L_assignment5;

import javax.swing.UIManager;

//This should be the only main() method. Use this to run the project
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