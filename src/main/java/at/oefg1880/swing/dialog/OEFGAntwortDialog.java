package at.oefg1880.swing.dialog;

import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.io.Antwort;
import at.oefg1880.swing.io.Fragebogen;
import at.oefg1880.swing.panel.AntwortPanel;
import at.oefg1880.swing.panel.OEFGAntwortPanel;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 2/2/12
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class OEFGAntwortDialog extends AntwortDialog {
    private AntwortPanel panel;

    public OEFGAntwortDialog(final TestToolFrame frame, String title, Fragebogen fragebogen) {
        super(frame, title, fragebogen);
    }

    public OEFGAntwortDialog(final TestToolFrame frame, String title, Fragebogen fragebogen, Antwort antwort) {
        super(frame, title, fragebogen, antwort);
    }

    @Override
    public AntwortPanel getAntwortPanel(final AntwortDialog dialog) {
        if (panel == null) {
            panel = new OEFGAntwortPanel(fragebogen == null, dialog);
        }
        panel.setSaveButton(getSaveButton());
        return panel;
    }
}
