package org.protesting.jft.generator;

import org.protesting.jft.config.Configurator;
import org.protesting.jft.config.GeneratorConfig;
import org.protesting.jft.form.fields.Field;
import org.protesting.jft.form.requirements.LengthRequirement;
import org.protesting.jft.generator.generic.IntegerGenerator;
import org.protesting.jft.generator.symbols.StringGenerator;
import org.protesting.jft.testsuite.TestValue;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: ab83625
 * Date: 27.02.2008
 * Time: 10:32:37
 */
public class LengthValueGenerator extends AbstractLengthValueGenerator {


    private static final String CORRECT_LENGTH = "CORRECT LENGTH";
    private static final String MAXIMUM_LENGTH = "MAXIMUM LENGTH";
    private static final String LONGER_THAN_ACCEPTED = "LONGER THAN ACCEPTED";
    private static final String MINIMUM_LENGTH = "MINIMUM LENGTH";
    private static final String LESS_THAN_ACCEPTED = "LESS THAN ACCEPTED";

    public LengthValueGenerator(LengthRequirement requirement) throws Exception {
        super(requirement);
    }

    public LengthValueGenerator(Field field) throws Exception {
        super(field);
    }


    public void generateRowValues() {
        int len = ((Integer) new IntegerGenerator()
                .getValue(
                        getRequirement().getLeftBoundary(),
                        getRequirement().getRightBoundary())).intValue();
        getRowValues().addAll(getValues(len, CORRECT_LENGTH));
        getRowValues().addAll(getValues(((Integer)getRequirement().getRightBoundary()).intValue(), MAXIMUM_LENGTH));

        getRowValues().addAll(getValues(((Integer)getRequirement().getRightBoundary()).intValue()+1, LONGER_THAN_ACCEPTED));

        int left = ((Integer)getRequirement().getLeftBoundary()).intValue();
        if (left >= 1) {
            getRowValues().addAll(getValues(((Integer)getRequirement().getLeftBoundary()).intValue(), MINIMUM_LENGTH));
        }
        if (left > 1) {
            getRowValues().addAll(getValues(((Integer)getRequirement().getLeftBoundary()).intValue()-1, LESS_THAN_ACCEPTED));
        }
    }

    private String getTestValue(int length, String range, String symbolType) throws UnsupportedEncodingException {
        return StringGenerator.getString(length, range, symbolType);
    }

