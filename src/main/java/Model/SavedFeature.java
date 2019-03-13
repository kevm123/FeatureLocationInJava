package Model;

import java.util.ArrayList;

import featureLocation.Entity;

public class SavedFeature {
	
	private ArrayList<Entity> EntityList;
	
	public SavedFeature(){
		EntityList = new ArrayList<Entity>();
	}
	
	public void addFeature(Entity in){
		EntityList.add(in);
	}
	
	public ArrayList<Entity> getFeature(){
		return EntityList;
	}
	
	public void resetFeature(){
		EntityList.clear();
	}
	
	public void removeEntity(Entity in){
		if (EntityList.size() > 0) {
			for (int i = 0; i < EntityList.size(); i++) {
				if(EntityList.get(i).getName().equals(in.getName())){
					EntityList.remove(i);
				}
			}
		}
	}

}
