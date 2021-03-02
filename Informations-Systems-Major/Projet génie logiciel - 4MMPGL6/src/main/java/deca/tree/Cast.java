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
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterManager;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 *
 * @author aitlahmn
 */
public class Cast extends AbstractExpr {
    private AbstractIdentifier castType;
    private AbstractExpr expr;

    public Cast(AbstractIdentifier castType, AbstractExpr expr){
       Validate.notNull(castType);
       Validate.notNull(expr);
       this.castType = castType;
       this.expr = expr;
    }


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type casttype = this.castType.verifyType(compiler);
        Type typeExpr = this.expr.verifyExpr(compiler, localEnv, currentClass);
        if(!typeExpr.getName().getName().equals("void"))
        {
            if(casttype.getName().getName().equals("float") && typeExpr.getName().getName().equals("int")
            || (casttype.getName().getName().equals("int") && typeExpr.getName().getName().equals("float"))
            || ((ClassType)typeExpr).isSubClassOf((ClassType) casttype) || ((ClassType)casttype).isSubClassOf((ClassType) typeExpr))
            {
                this.setType(casttype);
                return this.getType();
            }
        }
        throw new ContextualError("impossible Cast", this.getLocation());
    }

    /*------------------------Code generation---------------------------------*/
    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {
        DVal expressionValue = expr.codeGenExpr(compiler);
        Type typeExpression = expr.getType();
        Type typeButConversion = castType.getType();
        if (typeButConversion == typeExpression){
            return expressionValue; //Rien faire
        }
        else if (typeButConversion.isFloat() && typeExpression.isInt()){
            GPRegister r = RegisterManager.getFreeRegister();
            compiler.addInstruction(new FLOAT(expressionValue, r));
            return r;
        }

        else if (typeButConversion.isInt() && typeExpression.isFloat()){
            GPRegister r = RegisterManager.getFreeRegister();
            compiler.addInstruction(new INT(expressionValue, r));
            return r;
        }
        
        else if (typeButConversion.isClass()){
	    compiler.setGenInstanceOf(true);
            GPRegister classToCast = RegisterManager.getFreeRegister();
            GPRegister classDef = RegisterManager.getFreeRegister();
            
            if (expressionValue == classDef){compiler.addInstruction(new PUSH(classDef));}
            
            compiler.addInstruction(new LEA(new RegisterOffset(castType.getClassDefinition().getAdrTableMethodes(),GPRegister.GB), classDef));

            //On teste si l'instance est instanceof castType, sinon on branche vers une erreur
            compiler.addComment("Test de cast");

            compiler.addInstruction(new PUSH(classToCast));
            compiler.addInstruction(new PUSH(classDef));
            //On branche avec le code instance of généré dans le programme.
            compiler.addInstruction(new BSR(new Label("testInstanceOf.start")));
	    compiler.addInstruction(new SUBSP(2));
            
            compiler.addComment("Si le test est incompatible, on branche vers l'erreur");
            compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
            compiler.addInstruction(new BEQ(new Label("cast_error")));
            
            if (expressionValue == classDef){compiler.addInstruction(new POP(classDef));}
            return expressionValue;   
        }

        System.out.println("PROBLEME DANS CAST");
	return null;
    }
    /*------------------------------------------------------------------------*/

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        this.castType.decompile(s);
        s.print(") ");
        s.print("(");
        this.expr.decompile(s);
        s.print(")");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        castType.prettyPrint(s, prefix, true);
        expr.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        castType.iter(f);
        expr.iter(f);
    }

}
