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
public class OEFGAntwortPanel extends AntwortPanel {
    public final static int NUM_ANSWERS = 20;
    public final static char[] allowedValues = new char[]{' ', 'A', 'B', 'C'};

    public OEFGAntwortPanel(boolean isInCreateMode, AntwortDialog dialog) {
        super(isInCreateMode, dialog);
    }

    @Override
    public int getNumAnswers() {
        return NUM_ANSWERS;
    }

    @Override
    protected void setup() {
        FormLayout layout = new FormLayout(
                "center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow,6dlu,center:pref:grow",
                "pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref"
        );
        setLayout(layout);
        CellConstraints cc = new CellConstraints();
        // categories
        add(new JLabel(rh.getString(PROPERTY_NAME, KAT_A)), cc.xy(1, 3));
        add(new JLabel(rh.getString(PROPERTY_NAME, KAT_B)), cc.xy(1, 5));
        add(new JLabel(rh.getString(PROPERTY_NAME, KAT_C)), cc.xy(1, 7));
        add(new JLabel(rh.getString(PROPERTY_NAME, KAT_D)), cc.xy(1, 9));

        // answer labels
        add(new JLabel("1"), cc.xy(3, 1));
        add(new JLabel("2"), cc.xy(5, 1));
        add(new JLabel("3"), cc.xy(7, 1));
        add(new JLabel("4"), cc.xy(9, 1));
        add(new JLabel("5"), cc.xy(11, 1));

        // answer fields
        int k = 0;
        for (int j = 3; j <= 9; j += 2) {
            for (int i = 3; i <= 11; i += 2) {
                add(new AntwortTextField("", k++, getAllowedValues(), isInCreateMode), cc.xy(i, j));
            }
        }
    }

    @Override
    public AntwortTextField getAntwortTextField(int index) {
        if (index >= 0 && index < getNumAnswers()) {
            Component c = getComponent(index + 9);
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
