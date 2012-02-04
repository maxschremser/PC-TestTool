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
public abstract class AntwortPanel extends JPanel { //FormDebugPanel {
  private int[] values;

  public abstract int getNumAnswers();
  public abstract AntwortTextField getAntwortTextField(int index);
  protected abstract void setup();
  protected abstract boolean isInValue(char answer);

  public AntwortPanel() {
    super();
    setup();
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

  public boolean isFullyFilled() {
    for (int i = 0; i < getNumAnswers(); i++) {
      AntwortTextField atf = getAntwortTextField(i);
      if (!isInValue(atf.getAnswer()))
        return false;
    }
    return true;
  }
}
