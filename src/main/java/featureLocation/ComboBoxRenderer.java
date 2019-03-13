package featureLocation;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class ComboBoxRenderer extends JPanel implements ListCellRenderer {
	
	private Color[] colours;
    private String[] strings;

    JPanel textPanel;
    JLabel text;
	
	public ComboBoxRenderer(JComboBox combo){
		textPanel = new JPanel();
        textPanel.add(this);
        text = new JLabel();
        text.setOpaque(true);
        text.setFont(combo.getFont());
        textPanel.add(text);
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean callHasFocus) {
		text.setText(value.toString());
        if (index>-1) {
            text.setForeground(colours[index]);
        }
        return text;
	}

	public void setColors(Color[] colours) {
		this.colours = colours;
		
	}

	public void setStrings(String[] choices) {
		this.strings = choices;
		
	}

}
