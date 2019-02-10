package featureLocation;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import Model.SearchModel;
import javax.swing.SwingConstants;

public class ResultScreen extends JFrame implements ActionListener {
	private JTextField inputText = new JTextField(20);
	private JLabel name;
	private JButton selectBtn = new JButton("Select");
	private JButton returnBtn = new JButton("Return");
	private ArrayList<Entity> Entities;
	private DefaultListModel<String> model;
	private JList<String> list;
	private JScrollPane card1;
	private JPanel panel = new JPanel(new GridBagLayout());
	private JPanel card2 = new JPanel();;
	private CardLayout cardLayout;
	private String[] values;
	private JLabel xLabel = new JLabel("X");
	private GridBagConstraints constraints = new GridBagConstraints();

	public ResultScreen(ArrayList in){


		Entities = in;

		this.setUndecorated(true);
		model = new DefaultListModel<>();
		xLabel.setForeground(new Color(241,57,83));
        xLabel.setBounds(619,0,84,27);
        Font font = new Font("Courier", Font.BOLD,18);
        xLabel.setFont(font);

		for (int i = 0; i < Entities.size(); i++) {
			model.addElement(Entities.get(i).getName());
		}
		list = new JList<String>(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);

        selectBtn.addActionListener(this);
        returnBtn.addActionListener(this);
        showcard1();
    }
	
	private void showcard1(){
		JScrollPane listScrollPane = new JScrollPane();
		listScrollPane.setViewportView(list);
		
		JPanel internal = new JPanel();
		internal.add(listScrollPane);
		internal.add(selectBtn);
		internal.add(returnBtn);
		internal.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
		

        card1 = new JScrollPane();
        card1.setViewportView(internal);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(2,2,2,2);
        panel.add(card1, constraints);
        panel.setPreferredSize(new Dimension(400,300));
       
        this.getContentPane().add(panel);
       
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridx = 1;
        constraints.gridy = 0;
        xLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        panel.add(xLabel, constraints);
        pack();
         setLocationRelativeTo(null);
        card1.setVisible(true);
        xLabel.addMouseListener(new MouseAdapter(){
        	public void mouseClicked(MouseEvent e){
        		System.exit(0);
        	}
        });
	}
	
	private void showcard2(int index){
		Entity e = Entities.get(index);
		card2.removeAll();
		values = e.print2();
		Font font = new Font("Courier", Font.BOLD,12);
		Color red = new Color(241,57,83);
		String[]childrenArray = values[3].split("/");
		String[]incomingArray = values[4].split("/");
		String[]outgoingArray = values[5].split("/");
		JLabel NAME = new JLabel("NAME:");
		JLabel TYPE = new JLabel("TYPE:");
		JLabel PARENT = new JLabel("PARENT:");
		JLabel CHILDREN = new JLabel("CHILDREN:");
		JLabel INCOMING = new JLabel("INCOMING METHOD CALLS:");
		JLabel OUTGOING = new JLabel("OUTGOING METHOD CALLS:");
		JLabel name = new JLabel(values[0]);
		JLabel type = new JLabel(values[1]);
		JLabel parent = new JLabel(values[2]);
		JLabel children = new JLabel(values[3]);
		JLabel incoming = new JLabel(values[4]);
		JLabel outgoing = new JLabel(values[5]);
		
		NAME.setFont(font);
		NAME.setForeground(red);
		TYPE.setFont(font);
		TYPE.setForeground(red);
		PARENT.setFont(font);
		PARENT.setForeground(red);
		CHILDREN.setFont(font);
		CHILDREN.setForeground(red);
		INCOMING.setFont(font);
		INCOMING.setForeground(red);
		OUTGOING.setFont(font);
		OUTGOING.setForeground(red);
		card2.add(NAME);
		card2.add(name);
		card2.add(TYPE);
		card2.add(type);
		card2.add(PARENT);
		card2.add(parent);
		card2.add(CHILDREN);
		for(int i=0; i<childrenArray.length; i++){
			card2.add(new JLabel(childrenArray[i]));
		}
		card2.add(INCOMING);
		for(int i=0; i<incomingArray.length; i++){
			card2.add(new JLabel(incomingArray[i]));
		}
		card2.add(OUTGOING);
		for(int i=0; i<outgoingArray.length; i++){
			card2.add(new JLabel(outgoingArray[i]));
		}
		card2.add(returnBtn);
		card2.setLayout(new BoxLayout(card2, BoxLayout.Y_AXIS));
		card2.setBorder(BorderFactory.createLineBorder(Color.blue));
        panel.setPreferredSize(new Dimension(400,300));
		
		constraints.gridx = 0;
        constraints.gridy = 0;
		panel.add(card2, constraints);
		
       
        pack();
		 setLocationRelativeTo(null);
        card2.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		if (e.getActionCommand().equals("Select"))
        {
            int index = list.getSelectedIndex();
            card1.setVisible(false);
            showcard2(index);
        }        
		
		if (e.getActionCommand().equals("Return"))
        {
			if(card2.isVisible()){
				card2.setVisible(false);
				showcard1();
			}
			else if(card1.isVisible()){
				FeatureLocation.setUpSearch();
				dispose();
			}
        }           

	}
}
