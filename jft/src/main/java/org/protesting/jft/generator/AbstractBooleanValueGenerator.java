package org.protesting.jft.generator;

import org.protesting.jft.form.fields.Field;
import org.protesting.jft.form.requirements.BooleanRequirement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: ab83625
 * Date: 28.04.2008
 * Time: 13:15:10
 */
public abstract class AbstractBooleanValueGenerator extends AbstractValueGenerator {

    private static Log logger = LogFactory.getLog(AbstractBooleanValueGenerator.class);

    public AbstractBooleanValueGenerator(Field field) throws Exception {
        super(field);
    }

    public AbstractBooleanValueGenerator(BooleanRequirement requirement) throws Exception {
        this(new Field("unknown", requirement));
    }


    public BooleanRequirement getRequirement() {
        return (BooleanRequirement) getCommonRequirement();
    }


    public void addFormattedValues() {
        logger.debug("FormattedValues are not used in checked generator");
    }

    public void addFilteredValues() {
        logger.debug("FilteredValues are not used in checked generator");
    }

    public Object initGenerator() throws Exception {
        logger.debug("Generic Generators are not used in checked generator");
        return null;
    }
}
