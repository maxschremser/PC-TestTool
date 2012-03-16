package at.oefg1880.swing.dialog;

import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.list.AntwortList;
import at.oefg1880.swing.list.Fragebogen;
import at.oefg1880.swing.list.WFAAntwortList;
import at.oefg1880.swing.panel.AntwortPanel;
import at.oefg1880.swing.panel.WFAAntwortPanel;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 2/2/12
 * Time: 2:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class WFAFragebogenDialog extends FragebogenDialog {
  public WFAFragebogenDialog(TestToolFrame frame, String title) {
    super(frame, title);
  }

  public WFAFragebogenDialog(TestToolFrame frame, String title, Fragebogen fragebogen) {
    super(frame, title, fragebogen);
  }

  @Override
  public AntwortPanel getAntwortPanel() {
    return new WFAAntwortPanel(true);
  }

  @Override
  public AntwortList getAntwortList() {
    if (list == null) {
      list = new WFAAntwortList(frame, fragebogen);
      if (fragebogen != null)
        list.setVisibleRowCount(fragebogen.getSolved());
      list.requestFocus();
    }
    return list;
  }

}
