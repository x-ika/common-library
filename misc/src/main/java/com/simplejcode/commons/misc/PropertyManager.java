package com.simplejcode.commons.misc;

import com.simplejcode.commons.misc.util.*;
import org.apache.commons.configuration2.PropertiesConfiguration;

import java.io.*;
import java.net.*;

public class PropertyManager {

    private static PropertyManager instance;


    public static void setPropertiesFileName(String propertiesFileName) {
        instance = new PropertyManager(propertiesFileName);
    }

    public static PropertyManager getInstance() {
        return instance;
    }

    //-----------------------------------------------------------------------------------

    private final String propertiesFileName;

    private long lastUpdateTimestamp;

    private PropertiesConfiguration properties;


    public PropertyManager(String propertiesFileName) {
        this.propertiesFileName = propertiesFileName;
    }


    public void setProperty(String key, String value) {
        reloadIfNeeded();
        properties.setProperty(key, value);
        saveProperties();
    }

    public String getProperty(String key, String def) {
        reloadIfNeeded();
        String value = properties.getProperty(key).toString();
        return value == null ? def : value;
    }

    public String getProperty(String key) {
        return getProperty(key, null);
    }

    public int getPropertyInt(String key) {
        return Integer.parseInt(getProperty(key));
    }

    public double getPropertyDouble(String key) {
        return Double.parseDouble(getProperty(key));
    }


    private synchronized void reloadIfNeeded() {

        try {

            URL url = FileSystemUtils.getFileURL(propertiesFileName);
            if (url == null) {
                throw generate("Configuration was not found");
            }
            URLConnection conn = url.openConnection();

            if (conn.getLastModified() > lastUpdateTimestamp) {

                lastUpdateTimestamp = conn.getLastModified();

                PropertiesConfiguration config = new PropertiesConfiguration();
                try (InputStream inputStream = conn.getInputStream()) {
                    config.read(new InputStreamReader(inputStream));
                }
                properties = config;

            }

        } catch (Exception e) {
            throw convert(e);
        }

    }

    private void saveProperties() {
        try {
            properties.write(new FileWriter(propertiesFileName));
        } catch (Exception e) {
            throw convert(e);
        }
    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException convert(Exception e) {
        return ExceptionUtils.wrap(e);
    }

    private static RuntimeException generate(String message) {
        return ExceptionUtils.generate(message);
    }

}
