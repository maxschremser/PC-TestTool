package at.oefg1880.swing.panel;

import at.oefg1880.swing.dialog.FragebogenDialog;
import at.oefg1880.swing.dialog.OEFGFragebogenDialog;
import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.list.Fragebogen;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 2/2/12
 * Time: 1:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class OEFGFragebogenPanel extends FragebogenPanel {
  public OEFGFragebogenPanel(TestToolFrame frame) {
    super(frame);
  }

  @Override
  public JDialog createNewFragebogenDialog() {
    fragebogenDialog = new OEFGFragebogenDialog(frame, "Neuen Fragebogen erstellen ...");
    fragebogenDialog.setVisible(true);
    return fragebogenDialog;
  }

  @Override
  public FragebogenDialog editFragebogenDialog(Fragebogen fragebogen) {
    if (fragebogen == null) return null;
    fragebogenDialog = new OEFGFragebogenDialog(frame, "Fragebogen bearbeiten ...", fragebogen);
    fragebogenDialog.setVisible(true);
    fragebogenDialog.getAntwortList().requestFocus();
    return fragebogenDialog;
  }

}
