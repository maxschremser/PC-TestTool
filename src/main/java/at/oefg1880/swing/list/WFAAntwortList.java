package at.oefg1880.swing.list;

import at.oefg1880.swing.dialog.WFAAntwortDialog;
import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.io.Antwort;
import at.oefg1880.swing.io.Fragebogen;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 2/2/12
 * Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class WFAAntwortList extends AntwortList {
    public WFAAntwortList(TestToolFrame frame, Fragebogen fragebogen) {
        super(frame, fragebogen);
    }

    @Override
    protected void createEditAntwortDialog(Antwort antwort) {
        dialog = new WFAAntwortDialog(frame, rh.getString(PROPERTY_NAME, ANTWORT_EDIT), fragebogen, antwort);
        dialog.setVisible(true);
    }

}
