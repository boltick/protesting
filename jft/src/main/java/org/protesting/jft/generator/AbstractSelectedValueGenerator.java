package org.protesting.jft.generator;

import org.protesting.jft.form.fields.Field;
import org.protesting.jft.form.requirements.SelectedRequirement;

/**
 * User: ab83625
 * Date: 29.04.2008
 * Time: 11:15:27
 */
public abstract class AbstractSelectedValueGenerator extends AbstractValueGenerator {

    public AbstractSelectedValueGenerator(Field field) throws Exception {
        super(field);
    }

    public AbstractSelectedValueGenerator(SelectedRequirement requirement) throws Exception {
        this(new Field("unknown", requirement));
    }

    public SelectedRequirement getRequirement() {
        return (SelectedRequirement) getCommonRequirement();
    }

    public void addFormattedValues() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addFilteredValues() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object initGenerator() throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
