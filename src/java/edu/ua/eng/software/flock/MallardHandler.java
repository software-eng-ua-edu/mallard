package edu.ua.eng.software.flock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.ua.eng.software.flock.util.LineRange;

public class MallardHandler extends DefaultHandler
{	
	public void startElement (String uri, String localName, String qname, Attributes attributes) throws SAXException {
		if(qname.equals("method")) { 
			tmp = new Method(attributes.getValue("filename"));
			tmp.setLineRange(new LineRange(
					Integer.parseInt(attributes.getValue("startLine")),
					Integer.parseInt(attributes.getValue("endLine"))
			));

			String name = attributes.getValue("qname");
			tmp.setName(name);
			tmp.setQualifiedName(name);

			methods.add(tmp);
			map.put(name, tmp);
			fileMap.put(tmp.getFilename() + ":" + tmp.getLineRange().getEndLine(), tmp);
			callerMap.put(tmp, new ArrayList<String>());
		}
		else if(qname.equals("callee")) {
			tmp.addCallee(attributes.getValue("qname"));
		}
		else if(qname.equals("caller")) {
			//Callers can be full methods, but may not be processed yet
			//Adding to a map to be resolved in endDocument
			callerMap.get(tmp).add(attributes.getValue("qname"));
		}
	}
	
	public void endDocument() {
		for(Method m : callerMap.keySet()) {
			for(String qname : callerMap.get(m)) {
				Method caller = map.get(qname);
				if(caller != null) {
					m.addCaller(caller);
				}
			}
		}
	}
	
	public HashSet<Method> getMethods() { return methods; }
	public HashMap<String, Method> getFileMap() { return fileMap; }
	
	private Method tmp;
	private HashSet<Method> methods = new HashSet<Method>();
	private HashMap<String, Method> map = new HashMap<String, Method>();
	private HashMap<String, Method> fileMap = new HashMap<String, Method>();
	private HashMap<Method, ArrayList<String>> callerMap = new HashMap<Method, ArrayList<String>>(); 
}