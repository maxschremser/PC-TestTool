package at.oefg1880.swing;

import at.oefg1880.swing.frame.StartFrame;
import at.oefg1880.swing.utils.PropertyHandler;
import at.oefg1880.swing.utils.ResourceHandler;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.verhas.licensor.License;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

  public static void main(String[] args) {
    Runnable runner = new Runnable() {
      public void run() {
        try {
          UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
        } catch (UnsupportedLookAndFeelException ulnfe) {
          ulnfe.printStackTrace();
        }
        PropertyConfigurator.configureAndWatch("resources/log4j.properties", 60 * 1000);
        GregorianCalendar cal = new GregorianCalendar();
        log.info("Starting StartFrame at " + cal.get(Calendar.YEAR) + cal.get(Calendar.MONTH) + cal.get(Calendar.DAY_OF_MONTH));

        checkLicense();

        new StartFrame(ResourceHandler.getInstance().getString(getClass(), TITLE));
      }
    };
    EventQueue.invokeLater(runner);
  }

  public static void checkLicense() {
    byte[] digest = new byte[]{
        (byte) 0xDF, (byte) 0xFE, (byte) 0x80, (byte) 0x38, (byte) 0xF8, (byte) 0x3A, (byte) 0xD8, (byte) 0xD3, (byte) 0xBB,
        (byte) 0x2A, (byte) 0xC7, (byte) 0x5A, (byte) 0xEC, (byte) 0x9B, (byte) 0x1A, (byte) 0xE9, (byte) 0x54, (byte) 0xBB,
        (byte) 0xFB, (byte) 0x34, (byte) 0x62, (byte) 0x48, (byte) 0x8C, (byte) 0x4F, (byte) 0x18, (byte) 0x8A, (byte) 0x49,
        (byte) 0x48, (byte) 0xF4, (byte) 0x4A, (byte) 0x67, (byte) 0x04
    };
    License license = new License();
    PropertyHandler props = PropertyHandler.getInstance();
    String licenseFile = props.getProperty(LICENSE_FILE);
    String publicKey = props.getProperty(PUBLIC_KEY);
    try {
      license.loadKeyRingFromResource(publicKey, digest);
      license.setLicenseEncodedFromFile(licenseFile);
      checkDateAndVersionFromLicense(license);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }
  }

  public static void checkDateAndVersionFromLicense(License license) {
    String issueDate = license.getFeature(LICENSE_ISSUE_DATE);
    String validDate = license.getFeature(LICENSE_VALID_DATE);
    Date today = Calendar.getInstance().getTime();
    final String DATE_FORMAT = "yyyy.MM.dd";
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

    try {
      if (today.before(sdf.parse(issueDate))) {
        throw new IllegalArgumentException("Issue date is later than today. Will exit.");
      }
      if (today.after(sdf.parse(validDate))) {
        throw new IllegalArgumentException("Valid date is later than today. Will exit.");
      }
    } catch (ParseException pe) {
      pe.printStackTrace();
      System.exit(0);
    }
  }
}
