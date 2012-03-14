package at.oefg1880.swing;

import at.oefg1880.swing.frame.StartFrame;
import at.oefg1880.swing.utils.ResourceHandler;
import com.jgoodies.looks.windows.WindowsLookAndFeel;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
        GregorianCalendar cal = new GregorianCalendar();
        log.info("Starting StartFrame at " + cal.get(Calendar.YEAR) + cal.get(Calendar.MONTH) + cal.get(Calendar.DAY_OF_MONTH));

        new StartFrame(ResourceHandler.getInstance().getString(PROPERTY_NAME, TITLE));
      }
    };
    EventQueue.invokeLater(runner);
  }
}
