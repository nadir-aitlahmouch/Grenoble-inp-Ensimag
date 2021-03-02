package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.Location;
import java.util.Map;
import java.util.HashMap;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current" 
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current" 
 * dictionary and in the parentEnvironment if it fails. 
 * 
 * Insertion (through method declare) is always done in the "current" dictionary.
 * 
 * @author gl16
 * @date 01/01/2020
 */
public class EnvironmentType {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).


    private Map<Symbol, Definition> environment;
    private SymbolTable symbolTable;

    public Map<Symbol, Definition> getEnvironment() {
        return environment;
    }
    
    
    public EnvironmentType() {
        this.environment = new HashMap<Symbol, Definition>();
        this.symbolTable = new SymbolTable();
        this.initialisation();
    }
    
    private void initialisation() {
        Symbol symbolInt = this.symbolTable.create("int");
        Type typeInt = new IntType(symbolInt);
        TypeDefinition  typeDefinitionInt = new TypeDefinition(typeInt, Location.BUILTIN);
        this.environment.put(symbolInt, typeDefinitionInt);
        
        Symbol symbolFloat = this.symbolTable.create("float");
        Type typeFloat = new FloatType(symbolFloat);
        TypeDefinition  typeDefinitionFloat = new TypeDefinition(typeFloat, Location.BUILTIN);
        this.environment.put(symbolFloat, typeDefinitionFloat);
        
        Symbol symbolBoolean = this.symbolTable.create("boolean");
        Type typeBoolean = new BooleanType(symbolBoolean);
        TypeDefinition  typeDefinitionBoolean = new TypeDefinition(typeBoolean, Location.BUILTIN);
        this.environment.put(symbolBoolean, typeDefinitionBoolean);
        
        Symbol symbolVoid = this.symbolTable.create("void");
        Type typeVoid = new VoidType(symbolVoid);
        TypeDefinition  typeDefinitionVoid = new TypeDefinition(typeVoid, Location.BUILTIN);
        this.environment.put(symbolVoid, typeDefinitionVoid);
        
        Symbol symbolNull = this.symbolTable.create("null");
        Type typeNull = new NullType(symbolNull);
        TypeDefinition  typeDefinitionNull = new TypeDefinition(typeNull, Location.BUILTIN);
        this.environment.put(symbolNull, typeDefinitionNull);
        
 /*       Symbol symbolString = this.symbolTable.create("String");
        Type typeString = new StringType(symbolString);
        TypeDefinition  typeDefinitionString = new TypeDefinition(typeString, null);
        this.environment.put(symbolString, typeDefinitionString);
  */      
        // ajout de Objet
        Symbol symbolObject = this.symbolTable.create("Object");
        ClassType objectType = new ClassType(symbolObject, null, null);
        TypeDefinition classDefinitionObject = new ClassDefinition(objectType, null, null);
        this.environment.put(symbolObject, classDefinitionObject);

    }
    
    
    
    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
                public DoubleDefException(String message)
        {
            super(message);
        }
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public Definition get(Symbol key) {
        //throw new UnsupportedOperationException("not yet implemented");
        if (this.environment.containsKey(key)) {
            Definition result = this.environment.get(key);
            return result;
        }
        return null;
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary 
     * - or, hides the previous declaration otherwise.
     * 
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, Definition def) throws DoubleDefException {
        //throw new UnsupportedOperationException("not yet implemented");
        if(this.environment.containsKey(name))
           throw new DoubleDefException("Symbol is already declared in Environment Type");
        this.environment.put(name, def);       
    }
    
  public boolean containsSymbol (Symbol name){
      for(Symbol symbol : environment.keySet()){
            if(name.equals(symbol))
                return true;
        }
      return false;
  }

}
