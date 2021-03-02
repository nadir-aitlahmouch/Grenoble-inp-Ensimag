package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * @author gl16
 * @date 01/01/2020
 */
public class Initialization extends AbstractInitialization {

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type t2 = this.getExpression().verifyExpr(compiler, localEnv, currentClass);
        if(!t2.sameType(t))
        {
            if(t2.isInt() && t.isFloat())
            {
                ConvFloat convfloat = new ConvFloat(this.getExpression());
                this.setExpression(convfloat);
                convfloat.verifyExpr(compiler, localEnv, currentClass);
            }
            else if(!t2.isNull())
            {
                throw new ContextualError("incompatible initialization", this.getLocation());
            }
        } 	
            else
        {
            if(t2.isClass())
            {
                if(!((ClassType) t2).isSubClassOf((ClassType)t))
                {
                    throw new ContextualError(t2.getName().getName() +" must be a subClass of " + t.getName().getName(), this.getLocation());
                }
            }
        }
    }
    
    /*---------------------Code Generation-------------------------------------*/
    @Override
    DVal codeGenExpr(DecacCompiler compiler) {
        return (DVal)expression.codeGenExpr(compiler);
    }
    /*-------------------------------------------------------------------------*/

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" = ");
        this.expression.decompile(s);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }

}
