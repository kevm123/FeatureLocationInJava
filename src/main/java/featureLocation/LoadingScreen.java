package featureLocation;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoadingScreen extends JFrame  {
	
	private JFrame frame = new JFrame("Loading Screen");
	
	public LoadingScreen(){
		ImageIcon loading = new ImageIcon(getClass().getResource("/loader.gif"));

    	this.setUndecorated(true);
    	JPanel panel = new JPanel(new GridBagLayout());
    	panel.setPreferredSize(new Dimension(250,125));
    	frame.add(panel);
        
        panel.add(new JLabel("loading... ", loading, JLabel.CENTER));
        
        add(panel);
        
        pack();
        setLocationRelativeTo(null);
	}

}
