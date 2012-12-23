package at.oefg1880.swing.dialog;

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
public abstract class SavingLoggerDialog extends JDialog {
    protected final Logger log = Logger.getLogger(getClass());
    private WindowSaver saver = WindowSaver.getInstance();

    public SavingLoggerDialog(Frame owner, String title, boolean modal, String name) {
        super(owner, title, modal);
        setName(name);
        Toolkit.getDefaultToolkit().addAWTEventListener(saver, AWTEvent.WINDOW_EVENT_MASK);
    }

    @Override
    public void setVisible(boolean b) {
        setLocation(saver.loadSettings(getName()));
        super.setVisible(b);
    }

    @Override
    public void dispose() {
        saver.saveSettings(getName(), getX(), getY());
        super.dispose();
    }

}
