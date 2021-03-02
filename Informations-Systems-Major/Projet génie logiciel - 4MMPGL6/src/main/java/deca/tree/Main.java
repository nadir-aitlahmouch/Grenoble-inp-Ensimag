package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.VoidType;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;

/**
 * @author gl16
 * @date 01/01/2020
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(Main.class);
    private int NbVar = 0;
    
    private ListDeclVar declVariables;
    private ListInst insts;
    public Main(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        // A FAIRE: Appeler méthodes "verify*" de ListDeclVarSet et ListInst.
        // Vous avez le droit de changer le profil fourni pour ces méthodes
        // (mais ce n'est à priori pas nécessaire).
        //throw new UnsupportedOperationException("not yet implemented");
        EnvironmentExp mainEnvironmentExp = new EnvironmentExp(null);
        Symbol voidSymbol = new SymbolTable().create("void");
        ClassType mainType = new ClassType(new SymbolTable().create("0"), this.getLocation(), null);
        ClassDefinition mainClass = new ClassDefinition(mainType, this.getLocation(), null);
        this.declVariables.verifyListDeclVariable(compiler, mainEnvironmentExp, mainClass);
        this.insts.verifyListInst(compiler, mainEnvironmentExp, mainClass, new VoidType(voidSymbol));
    }
    @Override
    protected void codeGenMain(DecacCompiler compiler, int offset) {
        if(this.declVariables.getList().size() != 0){
            compiler.incrementStackSize(this.declVariables.getList().size());
            compiler.addInstruction(new ADDSP(this.declVariables.getList().size()));
    }
        for (AbstractDeclVar declVar : declVariables.getList()){
            declVar.codeGenDeclVar(compiler, NbVar + offset, false);
            NbVar++;
        }
        compiler.addComment("Beginning of main instructions:");
        insts.codeGenListInst(compiler);
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        this.declVariables.decompile(s);
        this.insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }
 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
