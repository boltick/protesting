package org.protesting.jft.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.protesting.jft.form.Form;
import org.protesting.jft.form.Template;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * User: ab83625
 * Date: 13.02.2008
 * Time: 12:53:12
 */
public class ParseFactory {

    private static Log logger = LogFactory.getLog(ParseFactory.class);

    public static Form getForm(File file, String dataSourceType) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String className = ParseFactory.class.getPackage() + "." + dataSourceType + ".FileDataParser";

        Class c = null;
        c = Class.forName(className.split(" ")[1]);
        Object o = c.newInstance();
        Method m = o.getClass().getMethod("getForm", new Class[]{(file).getClass()});
        Object entity = m.invoke(o, new Object[]{file});
        if (entity != null) {
            Form form = (Form) entity;
            logger.debug("Form [" +form.getName() +"] is parsed");
            logger.debug(form.toString());
            return form;
        }

        return null;
    }


    public static Template getTemplate(File file, String dataSourceType) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String className = ParseFactory.class.getPackage() + "." + dataSourceType + ".FileDataParser";

        Class c = null;
        c = Class.forName(className.split(" ")[1]);
        Object o = c.newInstance();
        Method m = o.getClass().getMethod("getTemplate", new Class[]{(file).getClass()});
        Object entity = m.invoke(o, new Object[]{file});
        if (entity != null) {
            Template template = (Template) entity;
            logger.debug("Form [" +template.getName() +"] is parsed");
            logger.debug(template.toString());
            return template;
        }


        return null;
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        System.out.println(getForm(new File("conf\\FormRequirementExample.xml"), "xml"));
        System.out.println(getForm(new File("conf\\FormRequirementExample.csv"), "csv"));
    }

}
