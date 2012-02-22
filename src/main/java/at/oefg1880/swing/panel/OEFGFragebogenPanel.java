package at.oefg1880.swing.panel;

import at.oefg1880.swing.dialog.AntwortDialog;
import at.oefg1880.swing.dialog.FragebogenDialog;
import at.oefg1880.swing.dialog.OEFGAntwortDialog;
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
    fragebogenDialog = new OEFGFragebogenDialog(frame, rh.getString(getClass(), FRAGEBOGEN_NEW));
    fragebogenDialog.setLocation(Integer.valueOf(props.getProperty(fragebogenDialog.PROPERTY_NAME + "." + POS_X)),
        Integer.valueOf(props.getProperty(fragebogenDialog.PROPERTY_NAME + "." + POS_Y)));
    fragebogenDialog.setVisible(true);
    return fragebogenDialog;
  }

  @Override
  public FragebogenDialog editFragebogenDialog(Fragebogen fragebogen) {
    if (fragebogen == null) return null;
    fragebogenDialog = new OEFGFragebogenDialog(frame, rh.getString(getClass(), FRAGEBOGEN_EDIT), fragebogen);
    fragebogenDialog.setLocation(Integer.valueOf(props.getProperty(fragebogenDialog.PROPERTY_NAME + "." + POS_X)),
        Integer.valueOf(props.getProperty(fragebogenDialog.PROPERTY_NAME + "." + POS_Y)));
    fragebogenDialog.setVisible(true);
    fragebogenDialog.getAntwortList().requestFocus();
    return fragebogenDialog;
  }

  @Override
  public AntwortDialog getAntwortDialog(Fragebogen fragebogen) {
    return new OEFGAntwortDialog(frame, rh.getString(getClass(), ANTWORT_NEW), fragebogen);
  }
}
