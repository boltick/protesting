package org.protesting.jft.utils;

import java.io.FilenameFilter;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: ab83625
 * Date: 09.02.2009
 * To change this template use File | Settings | File Templates.
 */
public class TemplateFilenameFilter implements FilenameFilter {


    public boolean accept(File dir, String name) {
        File tobeChecked = new File(dir.getName(), name);

        return tobeChecked.getName().substring(tobeChecked.getName().lastIndexOf('.')+1, tobeChecked.getName().length()).equals("xml");
    }
}
