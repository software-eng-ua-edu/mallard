/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.mallard;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;
import java.util.zip.ZipFile;

/**
 * @author      Nicholas A. Kraft <nkraft@cs.ua.edu>
 * @version     $Id: MethodParser.java 9 2012-04-06 20:39:29Z nkraft $
 */
public class MethodParser implements Runnable
{
    MethodParser(ZipFile zip, Queue<String> filenames, ConcurrentMap<String,Method> methods) {
        this.zip = zip;
        this.filenames = filenames;
        this.methods = methods;
    }

    public void run() {
        String fn = null;
        while (true) {
            synchronized(filenames) {
                if (!filenames.isEmpty()) {
                    fn = filenames.poll();
                } else {
                    return;
                }
            }
            parse(fn);
        }
    }

    private void parse(String filename) {
        JavaLexer lexer;
        JavaParser parser = null; // tell javac to shut the hell up
        try {
            lexer = new Java5Lexer(new ANTLRInputStream((this.zip.getInputStream(this.zip.getEntry(filename))), "iso-8859-1"));
            parser = new JavaParser(new CommonTokenStream(lexer));
            List<Method> methods = parser.compilationUnit();
            for (Method method : methods) {
                this.methods.putIfAbsent(method.getQualifiedName(), method);
            }
        } catch (IOException e) {
        } catch (RecognitionException e) {
            parser.reportError(e);
        }
    }

    private ZipFile zip;
    private Queue<String> filenames;
    private ConcurrentMap<String,Method> methods;
}
