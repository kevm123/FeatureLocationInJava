package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import featureLocation.Entity;

public class ResultModel {

	private ArrayList<Entity> Entities;
	private int[] Weights;
	private static String[] list;

	public ResultModel() {
		Entities = new ArrayList<Entity>();
	}

	public void addEntities(ArrayList in) {
		for (int i = 0; i < in.size(); i++) {
			Entities.add((Entity) in.get(i));
		}
	}

	public ArrayList getEntities() {
		return Entities;
	}

	public void clearEntities() {
		Entities.clear();
	}

	public static int getWeights(Entity in) {
		int count = 0;
		ArrayList<String> tempNames;
		tempNames = in.getAllNames();
		String entName, stem;
		int lastIndex;
		for (int i = 0; i < list.length; i++) {
			if(in.getName().toLowerCase().contains(list[i]))
				count=count+5;
		}
		for (int k = 0; k < tempNames.size(); k++) {
			entName = tempNames.get(k);
			for (int i = 0; i < list.length; i++) {
				stem = list[i];
				lastIndex = 0;
				while (lastIndex != -1) {

					lastIndex = entName.indexOf(stem, lastIndex);

					if (lastIndex != -1) {
						count++;
						lastIndex += stem.length();
					}
				}
			}
		}
		in.setWeight(count);
		return count;
	}

	public void sortEntities(String[] list) {
		this.list = list;
		removeDuplicates();
		Collections.sort(Entities, comparator);
	}

	private static Comparator<Entity> comparator = new Comparator<Entity>() {
		@Override
		public int compare(Entity o1, Entity o2) {
			int a = getWeights(o1);
			int b = getWeights(o2);
			if (a > b) {
				return -1;
			} else if (a < b) {
				return 1;
			}
			return 0;
		}
	};
	
	private void removeDuplicates(){ 
        Set<Entity> set = new LinkedHashSet<>(); 
  
        set.addAll(Entities);
        Entities.clear(); 
        Entities.addAll(set); 
	}

}
