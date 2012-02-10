package at.oefg1880.swing.text;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.panel.AntwortPanel;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Created by IntelliJ IDEA.
 * User: sensi
 * Date: 28.04.2010
 * Time: 22:04:10
 * To change this template use File | Settings | File Templates.
 */
public class AntwortTextField extends JTextField implements IConfig {
  private int index;        // to identify the focused component
  public final static String VALUE_CHANGED = "valueChanged";
  private Color oldColor;
  JButton button = null;

  public AntwortTextField(String s, int index) {
    super(s);
    this.index = index;
    setColumns(1);
    Font font = getFont();
    setFont(font.deriveFont(Font.BOLD));
    setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    setSelectionColor(selectedTextForeground);
    addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if (oldColor == null) {
          oldColor = getBackground();
        }
        setBackground(Color.yellow);

      }

      @Override
      public void focusLost(FocusEvent e) {
        if (oldColor != null)
          setBackground(oldColor);
        enableButton();
      }
    });
  }

  public int getIndex() {
    return index;
  }

  protected Document createDefaultModel() {
    return new InputTextDocument();
  }

  public boolean isValid() {
    try {
      Integer.parseInt(getText());
      return true;
    } catch (NumberFormatException e) {
      return false;
    } catch (Exception e) {
      return false;
    }
  }

  public void enableButton() {
    if (((AntwortPanel) getParent()).isFullyFilled()) {
      // enable saveButton and focus it
      Object o = getParent().getParent().getComponent(7);
      if (o instanceof JButton) {
        button = (JButton) o;
      } else {
        button = (JButton) getParent().getParent().getComponent(13);
      }
      button.setEnabled(true);
      button.requestFocus();
    }
  }

  class InputTextDocument extends PlainDocument {
    private char answer; // A,B,C
    private int value;     // 1,2,3

    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {
      if (str == null)
        return;
      String newString = str.substring(0, 1);
      boolean bSkip = false;

      if (newString.length() > 0) {
        try {
          value = Integer.parseInt(newString);
          if (value == 1) {
            answer = 'A';
          } else if (value == 2) {
            answer = 'B';
          } else if (value == 3) {
            answer = 'C';
          } else if (value == 4) {
            answer = 'D';
          } else {
            value = 0;
            answer = ' ';
            bSkip = true;
          }
        } catch (NumberFormatException nfe) {
          // no number was entered, check for A,B,C
          if (newString.equalsIgnoreCase("A")) {
            answer = 'A';
            value = 1;
          } else if (newString.equalsIgnoreCase("B")) {
            answer = 'B';
            value = 2;
          } else if (newString.equalsIgnoreCase("C")) {
            answer = 'C';
            value = 3;
          } else if (newString.equalsIgnoreCase("D")) {
            answer = 'D';
            value = 4;
          } else {
            answer = ' ';
            value = 0;
            bSkip = true;
          }
        }
        // set the value text A,B,C
        if (!bSkip) {
          int len = getContent().length();
          if (len > 1)
            setText("");

          String sAnswer = new String(new char[]{answer});
          super.insertString(0, sAnswer, a);
//          super.remove(0,getContent().length());
          int componentCount = getParent().getComponentCount();
          Component c;
          for (int i = 9; i < componentCount; i++) {
            c = getParent().getComponent(i);
            if (c instanceof AntwortTextField) {
              AntwortTextField atf = (AntwortTextField) c;
              if (atf.getIndex() == getIndex()) {
                getParent().firePropertyChange(VALUE_CHANGED + getIndex(), ' ', getValue());
                if ((i + 1) < componentCount) {
                  getParent().getComponent(i + 1).requestFocus();
                  break;
                }
              }
            }
          }
          enableButton();
        }
      }
    }

    public int getValue() {
      return value;
    }

    public char getAnswer() {
      return answer;
    }

    public void setValue(int value) {
      this.value = value;
    }

    public void setAnswer(char a) {
      this.answer = a;
    }
  }

  public int getValue() {
    return ((InputTextDocument) getDocument()).getValue();
  }

  public char getAnswer() {
    return ((InputTextDocument) getDocument()).getAnswer();
  }

  public void setAnswer(char answer) {
    ((InputTextDocument) getDocument()).setValue(translate(answer));
    setText(answer + "");
  }

  public void setValue(int value) {
    ((InputTextDocument) getDocument()).setAnswer(translate(value));
    setText(((InputTextDocument) getDocument()).getAnswer() + "");
  }

  public void setOldColor(Color c) {
    this.oldColor = c;
  }

  public static int translate(char answer) {
    if (answer == 'A') return 1;
    else if (answer == 'B') return 2;
    else if (answer == 'C') return 3;
    else if (answer == 'D') return 4;
    return 0;
  }

  public static char translate(int value) {
    if (value == 1) return 'A';
    else if (value == 2) return 'B';
    else if (value == 3) return 'C';
    else if (value == 4) return 'D';
    return ' ';
  }
}

