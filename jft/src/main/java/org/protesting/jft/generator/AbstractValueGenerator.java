package org.protesting.jft.generator;

import org.protesting.jft.form.fields.Field;
import org.protesting.jft.form.requirements.Requirement;
import org.protesting.jft.testsuite.TestValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: ab83625
 * Date: 14.03.2008
 * Time: 13:17:09
 */
public abstract class AbstractValueGenerator extends ValueGenerator implements GeneratorHelper, GenericTypeChooser {

    public static Log logger = LogFactory.getLog(AbstractValueGenerator.class);

    protected Field field;

    protected Object generator;


    public AbstractValueGenerator(Field field) throws Exception {
        super();
        this.field = field;
        this.generator = initGenerator();
        logger.debug("Generator for field ["+field.getName()+"] is initialized");
        this.generate();
        this.addDefaultValue();
    }


    public void generate() {
        logger.debug("Row values generation is - started");
        generateRowValues();
        logger.debug("Row values generation is - finished");

        logger.debug("Format values generation  is - started");
        addFormattedValues();
        logger.debug("Format values generation  is - finished");

        logger.debug("Filterred values generation is - started");
        addFilteredValues();
        logger.debug("Filterred values generation is - finished");

        logger.debug("Mandatory values generation is - started");
        addMandatoryValues();
        logger.debug("Mandatory values generation is - finished");
    }


    public abstract void generateRowValues();

    public void addMandatoryValues() {
//        if(!getField().getRequirement().isRequired()) {
            addValue(new TestValue("EMPTY", "", getCommonRequirement(), !getField().getRequirement().isRequired()));
//        } else {
//            addValue(new TestValue("EMPTY", "", getCommonRequirement(), !getField().getRequirement().isRequired()));
//        }
    }



    private void addDefaultValue() {
        if (getField().getValue()!= null && !getField().getValue().equals("")) {
            getRowValues().add(new TestValue("DEFAULT VALUE", getField().getValue(), getCommonRequirement(), true));
        }
    }


    public Requirement getCommonRequirement() {
        return field.getRequirement();
    }


    public Field getField() {
        return field;
    }
}
