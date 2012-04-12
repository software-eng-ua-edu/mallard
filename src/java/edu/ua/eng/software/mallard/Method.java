/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.mallard;

import edu.ua.cs.mallard.util.LineRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author      Nicholas A. Kraft <nkraft@cs.ua.edu>
 * @version     $Id: Method.java 8 2012-04-05 05:00:05Z nkraft $
 */
public class Method
{
    public Method() {
        this.range = new LineRange();
    }

    public void setLineRange(LineRange range) {
        this.range = range;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQualifiedName(String qname) {
        this.qualifiedName = qname;
    }

    public void addCaller(Method caller) {
        this.callers.add(caller);
    }

    public void addCallee(String callee) {
        this.callees.add(callee);
    }

    public LineRange getLineRange() { return range; }
    public String getName() { return name; }
    public String getQualifiedName() { return qualifiedName; }
    public List<Method> getCallers() { return callers; }
    public List<String> getCallees() { return callees; }

    public String toString() {
        return qualifiedName; // + ":(" + range.getStartLine() + "," + range.getEndLine() + ")";
    }

    private LineRange range;
    private String name;
    private String qualifiedName;
    private List<Method> callers = new ArrayList<Method>();
    private List<String> callees = new ArrayList<String>();
}
