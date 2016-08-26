package tianlinz_CS201L_assignment5;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.metal.MetalScrollBarUI;

//For a customized look: Combination of overriding UI & setUI() and overriding paintcomponent() in component class
public class HelperClasses {

	public static class MyTabbedPane extends JTabbedPane{

		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g) {
			try {
				g.drawImage(ImageIO.read(new File("resources/img/tabbedpane/background.png")), 0, 0, getWidth(), getHeight(), null);
				g.setColor(Color.white);
				Font font = new Font("Avenir", Font.BOLD, 200);
				String text = "TextEdit";
				HelperMethods.drawStringCentered(getHeight(), getWidth(), font, text, g);
			} catch (IOException e) {
				e.printStackTrace();
			}
			super.paintComponent(g);

		}
		
	}
	
	public static class MyBackgroundPanel extends JPanel{
		private static final long serialVersionUID = 1L;
		@Override
		protected void paintComponent(Graphics g) {
			try {
				g.drawImage(ImageIO.read(new File("resources/img/tabbedpane/background.png")), 0, 0, getWidth(), getHeight(), null);
				g.setColor(Color.white);
				Font font = new Font("Avenir", Font.BOLD, 100);
				String text = "TextEdit";
				HelperMethods.drawStringCentered(getHeight()-280, getWidth(), font, text, g);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static class MyTabUI extends BasicTabbedPaneUI{
		Color white = Color.white;
		public MyTabUI() {
			highlight = white;
			lightHighlight = white;
			shadow = white;
			darkShadow = white;
			focus = white;
		}
		
		@Override
		protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,boolean isSelected) {
			if(isSelected)
				g.setColor(Color.getHSBColor((float)0.5, (float)0.96, (float)0.63));
			else
				g.setColor(Color.white);
			
			g.fillRect(x, y, w, h);
		}
	}
	
	public static class MyScrollbarUI extends MetalScrollBarUI {

        private Image imageThumb, imageTrack;
        
        MyScrollbarUI() {
            imageThumb = Toolkit.getDefaultToolkit().createImage("resources/img/scrollbar/scrollBar.jpg");
            imageTrack = new ImageHelper().createColorBlock(32, 32, Color.getHSBColor((float)0.5, (float)0.96, (float)0.63));
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {        	
            ((Graphics2D) g).drawImage(imageThumb,
                r.x, r.y, r.width, r.height, null);
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            ((Graphics2D) g).drawImage(imageTrack,
            		r.x, r.y, r.width, r.height, null);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
        	JButton decButton = new JButton();
        	decButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("resources/img/scrollbar/arrow_up.png")));
        	decButton.setPreferredSize(new Dimension(0, 18));
        	decButton.setOpaque(false);
        	decButton.setContentAreaFilled(false);
        	decButton.setBorderPainted(false);
        	return decButton;
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
         	JButton incButton = new JButton();
         	incButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("resources/img/scrollbar/arrow_down.png")));
         	incButton.setPreferredSize(new Dimension(0, 18));
         	incButton.setOpaque(false);
         	incButton.setContentAreaFilled(false);
         	incButton.setBorderPainted(false);
         	return incButton;        
        }
        private class ImageHelper {

        	public Image createColorBlock(int w, int h, Color c) {
        		BufferedImage bi = new BufferedImage(
        				w, h, BufferedImage.TYPE_INT_ARGB);
        		Graphics2D g2d = bi.createGraphics();
        		g2d.setPaint(c);
        		g2d.fillRect(0, 0, w, h);
        		g2d.dispose();
        		return bi;
        	}
        }	
	}
	
	public static class MyButton extends JButton{
		
		private static final long serialVersionUID = 1L;

		private String label;
		private String filePath1;
		private String filePath2;
		
		public MyButton(String label, String fp) {
			super(label);
			this.label = label;
			filePath1 = fp;
			setOpaque(false);
			setContentAreaFilled(false);
			setBorderPainted(false);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			try {
				g.drawImage(ImageIO.read(new File(filePath1)), 0, 0, getWidth(), getHeight(), null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			HelperMethods.drawStringCentered(getHeight(), getWidth(), Constants.avenir, label, g);
		}
		
		public void setBackground(String fp) {
			filePath2 = filePath1;
			filePath1 = fp;
			this.paintComponent(getGraphics());
		}
		
		public void revertBackground(){
			filePath1 = filePath2;
			filePath2 = "";
		}
	}
	
	public static class MyComboBoxUI extends BasicComboBoxUI{
		@Override
		protected JButton createArrowButton() {
			JButton button = new MyButton("", "resources/img/combobox/arrow.png");
			button.setOpaque(false);
			button.setContentAreaFilled(false);
			button.setBorderPainted(false);
			return button;
		}
	}
	
	public static class MyMouseAdapter extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e) {
			super.mouseEntered(e);
			if(((JButton)(e.getSource())).isEnabled())
				((MyButton) e.getSource()).setBackground("resources/img/buttons/button_rollOver.png");
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			super.mouseExited(e);
			if(((JButton)(e.getSource())).isEnabled())
				((MyButton) e.getSource()).revertBackground();
		}

	}

	public static class MyPanel extends JPanel{
		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g) {
			try {
				g.drawImage(ImageIO.read(new File("resources/img/sidePanels/background.jpg")), 0, 0, getWidth(), getHeight(), null);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
