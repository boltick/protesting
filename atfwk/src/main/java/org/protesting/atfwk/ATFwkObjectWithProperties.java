package org.protesting.atfwk;

import org.apache.commons.lang.ClassUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Base class to support objects with properties.
 * Author: Alexey Bulat
 * Date: 23/09/11
 */
public class ATFwkObjectWithProperties {

    public static final Logger LOG = Logger.getLogger(ATFwkObjectWithProperties.class);

    private ATFwkContext pageContext;
    private Properties properties;

    protected ATFwkObjectWithProperties() {
        setPageContext(ATFwkContext.getInstance());
        initProperties();
    }

    private void setPageContext(ATFwkContext pageContext) {
        this.pageContext = pageContext;
    }

    private void initProperties() {
        String className = getATFwkClassName(getClass());
        if (!getPageContext().getPropertiesStorage().exists(className)) {
            this.properties = new Properties();
            List superClasses = ClassUtils.getAllSuperclasses(getClass());
            File file;
            for (int i = superClasses.size() - 2; i >= 0 ; i--) {
                Class aClass = (Class) superClasses.get(i);
                String atfwkPropertyKeyName = getATFwkClassName(aClass);
                file = new File(getResourcesPath(atfwkPropertyKeyName));
                if (getPageContext().getPropertiesStorage().getProperty(atfwkPropertyKeyName) == null) {
                    if (file.exists()) {
                        putAllProperties(file);
                        updateStorage(atfwkPropertyKeyName, getProperties());
                    }
                } else {
                    putAllProperties(getPageContext().getPropertiesStorage().getProperty(atfwkPropertyKeyName));
                }
            }
            file = new File(getResourcesPath(className));
            putAllProperties(file);
            updateStorage(this, getProperties());
        } else {
            setProperties(getPageContext().getPropertiesStorage().getProperty(getATFwkClassName(getClass())));
        }
    }

    private String getATFwkClassName(Class aClass) {
        return aClass.getName();
    }

    ATFwkContext getPageContext() {
        return pageContext;
    }

    private String getResourcesPath(String name) {
        return getPageContext().getResourcesPath(name);
    }

    private void putAllProperties(File propertiesFile) {
        try {
            this.properties.load(new FileReader(propertiesFile));
        } catch (IOException e) {
            LOG.warn("Property reading error", e);
        }
    }

    private void putAllProperties(Properties properties) {
        this.properties.putAll(properties);
    }

    private void updateStorage(Object parentKeyObj, Properties properties) {
        updateStorage(parentKeyObj.getClass().getSimpleName(), properties);
    }

    private void updateStorage(String className, Properties properties) {
        getPageContext().getPropertiesStorage().setProperty(className, (Properties)properties.clone());
    }

    protected Properties getProperties() {
        return properties;
    }

    private void setProperties(Properties properties) {
        this.properties = properties;
    }

    protected String getProperty(String key) {
        String value = getProperties().getProperty(key);

        return value != null ? value.trim() : "";
    }

    protected ATFwkWebBrowser getBrowser() {
        return getPageContext().getBrowser();
    }

    protected String getCurrentPageProperty(String key) {
        return ATFwkContext.getInstance().getCurrentPage().getProperty(key);
    }

}
