package com.myparser.uml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;

public class TestFile {

	public static void main(String[] args) {
		FileInputStream in = null;
		CompilationUnit cu = null;
		try {
			in = new FileInputStream("C:/Users/jaykrrishna/Downloads/uml-parser-test-1/uml-parser-test-1/A.java");
			cu = JavaParser.parse(in);
			System.out.println(cu.toString());
		} catch (FileNotFoundException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
