package at.oefg1880.swing.utils;

import at.oefg1880.swing.IConfig;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: AT003053
 * Date: 18.12.12
 * Time: 16:44
 * To change this template use File | Settings | File Templates.
 */
public class WindowSaver implements AWTEventListener, IConfig {
    private static WindowSaver windowSaver;
    private static Logger log = Logger.getLogger(WindowSaver.class);
    private static PropertyHandler props = PropertyHandler.getInstance();

    private WindowSaver() {
    }

    public static WindowSaver getInstance() {
        if (windowSaver == null)
            windowSaver = new WindowSaver();
        return windowSaver;
    }

    public static void loadSettings(JFrame frame) {
        frame.setLocation(loadSettings(frame.getName()));
        frame.validate();
    }

    public static void loadSettings(JDialog dialog) {
        dialog.setLocation(loadSettings(dialog.getName()));
        dialog.validate();
    }

    public static Point loadSettings(String name) {
        int x = Integer.valueOf(props.getProperty(name + "." + POS_X, ""));
        int y = Integer.valueOf(props.getProperty(name + "." + POS_Y, ""));
        log.debug("loadSettings('" + name + "')");
        return new Point(x, y);
    }

    public static void saveSettings(JFrame frame) {
        saveSettings(frame.getName(), frame.getX(), frame.getY());
    }

    public static void saveSettings(JDialog dialog) {
        saveSettings(dialog.getName(), dialog.getX(), dialog.getY());
    }

    public static void saveSettings(String name, int x, int y) {
        props.setProperty(name + "." + POS_X, x + "");
        props.setProperty(name + "." + POS_Y, y + "");
        log.debug("saveSettings('" + name + "', " + x + "," + y + ")");
        props.store();
    }

    @Override
    public void eventDispatched(AWTEvent event) {
        try {
            if (event.getID() == WindowEvent.WINDOW_OPENED) {
                ComponentEvent ce = (ComponentEvent) event;
                if (ce.getComponent() instanceof JFrame) {
                    JFrame f = (JFrame) ce.getComponent();
                    loadSettings(f);
                } else if (ce.getComponent() instanceof JDialog) {
                    JDialog d = (JDialog) ce.getComponent();
                    loadSettings(d);
                }
            } else if (event.getID() == WindowEvent.WINDOW_CLOSING) {
                ComponentEvent ce = (ComponentEvent) event;
                if (ce.getComponent() instanceof JFrame) {
                    JFrame f = (JFrame) ce.getComponent();
                    saveSettings(f);
                } else if (ce.getComponent() instanceof JDialog) {
                    JDialog d = (JDialog) ce.getComponent();
                    saveSettings(d);
                }
            }

        } catch (Exception e) {
            log.error(e.toString());
        }
        log.debug("eventDispatched...");
    }
}
