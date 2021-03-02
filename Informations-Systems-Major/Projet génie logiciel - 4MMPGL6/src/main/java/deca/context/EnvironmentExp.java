package fr.ensimag.deca.context;
import java.util.Map;
import java.util.HashMap;

import java.util.HashMap;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
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
public class EnvironmentExp {

    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    public HashMap<Symbol, ExpDefinition> getExpTable() {
        return this.ExpTable;
    }
    // d'empilement).
    HashMap<Symbol, ExpDefinition> ExpTable;
    private EnvironmentExp parentEnvironment;
    
    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
        this.ExpTable = new HashMap<>();
        if(parentEnvironment == null)
        {
            Signature s = new Signature();
            s.add(new ClassType(new SymbolTable().create("Object")));
            MethodDefinition EqualsDef = new MethodDefinition(new BooleanType(new SymbolTable().create("boolean")), null, s, 1);
            EqualsDef.setMethodName(new SymbolTable().create("equals"));
            ExpTable.put(new SymbolTable().create("equals"), EqualsDef);
        }
    }

    public EnvironmentExp getParent(){
        return this.parentEnvironment;
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
     * @param key
     * @return 
     */
    public ExpDefinition get(Symbol key) {
        EnvironmentExp CurrentEnvironmentExp = this;
        while(CurrentEnvironmentExp != null)
        {
            if(CurrentEnvironmentExp.ExpTable.containsKey(key))
            {
              return CurrentEnvironmentExp.ExpTable.get(key);
            }
            CurrentEnvironmentExp = CurrentEnvironmentExp.parentEnvironment;
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
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        /*
        boolean dejaExist = false;
        if(this.ExpTable.containsKey(name))
        {
            dejaExist = true;
            throw new DoubleDefException("Symbol is already declared in Environment Exp");
        }
        else
        {
            EnvironmentExp CurrentEnvironmentExp = this.parentEnvironment;
            while(CurrentEnvironmentExp != null)
            {
                if(CurrentEnvironmentExp.ExpTable.containsKey(name))
                {
                    dejaExist = true;
                    CurrentEnvironmentExp.ExpTable.put(name, def);
                }
                CurrentEnvironmentExp = CurrentEnvironmentExp.parentEnvironment;
            }
        }
        if(!dejaExist)
        {
            this.ExpTable.put(name, def);
        }
                */
        if(this.ExpTable.containsKey(name))
        {
            throw new DoubleDefException("Symbol is already declared in Environment Exp");
        }
        this.ExpTable.put(name, def);
    }
}
