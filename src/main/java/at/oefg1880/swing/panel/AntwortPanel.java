package at.oefg1880.swing.panel;

import at.oefg1880.swing.text.AntwortTextField;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 13.10.2010
 * Time: 12:54:36
 * To change this template use File | Settings | File Templates.
 */
public abstract class AntwortPanel extends JPanel {
  private int[] values;

  public AntwortPanel() {
    super();
    setup();
  }

  public abstract int getNumAnswers();

  private void setup() {
    FormLayout layout = new FormLayout(
        "6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu",
        "6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu"
    );
    setLayout(layout);
    CellConstraints cc = new CellConstraints();
    // categories
    add(new JLabel("Kat A"), cc.xy(2, 4));
    add(new JLabel("Kat B"), cc.xy(2, 6));
    add(new JLabel("Kat C"), cc.xy(2, 8));
    add(new JLabel("Kat D"), cc.xy(2, 10));
    // answer labels
    add(new JLabel("1"), cc.xy(4, 2));
    add(new JLabel("2"), cc.xy(6, 2));
    add(new JLabel("3"), cc.xy(8, 2));
    add(new JLabel("4"), cc.xy(10, 2));
    add(new JLabel("5"), cc.xy(12, 2));

    // answer fields
    add(new AntwortTextField("", 0), cc.xy(4, 4));
    add(new AntwortTextField("", 1), cc.xy(6, 4));
    add(new AntwortTextField("", 2), cc.xy(8, 4));
    add(new AntwortTextField("", 3), cc.xy(10, 4));
    add(new AntwortTextField("", 4), cc.xy(12, 4));

    add(new AntwortTextField("", 5), cc.xy(4, 6));
    add(new AntwortTextField("", 6), cc.xy(6, 6));
    add(new AntwortTextField("", 7), cc.xy(8, 6));
    add(new AntwortTextField("", 8), cc.xy(10, 6));
    add(new AntwortTextField("", 9), cc.xy(12, 6));

    add(new AntwortTextField("", 10), cc.xy(4, 8));
    add(new AntwortTextField("", 11), cc.xy(6, 8));
    add(new AntwortTextField("", 12), cc.xy(8, 8));
    add(new AntwortTextField("", 13), cc.xy(10, 8));
    add(new AntwortTextField("", 14), cc.xy(12, 8));

    add(new AntwortTextField("", 15), cc.xy(4, 10));
    add(new AntwortTextField("", 16), cc.xy(6, 10));
    add(new AntwortTextField("", 17), cc.xy(8, 10));
    add(new AntwortTextField("", 18), cc.xy(10, 10));
    add(new AntwortTextField("", 19), cc.xy(12, 10));


  }

  public AntwortTextField getAntwortTextField(int index) {
    if (index >= 0 && index < getNumAnswers()) {
      Component c = getComponent(index + 9);
      if (c instanceof AntwortTextField)
        return (AntwortTextField) getComponent(index + 9);
    }
    return null;
  }

  public void reset() {
    for (Component c : getComponents()) {
      if (c instanceof AntwortTextField) {
        ((AntwortTextField) c).setText("");
      }
    }
  }

  public int[] getValues() {
    values = new int[getNumAnswers()];
    for (Component c : getComponents()) {
      if (c instanceof AntwortTextField) {
        AntwortTextField atf = (AntwortTextField) c;
        values[atf.getIndex()] = atf.getValue();
      }
    }
    return values;
  }

  public void setValues(int[] values) {
    for (Component c : getComponents()) {
      if (c instanceof AntwortTextField) {
        AntwortTextField atf = (AntwortTextField) c;
        atf.setText(values[atf.getIndex()] + "");
      }
    }
  }

  private boolean isInValue(char answer) {
    if (answer == 'A' || answer == 'B' || answer == 'C' || answer == ' ') return true;
    return false;
  }

  public boolean isFullyFilled() {
    for (int i = 0; i < getNumAnswers(); i++) {
      AntwortTextField atf = getAntwortTextField(i);
      if (!isInValue(atf.getAnswer()))
        return false;
    }
    return true;
  }
}
