package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;


/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl16
 * @date 01/01/2020
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass){
        Type floatType = new FloatType(new SymbolTable().create("float"));
        this.setType(floatType);
        return this.getType();
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }
    
    /*----------------------------Generation du code------------------------*/

	@Override
	void codeGenOp(DecacCompiler compiler, GPRegister left, DVal right) {
            compiler.addInstruction(new FLOAT(right, left));
	}
	/*-----------------------------------------------------------------------*/
        
}
