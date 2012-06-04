package edu.ua.eng.software.flock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NiCadHandler extends DefaultHandler
{
	public NiCadHandler(Map<String, Method> map) {
		this.map = map;
	}
	public void startElement (String uri, String localName, String qname, Attributes attributes) throws SAXException {
		if(qname.equals("clone")) {
			tmp = new ArrayList<Method>();
			clones.add(tmp);
		}
		else if(qname.equals("source")) {
			String key = attributes.getValue("file") + ":" + attributes.getValue("endline");
			Method m = map.get(key);
			if (m == null) {
				System.err.println(String.format("Could not find method from %s", key));
			} else {
				tmp.add(m);
			}
		}
	}
	
	public ArrayList<ArrayList<Method>> getClones() { return clones; }
	
	Map<String, Method> map;
	ArrayList<ArrayList<Method>> clones = new ArrayList<ArrayList<Method>>();
	ArrayList<Method> tmp;
}