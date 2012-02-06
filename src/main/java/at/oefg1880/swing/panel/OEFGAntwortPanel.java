package at.oefg1880.swing.panel;

import at.oefg1880.swing.text.AntwortTextField;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 2/2/12
 * Time: 2:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class OEFGAntwortPanel extends AntwortPanel {
  private final static int NUM_ANSWERS = 20;

  @Override
  public int getNumAnswers() {
    return NUM_ANSWERS;
  }

  protected void setup() {
    FormLayout layout = new FormLayout(
        "6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu",
        "6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu"
    );
    setLayout(layout);
    CellConstraints cc = new CellConstraints();
    // categories
    add(new JLabel(rh.getString(getClass(), KAT_A)), cc.xy(2, 4));
    add(new JLabel(rh.getString(getClass(), KAT_B)), cc.xy(2, 6));
    add(new JLabel(rh.getString(getClass(), KAT_C)), cc.xy(2, 8));
    add(new JLabel(rh.getString(getClass(), KAT_D)), cc.xy(2, 10));
    // answer labels
    add(new JLabel("1"), cc.xy(4, 2));
    add(new JLabel("2"), cc.xy(6, 2));
    add(new JLabel("3"), cc.xy(8, 2));
    add(new JLabel("4"), cc.xy(10, 2));
    add(new JLabel("5"), cc.xy(12, 2));

    // answer fields
    int k = 0;
    for (int j = 4; j <= 10; j += 2) {
      for (int i = 4; i <= 12; i += 2) {
        add(new AntwortTextField("", k++), cc.xy(i, j));
      }
    }
  }

  public AntwortTextField getAntwortTextField(int index) {
    if (index >= 0 && index < getNumAnswers()) {
      Component c = getComponent(index + 9);
      if (c instanceof AntwortTextField)
        return (AntwortTextField) c;
    }
    return null;
  }

  protected boolean isInValue(char answer) {
    if (answer == 'A' || answer == 'B' || answer == 'C' || answer == ' ') return true;
    return false;
  }


}
