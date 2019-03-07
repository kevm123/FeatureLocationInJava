package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import featureLocation.Entity;

public class Matrix {
	
	private Set<Entity> methods;
	private int[][] matrix;
	private int size;
	
	public Matrix(){
		methods = new LinkedHashSet<>();
	}
	
	public void addMethod(Entity e){
		methods.add(e);
	}
	
	public void create(){
		size = methods.size();
		ArrayList<Entity> methodList = new ArrayList<Entity>();
		methodList.addAll(methods);
		matrix = new int[size][size];
		for(int row=0; row<size; row++)
		{
			Entity temp = methodList.get(row);
			
			ArrayList<Entity> callList = new ArrayList<Entity>();
			callList.addAll(temp.getIncoming());
			for (int i = 0; i < callList.size(); i++) {
				int col = methodList.indexOf(callList.get(i));
				if (col >= 0) {
					matrix[row][col] = 1;
					matrix[col][row] = 1;
				}
			}
		}
	}


}
