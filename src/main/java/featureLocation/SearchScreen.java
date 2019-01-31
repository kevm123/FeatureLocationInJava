package featureLocation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*; 
import javax.swing.*; 
import Model.SearchModel;

public class SearchScreen extends JFrame implements ActionListener {
		private String input;
	 	private JTextField inputText = new JTextField(20);
	    private JLabel searchLabel = new JLabel("Search");
	    private JButton searchBtn = new JButton("Search"); 
	    private SearchModel sm;


	    public SearchScreen(SearchModel in){

	    	sm = in;
	    	JPanel panel = new JPanel(new GridBagLayout());

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
	        
	        add(panel);

	        pack();
	        setLocationRelativeTo(null);
	        searchBtn.addActionListener(this);
	    }

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
	                input = inputText.getText().toString();
	                
	                if(!input.isEmpty()){
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