/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.cs.mallard;

import org.antlr.runtime.CharStream;

/**
 * @author      Nicholas A. Kraft <nkraft@cs.ua.edu>
 * @version     $Id: Java5Lexer.java 4 2012-04-04 19:06:29Z nkraft $
 */
public class Java5Lexer extends JavaLexer
{
    public Java5Lexer(CharStream input) {
        super(input);
    }

    protected boolean enumIsKeyword() { return true; }
    protected boolean assertIsKeyword() { return true; }
}
