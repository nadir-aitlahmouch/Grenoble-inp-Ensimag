/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.RegisterManager;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
/**
 *
 * @author aitlahmn
 */
public class Selection extends AbstractLValue {
    private AbstractExpr expr;
    private AbstractIdentifier ident;
    
    public Selection(AbstractExpr expression, AbstractIdentifier identifier){
       Validate.notNull(expression);
       Validate.notNull(identifier);
       this.expr = expression;
       this.ident = identifier;
    }
    
        @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type type2 = this.expr.verifyExpr(compiler, localEnv, currentClass);

        if(!type2.isClass()) throw new ContextualError(type2.getName().getName() + " is not a class", this.getLocation());

        Type selectionType;
        //if(this.expr instanceof Identifier){
            Definition classExpr = compiler.getEnvTypePredef().get(this.expr.getType().getName());
            EnvironmentExp exprMembers = ((ClassDefinition) classExpr).getMembers();
            //selectionType = this.ident.verifyField(compiler, exprMembers, currentClass);
            selectionType = this.ident.verifyField(compiler, exprMembers, (ClassDefinition) classExpr);

        //}
        //else{
         //   selectionType = this.ident.verifyField(compiler, currentClass.getMembers(), currentClass);
        //}
        Visibility fieldVisibility = this.ident.getFieldDefinition().getVisibility();
        if(fieldVisibility == Visibility.PROTECTED)
        {
            if(!(((ClassType)type2).isSubClassOf((ClassType)currentClass.getType()))
            || !(((ClassType)currentClass.getType()).isSubClassOf((ClassType)this.ident.getFieldDefinition().getContainingClass().getType())))
            {
                throw new ContextualError(this.ident.getName().getName() + " field is protected", this.getLocation());
            }
        }
        this.setType(selectionType);

        return this.ident.getType();

    }
    
    /*--------------------------Code generation--------------------------------*/
    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {
        DVal selectExpr = expr.codeGenExpr(compiler);
        GPRegister objAd;
        if (selectExpr instanceof GPRegister){
            objAd = (GPRegister) selectExpr;
        } else {
            objAd = RegisterManager.get(10);
            compiler.addInstruction(new LOAD(selectExpr, objAd));
        }
                
        if(!compiler.getCompilerOptions().getNoverify())
            {
                compiler.addInstruction(new CMP(new NullOperand(),objAd));
                compiler.addInstruction(new BEQ(new Label("dereferencement_null"))); 
            }
	return new RegisterOffset(ident.getFieldDefinition().getIndex(), objAd);
    }
    
    @Override 
    void codeGenInstPos(DecacCompiler compiler, Label etiquette){
        GPRegister r = RegisterManager.getFreeRegister();
        compiler.addInstruction(new LOAD(codeGenExpr(compiler), r));
        compiler.addInstruction(new BNE(etiquette));
    }
    
    @Override
    void codeGenInstNeg(DecacCompiler compiler, Label etiquette){
        GPRegister r = RegisterManager.getFreeRegister();
        compiler.addInstruction(new LOAD(codeGenExpr(compiler), r));
        compiler.addInstruction(new BEQ(etiquette));
    }
    
    /*-------------------------------------------------------------------------*/

    @Override
    public void decompile(IndentPrintStream s) {
        this.expr.decompile(s);
        s.print(".");
        this.ident.decompile(s);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.expr.prettyPrint(s, prefix, true);
        this.ident.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.expr.iter(f);
        this.ident.iter(f);
    }

}
