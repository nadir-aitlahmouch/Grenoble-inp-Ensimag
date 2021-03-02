package fr.ensimag.ima.pseudocode;

/**
 * Immediate operand representing a string.
 * 
 * @author Ensimag
 * @date 01/01/2020
 */
public class ImmediateString extends Operand {
    private String value;

    public ImmediateString(String value) {
        super();
        this.value = value;
        this.value = enleverq();
    }

    @Override
    public String toString() {
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
    String enleverq(){
        String returnvalue = "";
        for(int i = 0; i < this.value.length(); i ++){
                if(this.value.charAt(i) != '\\'){
                    returnvalue  = returnvalue + this.value.charAt(i);
                } 
        }
        return returnvalue;
    }
}
