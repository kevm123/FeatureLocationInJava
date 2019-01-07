package featureLocation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;

public class Entity {
	
	private int type;
	private String name;
	private Entity parent;
	private Set<Entity> children;
	private Set<Entity> incoming = new HashSet<Entity>();
	private Set<Entity> outgoing  = new HashSet<Entity>();
	private boolean hasOutgoing=false, hasIncoming=false;
	
	
	public void setType(int in)
	{
		type = in;
	}
	
	public int getType()
	{
		return type;
	}
	
	public void setName(String in)
	{
		name = in;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setParent(Entity in)
	{
		parent = in;
	}
	
	public Entity getParent()
	{
		return parent;
	}
	
	public void setChildren(Set<Entity> in)
	{
		children = in;
	}
	
	public Set<Entity> getChildren()
	{
		return children;
	}
	
	public void setIncoming(Set<Entity> in)
	{
		incoming = in;
	}
	
	public Set<Entity> getIncoming()
	{
		return incoming;
	}
	
	public void addIncoming(Entity e)
	{
		incoming.add(e);
	}
	
	public void setOutgoing(Set<Entity> in)
	{
		outgoing = in;
	}
	
	public Set<Entity> getOutgoing()
	{
		return outgoing;
	}
	
	public void addOutgoing(Entity e)
	{
		outgoing.add(e);
	}
	
	public void setHasOutgoing(boolean in)
	{
		hasOutgoing = in;
	}
	
	public void setHasIncoming(boolean in)
	{
		hasIncoming = in;
	}
	
	public void print()
	{
		System.out.println("Name:\t"+name);
		System.out.println("Type:\t"+type);
		if(type != 0 && type != 6)
			System.out.println("Parent:\t"+parent.getName());
		if(type == 0 || type == 3){

			Iterator<Entity> it = children.iterator();
			System.out.print("Children:");
			while(it.hasNext()){
				System.out.println("\t"+it.next().getName());
			}
			
			if(type == 3 && hasOutgoing){
			Iterator<Entity> it2 = outgoing.iterator();
			System.out.print("Outgoing:");
			while(it2.hasNext()){
				System.out.println("\t"+it2.next().getName());
			}
			}
			
			if(type == 3 && hasIncoming){
				Iterator<Entity> it2 = incoming.iterator();
				System.out.print("Incoming:");
				while(it2.hasNext()){
					System.out.println("\t"+it2.next().getName());
				}
				}
   	     
		}
		System.out.println();
		System.out.println("-------------------------------");
	}
}

