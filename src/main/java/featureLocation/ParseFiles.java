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
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

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

import Model.Matrix;

public class ParseFiles{
	private static String PATH ;

	static HashMap<String, Entity> EntitySet = new HashMap<String, Entity>();
	static ArrayList<Entity> Entities = new ArrayList<Entity>();
	static ArrayList<VariableUsage> VariableUsage = new ArrayList<VariableUsage>();
	private static String root = "";
	private static ArrayList<String> relationships= new ArrayList<String>();
	private int count;
	private static Entity file;
	private static Matrix matrix;

	public boolean parse(String in, Matrix matrix) throws IOException{
		
		EntitySet.clear();
		Entities.clear();
		VariableUsage.clear();
		relationships.clear();
		this.matrix = matrix;
		CompilationUnit cu = null;
		PATH = in + "\\src";
		File baseFile = new File(PATH);
		if (baseFile.exists()) {

			CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
			combinedTypeSolver.add(new ReflectionTypeSolver(false));
			combinedTypeSolver.add(new JavaParserTypeSolver(new File(PATH)));
			JavaParser.getStaticConfiguration().setSymbolResolver(new JavaSymbolSolver(combinedTypeSolver));

			ArrayList<File> filesInFolder = parseDirect(PATH);

			for (int i = 0; i < filesInFolder.size(); i++) {
				
				file = new Entity();
				file.setName(filesInFolder.get(i).getName());
				file.setType(6);

				System.out.println(i + "/" + filesInFolder.size());
				cu = JavaParser.parse(filesInFolder.get(i));

				String[] split = (filesInFolder.get(i).toString()).split("\\\\");
				Entities.clear();

				root = "";
				for (int k = 0; k < split.length - 1; k++) {
					root += (split[k] + ".");
				}

				// YamlPrinter printer = new YamlPrinter(true);
				// System.out.println(printer.output(cu));

				NodeList<TypeDeclaration<?>> ty = cu.getTypes();
				for (TypeDeclaration<?> typeDeclaration : ty) {
					Node node = (Node) typeDeclaration;
					processNode(node);
				}

				String key;
				for (int e = 0; e < Entities.size(); e++) {
					key = root + Entities.get(e).getName();
					EntitySet.put(key, Entities.get(e));
				}
				
				Entity p;
				if(EntitySet.get((cu.getPackageDeclaration()).get().getNameAsString()) != null){
					p = EntitySet.get((cu.getPackageDeclaration()).get().getNameAsString());
					p.addChildren(file);
				}
				else{
					p = new Entity();
					p.setName((cu.getPackageDeclaration()).get().getNameAsString());
					p.setType(7);
					p.addChildren(file);
					EntitySet.put(p.getName(), p);
				}
				
				file.setParent(p);
				EntitySet.put(filesInFolder.get(i).getAbsolutePath().replace("\\", "."), file);
			}

			matchRelations();

			for (int i = 0; i < filesInFolder.size(); i++) {
				System.out.println(i + "/" + filesInFolder.size());

				String[] split = (filesInFolder.get(i).toString()).split("\\\\");
				root = "";
				for (int k = 0; k < split.length - 1; k++) {
					root += (split[k] + ".");
				}

				cu = JavaParser.parse(filesInFolder.get(i));
				NodeList<TypeDeclaration<?>> ty = cu.getTypes();
				for (TypeDeclaration<?> typeDeclaration : ty) {
					Node node = (Node) typeDeclaration;
					getCalls(node);
				}

			}
			System.out.println(EntitySet.size());
			//printTest();
			
			if(EntitySet.size()>0){
				return true;
			}
			else
				return false;
			
		} else {
			System.out.println("Invalid Location");
			return false;
		}
	}

	public static ArrayList<File> parseDirect(String in) throws IOException {
		ArrayList<File> filesInFolder = new ArrayList<File>();
		Files.walk(Paths.get(in)).filter(Files::isRegularFile).forEach(filePath -> {
			String name = filePath.getFileName().toString();

			if (name.endsWith(".java")) {
				filesInFolder.add(filePath.toFile());
			}
		});
		;

		return (filesInFolder);

	}

	@SuppressWarnings("deprecation")
	static void processNode(Node node) {
		boolean global = false;

		Entity e = new Entity();

		if (node instanceof ClassOrInterfaceDeclaration) {

			Set<Entity> children = new HashSet<Entity>();

			e.setType(0);
			e.setName(((ClassOrInterfaceDeclaration) node).getNameAsString());
			e.setParent(file);
			file.addChildren(e);

			for (Node child : node.getChildNodes()) {

				Entity c = new Entity();
				if (child instanceof FieldDeclaration) {
					c = getFieldChild(child, e.getName());
				} else {
					c = getClassChild(child, e.getName());
				}
				if (c.getName() != null) {
					c.setParent(e);
					children.add(c);
				}
			}
			e.setChildren(children);
			Entities.add(e);

		}

	}

	static Entity getFieldChild(Node child, String ParentName) {

		Entity e = new Entity();

		for (VariableDeclarator var : ((FieldDeclaration) child).getVariables()) {
			e.setName(ParentName + "." + var.getNameAsString());

			if (child.toString().contains("static")) {
				e.setType(1);
			} else
				e.setType(2);
			Entities.add(e);
		}
		return e;
	}

