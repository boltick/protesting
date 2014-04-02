package org.protesting.jft.generator.generic;

/**
 * User: ab83625
 * Date: 26.02.2008
 * Time: 11:46:14
 */
public class IntegerGenerator implements GenericGenerator {

    public Object getSmallerThan(Object leftBorder) {
        long left = ((Integer) leftBorder).longValue();
        return new Long(--left);
    }

    public Object getGreaterThan(Object rightBorder) {
        long right = ((Integer) rightBorder).longValue();
        return new Long(++right);
    }

    public Object getValue(Object leftBorder, Object rightBorder) {
        long left = ((Integer) leftBorder).longValue(); // Use long to avoid silence overflow
        long right = ((Integer) rightBorder).longValue(); // for Integer.MIN_VALUE & Integer.MAX_VALUE
        return new Integer((int)Math.round(Math.random() * (right - left) + left));
    }

    public static int getValue(int leftBorder, int rightBorder) {
        long left = (long) leftBorder; // Use long to avoid silence overflow
        long right = (long)rightBorder; // for Integer.MIN_VALUE & Integer.MAX_VALUE
        return (int) Math.round(Math.random() * (right - left) + left);
    }

    public boolean validate(Object leftBorder, Object rightBorder) {
        long left = ((Integer) leftBorder).longValue(); // Use long to avoid silence overflow
        long right = ((Integer) rightBorder).longValue(); // for Integer.MIN_VALUE & Integer.MAX_VALUE
        if (left > right) {
            throw new IllegalArgumentException("left border ["+ left +"] is bigger than right [" + right + "]");
        }
        return Math.abs(left - right) > Math.abs(1);
    }


    public static int getRandomValue() {
        long left = 0;
        long right = Integer.MAX_VALUE;
        return (int)Math.round(Math.random() * (right - left) + left);
    }

    
    public static void main(String[] args) {
        IntegerGenerator generator = new IntegerGenerator();
        System.out.println(generator.getValue(new Integer(Integer.MIN_VALUE), new Integer(Integer.MAX_VALUE)));
        System.out.println(generator.getValue(new Integer(Integer.MIN_VALUE), new Integer(0)));
        System.out.println(generator.getValue(new Integer(Integer.MIN_VALUE), new Integer(100)));
        System.out.println(generator.getValue(new Integer(0), new Integer(Integer.MAX_VALUE)));
        System.out.println(generator.getValue(new Integer(-100), new Integer(100)));
        System.out.println(generator.getValue(new Integer(-100), new Integer(Integer.MAX_VALUE)));
        System.out.println(generator.getValue(new Integer(0), new Integer(1)));
        System.out.println(generator.getValue(new Integer(0), new Integer(0)));
        System.out.println(generator.getValue(new Integer(0), new Integer(1)));
        System.out.println(generator.getValue(new Integer(-1), new Integer(0)));
    }


}
