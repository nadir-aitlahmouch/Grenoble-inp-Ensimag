package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.RegisterManager;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;


/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl16
 * @date 01/01/2020
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {
    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        AbstractExpr leftOperand = this.getLeftOperand();
        AbstractExpr rightOperand = this.getRightOperand();
        Type leftType = leftOperand.verifyExpr(compiler, localEnv, currentClass);
        Type rightType = rightOperand.verifyExpr(compiler, localEnv, currentClass);
        Type type = new FloatType(new SymbolTable().create("float"));
        if(leftType.isInt() && rightType.isInt())
        {
            type = new IntType(new SymbolTable().create("int"));
        }
        else if(leftType.isFloat() && rightType.isInt())
        {
            this.setRightOperand((new ConvFloat(rightOperand)));
            this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        }
        else if(leftType.isInt() && rightType.isFloat())
        {
            this.setLeftOperand(new ConvFloat(leftOperand));
            this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        }
        else if(!(leftType.isFloat() && rightType.isFloat()))
        {
            throw new ContextualError("Unsupported Operation", this.getLocation());
        }
        this.setType(type);
        return type;
    }
    
    /*------------------------------Code Generation----------------------------------*/

    @Override
        public DVal codeGenExpr(DecacCompiler compiler) {
            DVal left = this.getLeftOperand().codeGenExpr(compiler);
            DVal right = this.getRightOperand().codeGenExpr(compiler);
            if(left instanceof GPRegister && right instanceof GPRegister)
            {
                if(((GPRegister)left).getNumber() == ((GPRegister)right).getNumber())
                {
                    right = (DVal) RegisterManager.findother((GPRegister)left);
                }
            }
            GPRegister RegistreUtilise;
            if(left instanceof GPRegister)
            {
                RegistreUtilise = (GPRegister) left;
            }
            else{
                RegistreUtilise = RegisterManager.getFreeRegister();
            }
            compiler.addInstruction(new LOAD(left, RegistreUtilise));
            codeGenOp(compiler, RegistreUtilise, right);
            if(right instanceof GPRegister)
            {
                 RegisterManager.Refresh(right);
            }
            if(!compiler.getCompilerOptions().getNoverify())
            {
                if(this.getLeftOperand().getType().isFloat() || this.getRightOperand().getType().isFloat())
                {
                    compiler.addInstruction(new BOV(new Label("overflow_error")));
                }
            }
            return RegistreUtilise;
	}
        
    abstract void codeGenOp(DecacCompiler compiler, GPRegister left, DVal right);
    /*--------------------------------------------------------------------------------*/

}
