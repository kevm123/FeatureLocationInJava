package featureLocation;

import java.awt.GridBagConstraints;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*; 
import Model.SearchModel;

public class SearchScreen extends JFrame implements ActionListener {
		private String input;
	 	private JTextField inputText = new JTextField(20);
	    private JLabel searchLabel = new JLabel("Search");
	    private JButton searchBtn = new JButton("Search"); 
	    private SearchModel sm;
	    private JPanel panel = new JPanel(new GridBagLayout());
	    
	    private JButton go = new JButton("Select Folder");
	    private String directoryString = "";
	    private static ParseFiles parser = new ParseFiles();


	    public SearchScreen(SearchModel in){
	    	sm = in;
	    	wordScreen();
	    	/*
	    	chooser.setCurrentDirectory(new java.io.File("."));
	        chooser.setDialogTitle("choosertitle");
	        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        chooser.setAcceptAllFileFilterUsed(false);
	    	JPanel directoryPanel = new JPanel(new GridBagLayout());
	    	GridBagConstraints constraints = new GridBagConstraints();
	        constraints.anchor = GridBagConstraints.WEST;
	        constraints.insets = new Insets(10, 10, 10, 10);
	         
	        constraints.gridx = 0;
	        constraints.gridy = 0;     
	        directoryPanel.add(selectDLabel, constraints);
	        
	        constraints.gridx = 1;
	        directoryPanel.add(chooser, constraints);
	        
	        constraints.gridx = 0;
	        constraints.gridy = 2;
	        constraints.gridwidth = 2;
	        constraints.anchor = GridBagConstraints.CENTER;
	        
	        directoryPanel.add(go, constraints);
	        
	        add(directoryPanel);

	        pack();
	        setLocationRelativeTo(null);
	        go.addActionListener(this);
*/
	    	
	    }
	    
	    private void wordScreen(){
	    	
	    	GridBagConstraints constraints = new GridBagConstraints();
	        constraints.anchor = GridBagConstraints.WEST;
	        constraints.insets = new Insets(10, 10, 10, 10);
	         
	        constraints.gridx = 0;
	        constraints.gridy = 0;     
	        panel.add(searchLabel, constraints);
	        
	        constraints.gridx = 1;
	        panel.add(inputText, constraints);
	        
	        constraints.gridx = 0;
	        constraints.gridy = 2;
	        constraints.gridwidth = 2;
	        constraints.anchor = GridBagConstraints.CENTER;
	        
	        panel.add(searchBtn, constraints);
	        
	        constraints.gridx = 0;
	        constraints.gridy = 3;
	        constraints.gridwidth = 2;
	        constraints.anchor = GridBagConstraints.CENTER;
	        panel.add(go, constraints);
	        
	        add(panel);

	        pack();
	        setLocationRelativeTo(null);
	        searchBtn.addActionListener(this);
	        go.addActionListener(this);
	    }

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			if (e.getActionCommand().equals("Search")){
	                input = inputText.getText().toString();
	                
	                if(directoryString.equals("")){
	                	JOptionPane.showMessageDialog(panel, "No Project Selected", "Warning",JOptionPane.WARNING_MESSAGE);
	                }
	                
	                else if(!input.isEmpty()){
	                	sm.setSearch(input);
	                	continueSearch();
	                }                
			}
			else if (e.getActionCommand().equals("Select Folder")){
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setCurrentDirectory(new java.io.File("."));
				jfc.setDialogTitle("Choose a File");
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jfc.setAcceptAllFileFilterUsed(false);

				int returned = jfc.showOpenDialog(this);
					if (returned == JFileChooser.APPROVE_OPTION) {
						if (jfc.getSelectedFile().isDirectory()) {
							directoryString = jfc.getSelectedFile().getAbsolutePath();
							System.out.println(directoryString);
						}
					}
					boolean ok=false;
					try {
						ok = parser.parse(directoryString);
					} catch (IOException x) {
						x.printStackTrace();
					}
					if(ok)
						System.out.println("ALL OK");
					else
						JOptionPane.showMessageDialog(panel, "Error with selected directory", "Warning",JOptionPane.WARNING_MESSAGE);
				}
			}

		
		private void continueSearch()
		{
			FeatureLocation.searchWord();
			dispose();
		}
}