package io.bmuskalla.system.properties;

import java.util.Properties;

class SystemPropertyScope implements AutoCloseable {

    private DelegatingProperties propertyStore;
    private final Properties originalProperties;

    SystemPropertyScope() {
        originalProperties = System.getProperties();
        propertyStore = new DelegatingProperties(originalProperties);
        System.setProperties(propertyStore);
    }

    public void setProperty(String key, String value) {
        propertyStore.addOverride(key, value);
    }

    @Override
    public void close() {
        System.setProperties(originalProperties);
    }

}
