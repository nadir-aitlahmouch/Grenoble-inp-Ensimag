package fr.ensimag.deca.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Manage unique symbols.
 *
 * A Symbol contains the same information as a String, but the SymbolTable
 * ensures the uniqueness of a Symbol for a given String value. Therefore,
 * Symbol comparison can be done by comparing references, and the hashCode()
 * method of Symbols can be used to define efficient HashMap (no string
 * comparison or hashing required).
 *
 * @author gl16
 * @date 01/01/2020
 */
public class SymbolTable {
    private Map<String, Symbol> map = new HashMap<String, Symbol>();

    /**
     * Create or reuse a symbol.
     *
     * If a symbol already exists with the same name in this table, then return
     * this Symbol. Otherwise, create a new Symbol and add it to the table.
     */
    public Map<String, Symbol> getMap()
    {
      return map;
    }
    public Symbol create(String name) {
        if (map.containsKey(name)) {
            return map.get(name);
        }
        else {
            Symbol s = new Symbol(name);
            map.put(name, s);
            return s;
        }
        //throw new UnsupportedOperationException("Symbol creation");
    }

    public static class Symbol {
        // Constructor is private, so that Symbol instances can only be created
        // through SymbolTable.create factory (which thus ensures uniqueness
        // of symbols).
        private Symbol(String name) {
            super();
            this.name = name;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 59 * hash + Objects.hashCode(this.name);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Symbol other = (Symbol) obj;
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            return true;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }

        private String name;
    }
}
