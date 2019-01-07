package featureLocation;

import java.util.Iterator;

public class VariableUsage {
	
	private Entity variable;
	private Entity caller;
	private Entity callee;
	
	public void setVariable(Entity in)
	{
		variable = in;
	}
	
	public Entity getVariable()
	{
		return variable;
	}
	
	public void setCaller(Entity in){
		caller = in;
	}
	
	public Entity getCaller(){
		return caller;
	}

	public void setCallee(Entity in){
		callee = in;
	}
	
	public Entity getCallee(){
		return callee;
	}
	
	public void print()
	{
		System.out.println("Name: "+getVariable().getName());
    	System.out.println("Caller: "+getCaller().getName());
    	System.out.println("Callee: "+getCallee().getName());
    	System.out.println();
		System.out.println("-------------------------------");
	}

}
