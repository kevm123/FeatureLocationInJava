package featureLocation;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*; 
import javax.swing.*; 
import Model.SearchModel;

public class SearchScreen extends JFrame implements ActionListener {
		private JFrame frame = new JFrame();
		private String input=null;
	 	private JTextField inputText = new JTextField(20);
	    private JLabel searchLabel = new JLabel("Search");
	    private JLabel xLabel = new JLabel("X");
	    private JButton searchBtn = new JButton("Search"); 
	    private SearchModel sm;


	    public SearchScreen(SearchModel in){

	    	sm = in;
	    	this.setUndecorated(true);
	    	JPanel panel = new JPanel(new GridBagLayout());
	    	frame.add(panel);

	    	GridBagConstraints constraints = new GridBagConstraints();
	        constraints.anchor = GridBagConstraints.WEST;
	        constraints.insets = new Insets(10, 10, 10, 10);
	        
	        constraints.gridx = 3;
	        constraints.gridy = 0;     
	        xLabel.setForeground(new Color(241,57,83));
	        xLabel.setBounds(619,0,84,27);
	        Font font = new Font("Courier", Font.BOLD,18);
	        xLabel.setFont(font);
	        panel.add(xLabel, constraints);
	        
	        constraints.gridx = 1;
	        constraints.gridy = 0;     
	        panel.add(new JLabel("Search Screen"), constraints);
	         
	        constraints.gridx = 1;
	        constraints.gridy = 1;     
	        panel.add(searchLabel, constraints);
	        
	        constraints.gridx = 2;
	        panel.add(inputText, constraints);
	        
	        constraints.gridx = 1;
	        constraints.gridy = 3;
	        constraints.gridwidth = 2;
	        constraints.anchor = GridBagConstraints.CENTER;
	        panel.add(searchBtn, constraints);
	        
	        add(panel);

	        pack();
	        setLocationRelativeTo(null);
	        searchBtn.addActionListener(this);
	        xLabel.addMouseListener(new MouseAdapter(){
	        	public void mouseClicked(MouseEvent e){
	        		System.exit(0);
	        	}
	        });
	    }

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
	                input = inputText.getText().toString();
	                
	                if(!input.isEmpty() && !input.equals(null)){
	                	sm.setSearch(input);
	                	continueSearch();
	                }                

		}
		
		private void continueSearch()
		{
			FeatureLocation.searchWord();
			dispose();
		}
}