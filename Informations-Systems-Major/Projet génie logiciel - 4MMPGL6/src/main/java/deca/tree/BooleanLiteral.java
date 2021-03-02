package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import java.io.PrintStream;

/**
 *
 * @author gl16
 * @date 01/01/2020
 */
public class BooleanLiteral extends AbstractExpr {

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
         Type booleanType = new BooleanType(new SymbolTable().create("boolean"));
         this.setType(booleanType);
         return booleanType;
    }

    /*-----------------------Code generation----------------------------------*/
    @Override
    void codeGenInstPos(DecacCompiler compiler, Label etiquette){
        if (value){
            compiler.addInstruction(new BRA(etiquette));
        }
    }

    @Override
    void codeGenInstNeg(DecacCompiler compiler, Label etiquette){
        if (!value){
            compiler.addInstruction(new BRA(etiquette));
        }
    }

    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {
	if (value) {
		return new ImmediateInteger(1);
	} else {
		return new ImmediateInteger(0);
	}
    }

    /*------------------------------------------------------------------------*/


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }


}
