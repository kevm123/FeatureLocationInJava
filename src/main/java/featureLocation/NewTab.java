package featureLocation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class NewTab extends JFrame {

	private JFrame frame = new JFrame();
	private JPanel panel = new JPanel(new GridBagLayout());
	private JPanel displayPanel = new JPanel();

	private JLabel xLabel = new JLabel("X");
	private JLabel title = new JLabel();

	public NewTab(Entity e) {

		ArrayList<Entity> relatedEntities = e.getRelations();
		displayPanel.removeAll();
		displayPanel.revalidate();
		displayPanel.repaint();
		panel.removeAll();
		panel.revalidate();
		panel.repaint();
		String[] values = e.print2();
		Font font = new Font("Courier", Font.BOLD, 12);
		Color red = new Color(241, 57, 83);
		String[] childrenArray = values[3].split("/");
		String[] incomingArray = values[4].split("/");
		String[] outgoingArray = values[5].split("/");
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

		ArrayList<String> optionalList = new ArrayList<String>();

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
		displayPanel.add(NAME);
		displayPanel.add(name);
		displayPanel.add(TYPE);
		displayPanel.add(type);
		displayPanel.add(PARENT);
		displayPanel.add(parent);
		displayPanel.add(CHILDREN);

		optionalList.add(values[2]);
		for (int i = 0; i < childrenArray.length; i++) {
			displayPanel.add(new JLabel(childrenArray[i]));
		}
		displayPanel.add(INCOMING);
		for (int i = 0; i < incomingArray.length; i++) {
			displayPanel.add(new JLabel(incomingArray[i]));
		}
		displayPanel.add(OUTGOING);
		for (int i = 0; i < outgoingArray.length; i++) {
			displayPanel.add(new JLabel(outgoingArray[i]));
		}

		String[] choices = new String[relatedEntities.size()];
		for (int i = 0; i < relatedEntities.size(); i++) {
			if (relatedEntities.get(i) != null) {
				choices[i] = relatedEntities.get(i).getName();
			}
		}

		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
		displayPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
		JScrollPane scroll = new JScrollPane(displayPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(300, 300));
		panel.setPreferredSize(new Dimension(400, 400));

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 1;
		panel.add(scroll, constraints);

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

		FrameDragListener frameDragListener = new FrameDragListener(frame);
		frame.addMouseListener(frameDragListener);
		frame.addMouseMotionListener(frameDragListener);

		frame.setUndecorated(true);
		xLabel.setForeground(new Color(241, 57, 83));
		xLabel.setBounds(619, 0, 84, 27);
		Font xFont = new Font("Courier", Font.BOLD, 18);
		xLabel.setFont(xFont);
		
		xLabel.addMouseListener(new MouseAdapter(){
        	public void mouseClicked(MouseEvent e){
        		frame.setVisible(false);
        		dispose();
        	}
        });

		pack();
		setLocationRelativeTo(null);

		displayPanel.setVisible(true);
		panel.setVisible(true);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}

	public static class FrameDragListener extends MouseAdapter {

		private final JFrame frame;
		private Point mouseDownCompCoords = null;

		public FrameDragListener(JFrame frame2) {
			this.frame = frame2;
		}

		public void mouseReleased(MouseEvent e) {
			mouseDownCompCoords = null;
		}

		public void mousePressed(MouseEvent e) {
			mouseDownCompCoords = e.getPoint();
		}

		public void mouseDragged(MouseEvent e) {
			Point currCoords = e.getLocationOnScreen();
			frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
		}
	}

}
