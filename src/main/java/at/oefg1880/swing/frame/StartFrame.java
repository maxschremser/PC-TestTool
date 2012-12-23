package at.oefg1880.swing.frame;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.panel.GradientPanel;
import at.oefg1880.swing.panel.ImagePanel;
import at.oefg1880.swing.utils.PropertyHandler;
import at.oefg1880.swing.utils.ResourceHandler;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 09.02.2010
 * Time: 10:41:56
 * To change this template use File | Settings | File Templates.
 */
public class StartFrame extends SavingLoggerFrame implements ITexts, IConfig {
    public final static String PROPERTY_NAME = "at.oefg1880.swing.frame.StartFrame";
    private ImagePanel oefgImagePanel;
    private ImagePanel wfaImagePanel;
    private PropertyHandler props = PropertyHandler.getInstance();
    private ResourceHandler rh = ResourceHandler.getInstance();
    private static Logger log = Logger.getLogger(SavingLoggerFrame.class);

    public StartFrame(String title) throws HeadlessException {
        super(title, PROPERTY_NAME);
        props.setOwner(this);
        setup();
    }

    private void setup() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FormLayout layout = new FormLayout(
                "6dlu,pref",
                "6dlu,pref,6dlu,pref");
        CellConstraints cc = new CellConstraints();
        GradientPanel panel = new GradientPanel();
        panel.setLayout(layout);
        panel.add(getOEFGImagePane(), cc.xy(2, 2));
        panel.add(getWFAImagePane(), cc.xy(2, 4));
        getContentPane().add(panel);

        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("resources/oefg_favicon.gif")).getImage());
        addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_1) {
                    showOEFGTestToolFrame();
                } else if (e.getKeyChar() == KeyEvent.VK_2) {
                    showWFATestToolFrame();
                }
            }
        });
        pack();
        setResizable(false);
        setVisible(true);
    }

    private void showOEFGTestToolFrame() {
        new OEFGTestToolFrame(rh.getString(PROPERTY_NAME, TITLE));
        dispose();
    }

    private void showWFATestToolFrame() {
        new WFATestToolFrame(rh.getString(PROPERTY_NAME, TITLE));
        dispose();
    }


    public ImagePanel getOEFGImagePane() {
        if (oefgImagePanel == null) {
            oefgImagePanel = new ImagePanel(getClass().getClassLoader().getResource("resources/oefg1880_logo.gif"));
            oefgImagePanel.setBorder(BorderFactory.createLineBorder(Color.black));
            oefgImagePanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    showOEFGTestToolFrame();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    oefgImagePanel.setBorder(BorderFactory.createLineBorder(Color.black, 7));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    oefgImagePanel.setBorder(BorderFactory.createLineBorder(Color.black));
                }
            });
        }
        return oefgImagePanel;
    }

    public ImagePanel getWFAImagePane() {

        if (wfaImagePanel == null) {
            wfaImagePanel = new ImagePanel(getClass().getClassLoader().getResource("resources/wfa_logo.gif"));
            wfaImagePanel.setBorder(BorderFactory.createLineBorder(Color.black));
            wfaImagePanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    log.debug("Mouse clicked");
                    showWFATestToolFrame();
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    log.debug("Mouse entered");
                    wfaImagePanel.setBorder(BorderFactory.createLineBorder(Color.black, 7));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    log.debug("Mouse exited");
                    wfaImagePanel.setBorder(BorderFactory.createLineBorder(Color.black));
                }
            });
        }
        return wfaImagePanel;
    }
}