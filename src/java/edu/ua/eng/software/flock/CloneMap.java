package edu.ua.eng.software.flock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CloneMap
{
	
	public static ConcurrentMap<Method, Set<Method>> getMap(ArrayList<ArrayList<Method>> arrayList) {
		for(List<Method> l : arrayList) {
			Method m1 = l.get(0);
			Method m2 = l.get(1);
			addMapping(m1, m2);
			addMapping(m2, m1);
		}
		
		return map;
	}
	
	private static void addMapping(Method k, Method v) {
		if(!map.containsKey(k)) map.put(k, new HashSet<Method>());
		map.get(k).add(v);
	}
	
	private static ConcurrentMap<Method, Set<Method>> map = new ConcurrentHashMap<Method, Set<Method>>();
}
