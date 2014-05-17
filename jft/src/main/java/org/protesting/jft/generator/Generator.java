package org.protesting.jft.generator;

import org.protesting.jft.config.RequirementConfig;
import org.protesting.jft.form.fields.Field;
import org.protesting.jft.form.requirements.Requirement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * User: ab83625
 * Date: 29.02.2008
 * Time: 17:43:26
 */
public class Generator {

    private static Log logger = LogFactory.getLog(Generator .class);

    private Generator() {
    }

    public static AbstractValueGenerator getValueGenerator(Requirement requirement) {
        logger.debug("Generator by requirement initialization - stared");
        AbstractValueGenerator generatorForRequirement = null;
        Class valGenClass = null;
        try {
            valGenClass = Class.forName(
                    RequirementConfig.getInstance()
                            .getRequirementData(
                                    requirement.getReqID(),
                                    RequirementConfig.DATA_GENERATOR_CLASS));
            Object[] args = new Object[] { requirement };
            Class[] types = new Class[] { requirement.getClass()};
            generatorForRequirement = (AbstractValueGenerator) valGenClass.getConstructor(types).newInstance(args);

            logger.debug("Generator " + generatorForRequirement.getClass() +" is initialized");
            logger.debug("Generator by requirement initialization - finished");
            return generatorForRequirement;
        } catch (ClassNotFoundException e) {
            logger.error("Generation error: ", e);
        } catch (NoSuchMethodException e) {
            logger.error("Generation error: ", e);
        } catch (IllegalAccessException e) {
            logger.error("Generation error: ", e);
        } catch (InvocationTargetException e) {
            logger.error("Generation error: ", e);
        } catch (InstantiationException e) {
            logger.error("Generation error: ", e);
        }

        throw new IllegalStateException("Generator by requirement initialization - FAILED");
    }


    public static AbstractValueGenerator getValueGenerator(Field field) {
        logger.debug("Generator by field initialization - started");
        AbstractValueGenerator generatorForRequirement = null;
        Class valGenClass = null;
        Requirement requirement = field.getRequirement();

        try {
            valGenClass = Class.forName(
                    RequirementConfig.getInstance()
                            .getRequirementData(
                                    requirement.getReqID(),
                                    RequirementConfig.DATA_GENERATOR_CLASS));
            Object[] args = new Object[] { field };
            Class[] types = new Class[] { Field.class };
            generatorForRequirement = (AbstractValueGenerator) valGenClass.getConstructor(types).newInstance(args);

            logger.debug("Generator " + generatorForRequirement.getClass() +" is initialized");
            logger.debug("Generator by field initialization - finished");
            return generatorForRequirement;

        } catch (ClassNotFoundException e) {
            logger.error("Generation error: ", e);
        } catch (NoSuchMethodException e) {
            logger.error("Generation error: ", e);
        } catch (IllegalAccessException e) {
            logger.error("Generation error: ", e);
        } catch (InvocationTargetException e) {
            logger.error("Generation error: ", e);
        } catch (InstantiationException e) {
            logger.error("Generation error: ", e);
        }

        IllegalStateException e = new IllegalStateException("Generator by field ["+field.getName()+"] initialization - FAILED");
        logger.error("Generation error: ", e);
        throw e;
    }

}
