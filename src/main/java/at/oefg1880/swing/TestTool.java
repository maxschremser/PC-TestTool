package at.oefg1880.swing;

import at.oefg1880.swing.frame.StartFrame;
import at.oefg1880.swing.utils.ResourceHandler;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 10.02.2010
 * Time: 11:35:19
 * To change this template use File | Settings | File Templates.
 */
public class TestTool implements ITexts, IConfig {
    private static Logger log = Logger.getLogger(TestTool.class);
    public final static String PROPERTY_NAME = "at.oefg1880.swing.TestTool";

    public static void main(String[] args) {
        Runnable runner = new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new WindowsLookAndFeel());
                } catch (UnsupportedLookAndFeelException ulnfe) {
                    ulnfe.printStackTrace();
                }
                PropertyConfigurator.configureAndWatch("log4j.properties", 60 * 1000);
                log.info("Starting StartFrame at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                Properties props = new Properties();
                try {
                    props.load(getClass().getClassLoader().getResourceAsStream("resources/version.properties"));
                    log.info("Version: " + props.get(VERSION) + "." + props.get(MAJOR) + "_" + props.get(MINOR));
                } catch (FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                new StartFrame(ResourceHandler.getInstance().getString(PROPERTY_NAME, TITLE));
            }
        };
        EventQueue.invokeLater(runner);
    }
}
