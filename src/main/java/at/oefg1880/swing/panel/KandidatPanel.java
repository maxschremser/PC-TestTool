package at.oefg1880.swing.panel;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.dialog.KandidatDialog;
import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.io.Kandidat;
import at.oefg1880.swing.list.KandidatTable;
import at.oefg1880.swing.utils.PropertyHandler;
import at.oefg1880.swing.utils.ResourceHandler;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: sensi
 * Date: 26.04.2010
 * Time: 22:44:11
 * To change this template use File | Settings | File Templates.
 */
public class KandidatPanel extends GradientPanel implements ITexts, IConfig, ActionListener {
    public final static String PROPERTY_NAME = "at.oefg1880.swing.panel.KandidatPanel";
    protected TestToolFrame frame;
    protected KandidatDialog kandidatDialog;
    protected PropertyHandler props = PropertyHandler.getInstance();
    protected ResourceHandler rh = ResourceHandler.getInstance();
    protected final Logger log = Logger.getLogger(getClass());
    private KandidatTable list;
    private final String NEW = "new", SAVE = "save";
    private JButton buttonSave, buttonNew;

    public KandidatDialog editKandidatDialog(Kandidat kandidat) {

        kandidatDialog = new KandidatDialog(frame, rh.getString(PROPERTY_NAME, FRAGEBOGEN_NEW), kandidat);
        kandidatDialog.loadProps();
        kandidatDialog.setVisible(true);
        return kandidatDialog;
    }

    public JDialog createNewKandidatDialog() {

        kandidatDialog = new KandidatDialog(frame, rh.getString(PROPERTY_NAME, FRAGEBOGEN_NEW));
        kandidatDialog.loadProps();
        kandidatDialog.setVisible(true);
        return kandidatDialog;
    }

    public KandidatPanel(TestToolFrame frame) {
        super(DIAGONAL);

        this.frame = frame;
        setup();
    }

    private void setup() {

        FormLayout layout = new FormLayout(
                "6dlu,pref:grow,6dlu,pref,6dlu",
                "6dlu,pref,6dlu,pref,6dlu");
        setLayout(layout);
        CellConstraints cc = new CellConstraints();
        add(new JLabel(rh.getString(PROPERTY_NAME, LABEL)), cc.xy(2, 2));
        buttonNew = new JButton(rh.getString(PROPERTY_NAME, BUTTON_NEW));
        buttonNew.addActionListener(this);
        buttonNew.setActionCommand(NEW);
        buttonNew.setFocusable(false);
        buttonNew.setMnemonic('N');
        buttonSave = new JButton(rh.getString(PROPERTY_NAME, BUTTON_SAVE));
        buttonSave.addActionListener(this);
        buttonSave.setActionCommand(SAVE);
        buttonSave.setFocusable(false);
        buttonSave.setMnemonic('S');
        buttonSave.setEnabled(false);
        JPanel buttonBarPanel = ButtonBarFactory.buildAddRemoveRightBar(buttonSave, buttonNew);
        buttonBarPanel.setOpaque(false);
        add(buttonBarPanel, cc.xy(4, 2));
        JScrollPane scrollPaneList = new JScrollPane(getKandidatTable(),
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneList.setPreferredSize(new Dimension(getWidth(), 170));
        add(scrollPaneList, cc.xywh(2, 4, 3, 1));
    }

    public KandidatTable getKandidatTable() {
        if (list == null) {
            list = new KandidatTable(frame);
            list.setup();
            list.requestFocus();
        }
        return list;
    }

    public JButton getButtonSave() {

        return buttonSave;
    }

    public JButton getButtonNew() {

        return buttonNew;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (NEW.equals(e.getActionCommand())) {
            log.debug("actionPerformed.NEW");
            createNewKandidatDialog();
        } else if (SAVE.equals(e.getActionCommand())) {
            log.debug("actionPerformed.SAVE");
            props.propertyChange(new PropertyChangeEvent(this, JOptionPane.VALUE_PROPERTY, 0, 0));
            String filePath = frame.exportData();
            int selectedOption = JOptionPane.showConfirmDialog(getParent(), rh.getString(PROPERTY_NAME, DIALOG_SAVED, new String[]{filePath}), UIManager.getString("OptionPane.titleText"), JOptionPane.YES_NO_OPTION);
            if (JOptionPane.OK_OPTION == selectedOption) {
                try {
                    URI uri = new URI(filePath);
                    Desktop.getDesktop().browse(uri);
                } catch (Exception exp) {
                    log.info(exp.getMessage());
                }
            }
        }
    }
}
