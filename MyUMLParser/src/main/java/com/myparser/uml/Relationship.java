package com.myparser.uml;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import net.sourceforge.plantuml.SourceStringReader;

/**
 * Hello world!
 *
 */
public class Relationship {

	static List<String> classesParsed = new ArrayList<String>();
	static List<String> interfaceParsed = new ArrayList<String>();
	static List<String> classes_interfaceParsed = new ArrayList<String>();
	// String ("1" *-- "many");
	static final String onetoMany = new String("\"1\"--\"*\"" );
	static final String zerotomany= new String("\"0\"--\"*\"");
	
	static final String zerotoone= new String("\"0\"--\"1\"");
	static 	final String onetoone= new String("\"1\"--\"1\"");
	
	static final String word_zerotomany= new String("\"0\"--\"many\"");
	
	public static List<String> collection_arr_association = new CopyOnWriteArrayList<String>();
	public static List<String> assosiationRelation = new CopyOnWriteArrayList <String>();	
	
	public static Set<String> associationAll = new HashSet<String>();
	public static Set<String> assosiation_array = new HashSet<String>();
	
	Relationship(){
		collection_arr_association = new CopyOnWriteArrayList<String>();
		 assosiationRelation = new CopyOnWriteArrayList <String>();
		 classesParsed = new ArrayList<String>();
		 interfaceParsed = new ArrayList<String>();
		 classes_interfaceParsed = new ArrayList<String>();
	}
	
	private void getPrasedList(String path) {
		final String JAVA_CONSTANT = "java";


		try {
			
			Files.walk(Paths.get(path)).forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					System.out.println(filePath);
					File f1 = filePath.toFile();
					System.out.println(f1.getName());
					String[] splitted = f1.getName().split("\\.");
					System.out.println();

					if (splitted[1].equals(JAVA_CONSTANT)) {
						try {
							FileInputStream in = new FileInputStream(filePath.toString());
							CompilationUnit cu = JavaParser.parse(in);

							Object node = cu.getData();

							// System.out.println(node.);
							// Node pr = cu.getParentNode();
							// parentnode.
							System.out.println("CU B1 types" + cu.getTypes());

							// Type Declaration can be AnnotationDeclaration,
							// ClassOrInterfaceDeclaration,
							// EmptyTypeDeclaration, EnumDeclaration
							// VVVVVVIMP ClassOrInterfaceDeclaration has private
							// boolean interface_;
							ClassOrInterfaceDeclaration cx = (ClassOrInterfaceDeclaration) cu.getTypes().get(0);
							if (cx.isInterface()) {
								interfaceParsed.add(splitted[0]);
							} else
								classesParsed.add(splitted[0]);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// parsedList.add(splitted[0]);
					}
				}
			});
			System.out.println(classesParsed);
			System.out.println(interfaceParsed);
			classes_interfaceParsed = new ArrayList<String>(classesParsed);
			classes_interfaceParsed.addAll(interfaceParsed);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
	
		FileInputStream in = null;
		CompilationUnit cu = null;
		List<ClassOrInterfaceType> extendList = null;
		List<ClassOrInterfaceType> interfaceList = null;
		String inputpath=args[0];
		Relationship app = new Relationship();
		System.out.println("inputpath_"+inputpath);
		app.getPrasedList(inputpath);

		SourceFile fileInfotemp = null;
		List<SourceFile> listFileDetails = new ArrayList<SourceFile>();
		ClassOrInterfaceDeclaration cx = null;

		
		final File folder = new File(inputpath);
		final String EXTENSION_JAVA = "java";

