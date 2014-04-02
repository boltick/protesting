package org.protesting.jft.generator.generic;

/**
 * User: ab83625
 * Date: 26.02.2008
 * Time: 11:46:22
 */
public class FloatGenerator implements GenericGenerator {
    public Object getSmallerThan(Object leftBorder) {
        float left = ((Float) leftBorder).floatValue();
        return new Float(left - Float.MIN_VALUE * 1000000000 * 1000000000 * 1000000000 * 1000000000 * 100000000);
    }

    public Object getGreaterThan(Object rightBorder) {
        float right = ((Float) rightBorder).floatValue();
        return new Float(right + Float.MIN_VALUE * 1000000000 * 1000000000 * 1000000000 * 1000000000 * 100000000);
    }

    public Object getValue(Object leftBorder, Object rightBorder) {
//        float left = ((Float) leftBorder).floatValue();
//        float right = ((Float) rightBorder).floatValue();
//        return new Float(RandomUtils.nextFloat() * (right - left) + left);
        double left = ((Float) leftBorder).doubleValue();   // Use double to avoid silence overflow
        double right = ((Float) rightBorder).doubleValue(); // for Float.MIN_VALUE & Float.MAX_VALUE
        return new Float(Math.random() *(right - left) + left);
    }

    public boolean validate(Object leftBorder, Object rightBorder) {
        double left = ((Float) leftBorder).doubleValue();
        double right = ((Float) rightBorder).doubleValue();
        if (left > right) {
            throw new IllegalArgumentException("left border ["+ left +"] is bigger than right [" + right + "]");
        }
        
        return Math.abs(left - right) > Math.abs(Float.MIN_VALUE);
    }

    public static void main(String[] args) {
        FloatGenerator generator = new FloatGenerator();
        System.out.println(generator.getValue(new Float(-Float.MAX_VALUE), new Float(Float.MAX_VALUE)));
        System.out.println(generator.getValue(new Float(Float.MIN_VALUE), new Float(0.00)));
        System.out.println(generator.getValue(new Float(-10.00), new Float(0.00)));
        System.out.println(generator.getValue(new Float(Float.MIN_VALUE), new Float(100.01)));
        System.out.println(generator.getValue(new Float(0.001), new Float(Float.MAX_VALUE)));

        System.out.println(generator.getValue(new Float(-100.001), new Float(100.001)));
        System.out.println("---");
        System.out.println(generator.getSmallerThan(new Float(-100.00)));
        System.out.println(generator.getValue(new Float(-100.00), new Float(-10.00)));
        System.out.println(generator.getValue(new Float(-100.01), new Float(Float.MAX_VALUE)));
        System.out.println("---");
        System.out.println(generator.getValue(new Float(0.01), new Float(1.01)));
        System.out.println(generator.getValue(new Float(0.01), new Float(0.01)));
        System.out.println(generator.getValue(new Float(-1), new Float(0)));
    }


}
