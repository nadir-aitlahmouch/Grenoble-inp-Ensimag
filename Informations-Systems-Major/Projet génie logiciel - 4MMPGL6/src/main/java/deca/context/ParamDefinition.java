package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

/**
 * Definition of a method parameter.
 *
 * @author gl16
 * @date 01/01/2020
 */
public class ParamDefinition extends ExpDefinition {
    

    private int index;
    
    public ParamDefinition(Type type, Location location, int index) {
        super(type, location);
        this.index = index;
    }
    
    
    public int getIndex() {
        return index;
    }


    @Override
    public String getNature() {
        return "parameter";
    }

    @Override
    public boolean isExpression() {
        return true;
    }

    @Override
    public boolean isParam() {
        return true;
    }
    
}