		for (final File fileEntry : folder.listFiles()) {
			cx = null;

			String absPath = fileEntry.getAbsolutePath();

			String extn = absPath.substring(absPath.lastIndexOf('.') + 1);
			
			if ((!fileEntry.isDirectory()) && (extn.equals(EXTENSION_JAVA))) {
				
				String filenamestr = fileEntry.getName();
				
				String[] filenamearr = filenamestr.split("\\.");
				

				fileInfotemp = new SourceFile();
				fileInfotemp.filename = filenamearr[0];
				fileInfotemp.filepath = absPath;
				try {
					in = new FileInputStream(fileEntry);
					cu = JavaParser.parse(in);
					Object node = cu.getData();

					for (TypeDeclaration typeDeclaration : cu.getTypes()) {
						if (typeDeclaration instanceof ClassOrInterfaceDeclaration) {
							cx = (ClassOrInterfaceDeclaration) typeDeclaration;
							break;
						}

					}

					if (cx.isInterface()) {
						fileInfotemp.isFIleAnInterface = true;
					}

					extendList = cx.getExtends();

					if (extendList != null) {
						for (ClassOrInterfaceType ciType : extendList) {

							fileInfotemp.classesExtnd.add(ciType.toString());
							fileInfotemp.classesExtnd.retainAll(classesParsed);
						}
					}

					interfaceList = cx.getImplements();
					if (interfaceList != null) {
						for (ClassOrInterfaceType ciType : interfaceList) {

							fileInfotemp.interfaceImpltd.add(ciType.toString());
							fileInfotemp.interfaceImpltd.retainAll(interfaceParsed);
						}
					}
					List<Node> nodes = cx.getChildrenNodes();
					if (nodes != null) {
						for (Node nodeChild : nodes) {
							if (nodeChild instanceof MethodDeclaration) {
								app.methodTypePopulation((MethodDeclaration) nodeChild, fileInfotemp);
							}
							if (nodeChild instanceof FieldDeclaration) {
								app.putAttributes((FieldDeclaration) nodeChild, fileInfotemp);
							}
							if (nodeChild instanceof ConstructorDeclaration) {
								app.constructorsPopulation((ConstructorDeclaration) nodeChild, fileInfotemp);
							}
						}
					}
					// to retain only user defd interfaces n classes
					fileInfotemp.interface_classesUsed_Mtds.retainAll(classes_interfaceParsed);
					//association.retainAll(classes_interfaceParsed);
					listFileDetails.add(fileInfotemp);

				} catch (FileNotFoundException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			

		}
		
		System.out.println(listFileDetails);
		System.out.println("Before transform_associations:"+assosiationRelation + " " + collection_arr_association);
		
		// create diagram D:\SJSU\Paul_202\JavaParser\DevTest\src
		
		String outputpath = args[0];
		String outputfilename= args[1];
		functionAssosiation();
		associationAll=new HashSet<>(assosiationRelation);
		assosiation_array= new HashSet<>(collection_arr_association);
		System.out.println("After transform_associations:"+assosiationRelation + " " + collection_arr_association);
		System.out.println("outputpath" +outputpath);
		classDiagramCreation(listFileDetails, outputpath,outputfilename);
		

	}
	
	private  static void functionAssosiation(){
		String[] splitstring=null;
		
		for(Iterator<String> it = assosiationRelation.iterator(); it.hasNext(); ){
			
			String s=it.next();
			System.out.println("loop_stringassociation"+ s);
			StringBuilder chkr= new StringBuilder();
			splitstring= s.split(zerotoone);
			System.out.println("Arrays_stringassociation"+Arrays.asList(splitstring));
			if(!splitstring[0].equals(s)){
				chkr.append(splitstring[1]);
				chkr.append(zerotoone);
				chkr.append(splitstring[0]);
				
				System.out.println("chkr.toString()"+ chkr.toString());
				if (assosiationRelation.contains(chkr.toString())){
					assosiationRelation.remove(chkr.toString());
					assosiationRelation.remove(s);
					
					StringBuilder new1to1entry= new StringBuilder();
					new1to1entry.append(splitstring[1]);
					new1to1entry.append(onetoone); 
					new1to1entry.append(splitstring[0]);
					assosiationRelation.add(new1to1entry.toString());
				}
			}
		}
		System.out.println("interim_association"+assosiationRelation);
		
		
		String iterator0toMany=null;
		for(Iterator<String> it = collection_arr_association.iterator(); it.hasNext(); ){
		
		iterator0toMany=it.next();
		System.out.println("loopstring_collection"+ iterator0toMany);
		StringBuilder chkr= new StringBuilder();
		splitstring= iterator0toMany.split(word_zerotomany);
		System.out.println("splitstring_collection:"+ Arrays.asList(splitstring));
		System.out.println("iterator0toMany.equals(splitstring)"+ iterator0toMany.equals(splitstring));
		if(!iterator0toMany.equals(splitstring[0]))	
			chkr.append(splitstring[1]);
			chkr.append(zerotoone);
			chkr.append(splitstring[0]);
			System.out.println("chkr"+chkr);
			String chkrString=chkr.toString();
			System.out.println("=====chkrString: " + chkrString+":association: " +assosiationRelation);
			if(assosiationRelation.contains(chkrString)){
				
				//association_to_be_removed.add(chkrString);
				assosiationRelation.remove(chkrString);
				//removes current 0 to many item
				collection_arr_association.remove(iterator0toMany);
				//association_to_be_removed.add(chkrString);
				StringBuilder newentry_bldr= new StringBuilder();
				newentry_bldr.append(splitstring[0]);
				newentry_bldr.append(onetoMany);
				newentry_bldr.append(splitstring[1]);
				System.out.println("newentry_bldr.toString()"+newentry_bldr.toString());
				collection_arr_association.add(newentry_bldr.toString());
			}
	}

		System.out.println("final_List_association"+ assosiationRelation);
		System.out.println("interim_List_collection_arr_association"+ collection_arr_association);
	
		
		for(int i=0;i<collection_arr_association.size();i++ ){
			
			collection_arr_association.set(i,collection_arr_association.get(i).replace(word_zerotomany, zerotomany));
		}
		
		System.out.println("final_collection_arr_association");
		
		System.out.println(collection_arr_association);
	}
	private static void classDiagramCreation(List<SourceFile> listFileDetails, String path, String outputfilename) {

		ByteArrayOutputStream bous = new ByteArrayOutputStream();

		final String interfaceConstant = " <|.. ";
		final String classExtdConstant = " <|-- ";
		final String usesConstant = " ..> ";

		StringBuilder sbr = new StringBuilder();
		sbr.append("\n");
		sbr.append("@startuml\n");
		sbr.append("skinparam classAttributeIconSize 0\n");
		/*
		 * source +="class Apartment{\n"; source +="rooms: int[]\n"; source
		 * +="}\n"; source +="class House\n"; source +="Apartment <|-- House\n";
		 * source +="class Commune\n";
		 */
		String filename = "";
		for (SourceFile fileInfo : listFileDetails) {
			filename = fileInfo.filename;
			
			if (fileInfo.isFIleAnInterface)
				sbr.append("interface ");
				
				else 
					sbr.append("class ");
			
			sbr.append(filename);
			sbr.append("{\n");

			for (String s : fileInfo.attributes) {

				sbr.append(s);
				sbr.append("\n");

			}
			for (String s : fileInfo.mtdsDcrld) {

				sbr.append(s);
				sbr.append("\n");

			}
			
			for (String s : fileInfo.constructorsDclrd) {

				sbr.append(s);
				sbr.append("\n");

			}
			
			sbr.append("}\n");
			// interface implts Class11 <|.. Class12 here Class11 is interface
			// impltd by Class12
			for (String interfaceimplt : fileInfo.interfaceImpltd) {
				sbr.append(interfaceimplt);
				sbr.append(interfaceConstant);
				sbr.append(filename);
				sbr.append("\n");
			}

			// extends Class01 <|-- Class02 here Class01 is parent , Class02 is
			// child
			for (String classextd : fileInfo.classesExtnd) {
				sbr.append(classextd);
				sbr.append(classExtdConstant);
				sbr.append(filename);
				sbr.append("\n");
			}

			// uses list Class15 ..> Class16 arrow from 15 to 16
			for (String uses : fileInfo.interface_classesUsed_Mtds) {
				sbr.append(filename);
				sbr.append(usesConstant);
				sbr.append(uses);
				sbr.append("\n");
			}

		}
		// add association n collarr appending here
		
		for (String uses : associationAll) {
			sbr.append(uses);
			sbr.append("\n");
		}
		
		for (String uses : assosiation_array) {
			sbr.append(uses);
			sbr.append("\n");
		}
		
		sbr.append("@enduml\n");

		System.out.println("final_mehnat"+sbr.toString());
		
		OutputStream png;
		String filePath = "classDiagram.png";
		try {
			png = new FileOutputStream(new File(path+"/"+outputfilename+".png"));
		System.out.println("yaha_hu filepath of output" +path+"/"+outputfilename+".png");
		SourceStringReader reader = new SourceStringReader(sbr.toString());
		reader.generateImage(png);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*SourceStringReader reader = new SourceStringReader(sbr.toString());
		// Write the first image to "png"
		String desc = "";
		try {
			desc = reader.generateImage(bous);

			// Return a null string if no generation
			byte[] data = bous.toByteArray();

			InputStream in = new ByteArrayInputStream(data);
			BufferedImage convImg = ImageIO.read(in);
			ImageIO.write(convImg, "png", new File(path));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}

	private void constructorsPopulation(ConstructorDeclaration n, SourceFile fileDetails) {
		
		if (n.getModifiers() == 1) {
			StringBuilder sbldr = new StringBuilder();

			
			
			
			List<Parameter> plist = n.getParameters();
			System.out.println("plist" + plist);
			StringBuilder sbr = new StringBuilder();

			boolean moreParams = false;

			for (Parameter p : plist) {

				if (moreParams) {
					sbr.append(",");
				}
				moreParams = true;


				sbr.append(p.getId().getName());
				sbr.append(":");
				sbr.append(p.getType());

			}

			StringBuilder sbrmtd = new StringBuilder();

			if (n.getModifiers() == 1 || n.getModifiers() == 9) {
				sbrmtd.append("+ ");
				System.out.println("getName______" + n.getName());
				sbrmtd.append(n.getName());
				sbrmtd.append("(");
				sbrmtd.append(sbr);
				sbrmtd.append(")");
				fileDetails.constructorsDclrd.add(sbrmtd.toString());
			}
			// This logic is for dependency
			if (n.getParameters().size()>0)
			putMethodsInClass(n.getParameters(), fileDetails);
			if (n.getBlock()!=null)
			populateMethods(n.getBlock(), fileDetails);
			
		}
	}

	void methodTypePopulation(MethodDeclaration n, SourceFile fileDetails) {
		
		BlockStmt blockStmt = n.getBody();

		System.out.println("n.getThrows()" + n.getThrows());
		System.out.println("n.getType()" + n.getType());

		String str = n.getDeclarationAsString(true, false);
		System.out.println("n.getDeclarationAsString(true, false): " + str);

		

		List<Parameter> plist = n.getParameters();
		System.out.println("plist" + plist);
		StringBuilder sbr = new StringBuilder();

		boolean moreParams = false;

		for (Parameter p : plist) {

			if (moreParams) {
				sbr.append(",");
			}
			moreParams = true;


			sbr.append(p.getId().getName());
			sbr.append(":");
			sbr.append(p.getType());

		}

		StringBuilder sbrmtd = new StringBuilder();

		if (n.getModifiers() == 1 || n.getModifiers() == 9 ||  n.getModifiers() == 0) {
			sbrmtd.append("+ ");
			System.out.println("getName______" + n.getName());
			sbrmtd.append(n.getName());
			sbrmtd.append("(");
			sbrmtd.append(sbr);
			sbrmtd.append(")");
			sbrmtd.append(" : ");
			sbrmtd.append(n.getType());
			fileDetails.mtdsDcrld.add(sbrmtd.toString());
		}

		/*System.out.println((n.getParameters()).get(0).getType());
		System.out.println((n.getParameters()).get(0).getType().getData());
		System.out.println(n.getModifiers());
*/
		//
		// below is for dependency in mtd declrn
		if (n.getParameters().size()>0)
		putMethodsInClass(n.getParameters(), fileDetails);
		if (n.getBody()!=null)
		populateMethods(n.getBody(), fileDetails);
		//
	}

	private void populateMethods(BlockStmt blckStmt, SourceFile fileInfo) {
		// TODO Auto-generated method stub
		for (Node nblock : blckStmt.getChildrenNodes()) {
			System.out.println("nblock" + nblock.getClass());
			if (nblock instanceof ExpressionStmt) {
				ExpressionStmt exprStmt = (ExpressionStmt) nblock;
				List<Node> nodeExpr = exprStmt.getChildrenNodes();
				if (nodeExpr.size() > 0 && nodeExpr.get(0) instanceof VariableDeclarationExpr) {
					System.out.println("nodeExpr.get(0).getClass()" + nodeExpr.get(0).getClass());
					System.out.println("nodeExpr.get(0)" + nodeExpr.get(0));
					// if
					VariableDeclarationExpr vd = (VariableDeclarationExpr) nodeExpr.get(0);
					System.out.println("nblock_vdgetType" + vd.getType());
					if (vd.getType().getChildrenNodes().size() > 0) {
						System.out.println("vdgetTypeChilds__" + vd.getType().getChildrenNodes().get(0).getClass());
						ClassOrInterfaceType cit = (ClassOrInterfaceType) vd.getType().getChildrenNodes().get(0);
						System.out.println("cit" + cit.getTypeArgs());

						fileInfo.interface_classesUsed_Mtds
								.add(cit.getTypeArgs() != null ? cit.getTypeArgs().get(0).toString() : cit.toString());
						if((cit.getTypeArgs() != null)&& (!(classesParsed.contains(cit.getTypeArgs().get(0).toString()))) ){
							fileInfo.interface_classesUsed_Mtds
							.add(cit.getTypeArgs().get(0).toString());
						}
						if((cit.getTypeArgs() == null)&&(!(classesParsed.contains(cit.toString())))){
							fileInfo.interface_classesUsed_Mtds
							.add(cit.toString());
							
						}
						
					}
				}

			}
		}
	}

	void putMethodsInClass(List<Parameter> list, SourceFile fileInfo) {
		
		for (Parameter p : list) {
			
			if (p.getType() instanceof ReferenceType) {
				
				if ((p.getType().getChildrenNodes().get(0).getChildrenNodes().size() == 0)&&(!(classesParsed.contains(p.getType().getChildrenNodes().get(0).toString()))))
					fileInfo.interface_classesUsed_Mtds.add(p.getType().getChildrenNodes().get(0).toString());
				
				if(!(p.getType().getChildrenNodes().get(0).getChildrenNodes().size() == 0)){
					
					
					if(!classesParsed.contains(p.getType().getChildrenNodes().get(0).getChildrenNodes().get(0).toString()))
					fileInfo.interface_classesUsed_Mtds
							.add(p.getType().getChildrenNodes().get(0).getChildrenNodes().get(0).toString());
				}

			}
			if (p.getType() instanceof ClassOrInterfaceType) {

				System.out.println("p.getType().getChildrenNodes().get(0)" + p.getType().getChildrenNodes().get(0));
				if(!classesParsed.contains(p.getType().getChildrenNodes().get(0).toString()))
				fileInfo.interface_classesUsed_Mtds.add(p.getType().getChildrenNodes().get(0).toString());
			}
		}
	}


	void putAttributes(FieldDeclaration fd, SourceFile fileDetails) {
		int modifier = fd.getModifiers();
		 System.out.println(fd + "fd.getModifiers() "+ fd.getModifiers() );
		if ((modifier == 1) || (modifier == 2)||((modifier == 4))) {
			
			String modifier_notation= modifier == 1? "+ ":"- ";
			
			System.out.println("populateAttributes if entered");
			
			VariableDeclarator vd = (VariableDeclarator) (fd.getVariables()).get(0);
			String vdid_str=vd.getId().toString();
			System.out.println(vdid_str);

			List<Node> node = fd.getChildrenNodes();
			// assocn code
			// ref Class09 -- Class10
			// class01 has many ref's of 2 Class01 "1" *-- "many" Class02 :
			// contains
			System.out.println("fd.getTypegetClass"+fd.getType().getClass());
			if (fd.getType() instanceof ReferenceType) {
				// assocation may be der
				String classassocnmul = null;
				System.out.println("node" + node);
				System.out.println("node.get(0).getClass()" + node.get(0).getClass());
				System.out.println("node.get(1).getClass()" + node.get(1).getClass());
				ReferenceType rfinside = (ReferenceType) node.get(0);

				System.out.println("rfinside: " + rfinside);

				String str = fd.getType().toString();
				StringBuilder sbr = new StringBuilder();
				
				if (str.contains("[")) {
					classassocnmul = node.get(0).getChildrenNodes().get(0).toString();
					System.out.println("classassocnmul" + classassocnmul);
					//this means class is not custom class 
					if (!classes_interfaceParsed.contains(classassocnmul)){
						fileDetails.attributes.add(modifier_notation+ vdid_str + ":" + fd.getType());
					}
					else
					assosiationPopulation(classassocnmul, fileDetails);
				}

				else if (str.contains("<")) {
					classassocnmul = node.get(0).getChildrenNodes().get(0).getChildrenNodes().get(0).toString();
					if (!classes_interfaceParsed.contains(classassocnmul)){
						fileDetails.attributes.add(modifier_notation + vdid_str + ":" + fd.getType());
					}
					else{
					assosiationPopulation(classassocnmul, fileDetails);
					System.out.println("[2__" + classassocnmul);
					}

				} else {
					classassocnmul=str;
					//if populates the java objects else populates custom classes   
					if (!classes_interfaceParsed.contains(classassocnmul)){
						fileDetails.attributes.add(modifier_notation+vdid_str + ":" + fd.getType());
					}
					else
					{
						System.out.println("entered here");
					assosiationFormation(rfinside.toString(), fileDetails);
					}
				}

			}
			if (fd.getType() instanceof PrimitiveType) {
				System.out.println("Primitve in " + fileDetails.filename);
				System.out.println(vdid_str + ":" + fd.getType());
				
				fileDetails.attributes.add(modifier_notation +vdid_str + ":" + fd.getType());

			}
		}
	}

	private void assosiationPopulation(String className, SourceFile fileDetails) {
		String filename = fileDetails.filename;
		StringBuilder in = new StringBuilder();

		// Class01 "1" *-- "many" Class02 : contains

		in.append(filename);
		in.append(word_zerotomany);
		in.append(className);

		if (!collection_arr_association.contains(in.toString()))
		collection_arr_association.add(in.toString());
		System.out.println("[__word_zerotomany" + in.toString());

	}

	private void assosiationFormation(String className, SourceFile fileDetails) {
		String filename = fileDetails.filename;
		StringBuilder in = new StringBuilder();
		
		in.append(filename);
		in.append(zerotoone);
		in.append(className);
			if (!assosiationRelation.contains(in.toString()))
		assosiationRelation.add(in.toString());
		System.out.println("[__zerotoone" + in.toString());
	}
	
}
