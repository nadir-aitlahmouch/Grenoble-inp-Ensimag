/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.RegisterManager;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 *
 * @author ossohou
 */
public class MethodBody extends AbstractMethodBody {
    
    private ListDeclVar declVariables;
    private ListInst insts;
    private int nbVar = 1;
    
    
    public MethodBody(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }
    
    @Override
    protected  void verifyMethodBody(DecacCompiler compiler,
            EnvironmentExp envExp, EnvironmentExp envExpParams,
            ClassDefinition currentClass, Type returnType) throws ContextualError{
        
        this.declVariables.verifyListDeclVariable(compiler, envExpParams, currentClass);
        this.insts.verifyListInst(compiler, envExpParams, currentClass, returnType);
    }
    
    
    /*-------------------------Code Generation---------------------------------*/
    @Override
    protected void codeGenBody(DecacCompiler compiler) {
        compiler.getProgramStack().add(compiler.getProgram());
        compiler.setProgram(new IMAProgram());
    	RegisterManager.AllUsed();
    	compiler.addComment("Variables locales");
        for (AbstractDeclVar variable : declVariables.getList()){
            variable.codeGenDeclVar(compiler, nbVar, true);
            nbVar++;
        }
        compiler.addComment("Instructions");
        for (AbstractInst i : this.insts.getList()) {
            i.codeGenInst(compiler);
        }
        int nbrpush = RegisterManager.getNbrPush();
        RegisterManager.PopAllUsed();
        compiler.addInstruction(new SUBSP(declVariables.size()));
        
        IMAProgram programBody = compiler.getProgram();
        compiler.setProgram(compiler.getProgramStack().pop());
        
        int valtsto = declVariables.size() + nbrpush; 
        //Generation du tsto
        if (!compiler.getCompilerOptions().getNoverify()) {
            compiler.addInstruction(new TSTO(valtsto));
            compiler.addInstruction(new BOV(new Label("stack_overflow_error")));
        }
    	
        compiler.addInstruction(new ADDSP(declVariables.size()));
        compiler.getProgram().append(programBody);
    }    
    /*-------------------------------------------------------------------------*/
    
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
    }

    @Override
    protected void iterChildren(TreeFunction f) {
      this.declVariables.iter(f);
      this.insts.iter(f);
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
      this.declVariables.prettyPrint(s, prefix, true);
      this.insts.prettyPrint(s, prefix, true);
    }


}
