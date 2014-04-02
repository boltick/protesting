package org.protesting.atfwk;

import java.util.Properties;
import java.util.Map;
import java.util.HashMap;

/**
 * ATFwkPropertiesStorage class
 * Author: Alexey Bulat
 * Date: 19.02.2010
 */
public class ATFwkPropertiesStorage {

    private String sessionId;
    private Map<String, Properties> propertiesMap;


    private ATFwkPropertiesStorage(String sessionId) {
        this.sessionId = sessionId;
        this.propertiesMap = new HashMap<String, Properties>();
    }

    public static ATFwkPropertiesStorage getInstance(String sessionId) {
        return new ATFwkPropertiesStorage(sessionId);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setProperty(String key, Properties properties) {
        propertiesMap.put(key, properties);
    }

    public Properties getProperty(String key) {
        return propertiesMap.get(key);
    }

    public boolean exists(String key) {
        return propertiesMap.get(key) != null;
    }
}
