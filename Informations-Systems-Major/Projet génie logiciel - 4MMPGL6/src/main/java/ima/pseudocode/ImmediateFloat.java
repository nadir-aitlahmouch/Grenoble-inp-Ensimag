package fr.ensimag.ima.pseudocode;

/**
 * Immediate operand containing a float value.
 * 
 * @author Ensimag
 * @date 01/01/2020
 */
public class ImmediateFloat extends DVal {
    private float value;

    public ImmediateFloat(float value) {
        super();
        this.value = value;
    }

    @Override
    public String toString() {
        return "#" + Float.toHexString(value);
    }

	public int getValue() {
		// TODO Auto-generated method stub
		return 0;
	}
}
