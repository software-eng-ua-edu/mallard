/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.cs.mallard;

import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author      Nicholas A. Kraft <nkraft@cs.ua.edu>
 * @version     $Id: CallGraph.java 9 2012-04-06 20:39:29Z nkraft $
 */
public class CallGraph
{
    // Build a call graph by visiting each .class file in the jar file.
    public static CallGraph build(String jarFileName) {
        CallGraphVisitor v = new CallGraphVisitor();
        try {
            JarFile jar = new JarFile(new File(jarFileName));
            Enumeration<? extends JarEntry> jarEntries = jar.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry entry = jarEntries.nextElement();
                String entryName = entry.getName();
                if (entryName.endsWith(".class")) {
                    ClassReader cr = new ClassReader(jar.getInputStream(entry));
                    cr.accept(v, 0);
                }
            }
        } catch (IOException e) {
        }
        return v.getCallGraph();
    }

    public void addCall(String caller, String callee) {
        if (!calls.containsKey(caller)) {
            List<String> callees = new ArrayList<String>();
            callees.add(callee);
            calls.put(caller, callees);
        } else {
            List<String> callees = calls.get(caller);
            callees.add(callee);
        }
    }

    public Set<String> getCallers() { return calls.keySet(); }

    public List<String> getCallees(String caller) {
        return calls.get(caller);
    }

    private Map<String,List<String>> calls = new HashMap<String,List<String>>();
}
