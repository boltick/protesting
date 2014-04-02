package org.protesting.jft.generator.generic;

/**
 * User: ab83625
 * Date: 26.02.2008
 * Time: 11:46:40
 */
public class LongGenerator implements GenericGenerator {

   public Object getSmallerThan(Object leftBorder) {
        long left = ((Long) leftBorder).longValue();
        return new Long(--left);
    }

    public Object getGreaterThan(Object rightBorder) {
        long right = ((Long) rightBorder).longValue();
        return new Long(++right);
    }

    public Object getValue(Object leftBorder, Object rightBorder) {
        float left = ((Long) leftBorder).floatValue(); // Use double to avoid silence overflow
        float right = ((Long) rightBorder).floatValue(); // for Long.MIN_VALUE & Long.MAX_VALUE
        return new Long(Math.round(Math.random() * (right - left) + left));
    }

    public static long getValue(long leftBorder, long rightBorder) {
        double left = leftBorder; // Use double to avoid silence overflow
        double right = rightBorder; // for Long.MIN_VALUE & Long.MAX_VALUE
        return Math.round(Math.random() * (right - left) + left);
    }

    public boolean validate(Object leftBorder, Object rightBorder) {
        long left = ((Long) leftBorder).longValue();
        long right = ((Long) rightBorder).longValue();
        if (left > right) {
            throw new IllegalArgumentException("left border ["+ left +"] is bigger than right [" + right + "]");
        }
        return Math.abs(left - right) > Math.abs(1);
    }


    public static void main(String[] args) {
        LongGenerator generator = new LongGenerator();
        System.out.println(generator.getValue(new Long(Long.MIN_VALUE), new Long(Long.MAX_VALUE)));
        System.out.println(generator.getValue(new Long(Long.MIN_VALUE), new Long(0)));
        System.out.println(generator.getValue(new Long(Long.MIN_VALUE), new Long(100)));
        System.out.println(generator.getValue(new Long(0), new Long(Long.MAX_VALUE)));
        System.out.println(generator.getValue(new Long(-100), new Long(100)));
        System.out.println(generator.getValue(new Long(-100), new Long(Long.MAX_VALUE)));
        System.out.println(generator.getValue(new Long(0), new Long(1)));
        System.out.println(generator.getValue(new Long(0), new Long(0)));
        System.out.println(generator.getValue(new Long(-1), new Long(0)));
    }

}
