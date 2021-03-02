package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import java.util.List;
import java.util.TreeSet;
import org.apache.log4j.Logger;

/**
 *
 * @author gl16
 * @date 01/01/2020
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        for(AbstractDeclClass classe : this.getList()){
            classe.verifyClass(compiler);
        }
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        for(AbstractDeclClass classe : this.getList()){
            classe.verifyClassMembers(compiler);
        }
        
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        List<AbstractDeclClass> list = this.getList();
        for (AbstractDeclClass classes : list) {
            classes.verifyClassBody(compiler);
        }
    }

    protected int codeGenClassMethodesTable(DecacCompiler compiler)
    {
        compiler.addComment("        Construction des tables des methodes");
        int offset = 1;
        compiler.addComment("Construction des tables des methodes de la classe Object");
        compiler.addInstruction(new LOAD(new NullOperand(),GPRegister.R0));
        compiler.addInstruction(new STORE(GPRegister.R0, new RegisterOffset(offset, GPRegister.GB)));
        offset += 1;
        compiler.addInstruction(new LOAD(new LabelOperand(new Label("code.Object.equals")), GPRegister.R0));
        compiler.addInstruction(new STORE(GPRegister.R0, new RegisterOffset(offset, GPRegister.GB)));
        offset += 1;
        for(AbstractDeclClass classe : this.getList()){
            classe.codeGenClassMethodeTable(compiler, offset);
            TreeSet<MethodDefinition> allMethods = ((DeclClass)classe).getName().getClassDefinition().getAllMethods();
            offset = offset + allMethods.size() + 1;
        }
        return offset;
    }
}
