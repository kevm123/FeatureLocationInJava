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
	// private static final String PATH = "../JPExamples";
	// final File folder = new File("C:/Users/kev00_000/Desktop/College/4th Year
	// Semester 1/FYP/workspace-jhotdraw ! /JHotDraw7/src/main/java");
	// final File folder = new
	// File(""../JPExamples/src/org/javaparser/samples"");

	static HashMap<String, Entity> EntitySet = new HashMap<String, Entity>();
	static ArrayList<Entity> Entities = new ArrayList<Entity>();
	static ArrayList<VariableUsage> VariableUsage = new ArrayList<VariableUsage>();

	public boolean parse(String PATH) throws IOException {

		CompilationUnit cu = null;

		try {
			TypeSolver reflectionTypeSolver = new ReflectionTypeSolver();
			TypeSolver javaParserTypeSolver = new JavaParserTypeSolver(new File("../JPExamples"));
			reflectionTypeSolver.setParent(reflectionTypeSolver);
			CombinedTypeSolver combinedSolver = new CombinedTypeSolver();
			combinedSolver.add(reflectionTypeSolver);
			combinedSolver.add(javaParserTypeSolver);
			JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedSolver);
			JavaParser.getStaticConfiguration().setSymbolResolver(symbolSolver);
			
			ArrayList<File> filesInFolder = parseDirect("../JPExamples");

			if (!filesInFolder.isEmpty()) {
				for (int i = 0; i < filesInFolder.size(); i++) {

					// System.out.println("------------------------------");

					// System.out.println(filesInFolder.get(i).toString());
					cu = JavaParser.parse(filesInFolder.get(i));

					String[] split = (filesInFolder.get(i).toString()).split("\\\\");
					String root = "";
					for (int k = 0; k < split.length - 1; k++) {
						root += (split[k] + ".");
					}

					YamlPrinter printer = new YamlPrinter(true);
					// System.out.println(printer.output(cu));

					NodeList<TypeDeclaration<?>> ty = cu.getTypes();
					for (TypeDeclaration<?> typeDeclaration : ty) {
						Node node = (Node) typeDeclaration;
						processNode(node);
					}

					/*
					 * for (TypeDeclaration<?> typeDeclaration : ty) { Node node =
					 * (Node) typeDeclaration; getCalls(node); }
					 */

					String key;
					for (int e = 0; e < Entities.size(); e++) {
						key = root + Entities.get(e).getName();
						EntitySet.put(key, Entities.get(e));
					}

					// System.out.println("------------------------------");
				}

				for (int i = 0; i < filesInFolder.size(); i++) {
					cu = JavaParser.parse(filesInFolder.get(i));
					// System.out.println((i+1)+" :
					// "+filesInFolder.get(i).toString());
					NodeList<TypeDeclaration<?>> ty = cu.getTypes();
					for (TypeDeclaration<?> typeDeclaration : ty) {
						Node node = (Node) typeDeclaration;
						getCalls(node);
					}
				}
				
				if (filesInFolder.size()>0)
					return false;
				else
					return true;
			}
		} catch (Exception e) {
			
		}

		return false;
		
		
		// printTest();
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
			// System.out.println(pair.getKey() + " = " +
			// ((Entity)pair.getValue()).getName());
			((Entity) pair.getValue()).print();
		}

		/*
		 * for(int i=0; i<EntitySet.size(); i++) { EntitySet.get(i).print(); }
		 */
		for (int i = 0; i < VariableUsage.size(); i++) {
			VariableUsage.get(i).print();
		}

	}

	/*
	 * child = nodes in file; m = any child of type MethodDeclaration; n =
	 * MethodCallExpr in m val = Hashmap Entries pair = Hashmap Entries
	 */
	public static void getCalls(Node node) {

		for (Node child : node.getChildNodes()) {

			// Find all Methods and use visitor to go through
			if (child instanceof MethodDeclaration) {
				MethodDeclaration m = (MethodDeclaration) child;
				m.accept(new VoidVisitorAdapter<Void>() {

					@Override
					public void visit(final MethodCallExpr n, final Void arg) {

						// visiting all Method calls see if its a call to a user
						// defined method
						Entity f = new Entity();
						Entity s = new Entity();
						boolean first = false, second = false;
						Iterator it = EntitySet.entrySet().iterator();

						// Using the set of all found entities, run through see
						// if they match the method calling or caller
						while (it.hasNext() || !first && !second) {
							// First get the name of each entity we have stored
							HashMap.Entry val = (HashMap.Entry) it.next();
							String entName;
							if (((Entity) val.getValue()).getName().contains(".")) {
								entName = ((Entity) val.getValue()).getName()
										.substring(((Entity) val.getValue()).getName().lastIndexOf(".") + 1);
							} else
								entName = ((Entity) val.getValue()).getName();

							// Next see if the entity is 1) a method, 2)has the
							// same name and 3)is being called in the same class
							// as the method in question
							if (((Entity) val.getValue()).getType() == 3 && m.getNameAsString().equals(entName)
									&& ((Entity) val.getValue()).getParent().getName()
											.equals(m.resolve().getClassName())) {
								first = true;
								f = (Entity) val.getValue();
							}

							// See if the entity is 1) has the same name as the
							// method call and 2) is from the same class as
							// where the method being called is instantiated
							else if (((Entity) val.getValue()).getType() == 3
									&& ((NodeWithSimpleName<ClassOrInterfaceDeclaration>) n.getParentNodeForChildren())
											.getNameAsString().equals(entName)) {
								try {
									if (n.resolve().hasName()) {
										if (((Entity) val.getValue()).getParent().getName()
												.equals(n.resolve().getClassName())) {
											second = true;
											s = (Entity) val.getValue();
										}
									}
								} catch (Exception e) {
									// System.out.println(((NodeWithSimpleName<ClassOrInterfaceDeclaration>)
									// n.getParentNodeForChildren()).getNameAsString()+"....");
								}
							}
						}

						if (first && second) {
							f.addOutgoing(s);
							s.addIncoming(f);

							// Find the 2 entities again and update them with
							// the incoming and outgoings
							first = false;
							second = false;
							String fKey = "";
							String sKey = "";
							for (HashMap.Entry<String, Entity> entry : EntitySet.entrySet()) {
								if (f.getName().equals(((Entity) entry.getValue()).getName())) {
									first = true;
									f.setHasOutgoing(true);
									fKey = entry.getKey();
								}
								if (s.getName().equals(((Entity) entry.getValue()).getName())) {
									second = true;
									s.setHasIncoming(true);
									sKey = entry.getKey();
								}
							}
							if (first && second) {
								EntitySet.put(fKey, f);
								EntitySet.put(sKey, s);
							}
						}

						super.visit(n, arg);

					}
				}, null);

				findVarUsage(child, child.getParentNode());
			}
		}
	}

	static void findVarUsage(Node node, Optional<Node> optional) {

		Optional<Node> Parent = optional;
		for (Node child : node.getChildNodes()) {
			if (child instanceof FieldAccessExpr) {
				getVariableUsage(child, Parent);
			} else {
				findVarUsage(child, Parent);
			}

		}
	}

	private static void getVariableUsage(Node child, Optional<Node> parent) {
		VariableUsage v = new VariableUsage();
		Node p = parent.get();
		Iterator it = EntitySet.entrySet().iterator();
		boolean one = false, two = false, three = false;
		while (it.hasNext()) {
			HashMap.Entry val = (HashMap.Entry) it.next();

			if ((child.toString()).substring(0, (child.toString()).indexOf('.'))
					.equals(((Entity) val.getValue()).getName())) {
				Entity e = (Entity) val.getValue();
				v.setCallee(e);
				one = true;
			}
			if (((NodeWithSimpleName<ClassOrInterfaceDeclaration>) p).getNameAsString()
					.equals(((Entity) val.getValue()).getName())) {
				Entity e = (Entity) val.getValue();
				v.setCaller(e);
				two = true;
			}
			if (child.toString().equals(((Entity) val.getValue()).getName())) {
				Entity e = (Entity) val.getValue();
				v.setVariable(e);
				three = true;
			}
		}
		if (one && two && three) {
			VariableUsage.add(v);
		}
	}

}