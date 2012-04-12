/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.mallard;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author      Nicholas A. Kraft <nkraft@cs.ua.edu>
 * @version     $Id: Methods.java 9 2012-04-06 20:39:29Z nkraft $
 */
public class Methods
{
    public static ConcurrentMap<String,Method> extract(String zipFileName) {
        // Extract method line information by visiting each .java file in the zip file.
        ZipFile zip = null;
        Queue<String> filenames = new LinkedList<String>();
        try {
            zip = new ZipFile(new File(zipFileName));
            Enumeration<? extends ZipEntry> zipEntries = zip.entries();
            while (zipEntries.hasMoreElements()) {
                ZipEntry entry = zipEntries.nextElement();
                String entryName = entry.getName();
                if (entryName.endsWith(".java")) {
                    filenames.offer(entryName);
                }
            }
        } catch (IOException e) {
        }
        ConcurrentMap<String,Method> methods = new ConcurrentHashMap<String,Method>();
        Thread[] threads = new Thread[NUMBER_OF_THREADS];
        for (int i = 0; i < threads.length; ++i) {
            threads[i] = new Thread(new MethodParser(zip, filenames, methods));
            threads[i].start();
        }
        for (int i = 0; i < threads.length; ++i) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
            }
        }
        return methods;
    }

    public static ConcurrentMap<String,Method> merge(CallGraph cg, ConcurrentMap<String,Method> methods) {
        // Merge call graph and method line information
        Set<String> callers = cg.getCallers();
        for (String qname : methods.keySet()) {
            Method method = methods.get(qname);
            if (callers.contains(qname)) {
                List<String> callees = cg.getCallees(qname);
                for (String callee : callees) {
                    method.addCallee(callee);
                    if (methods.containsKey(callee)) {
                        Method mCallee = methods.get(callee);
                        mCallee.addCaller(method);
                    }
                }
            }
        }
        return methods;
    }

    private static int NUMBER_OF_THREADS = 2;
}
