package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import java.util.TreeSet;
import org.apache.commons.lang.Validate;

/**
 * Definition of a class.
 *
 * @author gl16
 * @date 01/01/2020
 */
public class ClassDefinition extends TypeDefinition {
    
    protected int AdrTableMethodes = 1;
    
    public void setAdrTableMethodes(int offset)
    {
        AdrTableMethodes = offset;
    }
    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public void incNumberOfFields() {
        this.numberOfFields++;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public void setNumberOfMethods(int n) {
        Validate.isTrue(n >= 0);
        numberOfMethods = n;
    }
    
    public int incNumberOfMethods() {
        numberOfMethods++;
        return numberOfMethods;
    }

    private int numberOfFields = 0;
    private int numberOfMethods = 0;
    
    @Override
    public boolean isClass() {
        return true;
    }
    
    @Override
    public ClassType getType() {
        // Cast succeeds by construction because the type has been correctly set
        // in the constructor.
        return (ClassType) super.getType();
    };

    public ClassDefinition getSuperClass() {
        return superClass;
    }

    private final EnvironmentExp members;
    private final ClassDefinition superClass; 

    public EnvironmentExp getMembers() {
        return members;
    }
    
    public TreeSet<MethodDefinition> getAllMethods() {
        TreeSet<MethodDefinition> methodsSet = new TreeSet<MethodDefinition>();
       ClassDefinition currentClass = this;
       while (currentClass != null) {
               for(ExpDefinition expDef :currentClass.getMembers().getExpTable().values()){
                   
                   if(expDef.isMethod()) {
                       methodsSet.add((MethodDefinition)expDef);
                   }
               }
            currentClass = currentClass.superClass;
        }
       
       return methodsSet;
    }
    public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
        super(type, location);
        EnvironmentExp parent;
        if (superClass != null) {
            parent = superClass.getMembers();
        } else {
            parent = null;
            
        }
        members = new EnvironmentExp(parent);
        this.superClass = superClass;
        this.numberOfMethods = 1;
    }
    public ClassDefinition getClassDefinition(ExpDefinition def)
    {
        ClassDefinition currentclass = this;
        while(currentclass != null)
        {
            if(currentclass.getMembers().getExpTable().containsValue(def))
            {
                return currentclass;
            }
            currentclass = currentclass.superClass;
        }
        return null;
    }
    public int getAdrTableMethodes() {
        return this.AdrTableMethodes;
    }
    
    /*public boolean staticSubType(ClassDefinition classDefcompare) {
        if (this == classDefcompare) {
            return true;
        }
        if (superClass != null) {
            return getSuperClass().staticSubType(classDefcompare);
        }
        return false;
    }*/
    
}
