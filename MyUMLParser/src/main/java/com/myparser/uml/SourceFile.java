package com.myparser.uml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/***
 * 1 Object of this class to be created per java file parsed
 * To store data about each class/java file  
 * @author BG
 *
 */
public class SourceFile {
	
	//Logic..Populate the list with the classes which r to be parsed 
	//  call all the methods to be parsed i n for loop 1 by 1  for each do below
	// attributes 4 lists interface impltd,used n classes impltd n used , 1 list for mtd decln, 1 list of params, 1 list of uses
	//Step 1 determine its class/interface and make an entry in golbal list
	//Step 2 get the classes extnd n interfaces impltd
	//Get child A>if mtd declrn 1.populate signature 2. chk uses using prepopulated list of file parsed 
	//B> if field declrn 1. populate List of this class 2. Put association list any references
	
	//Note only the consumers of mtd will have dependency/ will populate the list , the declarator/creator of function will not populate dependency list
	
	public String filepath="";
	public String filename="";
	public boolean isFIleAnInterface=false;
	public List<String	> classesExtnd= new ArrayList<String>();
	public Set<String	> interface_classesUsed_Mtds= new HashSet<String>();
	public List<String	> interfaceImpltd= new ArrayList<String>();
	//public List<String	> interfaceUsed_Mtds= new ArrayList<String>();
	
	public	List<String	> mtdsDcrld= new ArrayList<String>();
	
	public List<String	> attributes= new ArrayList<String>();
	
	public List<String> constructorsDclrd=new ArrayList<String>();

	
	@Override
	public String toString(){
		
		StringBuilder sb = new StringBuilder();
		sb.append("filepath:" + filepath);
		sb.append("\r\n "+"filename:" +filename);
		sb.append("\r\n "+"isFIleAnInterface:" + isFIleAnInterface);
		sb.append("\r\n "+"classesExtnd:"+classesExtnd);
		sb.append("\r\n "+"interface_classesUsed_Mtds:"+interface_classesUsed_Mtds);
		//sb.append("\r\n "+"classesUsed_Mtds:"+classesUsed_Mtds);
		sb.append("\r\n "+"interfaceImpltd:"+interfaceImpltd);
		//sb.append("\r\n "+"interfaceUsed_Mtds:"+interfaceUsed_Mtds);
		sb.append("\r\n "+"mtdsDcrld:"+mtdsDcrld);
		sb.append("\r\n "+"constructorsDclrd:"+constructorsDclrd);
		sb.append("\r\n "+"attributes:"+attributes);
		sb.append("\r\n "+"Done" + "\r\n ");
		
		return sb.toString() ;
		
	}
	
	

}
