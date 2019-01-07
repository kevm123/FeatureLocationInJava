package featureLocation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithIdentifier;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.YamlPrinter;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedTypeParameterDeclaration;
import com.github.javaparser.resolution.types.ResolvedPrimitiveType;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.SymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

public class ParseFiles {
		private static final String PATH = "../JPExamples/src/org/javaparser/samples";
	    //final File folder = new File("C:/Users/kev00_000/Desktop/College/4th Year Semester 1/FYP/workspace-jhotdraw");
		//final File folder = new File(""../JPExamples/src/org/javaparser/samples"");
		
		static HashMap<Integer, Entity> EntitySet = new HashMap<Integer, Entity>();
		static ArrayList<Entity> Entities = new ArrayList<Entity>();
		static ArrayList<VariableUsage> VariableUsage = new ArrayList<VariableUsage>();
		
		static CombinedTypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(), new JavaParserTypeSolver(PATH));
    	SymbolSolver symbolSolver = new SymbolSolver(typeSolver);
    	TypeDeclaration typeDeclaration;
				
	    public void parse() throws IOException {

	        CompilationUnit cu = null;
	        TypeSolver tSolver = new ReflectionTypeSolver( ) ;
	    	JavaSymbolSolver sSolver = new JavaSymbolSolver(tSolver) ;
	    	JavaParser.getStaticConfiguration( ).setSymbolResolver(sSolver) ;
	
				ArrayList<File> filesInFolder = parseDirect("../JPExamples/src/org/javaparser/samples");
			
				for (int i = 0; i < filesInFolder.size(); i++) {
					
					//System.out.println("------------------------------");
					
					System.out.println(filesInFolder.get(i).toString());
					cu = JavaParser.parse(filesInFolder.get(i));
					
					YamlPrinter printer = new YamlPrinter(true);
					//System.out.println(printer.output(cu));
					
					NodeList<TypeDeclaration<?>> ty = cu.getTypes();
					for (TypeDeclaration<?> typeDeclaration : ty) {
						Node node = (Node) typeDeclaration;
						processNode(node);					
					}
					
					int Ecount=1;
					for (int e=0; e<Entities.size(); e++) {
					   EntitySet.put(Ecount, Entities.get(e));
					   Ecount++;
					}
					
					for (TypeDeclaration<?> typeDeclaration : ty) {
						Node node = (Node) typeDeclaration;
						getCalls(node);
					}

									
					//System.out.println("------------------------------");
				}
				//printTest();
	    }
	    
	    public static ArrayList<File> parseDirect(String in) throws IOException
	    {
	    	ArrayList<File> filesInFolder = new ArrayList<File>();	
			Files.walk(Paths.get(in))
						.filter(Files::isRegularFile).forEach(filePath ->{
							String name=filePath.getFileName().toString();

						    if (name.endsWith(".java")){
						        filesInFolder.add(filePath.toFile());
						    }
						});;
						
						return (filesInFolder);

	    }
	    
	    @SuppressWarnings("deprecation")
		static void processNode(Node node) {
	    	boolean global=false;
	    	
	    	    	
	    	Entity e = new Entity();
	    	

	    	   if (node instanceof ClassOrInterfaceDeclaration) {
	    		   
	    		   Set<Entity> children = new HashSet<Entity>();
	    		   
	    		   e.setType(0);
	    		   e.setName(((ClassOrInterfaceDeclaration) node).getNameAsString());

	    		   for (Node child : node.getChildNodes()){
	    			  
	    			   Entity c = new Entity();
	    			   if(child instanceof FieldDeclaration){
	    				   c = getFieldChild(child, e.getName());
	    			   }
	    			   else{
	    				   c = getClassChild(child, e.getName()); 
	    			   }
	    			   if(c.getName()!=null){
	    				   c.setParent(e);
	    				   children.add(c);
	    			   }
	 	    	   }
	    		   e.setChildren(children);
	    		   Entities.add(e);
	    	      
	    	   }

	    	  

	    	}

		static Entity getFieldChild(Node child, String ParentName){
	    	
	    	Entity e = new Entity();
	    	
	    	 for(VariableDeclarator var: ((FieldDeclaration) child).getVariables()){
	    		 e.setName(ParentName+"."+var.getNameAsString());
	    	 
	    	if(child.toString().contains("static")){
	    		e.setType(1);
	    	}
	    	else
	    		e.setType(2);
	    	Entities.add(e);
	    	}
			return e;
	    }
	    
	    static Entity getClassChild(Node node, String ParentName){
	    	
	    	Entity e = new Entity();
	    	
	    	if(node instanceof MethodDeclaration)
	    	{
	    		 Set<Entity> children = new HashSet<Entity>();	    		   
	    		 e.setType(3);
	    		 e.setName(ParentName+"."+((MethodDeclaration) node).getNameAsString());	    		   
	    		 children = findVarChild(node, e, children);	    		
	    		 e.setChildren(children);
	    			
	    		 Entities.add(e);
	    	}

	    	else if(node instanceof ConstructorDeclaration)
			   {
				   e = getConstructor(node);
			   }
	    	
	    	
	    	return e;
	    }
	    
	    
	    private static Entity getConstructor(Node child) {
			Entity e = new Entity();
			
			e.setName(((ConstructorDeclaration) child).getNameAsString());
			e.setType(4);
			Entities.add(e);
			return e;
		}
	    
	    private static void getVariableUsage(Node child, Optional<Node> parent) {
			VariableUsage v = new VariableUsage();
			Node p = parent.get();
			Iterator it = EntitySet.entrySet().iterator();
			boolean one = false,two = false,three = false;
        	while(it.hasNext())
        	{
        		HashMap.Entry val = (HashMap.Entry)it.next();
        		
        		if((child.toString()).substring(0, (child.toString()).indexOf('.')).equals(((Entity) val.getValue()).getName()))
	        	{
        			Entity e = (Entity) val.getValue();
        			v.setCallee(e);
        			one = true;
        		}
        		if(((NodeWithSimpleName<ClassOrInterfaceDeclaration>) p).getNameAsString().equals(((Entity) val.getValue()).getName()))
	        	{
        			Entity e = (Entity) val.getValue();
        			v.setCaller(e);      	
        			two = true;
        		}
        		if(((FieldAccessExpr) child).getNameAsString().equals(((Entity) val.getValue()).getName()))
        		{
        			Entity e = (Entity) val.getValue();
        			v.setVariable(e); 
        			three = true;
        		}
        	}
        	if(one && two && three){
			VariableUsage.add(v);
        	}
		}
	    	
	    	static Set<Entity> findVarChild(Node node, Entity e, Set<Entity> children){
	    		
	    		for (Node child : node.getChildNodes()){
	    			   
	    			   if(child instanceof VariableDeclarator)
	    		    	{
	    				 
	    				   Entity c = getMethodChild(child); 
	    				   c.setParent(e);
	    				   c.setName(e.getName()+"."+c.getName());
	    				   children.add(c); 
	    				   Entities.add(c);
	    		    	}
	    			   else{
	    				   findVarChild(child, e, children);
	    			   }
	    			   
	    		}
	    		return children;
	    	}
	    	
	    	static void findVarUsage(Node node, Optional<Node> optional){
	    		
	    		Optional<Node> Parent = optional;
	    		for (Node child : node.getChildNodes()){
	    			if(child instanceof FieldAccessExpr)
	    			    	{
	    			    		getVariableUsage(child, Parent);
	    			    	}
	    			   else{
	    				   findVarUsage(child, Parent);
	    			   }
	    			   
	    		}
	    	}
	    
	    static Entity getMethodChild(Node node){

	    	Entity e = new Entity();
	    	 if(node instanceof VariableDeclarator)
	    	{
	    		   
	    		   e.setType(5);
	    		   e.setName(((VariableDeclarator) node).getNameAsString());
	    	}
	    	 else{
	    		 
	    		 for (Node child : node.getChildNodes()){
	    		      node = child;
	    		   }
	    	 }

	    	
			return e;
	    }
	    
	    public static void printTest()
	    {
	    	for(int i=0; i<Entities.size(); i++)
	    	{
	    		Entities.get(i).print();
	    	}
	    	for(int i=0; i<VariableUsage.size(); i++)
	    	{
	    		VariableUsage.get(i).print();
	    	}
	    }
	    
	    
	    /*child = nodes in file; 
	     *m = any child of type MethodDeclaration; 
	     * 	n = MethodCallExpr in m
	     *val = Hashmap Entries
	     *pair = Hashmap Entries*/
	    public static void getCalls(Node node)
	    {
	    	Set<Entity> out = new HashSet<Entity>();
	    	Set<Entity> in = new HashSet<Entity>();
	    	for (Node child : node.getChildNodes()){
  			  
	    	  //Find all Methods and use visitor to go through
 			  if(child instanceof MethodDeclaration){
 				 MethodDeclaration m = (MethodDeclaration)child;
 				 m.accept(new VoidVisitorAdapter<Void>(){

					@Override
	    	        public void visit(final MethodCallExpr n, final Void arg){
	    	        
					//visiting all Method calls see if its a call to a user defined method
	    	        if(!(n.getParentNodeForChildren().toString().contains("."))){
	    	        	
	    	        	Entity f = EntitySet.get(0);
	    	        	Entity s = EntitySet.get(0);
	    	        	boolean first = false, second = false;
	    	        	Iterator it = EntitySet.entrySet().iterator();
	    	        	
	    	        	//Using the set of all found entities, run through see if they match the method calling or caller
	    	        	while(it.hasNext())
	    	        	{
	    	        		HashMap.Entry val = (HashMap.Entry)it.next();
	    	        		
	    	        		if(m.getNameAsString().equals(((Entity) val.getValue()).getName()))
	     	        		{
	    	        			first = true;
	    	        			f = (Entity) val.getValue();
	    	        		}
	    	        		else if(((NodeWithSimpleName<ClassOrInterfaceDeclaration>) n.getParentNodeForChildren()).getNameAsString().equals(((Entity) val.getValue()).getName()))
	     	        		{
	    	        			second = true;
	    	        			s = (Entity) val.getValue();
	     	        		}
	    	        	}
	    	        		        	
	    	        	if(first && second)
	    	        	{
	    	        		f.addOutgoing(s);
	    	        		s.addIncoming(f);
	    	        	
	    	        	
	    	        	//Find the 2 entities again and update them with the incoming and outgoings
	    	        	Iterator it2 = EntitySet.entrySet().iterator();
	    	        	first = false;
	    	        	second = false;
		 	        	while(it2.hasNext())
		 	        	{
		 	        		HashMap.Entry pair = (HashMap.Entry)it2.next();
		 	        		if(f.getName().equals(((Entity) pair.getValue()).getName()))
		 	        		{
		 	        			first = true;
		 	        			int tempInt = (int) pair.getKey();
		 	        			f.setHasOutgoing(true);
		 	        			EntitySet.put(tempInt, f);
		 	           		}
		 	        		if(s.getName().equals(((Entity) pair.getValue()).getName()))
		 	        		{
		 	        			second = true;
		 	        			int tempInt = (int) pair.getKey();
		 	        			s.setHasIncoming(true);
		 	        			EntitySet.put(tempInt, s);
		 	        		}
		 	        			
		 	          	}
	    	        	}
	    	        	
	    	        }
	    	         super.visit(n, arg);
	    	            
	    	        }
	    	    }, null);
 				 
 				 findVarUsage(child, child.getParentNode());
 			  }
 			}
	    }
}


