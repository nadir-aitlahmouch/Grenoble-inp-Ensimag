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
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;

/**
 *
 * @author fennaneo
 */
public class DeclMethod extends AbstractDeclMethod {
    final private AbstractIdentifier type;
    final private AbstractIdentifier methodName;
    final private ListDeclParam list_params;
    final private AbstractMethodBody body;
    
    public DeclMethod(AbstractIdentifier type, AbstractIdentifier methodName, 
                        AbstractMethodBody body, ListDeclParam list_params) {
        Validate.notNull(type);
        Validate.notNull(methodName);
        Validate.notNull(list_params);
        this.type = type;
        this.methodName = methodName;
        this.body = body;
        this.list_params = list_params;
    }
    
    //passe 2
    @Override
    protected void verifyDeclMethod(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition superClass, ClassDefinition
                    currentClass)
            throws ContextualError {
        
        Type typeRetour;
        this.setLocation(this.methodName.getLocation());
        // recuperation du type de retour de la methode
        // on utilse pas verify parce que verify interdit que type soit void or une methode peut etre de type void
         if(compiler.getEnvTypePredef().getEnvironment().containsKey(this.type.getName()))
            typeRetour = compiler.getEnvTypePredef().getEnvironment().get(this.type.getName()).getType();
         else throw new ContextualError(this.type.getName().getName() + " is a not defined type", this.getLocation());
        
        if(typeRetour.isNull())
            throw new ContextualError("return type cannot be null", this.getLocation());
        
        // mise a jour de type
        this.type.setType(typeRetour);
        this.type.setDefinition(new TypeDefinition(typeRetour, this.getLocation()));
        
        // verification et recuperation de la signature de la methode  
        Signature sig = this.list_params.verifyListDeclParamSignature(compiler, localEnv, currentClass);
      
  
        
          // mise a jour de methodName
        methodName.setType(typeRetour);
        methodName.setLocation(this.getLocation());
        
        // calcul de l'index
         if(currentClass.getNumberOfMethods() == 1) {
                currentClass.setNumberOfMethods(superClass
                        .getNumberOfMethods());   
            }
         int index = currentClass.getNumberOfMethods();

        // verifions s'il y a redifinition de la methode, si oui verifions les conditions
  
        EnvironmentExp superEnvExp = superClass.getMembers();
        Boolean condition = false;
        while(superEnvExp != null){
           HashMap<Symbol, ExpDefinition> mapSuper = superEnvExp.getExpTable();
          
           if(mapSuper.containsKey(this.methodName.getName())){ 
               
                // test pour mettre à jour la signature de equals dans son environment
                Symbol symEquals = new SymbolTable().create("equals");
                if(mapSuper.containsKey(symEquals)){
                      Signature sigEquals = ((MethodDefinition)mapSuper.get(symEquals)).getSignature();
                      Symbol symObject = new SymbolTable().create("Object");
                      Type typeObject = compiler.getEnvTypePredef().getEnvironment()
                       .get(symObject).getType();
                      sigEquals.add(typeObject);
                    }
                ExpDefinition method = mapSuper.get(this.methodName.getName());
                
                if(!method.isMethod()){
                   
                   throw new ContextualError(this.methodName.getName().getName() +" is not a method in the superclass" ,this.getLocation());
                }
               MethodDefinition defMethod = (MethodDefinition) method;
                
               if(sig.size() != defMethod.getSignature().size())
                   throw new ContextualError("RedefinitionError: "+this.methodName.getName().getName() + " incorrect signature" 
                           ,this.getLocation());
               
               for(int i = 0; i < sig.size();i++){
                   if(!(sig.paramNumber(i).sameType(defMethod.getSignature().paramNumber(i)))) {
                       throw new ContextualError("RedefinitionError : incorrect signature" 
                           ,this.getLocation());
                   }
                }
                   
               if(typeRetour.isClass() && defMethod.getType().isClass())
               {

                   if(! (((ClassType) typeRetour).isSubClassOf((ClassType) defMethod.getType()) || typeRetour.getName().equals(defMethod.getType().getName())))
                    throw new ContextualError("RedefinitionError:"
                   + typeRetour.getName().getName() + " is not a subClass of " + defMethod.getType().getName().getName(),this.getLocation());
               }
               
               else if(! typeRetour.sameType(defMethod.getType()))
                   throw new ContextualError("RedefinitionError: typeMethod must be " + defMethod.getType().getName().getName(),this.getLocation());
            
            index = defMethod.getIndex() - 1;
            condition = true;
            break;
           }
           
           superEnvExp = superEnvExp.getParent();
           }
           MethodDefinition definition = new MethodDefinition(typeRetour,this.getLocation(),
                                   sig,++index);
           methodName.setDefinition(definition);
           definition.setMethodName(methodName.getName());
           methodName.getMethodDefinition().setMethodName(methodName.getName());
            try {
                // ajout de la methode à l'environment local
                localEnv.declare(this.methodName.getName(), definition);
            } catch (EnvironmentExp.DoubleDefException ex) {
                Logger.getLogger(DeclMethod.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(!condition)
                currentClass.incNumberOfMethods();
        }
        
    
    // passe 3
    @Override
    protected void verifyDeclMethodBody(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // on recupere le type de retour
        Type returnType = this.type.getType();
        EnvironmentExp envExpParams = this.list_params.verifyListDeclParam
        (compiler,localEnv);
        this.body.verifyMethodBody(compiler,localEnv,envExpParams,currentClass,returnType);
        
    }

    /*---------------------Code generation------------------------------------*/
    @Override
    protected void codeGenDeclMethod(DecacCompiler compiler, ClassDefinition classDef) {
        MethodDefinition methDef = methodName.getMethodDefinition();
        compiler.getMethodDefinitionStack().addFirst(methDef);
        compiler.addComment("--------------Code de la méthode " + methodName.getName().getName());
        
        methDef.setCodeLabel(new Label("code." + classDef.getType().getName() + 
                                        "." + methodName.getName().getName()));
        methDef.setFinlabel(new Label("fin." + classDef.getType().getName() + 
                                        "." + methodName.getName().getName()));
        
        compiler.addLabel(methDef.getCodeLabel());
        body.codeGenBody(compiler);
        
        
        if (!type.getType().isVoid()){
            compiler.addInstruction(new WSTR("Erreur : sortie de la méthode <" + methodName.getName().getName() + "> sans return."));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }
        
        compiler.addLabel(methDef.getFinlabel());
        
        if (body instanceof MethodBody){
            compiler.addInstruction(new RTS());
        }
        compiler.getMethodDefinitionStack().remove(methDef);
      
    }
    /*------------------------------------------------------------------------*/
     
    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        methodName.decompile(s);
        s.print("(");
        list_params.decompile(s);
        s.print(") {");
        s.println();
        body.decompile(s);
        s.print("}");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        methodName.iter(f);
        list_params.iter(f);
        body.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        list_params.prettyPrint(s, prefix, true);
        body.prettyPrint(s, prefix, true);
    }
    
    @Override
    void codeGenTableMethode(DecacCompiler compiler, ClassDefinition currentClass, int offset) {
            compiler.addInstruction(new LOAD(new LabelOperand(new Label("code." + currentClass.getType().getName() + "." + this.methodName.getName().getName())), GPRegister.R0));
            compiler.addInstruction(new STORE(GPRegister.R0, new RegisterOffset(offset, GPRegister.GB)));
    }

}
