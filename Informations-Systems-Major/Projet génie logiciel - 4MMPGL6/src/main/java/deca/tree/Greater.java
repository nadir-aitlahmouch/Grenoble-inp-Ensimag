package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.SGT;
import fr.ensimag.ima.pseudocode.instructions.SLE;

/**
 *
 * @author gl16
 * @date 01/01/2020
 */
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">";
    }

    /*---------------------------Code Generation--------------------------*/
    @Override
    protected void codeGenInstCmpPos(DecacCompiler compiler, Label label) {
            compiler.addInstruction(new BGT(label));
    }

    @Override
    protected void codeGenInstCmpNeg(DecacCompiler compiler, Label label) {
            compiler.addInstruction(new BLE(label));
    }

    @Override
    protected void fetchCondPos(DecacCompiler compiler, GPRegister r) {
           compiler.addInstruction(new SGT(r));
    }

    @Override
    protected void fetchCondNeg(DecacCompiler compiler, GPRegister r) {
        compiler.addInstruction(new SLE(r));
    }
    /*--------------------------------------------------------------------*/
}
