package org.protesting.jft.testsuite;

import org.protesting.jft.generator.AbstractValueGenerator;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * Test Field object is used for store test field information.
 *
 * User: abulat
 * Date: 19.02.2008
 * Time: 21:30:51
 */
public class TestField {

    private String name;
    private String type;
    private TestValue value;
    protected AbstractValueGenerator generator;


    public TestField() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TestValue getValue() {
        return value;
    }

    public void setValue(TestValue value) {
        this.value = value;
    }


    public AbstractValueGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(AbstractValueGenerator generator) {
        this.generator = generator;
    }

    public String toString() {
        // do not change!!!
        // used for valid csv generation
        StringBuffer sb = new StringBuffer().append("\"");
//        String toAdd = value == null ? "\""+"; " : StringEscapeUtils.escapeXml(value.getValue().toString()) + "\""+"; ";
        String toAdd = value == null ? "\""+";" : value.getValue() + "\""+";";
        sb.append(toAdd);
        return sb.toString();
    }
}
