package org.protesting.jft.generator;

import org.protesting.jft.config.GeneratorConfig;
import org.protesting.jft.form.fields.Field;
import org.protesting.jft.form.requirements.BoundaryRequirement;
import org.protesting.jft.generator.generic.GenericGenerator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: ab83625
 * Date: 14.03.2008
 * Time: 13:23:12
 */
public abstract class AbstractBoundaryValueGenerator extends AbstractValueGenerator {

    private static Log logger = LogFactory.getLog(AbstractBoundaryValueGenerator.class);


    public AbstractBoundaryValueGenerator(BoundaryRequirement requirement) throws Exception {
        this(new Field("unknown", requirement));
    }

    public AbstractBoundaryValueGenerator(Field field) throws Exception {
        super(field);
    }

    public Object initGenerator() throws Exception {
        logger.debug("GenericGenerator initialization - started");
        String className = GeneratorConfig.getGeneratorClassName(GeneratorConfig.GENERIC_GENERATOR, getRequirement().getLeftBoundary().getClass().getName());
        Class genClass = Class.forName(className);
        GenericGenerator genericGenerator = (GenericGenerator) genClass.newInstance();
        logger.debug("GenericGenerator initialization - finished");
        logger.debug("GenericGenerator is instance of " + genericGenerator.getClass());
        return genericGenerator;
    }


    public BoundaryRequirement getRequirement() {
        return (BoundaryRequirement) getCommonRequirement();
    }

    public void setGenerator(GenericGenerator generator) {
        super.generator = generator;
    }

    public GenericGenerator getGenerator() {
        return (GenericGenerator) generator;
    }




}
