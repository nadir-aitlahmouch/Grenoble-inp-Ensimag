/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.NullType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.NullOperand;

import java.io.PrintStream;

/**
 *
 * @author fennaneo
 */
public class Null extends AbstractExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t = new NullType((new SymbolTable()).create("null"));
        this.setType(t);
        return t;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("null");
    }
    
    /*-------------------------Code Generation--------------------------------*/
    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {
	    return new NullOperand();
    }
    /*-------------------------------------------------------------------------*/

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
    	// leaf node => nothing to do
    }

    @Override
    protected void iterChildren(TreeFunction f) {
    	// leaf node => nothing to do
    }

}
