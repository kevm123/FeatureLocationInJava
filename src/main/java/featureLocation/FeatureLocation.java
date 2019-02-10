package featureLocation;

import Model.ResultModel;
import Model.SearchModel;
import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.SwingUtilities;

import com.alee.laf.WebLookAndFeel;

public class FeatureLocation {

	private static ParseFiles parser = new ParseFiles();
	private static SearchModel sm = new SearchModel();
	private static BagOfWords bagOfWords = new BagOfWords();
	private static ResultModel rm = new ResultModel();
	private static String searchString;

	public static void main(String[] args) throws IOException {
		 SwingUtilities.invokeLater ( new Runnable ()
	        {
	            public void run ()
	            {
	                WebLookAndFeel.install ();
	            }
	        } );
		parser.parse();
		bagOfWords.create();
		setUpSearch();
		
	}
	
	public static void setUpSearch(){
		SearchScreen ss = new SearchScreen(sm);
		ss.setVisible(true);
	}
	
	public static void searchWord(){
		searchString = sm.getSearch();
		bagOfWords.search(searchString, rm);
		ResultScreen rs = new ResultScreen(rm.getEntities());
		rs.setVisible(true);
		
	}

}
