package featureLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import Model.ResultModel;


public class BagOfWords {


	static ParseFiles parser = new ParseFiles();
	static HashMap<String, ArrayList<Entity>> SearchSet = new HashMap<String, ArrayList<Entity>>();
	static ArrayList<Entity> Entities = new ArrayList<Entity>();
	static ResultModel rm;

	static String[] reservedWords = { "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
			"class", "const", "continue", "default", "double", "do", "else", "enum", "extends", "false", "final",
			"finally", "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long",
			"native", "new", "null", "package", "private", "protected", "public", "return", "short", "static",
			"strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try",
			"void", "volatile", "while", "get", "set" };

	static Set<String> stopWords = new HashSet<String>(Arrays.asList(reservedWords));

	public static void create() throws IOException {
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

			//System.out.println("\n\n-----" + input + "------\n");
			String[] list = regexSplit(input);
			Stemmer stemmer = new Stemmer();
			for (int i = 0; i < list.length; i++) {
				stemmed = tidyStem(list[i]);
				list[i] = stemmed;
				//System.out.println("------------------------");

				if (!(stopWords.contains(stemmed))) {
					Entities = SearchSet.get(stemmed);
					if (Entities != null) {
						Entities.add((Entity) pair.getValue());
					} else {
						Entities = new ArrayList<Entity>();
						Entities.add((Entity) pair.getValue());
					}

					SearchSet.put(stemmed, Entities);
				}

			}
		}
	}
	
	public void search(String userSearch, ResultModel rmIn){

		rm = rmIn;
		rm.clearEntities();
		String stemmed;
		String[] list = regexSplit(userSearch);
		for (int i = 0; i < list.length; i++) {

			stemmed = tidyStem(list[i]);
			list[i] = stemmed;
			//System.out.println("------------------------");

		}

		for (int i = 0; i < list.length; i++) {
			Entities = SearchSet.get(list[i]);
			

			System.out.println("----" + list[i] + "----");
			if (Entities != null) {
				rm.addEntities(Entities);
				for (int k = 0; k < Entities.size(); k++) {
					System.out.println(Entities.get(k).getName());
				}
			} else {
				System.out.println("NO Data");
			}
		}
		rm.sortEntities(list);

	}

	private static String[] regexSplit(String in) {
		String[] list = in.split("(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])|[.]");
		return list;
	}

	private static String tidyStem(String in) {
		Stemmer stemmer = new Stemmer();
		//System.out.println(in);
		in = in.replaceAll("[^a-zA-Z0-9]", "");

		in = in.toLowerCase();

		String stemmed = stemmer.stem(in);
		in = stemmed;
		//System.out.println(stemmed);
		return stemmed;
	}

}
