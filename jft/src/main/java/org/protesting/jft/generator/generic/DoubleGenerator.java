package org.protesting.jft.generator.generic;

/**
 * User: ab83625
 * Date: 26.02.2008
 * Time: 13:50:28
 */
public class DoubleGenerator implements GenericGenerator {

    public Object getSmallerThan(Object leftBorder) {
        double left = ((Double) leftBorder).doubleValue();
        return new Double(left - Double.MIN_VALUE * 1000000000 * 1000000000
                * 1000000000 * 1000000000 * 1000000000 * 1000000000 * 100000000
                * 1000000000 * 1000000000 * 1000000000 * 1000000000 * 100000000
                * 1000000000 * 1000000000 * 1000000000 * 1000000000 * 100000000
                * 1000000000 * 1000000000 * 1000000000 * 1000000000 * 100000000
                * 1000000000 * 1000000000 * 1000000000 * 1000000000 * 100000000
                * 1000000000 * 1000000000 * 1000000000 * 1000000000 * 100000000
                * 1000000000 * 1000000000 * 1000000000 * 1000000000 * 1000);
    }

    public Object getGreaterThan(Object rightBorder) {
        double right = ((Double) rightBorder).doubleValue();
        return new Double(right + Double.MIN_VALUE * 1000000000 * 1000000000
                * 1000000000 * 1000000000 * 1000000000 * 1000000000 * 100000000
                * 1000000000 * 1000000000 * 1000000000 * 1000000000 * 100000000
                * 1000000000 * 1000000000 * 1000000000 * 1000000000 * 100000000
                * 1000000000 * 1000000000 * 1000000000 * 1000000000 * 100000000
                * 1000000000 * 1000000000 * 1000000000 * 1000000000 * 100000000
                * 1000000000 * 1000000000 * 1000000000 * 1000000000 * 100000000
                * 1000000000 * 1000000000 * 1000000000 * 1000000000 * 1000);
    }

    public Object getValue(Object leftBorder, Object rightBorder) {
        double left = ((Double) leftBorder).doubleValue();
        double right = ((Double) rightBorder).doubleValue();

        return new Double(Math.random() * (right - left) + left);
    }

    public boolean validate(Object leftBorder, Object rightBorder) {
        double left = ((Double) leftBorder).doubleValue();
        double right = ((Double) rightBorder).doubleValue();
        if (left > right) {
            throw new IllegalArgumentException("left border ["+ left +"] is bigger than right [" + right + "]");
        }
        
        return Math.abs(left - right) > Math.abs(Double.MIN_VALUE); 
    }

    public static void main(String[] args) {
        DoubleGenerator generator = new DoubleGenerator();
        System.out.println(generator.getValue(new Double(-Double.MAX_VALUE), new Double(Double.MAX_VALUE)));
        System.out.println(generator.getValue(new Double(Double.MIN_VALUE), new Double(0.00)));
        System.out.println(generator.getValue(new Double(-10.00), new Double(0.00)));
        System.out.println(generator.getValue(new Double(Double.MIN_VALUE), new Double(100.01)));
        System.out.println(generator.getValue(new Double(0.001), new Double(Double.MAX_VALUE)));
        System.out.println(generator.getValue(new Double(-100.001), new Double(100.001)));
        System.out.println(generator.getValue(new Double(-100.01), new Double(Double.MAX_VALUE)));
        System.out.println("---");
        System.out.println(generator.getSmallerThan(new Double(-100.00)));
        System.out.println(generator.getGreaterThan(new Double(-100.00)));
        System.out.println("---");
        System.out.println(generator.getValue(new Double(0.01), new Double(1.01)));
        System.out.println(generator.getValue(new Double(0.01), new Double(0.01)));
        System.out.println(generator.getValue(new Double(-1), new Double(0)));
    }
    

}
