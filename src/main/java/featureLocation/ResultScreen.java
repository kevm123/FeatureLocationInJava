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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import Model.*;
import javax.swing.SwingConstants;

public class ResultScreen extends JFrame implements ActionListener {
	private JTextField inputText = new JTextField(20);
	private JLabel name;
	private JButton selectBtn = new JButton("Select");
	private JButton returnBtn = new JButton("Return");
	private JButton saveBtn = new JButton("Save Current");
	private JButton undo = new JButton("Undo");
	private JButton removeBtn = new JButton("Remove");
	private JButton resetBtn = new JButton("Reset");
	private JButton newTab = new JButton("New Tab");
	private ArrayList<Entity> Entities;
	private DefaultListModel<String> model;
	private JList<String> list;
	private JScrollPane card1;
	private JPanel panel = new JPanel(new GridBagLayout());
	private JPanel card2 = new JPanel();
	private CardLayout cardLayout;
	private String[] values;
	private JLabel xLabel = new JLabel("X");
	private JLabel title = new JLabel();
	private JPanel buttons = new JPanel();
	private JComboBox<String> cb;
	private ArrayList<Entity> relatedEntities = new ArrayList<Entity>();
	private ArrayList<Entity> stack = new ArrayList<Entity>();
	private GridBagConstraints constraints = new GridBagConstraints();
	private SavedFeature savedFeature;
	private int searchOrFeature;
	private Entity currentEntity;

	public ResultScreen(ArrayList in, SavedFeature sF, int searchOrFeature) {

		this.searchOrFeature = searchOrFeature;
		savedFeature = sF;
		Entities = in;

		this.setUndecorated(true);
		model = new DefaultListModel<>();
		xLabel.setForeground(new Color(241, 57, 83));
		xLabel.setBounds(619, 0, 84, 27);
		Font font = new Font("Courier", Font.BOLD, 18);
		xLabel.setFont(font);

		for (int i = 0; i < Entities.size(); i++) {
			model.addElement(Entities.get(i).getName());
		}
		list = new JList<String>(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);

		selectBtn.addActionListener(this);
		selectBtn.setToolTipText("Show the selected entity in the current window");
		returnBtn.addActionListener(this);
		returnBtn.setToolTipText("Return to original entity search result screen");
		saveBtn.addActionListener(this);
		saveBtn.setToolTipText("Save the current entity to your feature");
		undo.addActionListener(this);
		undo.setToolTipText("Return to the previous entity information");
		removeBtn.addActionListener(this);
		removeBtn.setToolTipText("Remove selected entity from your feature");
		resetBtn.addActionListener(this);
		resetBtn.setToolTipText("Clear all saved entities from your feature");
		newTab.addActionListener(this);
		newTab.setToolTipText("Open selected entity information in a new window");
		showcard1();
	}

