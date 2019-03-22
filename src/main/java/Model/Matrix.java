package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import featureLocation.Entity;

public class Matrix {
	
	private Set<Entity> methods;
	ArrayList<Entity> methodList;
	private double[][] matrix;
	private int size;
	
	public Matrix(){
		methods = new LinkedHashSet<>();
	}
	
	public void addMethod(Entity e){
		methods.add(e);
	}
	
	public ArrayList<Entity> getMethods(){
		return methodList;
	}
	
	public double[][] getMatrix(){
		return matrix;
	}
	
	public void create(){
		size = methods.size();
		methodList = new ArrayList<Entity>();
		methodList.addAll(methods);
		matrix = new double[size][size];
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
		clean();
	}
	
	private void clean()
	{
		for(int i=0; i<matrix.length; i++){
			boolean allZero=true;
			for(int j=0; j<matrix.length; j++){
				if(matrix[i][j] == 1.0)
					allZero=false;
			}
			if(allZero){
				int newSize = matrix.length-1;
				double[][] tempMatrix = new double[newSize][newSize];
				for(int k = 0; k < i; k++){
					for(int l = 0; l < i; l++)
						tempMatrix[k][l] = matrix[k][l];
				    for(int l = i+1; l < tempMatrix.length; l++)
				    	tempMatrix[k][l-1] = matrix[k][l];
				}
			    for(int k = i+1; k < tempMatrix.length; k++){
			    	for(int l = 0; l < i; l++)
						tempMatrix[k-1][l] = matrix[k][l];
				    for(int l = i+1; l < tempMatrix.length; l++)
				    	tempMatrix[k-1][l-1] = matrix[k][l];		
			    }
			    matrix = tempMatrix;
			    methodList.remove(i);
			    i=i-1;
			}
		}
	}
	
	private void print() {
		System.out.println("---------------------------------------------------");
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				System.out.print(matrix[i][j]);
			}
			System.out.println();
		}
		System.out.println("---------------------------------------------------");
	}

}
