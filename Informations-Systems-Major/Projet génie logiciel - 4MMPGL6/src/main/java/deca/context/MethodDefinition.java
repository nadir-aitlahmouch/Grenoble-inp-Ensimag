package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;

/**
 * Definition of a method
 *
 * @author gl16
 * @date 01/01/2020
 */

public class MethodDefinition extends ExpDefinition implements Comparable{
    
        
    private Label codelabel;
    private Label finlabel;
    private Symbol methodName;

    public void setMethodName(Symbol methodName) {
        this.methodName = methodName;
    }

    public Object getMethodName() {
        return methodName;
    }

    @Override
    public boolean isMethod() {
        return true;
    }

    public Label getCodeLabel() {
        Validate.isTrue(codelabel != null,
                "setLabel() should have been called before");
        return codelabel;
    }

    public void setCodeLabel(Label label) {
        this.codelabel = label;
    }

    public Label getFinlabel() {
        return finlabel;
    }

    public void setFinlabel(Label finlabel) {
        this.finlabel = finlabel;
    }
    
    

    public int getIndex() {
        return index;
    }

    private int index;

    @Override
    public MethodDefinition asMethodDefinition(String errorMessage, Location l)
            throws ContextualError {
        return this;
    }

    private Signature signature;
    //private Label label;
    
    /**
     * 
     * @param type Return type of the method
     * @param location Location of the declaration of the method
     * @param signature List of arguments of the method
     * @param index Index of the method in the class. Starts from 0.
     */
    public MethodDefinition(Type type, Location location, Signature signature, int index) {
        super(type, location);
        this.signature = signature;
        this.index = index;
    }

    public Signature getSignature() {
        return signature;
    }

    @Override
    public String getNature() {
        return "method";
    }

    @Override
    public boolean isExpression() {
        return false;
    }

    @Override
    public int compareTo(Object t) {
        if(this.index < ((MethodDefinition)t).index) return -1;
        else if (this.index > ((MethodDefinition)t).index) return 1;
        else return 0;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + this.index;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MethodDefinition other = (MethodDefinition) obj;
        if (this.index != other.index) {
            return false;
        }
        return true;
    }


    public void codeGenTableMethode(DecacCompiler compiler, ClassDefinition currentClass, int offset) {
        compiler.addInstruction(new LOAD(new LabelOperand(new Label("code." + currentClass.getClassDefinition(this).getType().getName() + "." + this.methodName.getName())), GPRegister.R0));
        compiler.addInstruction(new STORE(GPRegister.R0, new RegisterOffset(offset, GPRegister.GB)));    }
    
}
