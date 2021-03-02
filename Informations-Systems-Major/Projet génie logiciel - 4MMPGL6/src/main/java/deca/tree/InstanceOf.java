/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterManager;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.SNE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
/**
 *
 * @author aitlahmn
 */
public class InstanceOf extends AbstractExpr {
    
    private AbstractExpr expr;
    private AbstractIdentifier compareClass;
    
    public InstanceOf(AbstractExpr expression, AbstractIdentifier classIdent){
       Validate.notNull(expression);
       Validate.notNull(classIdent);
       this.expr = expression;
       this.compareClass = classIdent;
    }
    
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type typeExpr = expr.verifyExpr(compiler, localEnv, currentClass);
        Type typeTarget = compareClass.verifyType(compiler);

        if ((!typeExpr.isNull() && !typeExpr.isClass()) || !typeTarget.isClass()){
                throw new ContextualError("InstanceOf can only be used with Classes", this.getLocation());
        }

        Type typeInst = new BooleanType(new SymbolTable().create("boolean"));
	setType(typeInst);
        compiler.setGenInstanceOf(true);
        return typeInst;
    }

    /*--------------------Code generation-------------------------------------*/
	@Override 
	public DVal codeGenExpr(DecacCompiler compiler) {
        ClassType thisClassType = (ClassType)expr.getType();
        if (expr.getType().isNull() || thisClassType.isSubClassOf((ClassType) compareClass.getType())){
            return new ImmediateInteger(1);
        } else {
            return dynamicSubTypetest(compiler);
            }
	}
        
        private DVal dynamicSubTypetest (DecacCompiler compiler) {
        compiler.addComment("Test is dynamic SubType");
        DVal obj = expr.codeGenExpr(compiler);
        RegisterOffset classDef = new RegisterOffset(compareClass.getClassDefinition().getAdrTableMethodes(), Register.GB);
        GPRegister targetClass = RegisterManager.getFreeRegister();
        assert targetClass != obj;
        compiler.addInstruction(new LEA(classDef, targetClass));
        GPRegister addrTargetObject  = RegisterManager.getFreeRegister();
        assert addrTargetObject != targetClass;
        compiler.addInstruction(new LOAD(obj, addrTargetObject));
        compiler.addInstruction(new LOAD(new RegisterOffset(0, addrTargetObject), addrTargetObject));
        compiler.addInstruction(new PUSH(addrTargetObject));
        compiler.addInstruction(new PUSH(targetClass));
        compiler.addInstruction(new BSR(new Label("testInstanceOf.start")));
	compiler.addInstruction(new SUBSP(2));
        compiler.addInstruction(new LOAD(Register.R0, targetClass));
        return targetClass;
    }

    @Override
    protected void codeGenInstPos(DecacCompiler compiler, Label etiquette)
    {
        GPRegister r = RegisterManager.getFreeRegister();
        compiler.addInstruction(new LOAD(codeGenExpr(compiler), r));
        compiler.addInstruction(new CMP(new ImmediateInteger(0), r));
        compiler.addInstruction(new BNE(etiquette));
    }

    @Override
    public void codeGenInstNeg(DecacCompiler compiler, Label etiquette) {
        GPRegister r = RegisterManager.getFreeRegister();
        compiler.addInstruction(new LOAD(codeGenExpr(compiler), r));
        compiler.addInstruction(new CMP(new ImmediateInteger(0), r));
        compiler.addInstruction(new BEQ(etiquette));
    }
    
    /*-------------------------------------------------------------------------*/
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        this.expr.decompile(s);
        s.print(" instanceof ");
        this.compareClass.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.compareClass.prettyPrint(s, prefix, true);
        this.expr.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
       this.compareClass.iter(f);
       this.expr.iter(f);
    }
}
