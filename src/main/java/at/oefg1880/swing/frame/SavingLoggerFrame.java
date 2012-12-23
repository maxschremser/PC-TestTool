package at.oefg1880.swing.frame;

import at.oefg1880.swing.utils.WindowSaver;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: AT003053
 * Date: 18.12.12
 * Time: 17:15
 * To change this template use File | Settings | File Templates.
 */
public abstract class SavingLoggerFrame extends JFrame {
    protected final Logger log = Logger.getLogger(getClass());
    private WindowSaver saver = WindowSaver.getInstance();

    public SavingLoggerFrame(String title, String name) {
        super(title);
        setName(name);
        Toolkit.getDefaultToolkit().addAWTEventListener(saver, AWTEvent.WINDOW_EVENT_MASK);
    }

    @Override
    public void setVisible(boolean b) {
        setLocation(saver.loadSettings(getName()));
        log.info("setVisible()");
        super.setVisible(b);
    }

    @Override
    public void dispose() {
        saver.saveSettings(getName(), getX(), getY());
        log.info("dispose()");
        super.dispose();
    }
}
