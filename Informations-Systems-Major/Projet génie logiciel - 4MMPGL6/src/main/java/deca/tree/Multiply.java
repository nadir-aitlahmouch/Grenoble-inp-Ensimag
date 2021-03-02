package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.instructions.MUL;

/**
 * @author gl16
 * @date 01/01/2020
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }

    /*------------------------------Code Generation----------------------------------*/
    @Override
    void codeGenOp(DecacCompiler compiler, GPRegister left, DVal right) {
	compiler.addInstruction(new MUL(right, left));
    }
    /*--------------------------------------------------------------------------------*/
    
}