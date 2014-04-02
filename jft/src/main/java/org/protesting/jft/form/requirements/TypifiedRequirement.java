package org.protesting.jft.form.requirements;

/**
 * User: ab83625
 * Date: 02.04.2008
 * Time: 14:01:50
 */
public class TypifiedRequirement extends Requirement {

    private String acceptedPatttern;
    private int maxLength;

    private String type;

    public TypifiedRequirement(String type) {
        super("typified");
        this.type = type;
    }

    public String getAcceptedPatttern() {
        return acceptedPatttern;
    }

    public void setAcceptedPatttern(String acceptedPatttern) {
        this.acceptedPatttern = acceptedPatttern;
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
