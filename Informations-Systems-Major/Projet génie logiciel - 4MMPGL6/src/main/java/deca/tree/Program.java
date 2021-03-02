package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterManager;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl16
 * @date 01/01/2020
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    final private ListDeclClass classes;
    final private AbstractMain main;
    
    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        
        // delegation de la verification aux sous arbres
      
        // passe 1
        this.classes.verifyListClass(compiler);
        
        // passe 2 
        this.classes.verifyListClassMembers(compiler);
        
        // passe 3
        this.classes.verifyListClassBody(compiler);
        this.main.verifyMain(compiler);
        
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
	compiler.addComment("----------------------------------------");
        compiler.addComment("             Table des méthodes");
        compiler.addComment("----------------------------------------");
        int offset = this.classes.codeGenClassMethodesTable(compiler);
        Label OverFlowLabel = new Label("overflow_error");
        Label StackOverFlow = new Label("stack_overflow_error");
	compiler.addComment("----------------------------------------");
        compiler.addComment("             Main Program");
        compiler.addComment("----------------------------------------");
        if(!compiler.getCompilerOptions().getNoverify())
        {
            compiler.addInstruction(new TSTO(new ImmediateInteger(compiler.getStackSize())));
            compiler.addInstruction(new BOV(StackOverFlow)); 
        }

        compiler.addInstruction(new ADDSP(new ImmediateInteger(offset)));

        main.codeGenMain(compiler, offset);
        RegisterManager.reinit();
        compiler.addInstruction(new HALT());
        compiler.addComment("end main program");

        //on genere les classes
        compiler.addComment("-> Declaration des classes");
        codeGenObjectClass(compiler);
       for (AbstractDeclClass c : this.getClasses().getList()) {
            c.codeGenDeclClass(compiler);
        }
       
       //On regarde si on a besoin de generer le code pour instance of
       if (compiler.isGenInstanceOf()){
            instanceOfProgram(compiler);
       }
      
       //On genere les erreurs
        if(!compiler.getCompilerOptions().getNoverify())
        {
            compiler.addComment("--------------ERREUR OVERFLOW------------");
            compiler.addLabel(OverFlowLabel);
            compiler.addInstruction(new WSTR("Error: Overflow during arithmetic operation"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
            
            
            compiler.addComment("------------ERREUR STACKOVERFLOW----------");
            compiler.addLabel(StackOverFlow);
            compiler.addInstruction(new WSTR("Error: Stack Overflow"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR()); 
            
            compiler.addComment("------------ERREUR DEREFERENCEMENT----------");
            compiler.addLabel(new Label("dereferencement_null"));
            compiler.addInstruction(new WSTR("Erreur : dereferencement de null"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
            
            compiler.addComment("------------ERREUR INPUT/OUTPUT----------");
            compiler.addLabel(new Label("IO_error"));
            compiler.addInstruction(new WSTR("Erreur : Input/Output invalide."));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
            
            compiler.addComment("------------ERREUR DE CAST----------");
            compiler.addLabel(new Label("cast_error"));
            compiler.addInstruction(new WSTR("Erreur : Cast Incompatible"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
            
        }
    }

    void codeGenObjectClass(DecacCompiler compiler){
        compiler.addComment("----------------------------------------");
        compiler.addComment("             Class Object");
        compiler.addComment("----------------------------------------");

        compiler.addLabel(new Label("init.Object"));
        compiler.addInstruction(new RTS());
        compiler.addLabel(new Label("code.Object.equals"));
        compiler.addInstruction(new TSTO(2));
        
        GPRegister reg1 = RegisterManager.get(2);
        GPRegister reg2 = RegisterManager.get(3);
        
        compiler.addComment("Sauvegarde de registres");
        compiler.addInstruction(new PUSH(reg1));
        compiler.addInstruction(new PUSH(reg2));

        compiler.addComment("Test de comparaison");
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, GPRegister.LB), reg1));
        compiler.addInstruction(new LOAD(new RegisterOffset(-3, GPRegister.LB), reg2));
        compiler.addInstruction(new CMP(reg1, reg2));
        compiler.addInstruction(new SEQ(GPRegister.R0));
        
        compiler.addComment("Restauration des registres");
        compiler.addInstruction(new POP(reg2));
        compiler.addInstruction(new POP(reg1));

        compiler.addLabel(new Label("fin.Object.equals"));
        compiler.addInstruction(new RTS());
     
    
    }    
    
        public void instanceOfProgram(DecacCompiler compiler) {
        compiler.addComment("----------------------------------------");
        compiler.addComment("             Code Test InstanceOf");
        compiler.addComment("----------------------------------------");
        Label debut = new Label("testInstanceOf.start");
        Label boucle = new Label("testInstanceOf.boucle");
        Label true_label = new Label("InstanceOf.true");
	Label false_label = new Label("InstanceOf.false");
	Label fin = new Label("InstanceOf.fin");

        GPRegister r2 = GPRegister.getR(2);
        GPRegister r3 = GPRegister.getR(3);

        compiler.addLabel(debut);
        compiler.addComment("Sauvegarde des registres");
        compiler.addInstruction(new PUSH(r2));
        compiler.addInstruction(new PUSH(r3));
        
        compiler.addComment("On charge le registre de la classe target");
        compiler.addInstruction(new LOAD(new RegisterOffset(-4, Register.SP), r3));
        compiler.addComment("On charge le registre de la classe courante");
        compiler.addInstruction(new LOAD(new RegisterOffset(-5, Register.SP), r2));

        compiler.addLabel(boucle);

        compiler.addComment("On teste si on est remonté jusqu'à la classe Object");
        compiler.addInstruction(new CMP(new NullOperand(), r2));
        compiler.addInstruction(new BEQ(false_label));

        compiler.addComment("On teste si on a rencontré la classe target en bouclant");
        compiler.addInstruction(new CMP(r2, r3));
        compiler.addInstruction(new BEQ(true_label));

        compiler.addComment("Sinon on change la classe courante avec sa superclasse et on reboucle");
        compiler.addInstruction(new LOAD(new RegisterOffset(0, r2), r2));
        compiler.addInstruction(new BRA(boucle));

        compiler.addLabel(true_label);
        compiler.addInstruction(new LOAD(new ImmediateInteger(1), Register.R0));
	compiler.addInstruction(new BRA(fin));

	compiler.addLabel(false_label);
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));

        compiler.addLabel(fin);
        compiler.addComment("On recharge les registres utilisés");
        compiler.addInstruction(new POP(r3));
        compiler.addInstruction(new POP(r2));
        compiler.addInstruction(new RTS());
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        this.classes.decompile(s);
        this.main.decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
