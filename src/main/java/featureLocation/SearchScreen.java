package featureLocation;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*; 
import javax.swing.*;

import Model.Matrix;
import Model.ResultModel;
import Model.SavedFeature;
import Model.SearchModel;

public class SearchScreen extends JFrame implements ActionListener {
		private JFrame frame = new JFrame();
		private String input=null;
	 	private JTextField inputText = new JTextField(20);
	    private JLabel searchLabel = new JLabel("Search");
	    private JLabel xLabel = new JLabel("X");
	    private JButton searchBtn = new JButton("Search");
	    private JButton featureBtn = new JButton("Feature");
	    private JButton graphBtn = new JButton("Graphical View");
	    private JButton returnBtn = new JButton("Return");
	    private SavedFeature savedFeature;
	    private SearchModel sm;
	    private Matrix matrix;


	    public SearchScreen(SearchModel in, SavedFeature sF, Matrix matrix){
	    	
	    	this.matrix = matrix;
	    	savedFeature = sF;
	    	featureBtn.setToolTipText("Entites saved to saved feature");
	    	graphBtn.setToolTipText("View a graphical representation of method relationships");
	    	returnBtn.setToolTipText("Load in a new Java File");

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
	        
	        constraints.gridx = 1;
	        constraints.gridy = 4;
	        constraints.gridwidth = 2;
	        constraints.anchor = GridBagConstraints.CENTER;
	        panel.add(featureBtn, constraints);
	        
	        constraints.gridx = 1;
	        constraints.gridy = 5;
	        constraints.gridwidth = 2;
	        constraints.anchor = GridBagConstraints.CENTER;
	        panel.add(graphBtn, constraints);
	        
	        constraints.gridx = 1;
	        constraints.gridy = 6;
	        constraints.gridwidth = 2;
	        constraints.anchor = GridBagConstraints.CENTER;
	        panel.add(returnBtn, constraints);
	        
	        add(panel);

	        pack();
	        setLocationRelativeTo(null);
	        searchBtn.addActionListener(this);
	        featureBtn.addActionListener(this);
	        graphBtn.addActionListener(this);
	        returnBtn.addActionListener(this);
	        xLabel.addMouseListener(new MouseAdapter(){
	        	public void mouseClicked(MouseEvent e){
	        		int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Close?",  JOptionPane.YES_NO_OPTION);
	        		if (reply == JOptionPane.YES_OPTION)
	        		{
	        		   System.exit(0);
	        		}
	        	}
	        });
	    }

		@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("Search")) {

			input = inputText.getText().toString();

			if (!input.isEmpty() && !input.equals(null)) {
				sm.setSearch(input);
				continueSearch();
			}
		}

		if (e.getActionCommand().equals("Feature"))

		{
			if (savedFeature.getFeature().size() > 0) {
				ResultModel rm = new ResultModel(savedFeature.getFeature());
				ResultScreen rs = new ResultScreen(rm.getEntities(), savedFeature, 1);
				rs.setVisible(true);
			}
			else{
				JOptionPane.showMessageDialog(new JFrame(),"No Entities added to Saved Feature yet!","Warning",JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if (e.getActionCommand().equals("Graphical View"))

		{
			matrix.create();
			illustration.drawings(matrix.getMatrix(), matrix.getMethods());
		}
		
		if (e.getActionCommand().equals("Return"))
		{
			dispose();
			FeatureLocation.redo();
		}
	}

		
		private void continueSearch()
		{
			FeatureLocation.searchWord();
			dispose();
		}
}