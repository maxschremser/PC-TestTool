package at.oefg1880.swing.dialog;

import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.list.Antwort;
import at.oefg1880.swing.list.Fragebogen;
import at.oefg1880.swing.panel.AntwortPanel;
import at.oefg1880.swing.panel.WFAAntwortPanel;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 2/2/12
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class WFAAntwortDialog extends AntwortDialog {
  public WFAAntwortDialog(TestToolFrame frame, String title, Fragebogen fragebogen) {
    super(frame, title, fragebogen);
  }

  public WFAAntwortDialog(TestToolFrame frame, String title, Fragebogen fragebogen, Antwort antwort) {
    super(frame, title, fragebogen, antwort);
  }

  @Override
  public AntwortPanel getAntwortPanel() {
    return new WFAAntwortPanel(fragebogen == null);
  }
}
