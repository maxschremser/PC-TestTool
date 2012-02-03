package at.oefg1880.swing.panel;

import at.oefg1880.swing.text.AntwortTextField;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 2/2/12
 * Time: 2:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class WFAAntwortPanel extends AntwortPanel {
  private final static int NUM_ANSWERS = 50;

  @Override
  public int getNumAnswers() {
    return NUM_ANSWERS;
  }

  public void setup() {
    FormLayout layout = new FormLayout(
        "6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow," +
            "6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu",
        "6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu"
    );
    setLayout(layout);
    CellConstraints cc = new CellConstraints();

    // categories
    add(new JLabel("Kat A"), cc.xy(2, 4));
    add(new JLabel("Kat B"), cc.xy(2, 6));
    add(new JLabel("Kat C"), cc.xy(2, 8));
    add(new JLabel("Kat D"), cc.xy(2, 10));
    add(new JLabel("Kat E"), cc.xy(2, 12));

    // answer labels
    add(new JLabel("1"), cc.xy(4, 2));
    add(new JLabel("2"), cc.xy(6, 2));
    add(new JLabel("3"), cc.xy(8, 2));
    add(new JLabel("4"), cc.xy(10, 2));
    add(new JLabel("5"), cc.xy(12, 2));
    add(new JLabel("6"), cc.xy(14, 2));
    add(new JLabel("7"), cc.xy(16, 2));
    add(new JLabel("8"), cc.xy(18, 2));
    add(new JLabel("9"), cc.xy(20, 2));
    add(new JLabel("10"), cc.xy(22, 2));

    // answer fields
    int k = 0;
    for (int j = 4; j <= 12; j += 2) {
      for (int i = 4; i <= 22; i += 2) {
        add(new AntwortTextField("", k++), cc.xy(i, j));
      }
    }
  }
}
