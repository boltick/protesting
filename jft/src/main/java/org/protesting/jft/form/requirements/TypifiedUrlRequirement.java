package org.protesting.jft.form.requirements;

/**
 * User: ab83625
 * Date: 02.04.2008
 * Time: 14:04:15
 */
public class TypifiedUrlRequirement extends TypifiedRequirement {

    private byte domainMaxLevel;
    private byte pathMaxLevel;
    private byte queryMaxParameters;

    public TypifiedUrlRequirement(byte domainMaxLevel, byte pathMaxLevel, byte queryMaxParameters, int maxLength) {
        super("url");
        this.domainMaxLevel = domainMaxLevel;
        this.pathMaxLevel = pathMaxLevel;
        this.queryMaxParameters = queryMaxParameters;
        this.setMaxLength(maxLength);
    }


    public byte getDomainMaxLevel() {
        return domainMaxLevel;
    }

    public void setDomainMaxLevel(byte domainMaxLevel) {
        this.domainMaxLevel = domainMaxLevel;
    }

    public byte getPathMaxLevel() {
        return pathMaxLevel;
    }

    public void setPathMaxLevel(byte pathMaxLevel) {
        this.pathMaxLevel = pathMaxLevel;
    }

    public byte getQueryMaxParameters() {
        return queryMaxParameters;
    }

    public void setQueryMaxParameters(byte queryMaxParameters) {
        this.queryMaxParameters = queryMaxParameters;
    }
}
