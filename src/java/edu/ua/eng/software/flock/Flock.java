package edu.ua.eng.software.flock;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

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
		
		ConcurrentMap<Method, Set<Method>> clones = CloneMap.getMap(niCadHandler.getClones());
		System.out.println(clones.size());
		
		for(Method m: clones.keySet()) {
			System.out.println("Clones of " + m);
			for(Method n: clones.get(m)) {
				System.out.println(n);
			}
			System.out.println();
		}
		
	}
}