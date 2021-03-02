package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.RegisterManager;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;

/**
 * @author gl16
 * @date 01/01/2020
 */
public class DeclVar extends AbstractDeclVar {

    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        try {
           if(localEnv.getExpTable().containsKey(this.varName.getName()))
            {  
                throw new ContextualError(this.varName.getName().getName() + " is already declared", this.getLocation());
            }
            Type typeType = this.type.verifyType(compiler);
            Definition typeDefinition = new TypeDefinition(typeType, this.getLocation());
            Definition varNameDefinition = new VariableDefinition(typeType, this.getLocation());
            this.type.setDefinition(typeDefinition);
            this.type.setType(typeType);
            this.varName.setDefinition(varNameDefinition);
            this.initialization.verifyInitialization(compiler, typeType, localEnv, currentClass);
            localEnv.declare(this.varName.getName(), this.varName.getVariableDefinition());
        } catch (EnvironmentExp.DoubleDefException ex) {
            Logger.getLogger(DeclVar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*--------------------Code Generation-------------------------------------*/
    @Override
    void codeGenDeclVar(DecacCompiler compiler, int offset, boolean local) {
        Operand init = initialization.codeGenExpr(compiler);
        DAddr adresse;
        if (!local) { 
        adresse = new RegisterOffset(offset, Register.GB);
        }
        else {
        adresse = new RegisterOffset(offset, Register.LB);	
        }
        this.varName.getVariableDefinition().setOperand(adresse);
        if (initialization instanceof Initialization){
            GPRegister RegistreUtilise = RegisterManager.getFreeRegister();
            compiler.addInstruction(new LOAD((DVal)init,RegistreUtilise));
            compiler.addInstruction(new STORE(RegistreUtilise,adresse));
            RegisterManager.Refresh(RegistreUtilise);
        }
    }
    /*------------------------------------------------------------------------*/
    @Override
    public void decompile(IndentPrintStream s) {
        this.type.decompile(s);
        s.print(" ");
        this.varName.decompile(s);
        this.initialization.decompile(s);
        s.print(";");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

}
