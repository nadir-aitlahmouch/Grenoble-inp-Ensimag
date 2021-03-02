package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.SGE;
import fr.ensimag.ima.pseudocode.instructions.SLT;

/**
 *
 * @author gl16
 * @date 01/01/2020
 */
public class Lower extends AbstractOpIneq {

    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "<";
    }

    /*---------------------------Code Generation--------------------------*/
    @Override
    protected void codeGenInstCmpPos(DecacCompiler compiler, Label label) {
            compiler.addInstruction(new BLT(label));
    }

    @Override
    protected void codeGenInstCmpNeg(DecacCompiler compiler, Label label) {
            compiler.addInstruction(new BGE(label));
    }

    @Override
    protected void fetchCondPos(DecacCompiler compiler, GPRegister r) {
           compiler.addInstruction(new SLT(r));
    }

    @Override
    protected void fetchCondNeg(DecacCompiler compiler, GPRegister r) {
        compiler.addInstruction(new SGE(r));
    }
    /*--------------------------------------------------------------------*/


}
