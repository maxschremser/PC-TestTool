package at.oefg1880.swing.panel;

import at.oefg1880.swing.dialog.AntwortDialog;
import at.oefg1880.swing.dialog.FragebogenDialog;
import at.oefg1880.swing.dialog.WFAAntwortDialog;
import at.oefg1880.swing.dialog.WFAFragebogenDialog;
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
public class WFAFragebogenPanel extends FragebogenPanel {
  public WFAFragebogenPanel(TestToolFrame frame) {
    super(frame);
  }

  public JDialog createNewFragebogenDialog() {
    fragebogenDialog = new WFAFragebogenDialog(frame, "Neuen Fragebogen erstellen ...");
    fragebogenDialog.setVisible(true);
    return fragebogenDialog;
  }

  @Override
  public FragebogenDialog editFragebogenDialog(Fragebogen fragebogen) {
    if (fragebogen == null) return null;
    fragebogenDialog = new WFAFragebogenDialog(frame, "Fragebogen bearbeiten ...", fragebogen);
    fragebogenDialog.setVisible(true);
    fragebogenDialog.getAntwortList().requestFocus();
    return fragebogenDialog;
  }

  @Override
  public AntwortDialog getAntwortDialog(Fragebogen fragebogen) {
    return new WFAAntwortDialog(frame, "Neue Antworten hinzufügen ...", fragebogen);
  }
}
