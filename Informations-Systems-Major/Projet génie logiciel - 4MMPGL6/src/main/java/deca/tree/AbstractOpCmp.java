package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.RegisterManager;
import java.util.ArrayList;


/**
 *
 * @author gl16
 * @date 01/01/2020
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        AbstractExpr leftOperand = this.getLeftOperand();
        AbstractExpr rightOperand = this.getRightOperand();
        Type leftOpType = leftOperand.verifyExpr(compiler, localEnv, currentClass);
        Type rightOpType = rightOperand.verifyExpr(compiler, localEnv, currentClass);

        ArrayList<String> infSupOp = this.getInfSupOp();
        ArrayList<String> eqNeqOp = this.getEqNeqOp();
        ArrayList<String> andOrOp = this.getAndOrOp();
        String operator = this.getOperatorName();

        if (infSupOp.contains(operator) || eqNeqOp.contains(operator))
        {
            Type typeBool;
            if(leftOpType.isInt() && rightOpType.isFloat()){
                typeBool = new BooleanType(new SymbolTable().create("boolean"));
                this.setLeftOperand((new ConvFloat(leftOperand)));
                this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
                this.setType(typeBool);
                return typeBool;
            }
            else if(leftOpType.isFloat() && rightOpType.isInt()){
                typeBool = new BooleanType(new SymbolTable().create("boolean"));
                this.setRightOperand((new ConvFloat(rightOperand)));
                this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
                this.setType(typeBool);
                return typeBool;
            }
            else if((leftOpType.isFloat() && rightOpType.isFloat())
                 || (leftOpType.isInt() && rightOpType.isInt())){
                typeBool = new BooleanType(new SymbolTable().create("boolean"));
                this.setType(typeBool);
                return typeBool;
            }
        }

        else if ((eqNeqOp.contains(operator)) && (leftOpType.isClass() &&
            rightOpType.isClass())){
            Type typeBool = new BooleanType(new SymbolTable().create("boolean"));
            this.setType(typeBool);
            return typeBool;
        }

        else if ((eqNeqOp.contains(operator) || andOrOp.contains(operator)) &&
            (leftOpType.isBoolean() && rightOpType.isBoolean())){
            Type typeBool = new BooleanType(new SymbolTable().create("boolean"));
            this.setType(typeBool);
            return typeBool;
        }
        throw new ContextualError("incompatible type expression", this.getLocation());
    }


    private ArrayList<String> getInfSupOp() {
        ArrayList<String> opList = new ArrayList<String>();
        opList.add("<");
        opList.add(">");
        opList.add(">=");
        opList.add("<=");
        return opList;
    }

    private ArrayList<String> getEqNeqOp() {
        ArrayList<String> opList = new ArrayList<String>();
        opList.add("==");
        opList.add("!=");
        return opList;
    }

    private ArrayList<String> getAndOrOp() {
        ArrayList<String> opList = new ArrayList<String>();
        opList.add("&&");
        opList.add("||");
        return opList;
    }


    /*----------------------------------Code Generation--------------------------------*/
	@Override
	public DVal codeGenExpr(DecacCompiler compiler) {
        DVal left = this.getLeftOperand().codeGenExpr(compiler);
        DVal right = this.getRightOperand().codeGenExpr(compiler);
        GPRegister RegistreUtilise;
        if(left instanceof GPRegister)
        {
            RegistreUtilise = (GPRegister) left;
        }
        else{
            RegistreUtilise = RegisterManager.getFreeRegister();
        }
        compiler.addInstruction(new LOAD(left, RegistreUtilise));
        compiler.addInstruction(new CMP(right, RegistreUtilise));
        if(right instanceof GPRegister)
        {
            RegisterManager.Refresh(right);
        }

        fetchCondPos(compiler, RegistreUtilise);
        return RegistreUtilise;
	}


    @Override
    protected void codeGenInstPos(DecacCompiler compiler, Label label)
    {
        DVal left = this.getLeftOperand().codeGenExpr(compiler);
        DVal right = this.getRightOperand().codeGenExpr(compiler);
        GPRegister RegistreUtilise;
        if(left instanceof GPRegister)
        {
            RegistreUtilise = (GPRegister) left;
        }
        else{
            RegistreUtilise = RegisterManager.getFreeRegister();
        }
        compiler.addInstruction(new LOAD(left, RegistreUtilise));
        compiler.addInstruction(new CMP(right, RegistreUtilise));
        codeGenInstCmpPos(compiler, label);
    }

    @Override
    protected void codeGenInstNeg(DecacCompiler compiler, Label label)
    {
        DVal left = this.getLeftOperand().codeGenExpr(compiler);
        DVal right = this.getRightOperand().codeGenExpr(compiler);
        GPRegister RegistreUtilise;
        if(left instanceof GPRegister)
        {
            RegistreUtilise = (GPRegister) left;
        }
        else{
            RegistreUtilise = RegisterManager.getFreeRegister();
        }
        compiler.addInstruction(new LOAD(left, RegistreUtilise));
        compiler.addInstruction(new CMP(right, RegistreUtilise));
        codeGenInstCmpNeg(compiler, label);
    }


    protected abstract void codeGenInstCmpPos(DecacCompiler compiler, Label label);
    protected abstract void codeGenInstCmpNeg(DecacCompiler compiler, Label label);

    protected void fetchCondPos(DecacCompiler compiler, GPRegister r){};
    protected void fetchCondNeg(DecacCompiler compiler, GPRegister r){};
	/*--------------------------------------------------------------------------------- */

}
