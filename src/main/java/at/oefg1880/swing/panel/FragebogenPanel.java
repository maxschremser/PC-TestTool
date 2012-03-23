package at.oefg1880.swing.panel;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.dialog.AntwortDialog;
import at.oefg1880.swing.dialog.FragebogenDialog;
import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.io.Fragebogen;
import at.oefg1880.swing.list.FragebogenList;
import at.oefg1880.swing.utils.PropertyHandler;
import at.oefg1880.swing.utils.ResourceHandler;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: sensi
 * Date: 26.04.2010
 * Time: 22:44:11
 * To change this template use File | Settings | File Templates.
 */
public abstract class FragebogenPanel extends GradientPanel implements ITexts, IConfig, ActionListener {
  public final static String PROPERTY_NAME = "at.oefg1880.swing.panel.FragebogenPanel";
  protected TestToolFrame frame;
  protected FragebogenDialog fragebogenDialog;
  protected AntwortDialog antwortDialog;
  protected PropertyHandler props = PropertyHandler.getInstance();
  protected ResourceHandler rh = ResourceHandler.getInstance();
  protected final Logger log = Logger.getLogger(getClass());
  private FragebogenList fragebogenList;
  private final String NEW = "new", SAVE = "save";
  private JButton buttonSave, buttonNew;

  public abstract JDialog createNewFragebogenDialog();

  public abstract FragebogenDialog editFragebogenDialog(Fragebogen fragebogen);

  public abstract AntwortDialog getAntwortDialog(Fragebogen fragebogen);

  public FragebogenPanel(TestToolFrame frame) {
    super(DIAGONAL);
    this.frame = frame;
    setup();
  }

  private void setup() {
    FormLayout layout = new FormLayout("6dlu,pref:grow,6dlu,pref,6dlu", "6dlu,pref,6dlu,pref,6dlu");
    setLayout(layout);
    CellConstraints cc = new CellConstraints();
    add(new JLabel(rh.getString(PROPERTY_NAME, LABEL)), cc.xy(2, 2));
    buttonNew = new JButton(rh.getString(PROPERTY_NAME, BUTTON_NEW));
    buttonNew.addActionListener(this);
    buttonNew.setActionCommand(NEW);
    buttonNew.setFocusable(false);
    buttonNew.setMnemonic('N');
    buttonSave = new JButton(rh.getString(PROPERTY_NAME, BUTTON_SAVE));
    buttonSave.addActionListener(this);
    buttonSave.setActionCommand(SAVE);
    buttonSave.setFocusable(false);
    buttonSave.setMnemonic('S');
    buttonSave.setEnabled(false);
    JPanel buttonBarPanel = ButtonBarFactory.buildAddRemoveRightBar(buttonSave, buttonNew);
    buttonBarPanel.setOpaque(false);
    add(buttonBarPanel, cc.xy(4, 2));
    JScrollPane scrollPaneList = new JScrollPane(getFragebogenList(),
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPaneList.setPreferredSize(new Dimension((int) getWidth(), 170));
    add(scrollPaneList, cc.xywh(2, 4, 3, 1));
  }

  public FragebogenList getFragebogenList() {
    if (fragebogenList == null) {
      fragebogenList = new FragebogenList(frame);
      fragebogenList.requestFocus();
    }
    return fragebogenList;
  }

  public JButton getButtonSave() {
    return buttonSave;
  }

  public JButton getButtonNew() {
    return buttonNew;
  }

  public FragebogenDialog getFragebogenDialog() {
    return fragebogenDialog;
  }

  public AntwortDialog createNewAntwortDialog(Fragebogen fragebogen) {
    if (fragebogen == null) return null;
    if (fragebogen.getSolved() == fragebogen.getExisting()) {
      return null;
    }
    antwortDialog = getAntwortDialog(fragebogen);
    antwortDialog.loadProps();
    antwortDialog.setVisible(true);
    return antwortDialog;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (NEW.equals(e.getActionCommand())) {
      createNewFragebogenDialog();
    } else if (SAVE.equals(e.getActionCommand())) {
      props.propertyChange(new PropertyChangeEvent(this, JOptionPane.VALUE_PROPERTY, 0, 0));
      String filePath = frame.exportData();
      // save the file in the properties
      int selectedOption = JOptionPane.showConfirmDialog(getParent(), rh.getString(PROPERTY_NAME, DIALOG_SAVED, new String[]{filePath}), UIManager.getString("OptionPane.titleText"), JOptionPane.YES_NO_OPTION);
      if (JOptionPane.OK_OPTION == selectedOption) {
        try {
          URI uri = new URI(filePath);
          Desktop.getDesktop().browse(uri);
        } catch (Exception exp) {
          log.info(exp.getMessage());
        }
      }
    }
  }
}
