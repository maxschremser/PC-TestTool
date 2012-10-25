package at.oefg1880.swing.panel;

import at.oefg1880.swing.dialog.AntwortDialog;
import at.oefg1880.swing.dialog.FragebogenDialog;
import at.oefg1880.swing.dialog.OEFGAntwortDialog;
import at.oefg1880.swing.dialog.OEFGFragebogenDialog;
import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.io.Antwort;
import at.oefg1880.swing.io.Fragebogen;

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
        log.debug("");
    }

    @Override
    public JDialog createNewFragebogenDialog() {
        log.debug("");
        fragebogenDialog = new OEFGFragebogenDialog(frame, rh.getString(PROPERTY_NAME, FRAGEBOGEN_NEW));
        fragebogenDialog.loadProps();
        fragebogenDialog.setVisible(true);
        return fragebogenDialog;
    }

    @Override
    public FragebogenDialog editFragebogenDialog(Fragebogen fragebogen) {
        log.debug("");
        if (fragebogen == null) return null;
        fragebogenDialog = new OEFGFragebogenDialog(frame, rh.getString(PROPERTY_NAME, FRAGEBOGEN_EDIT), fragebogen);
        fragebogenDialog.setLocation(Integer.valueOf(props.getProperty(fragebogenDialog.PROPERTY_NAME + "." + POS_X)),
                Integer.valueOf(props.getProperty(fragebogenDialog.PROPERTY_NAME + "." + POS_Y)));
        fragebogenDialog.setVisible(true);
        fragebogenDialog.getAntwortList().requestFocus();
        return fragebogenDialog;
    }

    @Override
    public AntwortDialog getAntwortDialog(Fragebogen fragebogen) {
        log.debug("");
        antwortDialog = new OEFGAntwortDialog(frame, rh.getString(PROPERTY_NAME, ANTWORT_NEW), fragebogen);
        return antwortDialog;
    }

    @Override
    public AntwortDialog getAntwortDialog(Fragebogen fragebogen, Antwort antwort) {
        log.debug("");
        antwortDialog = new OEFGAntwortDialog(frame, rh.getString(PROPERTY_NAME, ANTWORT_EDIT), fragebogen, antwort);
        return antwortDialog;
    }
}
