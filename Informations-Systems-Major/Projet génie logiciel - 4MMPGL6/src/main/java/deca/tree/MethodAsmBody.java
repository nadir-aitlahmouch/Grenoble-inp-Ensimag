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
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.InlinePortion;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 *
 * @author aitlahmn
 */

public class MethodAsmBody extends AbstractMethodBody {
    final private StringLiteral codeAsm;
    
    public MethodAsmBody(StringLiteral codeAsm){
        Validate.notNull(codeAsm);
        this.codeAsm = codeAsm;
    }
    

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp envExp, EnvironmentExp envExpParams, ClassDefinition currentClass, Type returnType) throws ContextualError {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        codeAsm.verifyExpr(compiler, envExp, currentClass);
    }

    
    /*---------------------------------Code generation--------------------------------------*/
    @Override
    protected void codeGenBody(DecacCompiler compiler) {
        //String[] lignesCode = codeAsm.getValue().split("\n");
        codeAsm.codeGenExpr(compiler);
        String[] lignesCode = codeAsm.getValue().substring(1, codeAsm.getValue().length() - 1).split("\n");
        StringBuffer  asm = new StringBuffer();
        for (String ligne : lignesCode)
            asm.append("\t" + ligne.trim() + "\n");
        compiler.add(new InlinePortion(asm.toString()));
    }
   
    /*---------------------------------------------------------------------------------------*/
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("asm(");
        codeAsm.decompile(s);
        s.print(")");    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
    	codeAsm.prettyPrint(s, prefix, true);   
    }

    @Override
    protected void iterChildren(TreeFunction f) {
    	codeAsm.iter(f);
    }

    
}
