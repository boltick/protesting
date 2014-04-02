package org.protesting.jft.form;

import java.util.LinkedHashMap;

/**
 * User: ab83625
 * Date: 26.05.2008
 * Time: 11:09:36
 */
public class Template extends Form {

    private LinkedHashMap  constructor;


    public Template(String name) {
        super(name, TYPE_ENTITY);
        this.constructor = new LinkedHashMap();
    }


    public LinkedHashMap getConstructor() {
        return constructor;
    }

    public void setConstructor(LinkedHashMap constructor) {
        this.constructor = constructor;
    }

    
}
