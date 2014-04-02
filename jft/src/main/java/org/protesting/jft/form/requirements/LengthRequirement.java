package org.protesting.jft.form.requirements;

/**
 * User: ab83625
 * Date: 18.01.2008
 * Time: 17:13:13
 */
public class LengthRequirement extends BoundaryRequirement {

    protected int length;

    public LengthRequirement(Object moreThan, Object lessThan, boolean isInclusive) {
        super(moreThan, lessThan, isInclusive, null);
        int left = ((Integer)moreThan).intValue();
        int right = ((Integer)lessThan).intValue();
        this.length = (right-left) + 1; 
        setReqID("length");
    }

    public LengthRequirement(int length) {
        this(new Integer(1), new Integer(length), true);
        this.length = length;
    }

    public int getLength() {
        return length;
    }


    public void setLength(int length) {
        this.length = length;
    }

    public String toString() {
        return super.toString() + "; length="+length;
    }
}
