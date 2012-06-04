package edu.ua.eng.software.flock;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Flock
{
	private static void usage() {
		System.out.println("Usage: java edu.ua.cs.software.flock.Flock CallGraphXML CloneClassXML");
	}
	
	public static void main (String [] args) throws Exception {
		if (2 != args.length) {
			usage();
			System.exit(1);
		}
		
		File cgFile = new File(args[0]);
		File ccFile = new File(args[1]);
		
		SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
		MallardHandler mallardHandler = new MallardHandler();
		saxParser.parse(cgFile, mallardHandler);
		
		Map<String, Method> fileMap = mallardHandler.getFileMap();
		
		NiCadHandler niCadHandler = new NiCadHandler(fileMap);
		saxParser.parse(ccFile, niCadHandler);
		
		ArrayList<ArrayList<Method>> clones = niCadHandler.getClones();
		System.out.println(clones.size());
		
		for(ArrayList<Method> clone : clones) {
			System.out.println("Clone group:");
			for(Method method : clone) {
				System.out.print(method);
				System.out.println(String.format(" called by %d", method.getCallers().size()));
			}
			System.out.println();
		}
	}
}