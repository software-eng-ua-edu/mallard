/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.eng.software.mallard;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.ConcurrentMap;

import edu.ua.eng.software.mallard.util.LineRange;

/**
 * @author	Blake Bassett <rbbassett@crimson.ua.edu>
 * @version	$Id: XMLPrinter.java 1 2012-05-06 17:16:22Z blkbsstt $
 */
public class XMLPrinter
{
	public XMLPrinter(Writer writer) {
		this.output = writer;
	}
	
	public void writeXML(ConcurrentMap<String,Method> methods) throws IOException { 
		startTag("methods", CLOSED);
		for (String qname : methods.keySet()) {
			Method method = methods.get(qname);
			startTag("method", OPEN);
			methodAttr(method);
			callingAttr(method);
			closeStartTag();
			startTag("callers", CLOSED);
			for (Method caller : method.getCallers()) {
				startTag("caller", OPEN);
				methodAttr(caller);
				closeStartTag();
				endTag("caller");
			}
			endTag("callers");
			startTag("callees", CLOSED);
			for (String cname : method.getCallees()) {
				startTag("callee", OPEN);
				addAttr("qname", cname);
				closeStartTag();
				endTag("callee");
			}
			endTag("callees");
			endTag("method");
		}
		endTag("methods");
		output.flush();
	};
	
	private void startTag(String tag, boolean closed) throws IOException {
		indent();
		output.write("<" + tag);
		if (closed) closeStartTag();
		++indentLevel;
	}
	
	private void closeStartTag() throws IOException {
		output.write(">\n");
	}
	
	private void endTag(String tag) throws IOException {
		--indentLevel;
		indent();
		output.write("</" + tag + ">\n");
	}
	
	private void indent() throws IOException {
		for(int i = 0; i < indentLevel; ++i) output.write(indent);
	}
	
	private void methodAttr(Method method) throws IOException {
		addAttr("qname", method.getQualifiedName());
		addAttr("filename", method.getFilename());
		
		LineRange range = method.getLineRange();
		addAttr("startLine", range.getStartLine());
		addAttr("endLine", range.getEndLine());
	}
	
	private void callingAttr(Method method) throws IOException {
		addAttr("ncallers", method.getCallers().size());
		addAttr("ncallees", method.getCallees().size());
	}
	
	private void addAttr(String key, String value) throws IOException {
		output.write(" " + key + "=\"" + value + "\"");
	}
	
	private void addAttr(String key, int value) throws IOException {
		addAttr(key, String.valueOf(value));
	}

	private Writer output;
	private int indentLevel = 0;
	private String indent = "\t";
	private final boolean CLOSED = true;
	private final boolean OPEN = false;
}
