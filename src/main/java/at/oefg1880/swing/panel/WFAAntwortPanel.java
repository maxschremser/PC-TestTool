package at.oefg1880.swing.panel;

import at.oefg1880.swing.dialog.AntwortDialog;
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
public class WFAAntwortPanel extends AntwortPanel {
    public final static int NUM_ANSWERS = 50;
    public final static char[] allowedValues = new char[]{CHAR_BLANK, CHAR_A, CHAR_B, CHAR_C, CHAR_D};

    public WFAAntwortPanel(boolean isInCreateMode, AntwortDialog dialog) {
        super(isInCreateMode, dialog);
    }

    @Override
    public int getNumAnswers() {
        return NUM_ANSWERS;
    }

    @Override
    protected void setup() {
        FormLayout layout = new FormLayout(
                "left:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow," +
                        "6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow",
                "pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref"
        );
        setLayout(layout);
        CellConstraints cc = new CellConstraints();

        // categories
        add(new JLabel(rh.getString(PROPERTY_NAME, KAT_A)), cc.xy(1, 3));
        add(new JLabel(rh.getString(PROPERTY_NAME, KAT_B)), cc.xy(1, 5));
        add(new JLabel(rh.getString(PROPERTY_NAME, KAT_C)), cc.xy(1, 7));
        add(new JLabel(rh.getString(PROPERTY_NAME, KAT_D)), cc.xy(1, 9));
        add(new JLabel(rh.getString(PROPERTY_NAME, KAT_E)), cc.xy(1, 11));

        // answer labels
        add(new JLabel(Q_1), cc.xy(3, 1));
        add(new JLabel(Q_2), cc.xy(5, 1));
        add(new JLabel(Q_3), cc.xy(7, 1));
        add(new JLabel(Q_4), cc.xy(9, 1));
        add(new JLabel(Q_5), cc.xy(11, 1));
        add(new JLabel(Q_6), cc.xy(13, 1));
        add(new JLabel(Q_7), cc.xy(15, 1));
        add(new JLabel(Q_8), cc.xy(17, 1));
        add(new JLabel(Q_9), cc.xy(19, 1));
        add(new JLabel(Q_10), cc.xy(21, 1));

        // answer fields
        int k = 0;
        for (int j = 3; j <= 11; j += 2) {
            for (int i = 3; i <= 21; i += 2) {
                add(new AntwortTextField("", k++, getAllowedValues(), isInCreateMode), cc.xy(i, j));
            }
        }
    }

    @Override
    public AntwortTextField getAntwortTextField(int index) {
        if (index >= 0 && index < getNumAnswers()) {
            Component c = getComponent(index + 15);
            if (c instanceof AntwortTextField)
                return (AntwortTextField) c;
        }
        return null;
    }

    @Override
    public char[] getAllowedValues() {
        return allowedValues;
    }
}
