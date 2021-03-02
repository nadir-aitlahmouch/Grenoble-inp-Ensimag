package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.RegisterManager;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl16
 * @date 01/01/2020
 */
public class DeclClass extends AbstractDeclClass {
    final private AbstractIdentifier name; 
    final private AbstractIdentifier superClass;
    final private ListDeclField list_fields;
    final private ListDeclMethod list_methods;
    
    public DeclClass(AbstractIdentifier name, 
                        AbstractIdentifier superclass,
                        ListDeclField list_fields,
                        ListDeclMethod list_methods) {
        Validate.notNull(name);
        Validate.notNull(superclass);
        Validate.notNull(list_fields);
        this.name = name;
        this.superClass = superclass;
        this.list_fields = list_fields;
        this.list_methods = list_methods;
    }

    public AbstractIdentifier getName() {
        return name;
    }
    
    
    // passe 1
    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        /**
         * verifions que le nom(symbol) de la classe n'est pas déjà présent
         * dans notre environnment_types sinon lever une erreur
         */
       
        if(!compiler.getEnvTypePredef().containsSymbol(this.superClass.getName()))
            throw new ContextualError(("the superclass " + this.superClass.getName() + " is not declared"), this.name.getLocation());
         
        if(compiler.getEnvTypePredef().containsSymbol(this.name.getName()))
            throw new ContextualError(this.name.getName().getName() + " is already declared", this.name.getLocation());
        
        Definition superClassDefinition = compiler.getEnvTypePredef().get(this.superClass.getName());
        this.superClass.setType(superClassDefinition.getType());
        this.superClass.setDefinition(superClassDefinition);
        
        if(!superClassDefinition.getType().isClass())
             throw new ContextualError(this.superClass.getName().getName() + "Type is not a class", this.superClass.getLocation());
        
        this.superClass.setType(superClassDefinition.getType());
        this.superClass.setDefinition(superClassDefinition);
        // creation du nouveau type et ajout dans l'environnement type
        this.name.setType(new ClassType(this.name.getName(),this.getLocation(), 
                this.superClass.getClassDefinition()));
        // il faut caster puisque getType retourne un type or Classdefiniation a besoin d'un classtype
        this.name.setDefinition(new ClassDefinition((ClassType)this.name.getType(),
                     this.getLocation(), this.superClass.getClassDefinition()));
        
        this.name.setLocation(this.getLocation());
        
