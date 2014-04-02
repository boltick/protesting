package org.protesting.jft.generator;

import org.protesting.jft.form.fields.Field;
import org.protesting.jft.form.requirements.LengthRequirement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: ab83625
 * Date: 14.03.2008
 * Time: 13:10:40
 */
public abstract class AbstractLengthValueGenerator extends AbstractValueGenerator {

    protected static Log logger = LogFactory.getLog(AbstractLengthValueGenerator.class);

    public  AbstractLengthValueGenerator(LengthRequirement requirement) throws Exception {
        this(new Field("unknown", requirement));
    }

    public  AbstractLengthValueGenerator(Field field) throws Exception {
        super(field);
    }

    public LengthRequirement getRequirement() {
        return (LengthRequirement) getCommonRequirement();
    }


    public Object initGenerator() throws Exception {
        logger.debug("Specific generator is not needed so it is not defined");
        return null;
    }
}