	private void showcard1() {

		stack.clear();
		panel.removeAll();
		panel.revalidate();
		panel.repaint();
		JScrollPane listScrollPane = new JScrollPane();
		listScrollPane.setViewportView(list);
		listScrollPane.setPreferredSize(new Dimension(250, 247));

		JPanel internal = new JPanel();
		buttons.removeAll();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
		internal.add(listScrollPane);
		buttons.add(selectBtn);
		buttons.add(returnBtn);
		buttons.add(newTab);

		if (searchOrFeature == 1) {
			buttons.add(removeBtn);
			buttons.add(resetBtn);
		}

		internal.add(buttons);
		internal.setLayout(new FlowLayout(FlowLayout.LEFT));
		internal.setPreferredSize(new Dimension(350, 250));

		card1 = new JScrollPane();
		card1.setViewportView(internal);

		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.insets = new Insets(2, 2, 2, 2);
		panel.add(card1, constraints);
		panel.setPreferredSize(new Dimension(400, 300));

		this.getContentPane().add(panel);

		constraints.anchor = GridBagConstraints.NORTH;
		constraints.gridx = 1;
		constraints.gridy = 0;
		xLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(xLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 0;
		title.setHorizontalAlignment(SwingConstants.LEFT);
		title.setText("Weighted Results");
		panel.add(title, constraints);

		pack();
		setLocationRelativeTo(null);
		card1.setVisible(true);
		card2.setVisible(false);
		xLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Close?",  JOptionPane.YES_NO_OPTION);
        		if (reply == JOptionPane.YES_OPTION)
        		{
        		   System.exit(0);
        		}
			}
		});
	}

	private void showcard2(Entity e) {

		currentEntity = e;

		if (stack.size() >= 1) {
			if (!(stack.get(stack.size() - 1).equals(e))) {
				stack.add(e);
			}
		} else
			stack.add(e);

		relatedEntities = e.getRelations();
		panel.removeAll();
		card2.removeAll();
		panel.revalidate();	
		card2.revalidate();
		card2.repaint();
		panel.repaint();
		values = e.print2();
		Font font = new Font("Courier", Font.BOLD, 12);
		String[] childrenArray = values[3].split("/");
		String[] incomingArray = values[5].split("/");
		String[] outgoingArray = values[4].split("/");
		JLabel NAME = new JLabel("NAME:");
		JLabel TYPE = new JLabel("TYPE:");
		JLabel PARENT = new JLabel("PARENT:");
		JLabel CHILDREN = new JLabel("CHILDREN:");
		JLabel INCOMING = new JLabel("INCOMING CALLS:");
		JLabel OUTGOING = new JLabel("OUTGOING CALLS:");
		JLabel name = new JLabel(values[0]);
		JLabel type = new JLabel(values[1]);
		JLabel parent = new JLabel(values[2]);
		JLabel children = new JLabel(values[3]);
		JLabel incoming = new JLabel(values[4]);
		JLabel outgoing = new JLabel(values[5]);

		ArrayList<String> optionalList = new ArrayList<String>();

		NAME.setFont(font);
		NAME.setForeground(Color.DARK_GRAY);
		TYPE.setFont(font);
		TYPE.setForeground(Color.DARK_GRAY);
		PARENT.setFont(font);
		PARENT.setForeground(Color.RED);
		CHILDREN.setFont(font);
		CHILDREN.setForeground(Color.BLUE);
		INCOMING.setFont(font);
		INCOMING.setForeground(Color.MAGENTA);
		OUTGOING.setFont(font);
		OUTGOING.setForeground(Color.GREEN);
		card2.add(NAME);
		card2.add(name);
		card2.add(TYPE);
		card2.add(type);
		card2.add(PARENT);
		card2.add(parent);
		card2.add(CHILDREN);

		optionalList.add(values[2]);
		for (int i = 0; i < childrenArray.length; i++) {
			card2.add(new JLabel(childrenArray[i]));
		}
		card2.add(INCOMING);
		for (int i = 0; i < incomingArray.length; i++) {
			card2.add(new JLabel(incomingArray[i]));
		}
		card2.add(OUTGOING);
		for (int i = 0; i < outgoingArray.length; i++) {
			card2.add(new JLabel(outgoingArray[i]));
		}

		Color[] colours = new Color[relatedEntities.size()];
		String[] choices = new String[relatedEntities.size()];
		for (int i = 0; i < relatedEntities.size(); i++) {
			if (relatedEntities.get(i) != null) {
				choices[i] = relatedEntities.get(i).getName();
				if(e.isA(0,relatedEntities.get(i)))
					colours[i] = Color.RED;
				else if(e.isA(1,relatedEntities.get(i)))
					colours[i] = Color.BLUE;
				else if(e.isA(2,relatedEntities.get(i)))
					colours[i] = Color.MAGENTA;
				else if(e.isA(3,relatedEntities.get(i)))
					colours[i] = Color.GREEN;
				else
					colours[i] = Color.BLACK;
				
			}
		}

		cb = new JComboBox<String>(choices);
		
		ComboBoxRenderer renderer = new ComboBoxRenderer(cb);

        renderer.setColors(colours);
        renderer.setStrings(choices);

        cb.setRenderer(renderer);
		
		buttons.removeAll();
		buttons.setLayout(new FlowLayout());
		buttons.add(selectBtn);
		buttons.add(saveBtn);
		buttons.add(newTab);
		buttons.add(returnBtn);
		buttons.add(undo);
		
		card2.setLayout(new BoxLayout(card2, BoxLayout.Y_AXIS));
		card2.setBorder(BorderFactory.createLineBorder(Color.blue));
		JScrollPane scroll = new JScrollPane(card2, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(250, 250));
		panel.setPreferredSize(new Dimension(400, 400));
		
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(scroll, constraints);
		constraints.gridx = 0;
		constraints.gridy = 2;
		panel.add(cb, constraints);
		constraints.gridx = 0;
		constraints.gridy = 3;
		panel.add(buttons, constraints);

		constraints.anchor = GridBagConstraints.NORTH;
		constraints.gridx = 1;
		constraints.gridy = 0;
		xLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(xLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 0;
		title.setHorizontalAlignment(SwingConstants.LEFT);
		title.setText("Entity Information");
		panel.add(title, constraints);
		

		pack();
		setLocationRelativeTo(null);

		card2.setVisible(true);
	}

	private void refresh() {
		ResultModel rm = new ResultModel(savedFeature.getFeature());
		ResultScreen rs = new ResultScreen(rm.getEntities(), savedFeature, 1);
		rs.setVisible(true);
		dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if (e.getActionCommand().equals("Select")) {
			if (card2.isVisible()) {
				int selected = cb.getSelectedIndex();
				showcard2(relatedEntities.get(selected));
			} else if (card1.isVisible()) {
				int index = list.getSelectedIndex();
				card1.setVisible(false);
				Entity ent = Entities.get(index);
				showcard2(ent);
			}
		}

		if (e.getActionCommand().equals("Return")) {
			if (card2.isVisible()) {
				if (searchOrFeature == 1) {
					refresh();
				} else {
					card2.setVisible(false);
					showcard1();
				}
			} else if (card1.isVisible()) {
				FeatureLocation.setUpSearch();
				dispose();
			}
		}

		if (e.getActionCommand().equals("Undo")) {
			if (stack.size() > 1) {
				int length = stack.size();
				stack.remove(length - 1);
				showcard2(stack.get(stack.size() - 1));
			} else {
				if (searchOrFeature == 1) {
					refresh();
				} else {
					card2.setVisible(false);
					showcard1();
				}
			}
		}

		if (e.getActionCommand().equals("Save Current")) {
			savedFeature.addFeature(currentEntity);
		}

		if (e.getActionCommand().equals("Remove")) {
			int index = list.getSelectedIndex();
			Entity ent = Entities.get(index);
			savedFeature.removeEntity(ent);
			refresh();
		}

		if (e.getActionCommand().equals("Reset")) {
			savedFeature.resetFeature();
			FeatureLocation.setUpSearch();
			dispose();
		}

		if (e.getActionCommand().equals("New Tab")) {
			if (card1.isVisible()) {
				int index = list.getSelectedIndex();
				Entity ent = Entities.get(index);
				NewTab nt = new NewTab(ent);
			} else {
				int selected = cb.getSelectedIndex();
				NewTab nt = new NewTab(relatedEntities.get(selected));
			}
		}
	}
}
