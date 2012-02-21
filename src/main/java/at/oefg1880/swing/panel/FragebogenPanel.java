package at.oefg1880.swing.panel;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.dialog.AntwortDialog;
import at.oefg1880.swing.dialog.FragebogenDialog;
import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.list.Fragebogen;
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
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: sensi
 * Date: 26.04.2010
 * Time: 22:44:11
 * To change this template use File | Settings | File Templates.
 */
public abstract class FragebogenPanel extends GradientPanel implements ITexts, IConfig, ActionListener {
  protected TestToolFrame frame;
  protected FragebogenDialog fragebogenDialog;
  protected AntwortDialog antwortDialog;
  protected PropertyHandler props = PropertyHandler.getInstance();
  protected ResourceHandler rh = ResourceHandler.getInstance();
  protected final Logger log = Logger.getLogger(getClass());
  private FragebogenList list;
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
    add(new JLabel(rh.getString(getClass(), LABEL)), cc.xy(2, 2));
    buttonNew = new JButton(rh.getString(getClass(), BUTTON_NEW));
    buttonNew.addActionListener(this);
    buttonNew.setActionCommand(NEW);
    buttonNew.setFocusable(false);
    buttonNew.setMnemonic('N');
    buttonSave = new JButton(rh.getString(getClass(), BUTTON_SAVE));
    buttonSave.addActionListener(this);
    buttonSave.setActionCommand(SAVE);
    buttonSave.setFocusable(false);
    buttonSave.setMnemonic('S');
    buttonSave.setEnabled(false);
    JPanel buttonBarPanel = ButtonBarFactory.buildAddRemoveRightBar(buttonSave, buttonNew);
    buttonBarPanel.setOpaque(true);
    add(buttonBarPanel, cc.xy(4, 2));
    JScrollPane scrollPaneList = new JScrollPane(getFragebogenList(),
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPaneList.setPreferredSize(new Dimension((int) getWidth(), 170));
    add(scrollPaneList, cc.xywh(2, 4, 3, 1));
  }

  public FragebogenList getFragebogenList() {
    if (list == null) {
      list = new FragebogenList(frame);
      list.requestFocus();
    }
    return list;
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
    antwortDialog.setVisible(true);
    return antwortDialog;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (NEW.equals(e.getActionCommand())) {
      createNewFragebogenDialog();
    } else if (SAVE.equals(e.getActionCommand())) {
      String filePath = frame.exportData();
      int selectedOption = JOptionPane.showConfirmDialog(getParent(), rh.getString(getClass(), DIALOG_SAVED, new String[]{filePath}), UIManager.getString("OptionPane.titleText"), JOptionPane.YES_NO_OPTION);
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
