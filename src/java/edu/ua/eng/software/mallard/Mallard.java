/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.mallard;

//import java.io.FileOutputStream;
//import java.io.PrintStream;
import java.io.FileWriter;
import java.util.concurrent.ConcurrentMap;

/**
 * @author      Nicholas A. Kraft <nkraft@cs.ua.edu>
 * @version     $Id: Mallard.java 9 2012-04-06 20:39:29Z nkraft $
 */
public class Mallard
{
    private static void usage() {
        System.out.println("Usage: java edu.ua.cs.mallard.Mallard JAR ZIP");
    }

    public static void main (String [] args) throws Exception {
        if (2 != args.length) {
            usage();
            System.exit(1);
        }

        // Redirect System.out and System.err to a tmp file.
        /*
        File ifile = new File(args[0]);
        File ofile = File.createTempFile("mallard-",".log");
        FileOutputStream ofs = new FileOutputStream(ofile);
        System.setOut(new PrintStream(ofs,true));
        System.setErr(new PrintStream(ofs,true));
        */

        long start = System.nanoTime();

        ConcurrentMap<String,Method> methods = Methods.merge(
            CallGraph.build(args[0]),
            Methods.extract(args[1])
        );
        computeMaxFanInFanOut(methods);
        
        XMLPrinter xp = new XMLPrinter(new FileWriter("out.xml"));
        xp.writeXML(methods);
        
        long elapsed = System.nanoTime() - start;
        System.out.println("Elapsed time in Mallard.main: " + (elapsed / 1000000000.0) + " seconds");
    }

    private static void computeMaxFanInFanOut(ConcurrentMap<String,Method> methods) {
        int maxFanIn = 0;
        String maxFanInName = null;
        int maxFanOut = 0;
        String maxFanOutName = null;
        for (String qname : methods.keySet()) {
            Method method = methods.get(qname);
            int fanIn = method.getCallers().size();
            if (fanIn > maxFanIn) {
                maxFanIn = fanIn;
                maxFanInName = qname;
            }
            int fanOut = method.getCallees().size();
            if (fanOut > maxFanOut) {
                maxFanOut = fanOut;
                maxFanOutName = qname;
            }
        }
        System.out.println("Max Fan-In: " + maxFanIn + " --- " + maxFanInName);
        System.out.println("Max Fan-Out: " + maxFanOut + " --- " + maxFanOutName);
    }
    
}
