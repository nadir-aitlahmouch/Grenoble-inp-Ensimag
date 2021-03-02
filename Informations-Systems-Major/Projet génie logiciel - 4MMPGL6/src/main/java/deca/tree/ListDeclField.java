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
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author fennaneo
 */
public class ListDeclField extends TreeList<AbstractDeclField> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField f : getList()) {
            f.decompile(s);
            s.print(";");
            s.println();
        }
    }

    
    // pour la passe 2 :  explication voir petit poly page 13
    public void verifyListDeclField(DecacCompiler compiler, ClassDefinition
            superClass, ClassDefinition currentClass, EnvironmentExp envExp) throws ContextualError {   
        for(AbstractDeclField field : this.getList()){
            field.verifyDeclField(compiler ,envExp,currentClass);
        } 
        if(this.getList().isEmpty())
        {
            currentClass.setNumberOfFields(currentClass.getSuperClass().getNumberOfFields());
        }
        
    }
    
    // pour la passe 3 : explication voir poly
    void verifyListDeclFieldBody(DecacCompiler compiler, EnvironmentExp envExp,
            ClassDefinition currentClass) throws ContextualError{
        
        for(AbstractDeclField field : this.getList()){
            field.verifyDeclFieldBody(compiler, envExp, currentClass);
        }
    }
        
}
