package featureLocation;

import java.io.IOException;

public class FeatureLocation {
	
	static ParseFiles parser = new ParseFiles();

	public static void main(String[]args) throws IOException{
		parser.parse();
		String input = "calc";
		for(int i=0; i<parser.Entities.size(); i++){
			if(parser.Entities.get(i).getName().contains(input))
			{
				parser.Entities.get(i).print();
			}
		}

	}

}