	static Entity getClassChild(Node node, String ParentName) {

		Entity e = new Entity();

		if (node instanceof MethodDeclaration) {
			Set<Entity> children = new HashSet<Entity>();
			e.setType(3);
			e.setName(ParentName + "." + ((MethodDeclaration) node).getNameAsString());
			children = findVarChild(node, e, children);
			e.setChildren(children);

			Entities.add(e);
			int index = Entities.indexOf(e);
			matrix.addMethod(Entities.get(index));
			
			MethodDeclaration m = (MethodDeclaration) node;
			m.accept(new VoidVisitorAdapter<Void>() {

				String root2 = PATH.replace("\\", ".");
				@Override
				public void visit(final MethodCallExpr n, final Void arg) {
					
					try{
						relationships.add(root+(m.resolve()).getClassName() + "." + (m.resolve()).getName()+"---"+(root2+"."+n.resolve().getQualifiedName()));
					}catch(Exception e){
						
					}
					
				super.visit(n, arg);
				}
			}, null);
		}

		else if (node instanceof ConstructorDeclaration) {
			e = getConstructor(node, ParentName);
		}

		return e;
	}

	private static Entity getConstructor(Node child, String ParentName) {
		Entity e = new Entity();

		e.setName(ParentName + "." + ((ConstructorDeclaration) child).getNameAsString());
		e.setType(4);
		Entities.add(e);
		return e;
	}

	static Set<Entity> findVarChild(Node node, Entity e, Set<Entity> children) {

		for (Node child : node.getChildNodes()) {

			if (child instanceof VariableDeclarator) {

				Entity c = getMethodChild(child);
				c.setParent(e);
				c.setName(e.getName() + "." + c.getName());
				children.add(c);
				Entities.add(c);
			} else {
				findVarChild(child, e, children);
			}

		}
		return children;
	}

	static Entity getMethodChild(Node node) {

		Entity e = new Entity();
		if (node instanceof VariableDeclarator) {

			e.setType(5);
			e.setName(((VariableDeclarator) node).getNameAsString());
		} else {

			for (Node child : node.getChildNodes()) {
				node = child;
			}
		}

		return e;
	}

	public static void printTest() {
		Iterator it = EntitySet.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			((Entity) pair.getValue()).print();
		}

		for (int i = 0; i < VariableUsage.size(); i++) {
			VariableUsage.get(i).print();
		}

	}


	public static void getCalls(Node node) {

		for (Node child : node.getChildNodes()) {
			if (child instanceof MethodDeclaration) {
				findVarUsage(child, ((NodeWithSimpleName<ClassOrInterfaceDeclaration>) node).getNameAsString()+"."
						+((NodeWithSimpleName<ClassOrInterfaceDeclaration>) child).getNameAsString());
			}
			else if (child instanceof FieldDeclaration) {
				findVarUsage(child, ((NodeWithSimpleName<ClassOrInterfaceDeclaration>) node).getNameAsString());
			}
		}
	}

	static void findVarUsage(Node node, String callerString) {
		for (Node child : node.getChildNodes()) {
			if (child instanceof FieldAccessExpr) {
				getVariableUsage(child, callerString);
			} else {
				findVarUsage(child, callerString);
			}

		}
	}

	private static void getVariableUsage(Node child, String callerString) {
		VariableUsage v = new VariableUsage();
		
		String caller = root+callerString;		
		String callee = root+(child.toString()).substring(0, (child.toString()).indexOf('.'));
		String variable = root+child.toString();
		
		if (EntitySet.get(callee) != null) {
			if (EntitySet.get(caller) != null) {
				if (EntitySet.get(variable) != null) {

					Entity calleeE = EntitySet.get(callee);
					v.setCallee(calleeE);
					
					
					Entity callerE = EntitySet.get(caller);
					v.setCaller(callerE);

					
					Entity variableE = EntitySet.get(variable);
					v.setVariable(variableE);

					callerE.addOutgoing(calleeE);
					callerE.addOutgoing(variableE);
					calleeE.addIncoming(callerE);
					variableE.addIncoming(callerE);	
					
					callerE.setHasOutgoing(true);
					calleeE.setHasIncoming(true);
					variableE.setHasIncoming(true);
					
					EntitySet.put(caller, callerE);
					EntitySet.put(callee, calleeE);
					EntitySet.put(variable, variableE);
					
					VariableUsage.add(v);
					System.out.println(callerE.getName()+"--"+calleeE.getName()+"--"+variableE.getName());
				}
				
			}
		}

	}
	
	private void matchRelations(){
		try{
			String[]splits;
			Entity f,s;
			
			for(int i=0; i<relationships.size(); i++){
				
				
				splits = relationships.get(i).split("---");
			
				if (EntitySet.get(splits[0]) != null) {
					if (EntitySet.get(splits[1]) != null) {
						f = EntitySet.get(splits[0]);
						s = EntitySet.get(splits[1]);

						f.addIncoming(s);
						s.addOutgoing(f);

						f.setHasIncoming(true);
						s.setHasOutgoing(true);
						EntitySet.put(splits[0], f);
						EntitySet.put(splits[1], s);

						count++;
					}
				}
			}
			}catch(Exception e){
			}
	}

}