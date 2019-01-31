package featureLocation;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Set;

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

public class ResultScreen extends JFrame implements ActionListener {
	private JTextField inputText = new JTextField(20);
	private JLabel name;
	private JButton selectBtn = new JButton("Select");
	private JButton returnBtn = new JButton("Return");
	private ArrayList<Entity> Entities;
	private DefaultListModel<String> model;
	private JList<String> list;
	private JScrollPane card1;
	private JPanel panel = new JPanel();
	private JPanel card2;
	private CardLayout cardLayout;

	public ResultScreen(ArrayList in){


		Entities = in;

		model = new DefaultListModel<>();

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

        panel.add(card1, "Card1");
        //panel.setPreferredSize(new Dimension(400, 200));
       
        this.getContentPane().add(panel);
        pack();
         setLocationRelativeTo(null);
        card1.setVisible(true);
	}
	
	private void showcard2(int index){
		Entity e = Entities.get(index);
		String[] values = e.print2();
		JLabel name = new JLabel(values[0]);
		JLabel type = new JLabel(values[1]);
		JLabel parent = new JLabel(values[2]);
		JLabel children = new JLabel(values[3]);
		JLabel incoming = new JLabel(values[4]);
		JLabel outgoing = new JLabel(values[5]);
		
		
		card2 = new JPanel();
		card2.add(name);
		card2.add(type);
		card2.add(parent);
		card2.add(children);
		card2.add(incoming);
		card2.add(outgoing);
		card2.add(returnBtn);
		card2.setLayout(new GridLayout(7,1));
        //panel.setPreferredSize(new Dimension(400, 200));
		
		panel.add(card2, "Card2");
		
       
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
            System.out.println("Index Selected: " + index);
            String s = (String) list.getSelectedValue();
            System.out.println("Value Selected: " + s);
            card1.setVisible(false);
            showcard2(index);
        }        
		
		if (e.getActionCommand().equals("Return"))
        {
			if(card2.isVisible()){
				card2.setVisible(false);
				showcard1();
			}
			else{
				FeatureLocation.setUpSearch();
				dispose();
			}
        }           

	}
}
