package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.REM;

/**
 *
 * @author gl16
 * @date 01/01/2020
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

        AbstractExpr leftOperand = this.getLeftOperand();
        AbstractExpr rightOperand = this.getRightOperand();
        Type leftType = leftOperand.verifyExpr(compiler, localEnv, currentClass);
        Type rightType = rightOperand.verifyExpr(compiler, localEnv, currentClass);
        if (leftType.isInt() && rightType.isInt()) {
            this.setType(leftType);
            return leftType;
        }
        else throw new ContextualError("type must be int for modulo", this.getLocation());

    }


    @Override
    protected String getOperatorName() {
        return "%";
    }

    /*------------------------------Code Generation----------------------------------*/
    @Override
    void codeGenOp(DecacCompiler compiler, GPRegister left, DVal right) {
		compiler.addInstruction(new REM(right, left));
                if(!compiler.getCompilerOptions().getNoverify()){
                    if (this.getRightOperand() instanceof IntLiteral){
                        if (((IntLiteral)this.getRightOperand()).getValue() == 0){
                            compiler.addInstruction(new BOV(new Label("overflow_error")));
                   }
                }
        }
    
    }
    /*--------------------------------------------------------------------------------*/

}
