package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 * Field declaration
 *
 * @author gl16
 * @date 01/01/2020
 */
public abstract class AbstractDeclParam extends Tree {
      
    protected abstract void verifyDeclParamSignature(DecacCompiler compiler)
            throws ContextualError;
    
    protected abstract void verifyDeclParam(DecacCompiler compiler,
            EnvironmentExp localEnvParams, int offset) throws
            ContextualError;
    
    public abstract Type getType();
}