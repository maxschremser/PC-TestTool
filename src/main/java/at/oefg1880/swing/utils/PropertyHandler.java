package at.oefg1880.swing.utils;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.frame.SheetableFrame;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.Properties;

public class PropertyHandler implements PropertyChangeListener, ITexts, IConfig {
    public volatile static PropertyHandler propertyHandler; // volatile is needed so that multiple thread can reconcile the saver
    public volatile static ResourceHandler rh = ResourceHandler.getInstance(); // volatile is needed so that multiple thread can reconcile the saver
    private Properties props;
    private JFrame frame;
    private final Logger log = Logger.getLogger(PropertyHandler.class);
    private final static String CONFIG_FILE = "config.properties";
    private final static String LICENSE_FILE = "license.file";
    private final static String PUBLIC_KEY = "public.key";
    private final static String APP_DIR = ".PC-TestTool";
    private final static String file_separator = System.getProperty("file.separator");
    private final static String user_home = System.getProperty("user.home");
    private final static String USER_CONFIG_PATH = new StringBuffer(user_home).
            append(file_separator).
            append(APP_DIR).
            append(file_separator).
            append(CONFIG_FILE).toString();
    private final static String USER_LICENSE_PATH = new StringBuffer(user_home).
            append(file_separator).
            append(APP_DIR).
            append(file_separator).
            append(LICENSE_FILE).toString();

    private final static String APP_CONFIG_PATH = new StringBuffer("resources").
            append(file_separator).
            append(CONFIG_FILE).toString();

    private PropertyHandler() {
        props = new Properties();
        loadProperties();
    }

    public static PropertyHandler getInstance() {                 //synchronized keyword has been removed from here
        if (propertyHandler == null) {                            //needed because once there is singleton available no need to aquire monitor again & again as it is costly
            synchronized (PropertyHandler.class) {
                if (propertyHandler == null) {                    //this is needed if two threads are waiting at the monitor at the time when singleton was getting instantiated
                    propertyHandler = new PropertyHandler();
                }
            }
        }
        return propertyHandler;
    }

    private void loadProperties() {
        try {
            log.info("Load from: " + USER_CONFIG_PATH);
            props.load(new FileInputStream(USER_CONFIG_PATH));
        } catch (Exception e) {
            try {
                log.info("Load from: " + APP_CONFIG_PATH);
                props.load(getClass().getClassLoader().getResourceAsStream(APP_CONFIG_PATH));
            } catch (IOException ioe2) {
            } catch (NullPointerException npe) {
            }
        }
    }

    public void store() {
        try {
            File f = new File(USER_CONFIG_PATH);
            log.info("store props to : " + f.getAbsoluteFile());
            OutputStream os = new FileOutputStream(f);
            try {
                props.store(os, "");
            } catch (IOException ioe) {
            }
        } catch (FileNotFoundException fnfe) {
        }
    }

    public String getProperty(String key) {
        return getProperty(key, key);
    }

    public String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public void setProperty(String key, String value) {
        props.setProperty(key, value);
    }

    public void setOwner(JFrame frame) {
        this.frame = frame;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(JOptionPane.VALUE_PROPERTY)) {
            if (Integer.valueOf(evt.getNewValue().toString()).intValue() == 0) { // OK Button pressed
                File f = new File(USER_CONFIG_PATH);
                try {
                    if (!f.exists())
                        createRecursive(f);
                    store();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    private void createRecursive(File f) throws IOException {
        // user_home/.TestTool/config.properties
        File user_homeFile = new File(user_home);
        if (!user_homeFile.exists()) {
            throw new IOException("Directory <user.home> (" + user_homeFile.getAbsolutePath() + ") must exist.");
        }

        File app_homeDir = new File(user_home + file_separator + APP_DIR);
        if (!app_homeDir.exists() && !app_homeDir.mkdirs()) {
            throw new IOException("Could not create directory <app.dir> (" + app_homeDir.getAbsolutePath() + ").");
        }

        f.createNewFile();
    }

}
