package at.oefg1880.swing.panel;

import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.text.AntwortTextField;
import at.oefg1880.swing.utils.ResourceHandler;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 13.10.2010
 * Time: 12:54:36
 * To change this template use File | Settings | File Templates.
 */
public abstract class AntwortPanel extends JPanel implements ITexts { //FormDebugPanel {
  public final static String PROPERTY_NAME = "at.oefg1880.swing.panel.AntwortPanel";

  private int[] values;
  protected boolean isInCreateMode = false;

  protected ResourceHandler rh = ResourceHandler.getInstance();

  public abstract int getNumAnswers();

  protected abstract void setup();

  public abstract AntwortTextField getAntwortTextField(int index);

  public abstract char[] getAllowedValues();

  public AntwortPanel(boolean isInCreateMode) {
    super();
    this.isInCreateMode = isInCreateMode;
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

  public boolean isInValue(char answer, boolean isInCreateMode) {
    if (isInCreateMode && answer == ' ') return false;
    char[] answers = getAllowedValues();
    for (char a : answers) {
      if (a == answer) {
        return true;
      }
    }
    return false;
  }

  public void setValues(int[] values) {
    for (Component c : getComponents()) {
      if (c instanceof AntwortTextField) {
        AntwortTextField atf = (AntwortTextField) c;
        atf.setText(values[atf.getIndex()] + "");
      }
    }
  }

  public boolean isFullyFilled(boolean isInCreateMode) {
    for (int i = 0; i < getNumAnswers(); i++) {
      AntwortTextField atf = getAntwortTextField(i);
      if (!isInValue(atf.getAnswer(), isInCreateMode))
        return false;
    }
    return true;
  }
}
