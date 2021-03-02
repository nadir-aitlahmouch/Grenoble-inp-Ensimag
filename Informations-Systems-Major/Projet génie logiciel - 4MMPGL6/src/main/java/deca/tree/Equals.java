package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.SEQ;
import fr.ensimag.ima.pseudocode.instructions.SNE;

/**
 *
 * @author gl16
 * @date 01/01/2020
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "==";
    }

    /*---------------------------Code Generation--------------------------*/
    @Override
    protected void codeGenInstCmpPos(DecacCompiler compiler, Label label) {
            compiler.addInstruction(new BEQ(label));
    }

    @Override
    protected void codeGenInstCmpNeg(DecacCompiler compiler, Label label) {
            compiler.addInstruction(new BNE(label));
    }

    @Override
    protected void fetchCondPos(DecacCompiler compiler, GPRegister r) {
           compiler.addInstruction(new SEQ(r));
    }

    @Override
    protected void fetchCondNeg(DecacCompiler compiler, GPRegister r) {
        compiler.addInstruction(new SNE(r));
    }
    /*--------------------------------------------------------------------*/
}
