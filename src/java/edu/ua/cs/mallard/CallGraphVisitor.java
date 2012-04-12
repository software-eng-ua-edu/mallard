/*
 * [The "New BSD" license]
 * Copyright (c) 2012 The Board of Trustees of The University of Alabama
 * All rights reserved.
 *
 * See LICENSE for details.
 */
package edu.ua.cs.mallard;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.regex.Pattern;

/**
 * @author      Nicholas A. Kraft <nkraft@cs.ua.edu>
 * @version     $Id: CallGraphVisitor.java 7 2012-04-05 03:35:07Z nkraft $
 */
public class CallGraphVisitor extends AsmClassMethodVisitor
{
    public CallGraph getCallGraph() {
        return callGraph;
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.currentClass = name.replaceAll("/", ".");
        this.currentClass = this.currentClass.replaceAll(Pattern.quote("$"), ".");
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if ("<clinit>".equals(name)) {
            this.currentMethod = null;
            return null;
        }

        if ("<init>".equals(name)) {
            this.currentMethod = this.currentClass + "." + this.currentClass.substring(this.currentClass.lastIndexOf('.') + 1) + parseMethodDescriptor(desc);
        } else {
            this.currentMethod = this.currentClass + "." + name + parseMethodDescriptor(desc);
        }
        return this;
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        /*
        switch (opcode) {
            case Opcodes.INVOKEDYNAMIC: break;
            case Opcodes.INVOKEINTERFACE: break;
            case Opcodes.INVOKESPECIAL: break;
            case Opcodes.INVOKESTATIC: break;
            case Opcodes.INVOKEVIRTUAL: break;
        }
        */
        if (null == this.currentMethod) {
            return;
        }

        if (owner.startsWith("[")) {
            owner = owner.substring(2, owner.length() - 1);
        }
        String ownerQualifiedName = owner.replaceAll("/", ".");
        ownerQualifiedName = ownerQualifiedName.replaceAll(Pattern.quote("$"), ".");
        String ownerName = ownerQualifiedName.substring(ownerQualifiedName.lastIndexOf('.') + 1);
        if ("<init>".equals(name)) {
            callGraph.addCall(this.currentMethod, ownerQualifiedName + "." + ownerName + parseMethodDescriptor(desc));
        } else {
            callGraph.addCall(this.currentMethod, ownerQualifiedName + "." + name + parseMethodDescriptor(desc));
        }
    }

    private static String parseMethodDescriptor(String desc) {
        String parameters = desc.substring(1, desc.lastIndexOf(')'));
        int plen = parameters.length();
        if (0 == plen) {
            return "()";
        }
        String signature = "(";

        String array = "";
        String clazz = "";
        int i = 0;
        while (i < plen) {
            char current = parameters.charAt(i);
            while ('[' == current) {
                array += "[]";
                current = parameters.charAt(++i);
            }
            switch (current) {
                case 'Z': signature += "boolean"; break;
                case 'C': signature += "char";    break;
                case 'B': signature += "byte";    break;
                case 'S': signature += "short";   break;
                case 'I': signature += "int";     break;
                case 'F': signature += "float";   break;
                case 'J': signature += "long";    break;
                case 'D': signature += "double";  break;
                case 'L':
                    current = parameters.charAt(++i);
                    while (';' != current) {
                        clazz += current;
                        current = parameters.charAt(++i);
                    }
                    clazz = clazz.substring(clazz.lastIndexOf('/') + 1);
                    clazz = clazz.replaceAll(Pattern.quote("$"), ".");
                    signature += clazz;
                    break;
            }
            signature += (array + ',');
            array = "";
            clazz = "";
            ++i;
        }
        return signature.substring(0, signature.lastIndexOf(',')) + ')';
    }

    private String currentClass;
    private String currentMethod;
    private CallGraph callGraph = new CallGraph();
}
