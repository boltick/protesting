package org.protesting.jft.generator.generic;

/**
 * User: ab83625
 * Date: 26.02.2008
 * Time: 13:37:34
 */
public interface GenericGenerator {

    public Object getSmallerThan(Object leftBorder);

    public Object getGreaterThan(Object rightBorder);

    public Object getValue(Object leftBorder, Object rightBorder);

    public boolean validate(Object leftBorder, Object rightBorder);

//    public Object getMaximum();

//    public Object getMinimum();

}
