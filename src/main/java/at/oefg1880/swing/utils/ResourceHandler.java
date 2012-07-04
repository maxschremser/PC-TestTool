package at.oefg1880.swing.utils;

import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 29.04.2010
 * Time: 18:53:54
 * To change this template use File | Settings | File Templates.
 */

public class ResourceHandler {
    public volatile static ResourceHandler resourceHandler; // volatile is needed so that multiple thread can reconcile the instance
    private final static String PATH = "resources.texts";
    private ResourceBundle rb;

    private ResourceHandler() {
        load();
    }

    public static ResourceHandler getInstance() {          //synchronized keyword has been removed from here
        if (resourceHandler == null) {                            //needed because once there is singleton available no need to aquire monitor again & again as it is costly
            synchronized (ResourceHandler.class) {
                if (resourceHandler == null) {                    //this is needed if two threads are waiting at the monitor at the time when singleton was getting instantiated
                    resourceHandler = new ResourceHandler();
                }
            }
        }
        return resourceHandler;
    }

    public void load() {
        load(PATH);
    }

    public void load(String path) {
        try {
            rb = ResourceBundle.getBundle(path);
        } catch (Exception e) {
        }
    }

    public String getString(String key) {
        return rb.getString(key);
    }

    public String getString(String clazz, String INDICATOR) {
        StringBuffer sb = new StringBuffer(clazz);
        sb.append(".");
        sb.append(INDICATOR);
        return getString(sb.toString());
    }

    public String getString(String clazz, String INDICATOR, String[] values) {
        StringBuffer sb = new StringBuffer(clazz);
        sb.append(".");
        sb.append(INDICATOR);
        String replacedString = getString(sb.toString()).replaceAll("%1", values[0]);
        return replacedString;
    }

}