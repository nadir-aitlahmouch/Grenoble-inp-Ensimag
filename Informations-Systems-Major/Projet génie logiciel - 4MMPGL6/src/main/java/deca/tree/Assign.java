package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.RegisterManager;
import fr.ensimag.ima.pseudocode.instructions.PEA;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl16
 * @date 01/01/2020
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type typeLvalue = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        if (typeLvalue == null) throw new ContextualError("leftOperand cannot be null", this.getLocation()); 
        AbstractExpr expr = this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, typeLvalue);
        if(typeLvalue.isFloat() && expr.getType().isFloat())
        {
            this.setRightOperand(expr);
        }
        this.setType(this.getRightOperand().getType());
        return this.getRightOperand().getType();
    }
    
    /*-----------------------Code generation----------------------------------*/
    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {
    	RegisterOffset left = (RegisterOffset) getLeftOperand().codeGenExpr(compiler);
    	DVal right = (DVal) getRightOperand().codeGenExpr(compiler);
    	GPRegister reg;
        if (right instanceof Register) {
            reg = (GPRegister) right;
        } else {
        	reg = RegisterManager.getFreeRegister();
                compiler.addInstruction(new LOAD(right, reg));
        }
        compiler.addInstruction(new STORE(reg, left));
        RegisterManager.Refresh(reg);
    	return right;
    }

    /*------------------------------------------------------------------------*/

    @Override
    protected String getOperatorName() {
        return "=";
    }

}
