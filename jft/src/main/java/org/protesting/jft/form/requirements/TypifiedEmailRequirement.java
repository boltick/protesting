package org.protesting.jft.form.requirements;

/**
 * User: ab83625
 * Date: 02.04.2008
 * Time: 14:04:31
 */
public class TypifiedEmailRequirement extends TypifiedRequirement {


    public TypifiedEmailRequirement(int length) {
        super("email");
        this.setMaxLength(length);
    }


}
