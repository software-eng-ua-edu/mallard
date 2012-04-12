/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.mallard.util;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author      Nicholas A. Kraft <nkraft@cs.ua.edu>
 * @version     $Id: LineRange.java 4 2012-04-04 19:06:29Z nkraft $
 */
public class LineRange
{
    public LineRange() {
        this.lines = new ImmutablePair<Integer,Integer>(-1,-1);
    }

    public LineRange(int line) {
        this.lines = new ImmutablePair<Integer,Integer>(line, line);
    }

    public LineRange(int startLine, int endLine) {
        this.lines = new ImmutablePair<Integer,Integer>(startLine, endLine);
    }

    public int getStartLine() { return this.lines.getLeft(); }
    public int getEndLine() { return this.lines.getRight(); }

    private Pair<Integer,Integer> lines;
}
