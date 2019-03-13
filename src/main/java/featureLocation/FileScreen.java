package featureLocation;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FileScreen extends JFrame implements ActionListener {
	private JFrame frame = new JFrame();
	private String input;
    private JButton selectBtn = new JButton("Use Selected File");
    private JLabel xLabel = new JLabel("X");
    private JLabel qLabel = new JLabel("?");
    private JButton searchBtn = new JButton("Search"); 
    private JFileChooser chooser = new JFileChooser();
    private FeatureLocation fl = new FeatureLocation();


    public FileScreen(){
    	
    	this.setUndecorated(true);
    	JPanel panel = new JPanel(new GridBagLayout());
    	panel.setPreferredSize(new Dimension(250,125));
    	frame.add(panel);

    	GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
        
        constraints.gridx = 4;
        constraints.gridy = 0;     
        xLabel.setForeground(new Color(241,57,83));
        xLabel.setBounds(619,0,84,27);
        Font font = new Font("Courier", Font.BOLD,18);
        xLabel.setFont(font);
        panel.add(xLabel, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 0;     
        qLabel.setForeground(new Color(0,0,250));
        qLabel.setBounds(619,0,84,27);
        qLabel.setFont(font);
        panel.add(qLabel, constraints);
        
        constraints.gridx = 2;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(searchBtn, constraints);
        
        constraints.gridx = 2;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(new JLabel("(Please select projects root)"),constraints);
        
        add(panel);
        
        pack();
        setLocationRelativeTo(null);
        searchBtn.addActionListener(this);
        xLabel.addMouseListener(new MouseAdapter(){
        	public void mouseClicked(MouseEvent e){
        		System.exit(0);
        	}
        });
        
        qLabel.addMouseListener(new MouseAdapter(){
        	public void mouseClicked(MouseEvent e){
        		openFile();
        	}
        });
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
                
		if (e.getActionCommand().equals("Search")) {
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("select folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				input = chooser.getSelectedFile().toString();
				System.out.println(input);
				dispose();
				try {
					fl.startParse(input);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}

	}
	
	private void openFile(){
		try {
			
		    Path tempOutput = null;
		    String tempFile = "myFile";
		    tempOutput = Files.createTempFile(tempFile, ".pdf");
		    tempOutput.toFile().deleteOnExit();
		    InputStream is = getClass().getResourceAsStream("/InformationSheet.pdf");
		    Files.copy(is,tempOutput,StandardCopyOption.REPLACE_EXISTING);
		    if(Desktop.isDesktopSupported())
		    {
		        Desktop dTop = Desktop.getDesktop();
		        if(dTop.isSupported(Desktop.Action.OPEN))
		        {
		            dTop.open(tempOutput.toFile());
		        }
		    }

		    /*
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("InformationSheet.pdf").getFile());
			if (file.exists()) {

				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(file);
				} else {
					System.out.println("Awt Desktop is not supported!");
				}
				

			} */else {
				System.out.println("File is not exists!");
			}


		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void continueSearch()
	{
		FeatureLocation.searchWord();
		dispose();
	}
}