        try {
            // ajout dans l'environnement type
            compiler.getEnvTypePredef().declare(this.name.getName(), this.name.getDefinition());
        } catch (EnvironmentType.DoubleDefException ex) {
            Logger.getLogger(DeclClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    // passe 2
    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {

        // recuperation de l'environment exp
        EnvironmentExp envExp = this.name.getClassDefinition().getMembers();
        
        //verification des champs
        this.list_fields.verifyListDeclField(compiler, this.superClass.getClassDefinition()
                                             ,this.name.getClassDefinition(),envExp);
        
        //verification des methodes
        this.list_methods.verifyListDeclMethod(compiler, 
                envExp,this.superClass.getClassDefinition(),
                this.name.getClassDefinition());  
    }
    
    
    // passe 3
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
         
        // on verifie que la classe existe dans envType
        if(!compiler.getEnvTypePredef().containsSymbol(this.name.getName()))
            throw new ContextualError(this.name.getName().getName()+
                    "is not defined",
                        name.getLocation());
        
        // recuperation de l 'envExp qui est maintenant rempli
        Definition def =  compiler.getEnvTypePredef()
                .getEnvironment().get(this.name.getName());

        if(! def.isClass())
            throw new ContextualError(this.name.getName().getName() + " is not a class",
             def.getLocation());
        
        ClassDefinition currentClass = (ClassDefinition) def;
        EnvironmentExp envExp = currentClass.getMembers();
        // verification des champs pour la passe 3
        this.list_fields.verifyListDeclFieldBody(compiler, envExp, currentClass);
        // verification du corps des  methodes pour la passe 3
        this.list_methods.verifyListDeclMethodBody(compiler,envExp, currentClass);
    }

    /*------------------------Code generation----------------------------------*/
    @Override
    protected void codeGenDeclClass(DecacCompiler compiler) {
        compiler.addComment("----------------------------------------");
        compiler.addComment("             Class " + name.getName().toString());
        compiler.addComment("----------------------------------------");
        
        ClassDefinition classDefinition = name.getClassDefinition();
        compiler.addLabel(new Label("init." + classDefinition.getType().getName()));
        
        compiler.getProgramStack().add(compiler.getProgram());
        compiler.setProgram(new IMAProgram());
        
        GPRegister objectAd = RegisterManager.get(0);
        GPRegister registreT = RegisterManager.get(1);
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, GPRegister.LB), objectAd));
        
        //On initialise nos champs à zero
        for (AbstractDeclField field : list_fields.getList()){
            int offsetField = field.getFieldName().getFieldDefinition().getIndex();
            RegisterOffset addrField = new RegisterOffset(offsetField, objectAd);
            Type typeField = field.getFieldName().getDefinition().getType();
            if (typeField.isInt()){
                compiler.addInstruction(new LOAD(new ImmediateInteger(0), registreT));
                compiler.addInstruction(new STORE(registreT, addrField));
            }
            else if (typeField.isFloat()) {
                compiler.addInstruction(new LOAD(new ImmediateFloat(0), registreT));
                compiler.addInstruction(new STORE(registreT, addrField));
            }
            else if (typeField.isClass()){
                compiler.addInstruction(new LOAD(new NullOperand(), registreT));
                compiler.addInstruction(new STORE(registreT, addrField));
            }
        }
        
        //fields de superclass 
        if (!"Object".equals(name.getClassDefinition().getSuperClass().getType().getName().getName())){
            compiler.addInstruction(new PUSH(objectAd));
            compiler.addInstruction(new BSR(new Label("init." + superClass.getType().getName())));
            compiler.addInstruction(new POP(objectAd));
        }
        
       //On genere les fields avec leurs éventuelles initialisations.
       for (AbstractDeclField field : list_fields.getList()){
            int offsetField = field.getFieldName().getFieldDefinition().getIndex();
            field.codeGenDeclField(compiler, objectAd, offsetField);
        }
       
       compiler.addInstruction(new RTS());
       
        IMAProgram programToAppend = compiler.getProgram();
        compiler.setProgram(compiler.getProgramStack().pop());
        
       //On doit ajouter le "new TSTO()"
       //System.out.println("AJOUTEZ LE TSTO DANS CODEGENDECLCLASS");
        //int valtsto = 0;
        //if (!compiler.getCompilerOptions().getNoverify()) {
        //    compiler.addInstruction(new TSTO(valtsto));
        //    compiler.addInstruction(new BOV(new Label("stack_overflow_error")));
        //}
        
        compiler.getProgram().append(programToAppend);
       
       //on genere les methodes
       for (AbstractDeclMethod method : list_methods.getList()){
           method.codeGenDeclMethod(compiler, classDefinition);
       }
       
              
  }
    
    @Override
    protected void codeGenClassMethodeTable(DecacCompiler compiler, int offset)
    {   
        compiler.addComment("Construction des tables des methodes de la classe " + this.name.getClassDefinition().getType().getName());
        this.name.getClassDefinition().setAdrTableMethodes(offset);
        compiler.addInstruction(new LEA(new RegisterOffset(this.superClass.getClassDefinition().getAdrTableMethodes(), GPRegister.GB), GPRegister.R0));
        compiler.addInstruction(new STORE(GPRegister.R0, new RegisterOffset(offset, GPRegister.GB)));
        ClassDefinition classdef = ((ClassDefinition)compiler.getEnvTypePredef().getEnvironment().get(this.name.getName()));
        for(MethodDefinition methode : classdef.getAllMethods())
        {
            offset += 1;
            methode.codeGenTableMethode(compiler, this.name.getClassDefinition(), offset);
        }
    }
    /*-------------------------------------------------------------------------*/
   
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class "); name.decompile(s); 
        s.print(" extends ");
        superClass.decompile(s);
        s.print(" {"); s.println() ;s.indent();
        list_fields.decompile(s);
        list_methods.decompile(s);
        s.unindent();s.print("}");
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, true);
        superClass.prettyPrint(s, prefix, true);
        list_fields.prettyPrint(s, prefix, true);
        list_methods.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        name.iter(f);
        superClass.iter(f);
        list_fields.iter(f);
        list_methods.iter(f);
    }

}