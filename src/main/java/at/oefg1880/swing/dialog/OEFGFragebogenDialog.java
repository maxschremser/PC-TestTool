package at.oefg1880.swing.dialog;

import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.list.AntwortList;
import at.oefg1880.swing.list.Fragebogen;
import at.oefg1880.swing.list.OEFGAntwortList;
import at.oefg1880.swing.panel.AntwortPanel;
import at.oefg1880.swing.panel.OEFGAntwortPanel;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 2/2/12
 * Time: 2:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OEFGFragebogenDialog extends FragebogenDialog {
  public OEFGFragebogenDialog(TestToolFrame frame, String title) {
    super(frame, title);
  }

  public OEFGFragebogenDialog(TestToolFrame frame, String title, Fragebogen fragebogen) {
    super(frame, title, fragebogen);
  }

  @Override
  public AntwortPanel getAntwortPanel() {
    return new OEFGAntwortPanel();
  }

  @Override
  public AntwortList getAntwortList() {
    if (list == null) {
      list = new OEFGAntwortList(frame, fragebogen);
      list.setVisibleRowCount(fragebogen.getSolved());
      list.requestFocus();
    }
    return list;
  }

}
