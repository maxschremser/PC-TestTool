package at.oefg1880.swing.frame;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.list.Fragebogen;
import at.oefg1880.swing.panel.FragebogenPanel;
import at.oefg1880.swing.panel.GradientPanel;
import at.oefg1880.swing.panel.ImagePanel;
import at.oefg1880.swing.utils.PropertyHandler;
import at.oefg1880.swing.utils.ResourceHandler;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 09.02.2010
 * Time: 10:41:56
 * To change this template use File | Settings | File Templates.
 */
public abstract class TestToolFrame extends SheetableFrame implements ITexts, IConfig {
  public final String PROPERTY_NAME = "at.oefg1880.swing.frame.TestToolFrame";
  protected FragebogenPanel fragebogenPanel;
  protected final Logger log = Logger.getLogger(TestToolFrame.class);
  private PropertyHandler props = PropertyHandler.getInstance();
  private ImagePanel imagePanel;
  protected ResourceHandler rh = ResourceHandler.getInstance();
  private int returnValue;
  private JDialog dialog;

  public abstract String getImageName();

  public abstract String getFavicon();

  public abstract FragebogenPanel getFragebogenPanel();

  public abstract void exportFragebogen(Workbook wb, Fragebogen f);

  public abstract String getFragebogenName();

  public TestToolFrame(String title) throws HeadlessException {
    super(title);
    props.setOwner(this);
    setup();
  }

  private void setup() {
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        if (fragebogenPanel.getFragebogenList().getModel().getSize() > 0) {
          int a = JOptionPane.showConfirmDialog(getParent(), rh.getString(getClass(), QUESTION_SAVE));
          if (JOptionPane.YES_OPTION == a) {
            getFragebogenPanel().getButtonSave().doClick();
            storeProps();
            dispose();
            return;
          } else if (JOptionPane.NO_OPTION == a) {
            dispose();
            return;
          } else {
            return;
          }
        }
        dispose();
      }
    });
    FormLayout layout = new FormLayout(
        "6dlu,pref,6dlu",
        "6dlu,pref,6dlu,pref,6dlu");
    CellConstraints cc = new CellConstraints();
    GradientPanel panel = new GradientPanel();
    panel.setLayout(layout);
    panel.add(getImagePane(), cc.xy(2, 2));
    panel.add(getFragebogenPanel(), cc.xy(2, 4));
//    panel.add(getButtonPane(), cc.xy(2, 6));
    getContentPane().add(panel);

    if (props.getProperty(PROPERTY_NAME + "." + POS_X, "").length() > 0) {
      int x = Integer.valueOf(props.getProperty(PROPERTY_NAME + "." + POS_X, ""));
      int y = Integer.valueOf(props.getProperty(PROPERTY_NAME + "." + POS_Y, ""));

      Point p = new Point(x, y);
      setLocation(p);
    }

    // we are now using the Dissolver to fade out the frame
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setIconImage(new ImageIcon(getClass().getClassLoader().getResource(getFavicon())).getImage());
    pack();
    setResizable(false);
    setVisible(true);
  }

  public ImagePanel getImagePane() {
    if (imagePanel == null) {
      imagePanel = new ImagePanel(getClass().getClassLoader().getResource(getImageName()));
      imagePanel.setBorder(BorderFactory.createLineBorder(Color.black));
    }
    return imagePanel;
  }

  public void setReturnValue(int returnValue) {
    this.returnValue = returnValue;
  }

  public JDialog getDialog() {
    return dialog;
  }

  public int showDeleteFragebogenDialog(ActionListener list, String message, String title) {
    // Ja, Nein, Abbrechen Dialog mit einer Frage und einem Icon
    dialog = new JDialog(this, title, true);
    FormLayout layout = new FormLayout(
        "6dlu,center:pref,6dlu", "6dlu,pref,6dlu,pref,6dlu"
    );
    PanelBuilder builder = new PanelBuilder(layout);
    CellConstraints cc = new CellConstraints();
    builder.addLabel(message.toString(), cc.xy(2, 2));
    JButton okButton = new JButton(OK);
    okButton.setMnemonic('O');
    okButton.addActionListener(list);
    okButton.setActionCommand(OK);
    JButton noButton = new JButton(NO);
    noButton.setMnemonic('N');
    noButton.addActionListener(list);
    noButton.setActionCommand(NO);
    JPanel bar = ButtonBarFactory.buildOKCancelBar(okButton, noButton);
    builder.add(bar, cc.xy(2, 4));
    dialog.getContentPane().add(builder.getPanel());
    dialog.pack();
    dialog.setLocation((getLocation().x + (getWidth() / 2)) - (dialog.getWidth() / 2), (getLocation().y + (getHeight() / 2) - (dialog.getHeight() / 2)));
    dialog.setVisible(true);
    dialog.dispose();

    return returnValue;
  }

  private void storeProps() {
    props.setProperty(PROPERTY_NAME + "." + POS_X, getX() + "");
    props.setProperty(PROPERTY_NAME + "." + POS_Y, getY() + "");
    props.store();
  }

  public String exportData() {
    File file;
    try {
      Workbook wb = new HSSFWorkbook();
      Calendar cal = Calendar.getInstance();
      final String DATE_FORMAT = "yyyyMMdd";
      SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
      String date = sdf.format(cal.getTime());
      file = new File(getFragebogenName() + "-" + date + ".xls");
      file.createNewFile();
      log.info("Saved at: " + file.getAbsolutePath());
      FileOutputStream fos = new FileOutputStream(file);
      DefaultListModel model = (DefaultListModel) fragebogenPanel.getFragebogenList().getModel();
      Enumeration<Fragebogen> enums = (Enumeration<Fragebogen>) model.elements();
      while (enums.hasMoreElements()) {
        Fragebogen f = enums.nextElement();
        exportFragebogen(wb, f);
      }
      wb.write(fos);
      fos.close();
      return file.getAbsolutePath().replaceAll("\\\\", "/");
    } catch (FileNotFoundException fnfne) {
    } catch (IOException ioe) {
    }
    return "";
  }

}