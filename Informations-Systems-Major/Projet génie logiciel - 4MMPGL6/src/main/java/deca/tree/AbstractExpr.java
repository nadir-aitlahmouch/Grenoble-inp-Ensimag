package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl16
 * @date 01/01/2020
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     *
     * implements non-terminals "expr" and "lvalue"
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments
     *
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass,
            Type expectedType)
            throws ContextualError {
    
        Type t2 = this.verifyExpr(compiler, localEnv, currentClass);
        if(!t2.sameType(expectedType))
        {
            if(t2.isInt() && expectedType.isFloat())
            {
                AbstractExpr convfloat = new ConvFloat(this);
                convfloat.verifyExpr(compiler, localEnv, currentClass);
                return convfloat;
            }
            else
            {
                throw new ContextualError("incompatible type ", this.getLocation());
            }
        }
        else
        {
            if(t2.isClass())
            {
                if(!((ClassType) t2).isSubClassOf((ClassType)expectedType))
                {
                    throw new ContextualError(t2.getName().getName() +" must be a subClass of " + expectedType.getName().getName(), this.getLocation());
                }
            }
        }
        return this; /* an additional ConvFloat if needed */
    }


    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.verifyExpr(compiler, localEnv, currentClass);
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type conditionType = this.verifyExpr(compiler, localEnv, currentClass);
        if (!(conditionType.isBoolean()))
        {
            throw new ContextualError("incompatible type condition", this.getLocation());
        }
    }

    /**
     * Generate code to print the expression
     *
     */
    protected void codeGenPrint(DecacCompiler compiler, boolean hexadecimal) {
         DVal expr = (DVal)codeGenExpr(compiler);

         if (expr != GPRegister.R1)
             compiler.addInstruction(new LOAD(expr, GPRegister.R1));
        if (getType() == null) System.out.println("Type dans codegenprint null");
         if (getType().isFloat()) {
             if (hexadecimal) {
                 compiler.addInstruction(new WFLOATX());
              } else {
              compiler.addInstruction(new WFLOAT());
             }
         }
         else if (getType().isInt()) {
            if (hexadecimal) {
                compiler.addInstruction(new FLOAT(this.codeGenExpr(compiler),GPRegister.R1));
                compiler.addInstruction(new WFLOATX());
            }
             else{
             compiler.addInstruction(new WINT());
                     }
         }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	codeGenExpr(compiler);
    }

    /**
     * Generate expression and returns DVal
     */
    abstract public DVal codeGenExpr(DecacCompiler compiler);

    /*We genrate code (branch) whether the expression is true (Pos) or false (Neg)*/
    void codeGenInstPos(DecacCompiler compiler, Label etiquette){}
    void codeGenInstNeg(DecacCompiler compiler, Label etiquette){}


    @Override
    protected void decompileInst(IndentPrintStream s) {
        this.decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }
    
    public boolean isReturn() {
        return false;
    }
}
