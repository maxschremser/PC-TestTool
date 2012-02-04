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
public class StartFrame extends SheetableFrame implements ITexts, IConfig {
  private ImagePanel oefgImagePanel;
  private ImagePanel wfaImagePanel;
  private PropertyHandler props = PropertyHandler.getInstance();
  private ResourceHandler rh = ResourceHandler.getInstance();
  private final Logger log = Logger.getLogger(StartFrame.class);

  public StartFrame(String title) throws HeadlessException {
    super(title);
    props.setOwner(this);
    setup();
    pack();
  }

  private void setup() {
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        dispose();
      }
    });
    FormLayout layout = new FormLayout(
        "6dlu,pref,6dlu",
        "6dlu,pref,6dlu,pref,6dlu");
    CellConstraints cc = new CellConstraints();
    GradientPanel panel = new GradientPanel();
    panel.setLayout(layout);
    panel.add(getOEFGImagePane(), cc.xy(2, 2));
    panel.add(getWFAImagePane(), cc.xy(2, 4));
    getContentPane().add(panel);

    int x = Integer.valueOf(props.getProperty("x"));
    int y = Integer.valueOf(props.getProperty("y"));
    Point p = new Point(x, y);
    setLocation(p);
    // we are now using the Dissolver to fade out the frame
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setIconImage(new ImageIcon(getClass().getClassLoader().getResource("resources/oefg_favicon.gif")).getImage());
    addKeyListener(new KeyAdapter() {
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_1) {
          new OEFGTestToolFrame(ResourceHandler.getInstance().getString(OEFGTestToolFrame.class.getName() + "." + TITLE));
          dispose();
        } else if (e.getKeyChar() == KeyEvent.VK_2) {
          new WFATestToolFrame(ResourceHandler.getInstance().getString(WFATestToolFrame.class.getName() + "." + TITLE));
          dispose();
        }
      }
    });
    pack();
    setResizable(false);
    setVisible(true);
  }

  public ImagePanel getOEFGImagePane() {
    if (oefgImagePanel == null) {
      oefgImagePanel = new ImagePanel(getClass().getClassLoader().getResource("resources/oefg1880_logo.gif"));
      oefgImagePanel.setBorder(BorderFactory.createLineBorder(Color.black));
      oefgImagePanel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          new OEFGTestToolFrame(ResourceHandler.getInstance().getString(OEFGTestToolFrame.class.getName() + "." + TITLE));
          dispose();
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
          new WFATestToolFrame(ResourceHandler.getInstance().getString(WFATestToolFrame.class.getName() + "." + TITLE));
          dispose();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
          wfaImagePanel.setBorder(BorderFactory.createLineBorder(Color.black, 7));
        }

        @Override
        public void mouseExited(MouseEvent e) {
          wfaImagePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        }
      });
    }
    return wfaImagePanel;
  }
}