package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import java.util.HashMap;
import java.util.Map;

/**
 * Field declaration
 *
 * @author gl16
 * @date 01/01/2020
 */
public abstract class AbstractDeclField extends Tree {
      
protected abstract void verifyDeclField(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;
    
protected abstract void verifyDeclFieldBody(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

protected abstract GPRegister codeGenDeclField(DecacCompiler compiler, GPRegister ClassAdress, int offset);

public abstract AbstractIdentifier getType();
public abstract AbstractIdentifier getFieldName(); 
public abstract AbstractInitialization getInitialization(); 
public abstract Visibility getVisibilite(); 
}
