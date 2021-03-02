package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.RegisterManager;
import fr.ensimag.ima.pseudocode.instructions.BRA;

/**
 *
 * @author gl16
 * @date 01/01/2020
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
    @Override
    protected String getOperatorName() {
        return "&&";
    }

    /*--------------------------Code generation-------------------------------*/
    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {
	DVal right = getRightOperand().codeGenExpr(compiler);
        GPRegister RegistreUtilise = RegisterManager.getFreeRegister();
        compiler.addInstruction(new LOAD(right, RegistreUtilise));

        Label etCorps = new Label("AndBody" + this.getLocation().toStringLabel());
        Label etFin = new Label("AndEnd" + this.getLocation().toStringLabel());

        getLeftOperand().codeGenInstNeg(compiler, etCorps);

        compiler.addInstruction(new BRA(etFin));
        compiler.addLabel(etCorps);
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), RegistreUtilise));

        compiler.addLabel(etFin);

        return RegistreUtilise;
    }

    @Override
    void codeGenInstNeg(DecacCompiler compiler, Label etiquette){
        getLeftOperand().codeGenInstNeg(compiler, etiquette);
        getRightOperand().codeGenInstNeg(compiler, etiquette);
    }

    @Override
    void codeGenInstPos(DecacCompiler compiler, Label etiquette){
        Label andFin = new Label("FinAnd" + this.getLocation().toStringLabel());
        getLeftOperand().codeGenInstNeg(compiler, etiquette);
        getRightOperand().codeGenInstPos(compiler, etiquette);
        compiler.addLabel(andFin);
    }

    /*--------------------------------------------------------------------------*/
}
