package com.jgoodies.validation.tutorial.basics;

import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.tutorial.util.ExampleComponentFactory;
import com.jgoodies.validation.tutorial.util.TutorialUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;
import com.jgoodies.validation.view.ValidationResultViewFactory;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Demonstrates a style how to provide input hints,
 * so that users can avoid entering invalid data.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.4 $
 */
public class InfoOnFocusExample {
    private JLabel infoLabel;
    private JTextArea infoArea;
    private JComponent infoAreaPane;
    private JTextField orderNoField;
    private JTextField orderDateField;
    private JTextField deliveryDateField;
    private JTextArea deliveryNotesArea;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            // Likely Plastic is not in the classpath; ignore it.
        }
        JFrame frame = new JFrame();
        frame.setTitle("Basics :: Hints on Focus");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JComponent panel = new InfoOnFocusExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        TutorialUtils.locateOnOpticalScreenCenter(frame);
        frame.setVisible(true);
    }

    // Component Creation and Initialization **********************************

    /**
     * Creates and intializes the UI components.
     */
    private void initComponents() {
        orderNoField = new JTextField();
        orderDateField = ExampleComponentFactory.createDateField(new ValueHolder());
        deliveryDateField = ExampleComponentFactory.createDateField(new ValueHolder());
        deliveryNotesArea = new JTextArea();
    }


    private void initComponentAnnotations() {
        ValidationComponentUtils.setInputHint(orderNoField, "Mandatory, length in [5, 10]");
        ValidationComponentUtils.setInputHint(orderDateField, "Mandatory, before delivery date");
        ValidationComponentUtils.setInputHint(deliveryDateField, "After delivery date");
        ValidationComponentUtils.setInputHint(deliveryNotesArea, "Length <= 30");
    }

    private void initEventHandling() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addPropertyChangeListener(new FocusChangeHandler());
    }


    // Building ***************************************************************

    /**
     * Builds and returns the whole editor.
     */
    public JComponent buildPanel() {
        initComponents();
        initComponentAnnotations();
        initEventHandling();

        FormLayout layout = new FormLayout(
                "fill:default:grow",
                "max(14dlu;pref), 6dlu, pref, 3dlu, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.add(buildInfoAreaPane(), cc.xy(1, 1));
        builder.addSeparator("Order", cc.xy(1, 3));
        builder.add(buildEditorPanel(), cc.xy(1, 5));
        return builder.getPanel();
    }

    private JComponent buildEditorPanel() {
        FormLayout layout = new FormLayout(
                "right:max(65dlu;pref), 4dlu, 40dlu, 2dlu, 40dlu, 80dlu:grow",
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 2px"); // extra bottom space for icons

        layout.setRowGroups(new int[][]{{1, 3, 5, 7}});
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.addLabel("Order No", cc.xy(1, 1));
        builder.add(orderNoField, cc.xyw(3, 1, 3));
        builder.addLabel("Order-/Delivery Date", cc.xy(1, 3));
        builder.add(orderDateField, cc.xy(3, 3));
        builder.add(deliveryDateField, cc.xy(5, 3));
        builder.addLabel("Notes", cc.xy(1, 5));
        builder.add(new JScrollPane(deliveryNotesArea),
                cc.xywh(3, 5, 4, 3));
        return builder.getPanel();
    }

    private JComponent buildInfoAreaPane() {
        infoLabel = new JLabel(ValidationResultViewFactory.getInfoIcon());
        infoArea = new JTextArea(1, 38);
        infoArea.setEditable(false);
        infoArea.setOpaque(false);
        FormLayout layout = new FormLayout("pref, 2dlu, default", "pref");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.add(infoLabel, cc.xy(1, 1));
        builder.add(infoArea, cc.xy(3, 1));
        infoAreaPane = builder.getPanel();
        infoAreaPane.setVisible(false);
        return infoAreaPane;
    }

    // Validation Handler *****************************************************

    /**
     * Displays an input hint for components that get the focus permanently.
     */
    private class FocusChangeHandler implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            String propertyName = evt.getPropertyName();
            if (!"permanentFocusOwner".equals(propertyName))
                return;
            Component focusOwner = KeyboardFocusManager
                    .getCurrentKeyboardFocusManager().getFocusOwner();
            String focusHint = (focusOwner instanceof JComponent)
                    ? (String) ValidationComponentUtils
                    .getInputHint((JComponent) focusOwner)
                    : null;
            infoArea.setText(focusHint);
            infoAreaPane.setVisible(focusHint != null);
        }
    }
}