package org.protesting.jft.form.requirements;

/**
 * User: ab83625
 * Date: 02.04.2008
 * Time: 14:01:50
 */
public class TypifiedRequirement extends Requirement {

    private String acceptedPattern;
    private int maxLength;

    private String type;

    public TypifiedRequirement(String type) {
        super("typified");
        this.type = type;
    }

    public String getAcceptedPattern() {
        return acceptedPattern;
    }

    public void setAcceptedPattern(String acceptedPattern) {
        this.acceptedPattern = acceptedPattern;
    }


    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }


    public String getType() {
        return type;
    }

}
