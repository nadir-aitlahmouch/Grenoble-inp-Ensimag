package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.RegisterManager;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;
import java.util.Map;
import org.apache.commons.lang.Validate;

/**
 * Deca Identifier
 *
 * @author gl16
 * @date 01/01/2020
 */
public class Identifier extends AbstractIdentifier {

    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     *
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        if(localEnv.get(this.getName()) == null || localEnv.get(this.getName()).isMethod()) 
        {
            throw new ContextualError(this.getName().getName() +" is not declared", this.getLocation());
        }
        
        Definition identDefinition = localEnv.get(this.name);
        this.setDefinition(identDefinition);
        this.setType(identDefinition.getType());
        return this.getType();
    }
    @Override
    public Type verifyMethod(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass) throws ContextualError {
        if(localEnv.get(this.getName()) == null) 
            {
                throw new ContextualError("undeclared method " + this.getName().getName(), this.getLocation());
            }
            Definition identDefinition = localEnv.get(this.name);
            this.setDefinition(identDefinition);
            this.setType(identDefinition.getType());
            return this.getType();
    }
    
    public Type verifyField(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass) throws ContextualError {
        if(localEnv.get(this.getName()) == null || !localEnv.get(this.getName()).isField()) 
        {
            throw new ContextualError("the field "+this.getName().getName()+" is not declared", this.getLocation());
        }
        Definition identDefinition = localEnv.get(this.name);
        this.setDefinition(identDefinition);
        this.setType(identDefinition.getType());
        return this.getType();
    }
    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        Map<Symbol, Definition> tabtype = compiler.getEnvTypePredef().getEnvironment();
        if(tabtype.containsKey(this.name)
        && !tabtype.get(this.name).getType().isVoid())
        {
			/*
            Definition typeDefinition = new TypeDefinition(tabtype.get(this.name).getType(), this.getLocation());
            this.setDefinition(typeDefinition);
            this.setType(tabtype.get(this.name).getType());
            return tabtype.get(this.name).getType();
			*/
	    Definition typeDefinition = tabtype.get(this.name);
            this.setDefinition(typeDefinition);
            this.setType(typeDefinition.getType());
            return getType();
        }
        throw new ContextualError(this.name.getName() + " :incompatible  declaration type", this.getLocation());
    }


    private Definition definition;

    /*---------------------------Code Generation-------------------------------*/
    @Override
    public DVal codeGenExpr(DecacCompiler compiler) {  
       if (getExpDefinition().isField()){
            GPRegister r = RegisterManager.getFreeRegister();
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, GPRegister.LB), r));
            return new RegisterOffset(getFieldDefinition().getIndex(),r);
       } else if (getExpDefinition().isParam()) {
            return new RegisterOffset(-2 - ((ParamDefinition)definition).getIndex(), GPRegister.LB);
       } 
       
       return this.getExpDefinition().getOperand();
     }

    @Override
    public void codeGenInstPos(DecacCompiler compiler, Label etiquette){
        DVal dvalue = this.codeGenExpr(compiler);
        if ((dvalue instanceof ImmediateInteger) && ((ImmediateInteger)dvalue).getValue() == 1){
            compiler.addInstruction(new BRA(etiquette));
            return;
        }
        GPRegister RegistreUtilise = RegisterManager.getFreeRegister();
        compiler.addInstruction(new LOAD(dvalue, RegistreUtilise));
        compiler.addInstruction(new BNE(etiquette));
    }

    @Override
    public void codeGenInstNeg(DecacCompiler compiler, Label etiquette){
        DVal dvalue = this.codeGenExpr(compiler);
        if ((dvalue instanceof ImmediateInteger) && ((ImmediateInteger)dvalue).getValue() == 0){
            compiler.addInstruction(new BRA(etiquette));
            return;
        }

        GPRegister RegistreUtilise = RegisterManager.getFreeRegister();
        compiler.addInstruction(new LOAD(dvalue, RegistreUtilise));
        compiler.addInstruction(new BNE(etiquette));
    }

    /*-------------------------------------------------------------------------*/
    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.getName());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }

}
