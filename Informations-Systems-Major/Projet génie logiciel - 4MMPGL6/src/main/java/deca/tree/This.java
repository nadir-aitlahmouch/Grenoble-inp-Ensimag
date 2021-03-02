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
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.RegisterManager;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

/**
 *
 * @author fennaneo
 */
public class This extends AbstractExpr {
    private boolean impl;

    public This(boolean impl) {
        this.impl = impl;
    }

    public boolean getValue() {
        return impl;
    }
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        this.setType(currentClass.getType());
         if(this.getType().isClass()
         && !this.getType().getName().getName().equals("0"))
        {
           return this.getType();
        }
        throw new ContextualError("cannot use this in main", this.getLocation());
    }
    
    /*-------------------------Code generation--------------------------------*/
    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {
        GPRegister reg = RegisterManager.getFreeRegister();
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, GPRegister.LB), reg));
        return reg;
	}
    /*------------------------------------------------------------------------*/
    
    
    @Override
    public void decompile(IndentPrintStream s) {
        //s.print(Boolean.toString(impl));
        s.print("this");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
}