    private List getValues(int length, String description) {
        List valuesList = new ArrayList();

        Map symbolsMap = Configurator.getInstance().getSupportedSymbols();
        Set keys = symbolsMap.keySet();
        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
            Object key = iterator.next();
            String desc = GeneratorConfig.getInstance()
                    .getSymbolProperty(key.toString(), GeneratorConfig.SYMBOL_ALIASE) + " "+ description;

            String range = GeneratorConfig.getInstance().getSymbolCharRange(key.toString());
            String regexp = null;
            if (getRequirement().getForbiddenChars() != null && !getRequirement().getForbiddenChars().equals("")) {
                regexp = "["+getRequirement().getForbiddenChars()+"]*";
            } else {
                regexp = "";
            }

            try {
                StringBuffer sb = new StringBuffer();
                String testValue = null;
                int counter = 0;
                do {
                    sb.append(
                            getTestValue(length - sb.toString().length(),
                                    range,
                                    GeneratorConfig.getInstance()
                                            .getSymbolProperty(key.toString(), GeneratorConfig.SYMBOL_CHAR_ENCODE))
                                    .replaceAll(regexp, ""));
                    counter++;
                }  while(sb.toString().length() != length && counter <50);
                if (sb.toString().equalsIgnoreCase("")) {
                    testValue = getTestValue(length - sb.toString().length(),
                            range,
                            GeneratorConfig.getInstance()
                                    .getSymbolProperty(key.toString(), GeneratorConfig.SYMBOL_CHAR_ENCODE));
                } else {
                    testValue = sb.toString();
                }

                boolean isOk = Boolean.valueOf(symbolsMap.get(key).toString()).booleanValue()
                        && testValue.length() <= ((Integer)getRequirement().getRightBoundary()).intValue()
                        && testValue.length() >= ((Integer)getRequirement().getLeftBoundary()).intValue();
                valuesList.add(new TestValue(desc, testValue, getRequirement(), isOk));
//                    valuesList.add(new TestValue(desc + " UPPER CASE", testValue.toUpperCase(), getRequirement(), isOk));
//                    valuesList.add(new TestValue(desc + " LOWER CASE", testValue.toLowerCase(), getRequirement(), isOk));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return valuesList;
    }


    public void addFilteredValues() {
        Pattern pattern = null;
        Matcher match = null;
        int startPoint = ((Integer)getRequirement().getLeftBoundary()).intValue();
        int endPoint = ((Integer)getRequirement().getRightBoundary()).intValue();

        if(getRequirement().getAcceptedChars()!=null && !getRequirement().getAcceptedChars().equals("")) {
            // TODO complite refactoring
//            String regexp = "["+getRequirement().getAcceptedChars()+"]"+"{"+startPoint+","+endPoint+"}";
            String regexp = "["+getRequirement().getAcceptedChars()+"]*";
            pattern = Pattern.compile(regexp);
            for (int i = 0; i < getRowValues().size(); i++) {
                TestValue testValue = (TestValue) getRowValues().get(i);
                match = pattern.matcher(testValue.getValue().toString());
                if (match.matches()) {
                    String newDesc = testValue.getDescription()+ " VALID CHAR RANGE "+ "["+getRequirement().getAcceptedChars()+"]";
                    testValue.setDescription(newDesc);
                } else {
                    testValue.setOk(match.matches());
                }
            }
            try {
                TestValue valueByCharRange =
                        new TestValue(
                                "CHAR RANGE "+"["+getRequirement().getAcceptedChars()+"]",
                                StringGenerator.getString(getRequirement().getLength(), getRequirement().getAcceptedChars()),
                                getRequirement(),
                                true);
                addValue(valueByCharRange);
                if (getRequirement().getAcceptedChars().indexOf(" ") != -1) {
                    addValue(new TestValue(
                            "ONLY SPACES",
                            StringGenerator.getString(getRequirement().getLength(), " "),
                            getRequirement(),
                            true));
                }
            } catch (UnsupportedEncodingException e) {
                logger.error(e);
            }
        }

        if (getRequirement().getForbiddenChars()!=null && !getRequirement().getForbiddenChars().equals("")) {
//            String regexp = "["+getRequirement().getForbiddenChars()+"]"+"{"+startPoint+","+endPoint+"}";
            String regexp = "["+getRequirement().getForbiddenChars()+"]*";
            pattern = Pattern.compile(regexp);
            for (int i = 0; i < getRowValues().size(); i++) {
                TestValue testValue = (TestValue) getRowValues().get(i);
                match = pattern.matcher(testValue.getValue().toString());
                if (!match.matches()) {
                    String newDesc = testValue.getDescription()+ " VALID CHAR RANGE";
                    testValue.setDescription(newDesc);
                } else {
                    testValue.setOk(!match.matches());
                }
            }
            try {
                TestValue valueByCharRange =
                        new TestValue(
                                "CHAR RANGE "+"["+getRequirement().getForbiddenChars()+"]",
                                StringGenerator.getString(getRequirement().getLength(), getRequirement().getForbiddenChars()),
                                getRequirement(),
                                false);
                addValue(valueByCharRange);
                if (getRequirement().getForbiddenChars().indexOf(" ") != -1) {
                    addValue(new TestValue(
                            "ONLY SPACES",
                            StringGenerator.getString(getRequirement().getLength(), " "),
                            getRequirement(),
                            false));
                }
            } catch (UnsupportedEncodingException e) {
                logger.error(e);
            }
        }

        List falseValuesList = new ArrayList();
        for (int i = 0; i < getRowValues().size(); i++) {
            TestValue testValue = (TestValue) getRowValues().get(i);
            if (!testValue.isOk()) {
                if (testValue.getDescription().contains("VALID CHAR RANGE"))  {
                    continue;
                }
                falseValuesList.add(testValue);
            }
        }

        Map symbolsMap = Configurator.getInstance().getSupportedSymbols();
        Set keys = symbolsMap.keySet();
        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
            Object key = iterator.next();
            String toBeRemoved = GeneratorConfig.getInstance()
                    .getSymbolProperty(key.toString(), GeneratorConfig.SYMBOL_ALIASE);
            for (int i = 0; i < falseValuesList.size(); i++) {
                TestValue testValue  = (TestValue) falseValuesList.get(i);
                if (testValue.getDescription().equalsIgnoreCase(toBeRemoved + " " + MAXIMUM_LENGTH) ) {
                    getRowValues().remove(testValue);
                }
                if (testValue.getDescription().equalsIgnoreCase(toBeRemoved + " " + MINIMUM_LENGTH) ) {
                    getRowValues().remove(testValue);
                }
//                if (testValue.getDescription().equalsIgnoreCase(toBeRemoved + " " + LONGER_THAN_ACCEPTED) ) {
//                    getRowValues().remove(testValue);
//                }
                if (testValue.getDescription().equalsIgnoreCase(toBeRemoved + " " + LESS_THAN_ACCEPTED) ) {
                    getRowValues().remove(testValue);
                }
            }

        }
    }


    public void addFormattedValues() {
        // todo: remove duplicated piece of code (see BoundaryValueGenerator class)

        if(getRequirement().getFormatType() == null) {
            return;
        }
        // stage 1 - format values to positive but leaving one negative as defaul formatted
        int index = 0;
        List first = new ArrayList(getRowValues());
        for (int i = 0; i < first.size(); i++) {
            TestValue value = (TestValue) first.get(i);
            TestValue testValue = new TestValue(value);
            testValue.setValue(getRequirement().getFormatType().format(testValue.getValue()));
            testValue.setDescription(testValue.getDescription() + " CORRECTLY FORMATTED");
            if(value.isOk() ||  index == 0) {
                index++;
            }
            value.setDescription(value.getDescription() + " DEFAULT FORMAT");
            value.setOk(false);
            addValue(testValue);
            if(index > 1) {
                getRowValues().remove(value);
            }
        }
    }

//    public void addMandatoryValues() {
//        if(!getField().getRequirement().isRequired()) {
//            addValue(new TestValue("EMPTY", "", getRequirement(), !getField().getRequirement().isRequired()));
//        } else {
//            addValue(new TestValue("EMPTY", "", getRequirement(), !getField().getRequirement().isRequired()));
//        }
//    }



}
