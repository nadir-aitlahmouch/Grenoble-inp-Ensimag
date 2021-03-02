package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;

/**
 *
 * @author gl16
 * @date 01/01/2020
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

    /*------------------------------Code Generation----------------------------------*/
	@Override
	void codeGenOp(DecacCompiler compiler, GPRegister left, DVal right) {
            if(this.getLeftOperand().getType().isFloat() && this.getRightOperand().getType().isFloat())
            {
                compiler.addInstruction(new DIV(right, left));
            }
            else if(this.getLeftOperand().getType().isInt() && this.getRightOperand().getType().isInt())
            {
		compiler.addInstruction(new QUO(right, left));
                if(!compiler.getCompilerOptions().getNoverify())
                {
                   if(this.getRightOperand() instanceof IntLiteral){
                   if(((IntLiteral)this.getRightOperand()).getValue() == 0)
                    {
                        compiler.addInstruction(new BOV(new Label("overflow_error")));
                    }
                }
                }
            }
	}
	
    /*--------------------------------------------------------------------------------*/


}


