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
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author fennaneo
 */
public class ListDeclParam extends TreeList<AbstractDeclParam> {

    @Override
    public void decompile(IndentPrintStream s) {
	int i = 1;
        for (AbstractDeclParam p : getList()){
            p.decompile(s);
	    if (i != getList().size()){
            	s.print(", ");
		i = i + 1;
	    }
        }
    }

 
    public Signature verifyListDeclParamSignature(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Signature signature = new Signature();
        for(AbstractDeclParam param : this.getList()){
            param.verifyDeclParamSignature(compiler);
            signature.add(param.getType());
        }
        return signature;
    }
    
    public EnvironmentExp verifyListDeclParam(DecacCompiler compiler, 
            EnvironmentExp parent) throws ContextualError{
        int offset = 1; 
        EnvironmentExp localEnvParams = new EnvironmentExp(parent);
        for(AbstractDeclParam param : this.getList()){
            param.verifyDeclParam(compiler, localEnvParams, offset);
            offset = offset + 1;
        }
        return localEnvParams;
    }
}
