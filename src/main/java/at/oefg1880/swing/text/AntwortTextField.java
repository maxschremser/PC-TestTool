package at.oefg1880.swing.text;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.panel.AntwortPanel;
import org.apache.log4j.Logger;

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
    protected char[] allowedValues;
    public final static String VALUE_CHANGED = "valueChanged";
    private Color oldColor;
    private boolean isInCreateMode = false;

    public AntwortTextField(String s, int index, char[] allowedValues, boolean isInCreateMode) {
        super(s);
        this.index = index;
        this.allowedValues = allowedValues;
        this.isInCreateMode = isInCreateMode;
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
        if (((AntwortPanel) getParent()).checkEnableSaveButton(isInCreateMode)) {
            // enable saveButton and focus it
            JButton button = ((AntwortPanel) getParent()).getSaveButton();
            button.setEnabled(true);
            button.requestFocus();
        }
    }

    class InputTextDocument extends PlainDocument {
        private char answer; // A,B,C,D
        private int value;     // 1,2,3,4

        public void insertString(int offs, String str, AttributeSet a)
                throws BadLocationException {
            if (str == null)
                return;
            String newString = str.substring(0, 1);
            boolean bSkip = false;

            if (newString.length() > 0) {
                try {
                    value = Integer.parseInt(newString);
                    answer = allowedValues[value];
                    if (isInCreateMode && answer == ' ') {
                        bSkip = true;
                    }
                } catch (NumberFormatException nfe) {
                    // no number was entered, check for A,B,C,D,' '
                    boolean bFound = false;
                    for (int i = 0; i < allowedValues.length; i++) {
                        if ((answer = Character.toUpperCase(newString.toCharArray()[0])) == allowedValues[i]) {
                            value = i;
                            bFound = true;
                            break;
                        }
                    }
                    // the value must be valid, in create mode ' ' is not allowed
                    if ((isInCreateMode && answer == ' ') || !bFound) {
                        bSkip = true;
                    }
                    if (!bFound) {
                        Logger.getLogger(getClass()).info("skipping value: " + str);
                    }

                } catch (ArrayIndexOutOfBoundsException aiobe) {
                    // skip the value
                    bSkip = true;
                    Logger.getLogger(getClass()).info("skipping value: " + str);
                }
                // set the value text A,B,C
                if (!bSkip) {
                    int len = getContent().length();
                    if (len > 1)
                        setText("");

                    String sAnswer = new String(new char[]{answer});
                    super.insertString(0, sAnswer, a);
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
        ((InputTextDocument) getDocument()).setValue(translate(allowedValues, answer));
        setText(answer + "");
    }

    public void setValue(int value) {
        ((InputTextDocument) getDocument()).setAnswer(translate(allowedValues, value));
        setText(((InputTextDocument) getDocument()).getAnswer() + "");
    }

    public void setOldColor(Color c) {
        this.oldColor = c;
    }

    public static int translate(char[] allowedValues, char answer) {
        for (int i = 0; i < allowedValues.length; i++) {
            if (allowedValues[i] == answer)
                return i;
        }
        return 0;
    }

    public static char translate(char[] allowedValues, int value) {
        return allowedValues[value];
    }
}

