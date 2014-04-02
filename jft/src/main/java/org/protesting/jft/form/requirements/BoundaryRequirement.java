package org.protesting.jft.form.requirements;

import java.text.Format;
import java.util.Date;

/**
 * User: ab83625
 * Date: 18.01.2008
 * Time: 17:13:13
 */
public class BoundaryRequirement extends Requirement {

    protected Object [] boundary = new Object[2];

    protected boolean isInclusive;

    private BoundaryRequirement() {
        super("boundary");
    }

    public BoundaryRequirement(Object leftBoundary, Object rightBoundary, boolean isInclusive, Format formatType) {
        this();
        this.boundary[0] = leftBoundary;
        this.boundary[1] = rightBoundary;
        this.isInclusive = isInclusive;
        this.setFormatType(formatType);
    }



    public BoundaryRequirement(Object leftBoundary, Object rightBoundary) {
        this(leftBoundary, rightBoundary, false, null);
    }

    public BoundaryRequirement(Object leftBoundary, Object rightBoundary, boolean isInclusive) {
        this(leftBoundary, rightBoundary, isInclusive, null);
    }

    public BoundaryRequirement(int leftBoundary, int rightBoundary, boolean isInclusive) {
        this(new Integer(leftBoundary), new Integer(rightBoundary), isInclusive, null);
    }

    public BoundaryRequirement(long leftBoundary, long rightBoundary, boolean isInclusive) {
        this(new Long(leftBoundary), new Long(rightBoundary), isInclusive,  null);
    }

    public BoundaryRequirement(float leftBoundary, float rightBoundary, boolean isInclusive) {
        this(new Float(leftBoundary), new Float(rightBoundary), isInclusive, null);
    }

    public BoundaryRequirement(double leftBoundary, double rightBoundary, boolean isInclusive) {
        this(new Double(leftBoundary), new Double(rightBoundary), isInclusive, null);
    }

    public BoundaryRequirement(int leftBoundary, int rightBoundary, boolean isInclusive, Format formatType) {
        this(new Integer(leftBoundary), new Integer(rightBoundary), isInclusive, formatType);
    }

    public BoundaryRequirement(long leftBoundary, long rightBoundary, boolean isInclusive, Format formatType) {
        this(new Long(leftBoundary), new Long(rightBoundary), isInclusive, formatType);
    }

    public BoundaryRequirement(float leftBoundary, float rightBoundary, boolean isInclusive, Format formatType) {
        this(new Float(leftBoundary), new Float(rightBoundary), isInclusive, formatType);
    }

    public BoundaryRequirement(double leftBoundary, double rightBoundary, boolean isInclusive, Format formatType) {
        this(new Double(leftBoundary), new Double(rightBoundary), isInclusive, formatType);
    }

    public BoundaryRequirement(Date leftBoundary, Date rightBoundary, boolean isInclusive, Format formatType) {
        this((Object)leftBoundary, (Object)rightBoundary, isInclusive, formatType);
    }

    private Object getBoundary(int index) {
        return boundary[index];
    }

    public Object getLeftBoundary() {
        return getBoundary(0);
    }

    public Object getRightBoundary() {
        return getBoundary(1);
    }

    public boolean isInclusive() {
        return isInclusive;
    }


    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(super.toString()).append("[");
        if (getFormatType()!=null) {
            sb.append(getFormatType().format(boundary[0])).append("..").append(getFormatType().format(boundary[1])).append("]; ");
        } else {
            sb.append(boundary[0]).append("..").append(boundary[1]).append("]; ");
        }

        sb.append("inclusive=").append(isInclusive);

        return sb.toString();
    }
}
