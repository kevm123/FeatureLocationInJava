package featureLocation;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.tartarus.snowball.ext.porterStemmer;

public class FeatureLocation {
	
	static ParseFiles parser = new ParseFiles();

	public static void main(String[]args) throws IOException{
		parser.parse();
		String input = "getFormativeHopefulGoodnessHappinessRevivalDefensibleFastingFizzingSkyAlienateDigitizerOperatorSensibleGrandful";
		
		Iterator it = parser.EntitySet.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            input = ((Entity)pair.getValue()).getName();
            System.out.println("\n\n-----"+input+"------\n");
            
            //(?=[A-Z])
            
		String[] list = input.split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])|[.]");
		Stemmer stemmer = new Stemmer();
		//porterStemmer stemmer = new porterStemmer();
		for(int i=0; i<list.length; i++)
		{
			System.out.println(list[i]);
			list[i] = list[i].replaceAll("[^a-zA-Z0-9]", "");
			
			list[i] = list[i].toLowerCase();
			
			System.out.println(stemmer.stem(list[i]));
			System.out.println("------------------------");
			
			/*stemmer.setCurrent(list[i]);
			if(stemmer.stem()){
				System.out.println(stemmer.getCurrent());
				list[i]=stemmer.getCurrent();
			}
			*/
		}
		}
		
		/*for(int i=0; i<parser.Entities.size(); i++){
			if(parser.Entities.get(i).getName().contains(input))
			{
				parser.Entities.get(i).print();
			}
		}
*/
	}

}
