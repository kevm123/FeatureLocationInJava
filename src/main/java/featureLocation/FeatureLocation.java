package featureLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class FeatureLocation {

	static ParseFiles parser = new ParseFiles();
	static HashMap<String, ArrayList<Entity>> SearchSet = new HashMap<String, ArrayList<Entity>>();
	static ArrayList<Entity> Entities = new ArrayList<Entity>();
	
	static String[] reservedWords = {"abstract","assert","boolean","break","byte","case",
	                                 "catch","char","class","const","continue","default",
	                                 "double","do","else","enum","extends","false",
	                                 "final","finally","float","for","goto","if",
	                                 "implements","import","instanceof","int","interface","long",
	                                 "native","new","null","package","private","protected",
	                                 "public","return","short","static","strictfp","super",
	                                 "switch","synchronized","this","throw","throws","transient",
	                                 "true","try","void","volatile","while","get","set"};
	
	static Set<String> stopWords = new HashSet<String>(Arrays.asList(reservedWords));

	public static void main(String[] args) throws IOException {
		parser.parse();
		String input;
		String stemmed;

		Iterator it = parser.EntitySet.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			

			if (((Entity) pair.getValue()).getName().contains(".")) {
				input = ((Entity) pair.getValue()).getName()
						.substring(((Entity) pair.getValue()).getName().lastIndexOf(".") + 1);
			} else
				input = ((Entity) pair.getValue()).getName();
			
			System.out.println("\n\n-----" + input + "------\n");
			String[] list = input.split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])|[.]");
			Stemmer stemmer = new Stemmer();
			for (int i = 0; i < list.length; i++) {
				System.out.println(list[i]);
				list[i] = list[i].replaceAll("[^a-zA-Z0-9]", "");

				list[i] = list[i].toLowerCase();

				stemmed = stemmer.stem(list[i]);
				System.out.println(stemmed);
				System.out.println("------------------------");
				
				if(!(stopWords.contains(stemmed))){
				Entities = SearchSet.get(stemmed);
				if(Entities != null){
					Entities.add((Entity) pair.getValue());
				}else{
					Entities = new ArrayList<Entity>();
					Entities.add((Entity) pair.getValue());
				}
				
				SearchSet.put(stemmed, Entities);
				}

			}
		}
		
		it = SearchSet.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			
			System.out.println("\n\n-----" + pair.getKey() + "------\n");
			Entities = (ArrayList<Entity>) pair.getValue();
			for(int i = 0; i<Entities.size(); i++)
			{
				System.out.println(Entities.get(i).getName());
			}
		}

	}

}
