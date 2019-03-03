package featureLocation;

import Model.ResultModel;
import Model.SearchModel;
import java.awt.EventQueue;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.alee.laf.WebLookAndFeel;

public class FeatureLocation {

	private static ParseFiles parser = new ParseFiles();
	private static SearchModel sm = new SearchModel();
	private static BagOfWords bagOfWords = new BagOfWords();
	private static ResultModel rm = new ResultModel();
	private static String searchString;
	private static FileScreen fs = new FileScreen();
	//private static String startTime;

	public static void main(String[] args) throws IOException {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				WebLookAndFeel.install();
			}
		});

		fs.setVisible(true);
	}
	
	public static void startParse(String input) throws IOException {
		/*DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		startTime = dateFormat.format(date);
		*/
		boolean okParse = parser.parse(input);
		if (okParse) {
			bagOfWords.create();
			setUpSearch();
		}
		else{
			fs.setVisible(true);
			JOptionPane.showMessageDialog(new JFrame(),"Invalid File Location","Warning",JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void setUpSearch() {
		/*DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		System.out.println(startTime+"\n"+dateFormat.format(date));
		JOptionPane.showMessageDialog(new JFrame(),startTime+"\n"+dateFormat.format(date),"Warning",JOptionPane.ERROR_MESSAGE);
		*/
		SearchScreen ss = new SearchScreen(sm);
		ss.setVisible(true);
	}

	public static void searchWord() {
		searchString = sm.getSearch();
		bagOfWords.search(searchString, rm);
		ResultScreen rs = new ResultScreen(rm.getEntities());
		rs.setVisible(true);
	}

}
