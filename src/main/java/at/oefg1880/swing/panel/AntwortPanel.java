package at.oefg1880.swing.panel;

import at.oefg1880.swing.text.AntwortTextField;

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

  public abstract void setup();

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
