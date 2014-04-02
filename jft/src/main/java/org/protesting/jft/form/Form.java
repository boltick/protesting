package org.protesting.jft.form;

/**
 * User: ab83625
 * Date: 18.01.2008
 * Time: 17:13:03
 */
public class Form extends JftEntity {

    public static final int FORM_ENTITY = 0;
    public static final int TYPE_ENTITY = 1;


    public Form(String name) {
        super(name, FORM_ENTITY);
    }

    protected Form(String formName, int type) {
        super(formName, type);
        logger.debug("Form [" + formName + "] is initialized");
    }
    
}
