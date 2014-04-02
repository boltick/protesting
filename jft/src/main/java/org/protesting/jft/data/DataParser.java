package org.protesting.jft.data;

import org.protesting.jft.form.Form;

import java.io.File;

/**
 * User: ab83625
 * Date: 13.02.2008
 * Time: 11:38:16
 */
public interface DataParser {

    abstract public Form getForm(File file);

    abstract public Form getTemplate(File file);

}
