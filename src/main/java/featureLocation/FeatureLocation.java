package featureLocation;

import Model.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.alee.laf.WebLookAndFeel;

public class FeatureLocation {

	private static ParseFiles parser = new ParseFiles();
	private static SearchModel sm = new SearchModel();
	private static BagOfWords bagOfWords = new BagOfWords();
	private static ResultModel rm;
	private static String searchString;
	private static FileScreen fs = new FileScreen();
	private static SavedFeature savedFeature;
	private static Matrix matrix;
	private static LoadingScreen ls = new LoadingScreen();

	public static void main(String[] args) throws IOException {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				WebLookAndFeel.install();
			}
		});
		fs.setVisible(true);
	}
	
	public static void redo(){
		fs.setVisible(true);
	}
	
	public static void startParse(String input) throws IOException {
		matrix = new Matrix();
		savedFeature = new SavedFeature();
		parser = new ParseFiles();
		SearchModel sm = new SearchModel();
		BagOfWords bagOfWords = new BagOfWords();
		
		SwingWorker backThread = new SwingWorker()  
        { 
  
            @Override
            protected Object doInBackground() throws Exception  
            { 
            	boolean okParse = parser.parse(input, matrix);
				return okParse;
            } 
  
            @Override
            protected void done()  
            { 
            	boolean okParse;
				try {
					okParse = (boolean) get();
					if (okParse) {
						ls.setVisible(false);
	        			//matrix.create();
	        			//illustration.drawings(matrix.getMatrix(), matrix.getMethods());
	        			bagOfWords.create();
	        			setUpSearch();
	        		}
	        		else{
	        			fs.setVisible(true);
	        			JOptionPane.showMessageDialog(new JFrame(),"Invalid File Location","Warning",JOptionPane.ERROR_MESSAGE);
	        		}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
            	
            } 
        }; 
        
		ls.setVisible(true);
		backThread.execute();  
	}

	public static void setUpSearch() {
		rm = new ResultModel();
		SearchScreen ss = new SearchScreen(sm, savedFeature, matrix);
		ss.setVisible(true);
	}

	public static void searchWord() {
		searchString = sm.getSearch();
		bagOfWords.search(searchString, rm);
		ResultScreen rs = new ResultScreen(rm.getEntities(), savedFeature, 0);
		rs.setVisible(true);
	}

}
