package at.oefg1880.swing.dialog;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.io.Adresse;
import at.oefg1880.swing.io.Kandidat;
import at.oefg1880.swing.panel.GradientPanel;
import at.oefg1880.swing.panel.KandidatPanel;
import at.oefg1880.swing.utils.PropertyHandler;
import at.oefg1880.swing.utils.ResourceHandler;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 14.10.2010
 * Time: 13:21:52
 * To change this template use File | Settings | File Templates.
 */
public class KandidatDialog extends SavingLoggerDialog implements ActionListener, IConfig, ITexts {
    public final static String PROPERTY_NAME = "at.oefg1880.swing.dialog.KandidatDialog";
    protected final Logger log = Logger.getLogger(getClass());
    private ResourceHandler rh = ResourceHandler.getInstance();
    private PropertyHandler props = PropertyHandler.getInstance();
    private TestToolFrame frame;
    private Kandidat kandidat;
    private JLabel labelTitle;
    private JTextField tfTitle, tfName, tfStrasse, tfPLZ, tfOrt, tfGeburtsort, tfTelefon, tfEmail;
    private JComboBox comboGeburtstagTag, comboGeburtstagMonat, comboGeburtstagJahr;
    private JCheckBox cbKursunterlagen, cbPassfoto, cbAnwesend;
    private JButton saveButton, cancelButton;
    private final static String SAVE = "update";

    // New Kandidat
    public KandidatDialog(TestToolFrame frame, String title) {
        this(frame, title, null);
    }

    // Update Kandidat
    public KandidatDialog(TestToolFrame frame, String title, Kandidat kandidat) {
        super(frame, title, true, PROPERTY_NAME);
        this.frame = frame;
        this.kandidat = kandidat;

        setup();

        if (kandidat != null)
            fillValues();
    }
    
    private void addFocusListener(final JTextField field) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        field.selectAll();
                    }
                });
            }
        });
    }

    private void initComponents() {
        saveButton = new JButton(rh.getString(PROPERTY_NAME, BUTTON_SAVE));
        saveButton.addActionListener(this);
        saveButton.setActionCommand(SAVE);

        cancelButton = new JButton(rh.getString(PROPERTY_NAME, BUTTON_CANCEL));
        cancelButton.addActionListener(this);
        cancelButton.setActionCommand(CANCEL);

        labelTitle = new JLabel(" ");
        Font font = labelTitle.getFont().deriveFont(Font.PLAIN, 21);
        labelTitle.setFont(font);

        tfTitle = new JTextField(20);

        tfName = new JTextField(rh.getString(PROPERTY_NAME, FIRST_LAST_NAME), 20);
        tfName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                    reset();
                    dispose();
                }
            }

            public void keyReleased(KeyEvent e) {
                labelTitle.setText(tfName.getText());
            }
        });
        addFocusListener(tfName);

        tfStrasse = new JTextField(rh.getString(PROPERTY_NAME, STREET), 20);
        addFocusListener(tfStrasse);
        tfPLZ = new JFormattedTextField(new DecimalFormat("####"));
        tfPLZ.setText(rh.getString(PROPERTY_NAME, PLZ));
        tfPLZ.setHorizontalAlignment(JTextField.RIGHT);
        addFocusListener(tfPLZ);
        tfOrt = new JTextField(rh.getString(PROPERTY_NAME, CITY), 20);
        addFocusListener(tfOrt);
        tfGeburtsort = new JTextField(rh.getString(PROPERTY_NAME, BIRTHPLACE), 20);
        addFocusListener(tfGeburtsort);
        tfTelefon = new JTextField(rh.getString(PROPERTY_NAME, TELEPHONE), 20);
        addFocusListener(tfTelefon);
        tfEmail = new JTextField(rh.getString(PROPERTY_NAME, EMAIl), 20);
        addFocusListener(tfEmail);

        Vector<String> vDays = new Vector<String>();
        for (int i = 1; i <= 31; i++) {
            vDays.add(i + "");
        }
        comboGeburtstagTag = new JComboBox(vDays);
        comboGeburtstagMonat = new JComboBox(rh.getString(PROPERTY_NAME, MONTHS).split(","));
        Vector<String> vYears = new Vector<String>();
        int thisYear = new GregorianCalendar().get(GregorianCalendar.YEAR);
        for (int i = 1900; i <= thisYear - 10; i++) {
            vYears.add(i + "");
        }
        comboGeburtstagJahr = new JComboBox(vYears);

        cbKursunterlagen = new JCheckBox(rh.getString(PROPERTY_NAME, COURSE_PAID));
        cbKursunterlagen.setOpaque(false);
        cbPassfoto = new JCheckBox(rh.getString(PROPERTY_NAME, PASSPHOTO));
        cbPassfoto.setOpaque(false);
        cbAnwesend = new JCheckBox(rh.getString(PROPERTY_NAME, PRESENT));
        cbAnwesend.setOpaque(false);
    }

    private JPanel buildPanel() {
        initComponents();

        FormLayout layout = new FormLayout(
                "6dlu,right:pref,6dlu,pref,6dlu,pref,6dlu,pref",
                "6dlu,20dlu,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref");
        PanelBuilder builder = new PanelBuilder(layout, new GradientPanel());
        CellConstraints cc = new CellConstraints();

        // Title
        builder.add(labelTitle, cc.xywh(2, 2, 5, 1));
        builder.addSeparator(rh.getString(PROPERTY_NAME, NAME), cc.xywh(2, 4, 7, 1));

        // Titel
        builder.add(new JLabel(rh.getString(PROPERTY_NAME, TITLE)), cc.xy(2, 6));
        builder.add(tfTitle, cc.xywh(4, 6, 5, 1));

        // Name
        builder.add(new JLabel(rh.getString(PROPERTY_NAME, FIRST_LAST_NAME)), cc.xy(2, 8));
        builder.add(tfName, cc.xywh(4, 8, 5, 1));
        // Strasse
        builder.add(new JLabel(rh.getString(PROPERTY_NAME, STREET)), cc.xy(2, 10));
        builder.add(tfStrasse, cc.xywh(4, 10, 5, 1));
        // PLZ / Ort
        builder.add(new JLabel(rh.getString(PROPERTY_NAME, PLZ_ORT)), cc.xy(2, 12));
        builder.add(tfPLZ, cc.xy(4, 12));
        builder.add(tfOrt, cc.xywh(6, 12, 3, 1));

        // Personal Data
        builder.addSeparator(rh.getString(PROPERTY_NAME, PERSONAL_DATA), cc.xywh(2, 14, 7, 1));
        // Birthday
        builder.add(new JLabel(rh.getString(PROPERTY_NAME, BIRTHDAY)), cc.xy(2, 16));
        builder.add(comboGeburtstagTag, cc.xy(4, 16));
        builder.add(comboGeburtstagMonat, cc.xy(6, 16));
        builder.add(comboGeburtstagJahr, cc.xy(8, 16));
        // Birthplace
        builder.add(new JLabel(rh.getString(PROPERTY_NAME, BIRTHPLACE)), cc.xy(2, 18));
        builder.add(tfGeburtsort, cc.xywh(4, 18, 5, 1));
        // Telephone
        builder.add(new JLabel(rh.getString(PROPERTY_NAME, TELEPHONE)), cc.xy(2, 20));
        builder.add(tfTelefon, cc.xywh(4, 20, 5, 1));
        // E-Mail
        builder.add(new JLabel(rh.getString(PROPERTY_NAME, EMAIl)), cc.xy(2, 22));
        builder.add(tfEmail, cc.xywh(4, 22, 5, 1));

        // Test Data
        builder.addSeparator(rh.getString(PROPERTY_NAME, TEST_DATA), cc.xywh(2, 24, 7, 1));
        // Kursunterlagen bezahlt
        builder.add(cbKursunterlagen, cc.xywh(2, 26, 7, 1));
        // Passfoto
        builder.add(cbPassfoto, cc.xywh(2, 28, 7, 1));
        // anwesend
        builder.add(cbAnwesend, cc.xywh(2, 30, 7, 1));

        JPanel buttonOKBar = ButtonBarFactory.buildOKCancelBar(saveButton, cancelButton);
        buttonOKBar.setOpaque(false);
        builder.add(buttonOKBar, cc.xywh(2, 32, 7, 1));
        builder.setBorder(BorderFactory.createLineBorder(Color.black));
        getRootPane().setDefaultButton(saveButton);

        return builder.getPanel();
    }

    private void setup() {
        getContentPane().add(buildPanel());
        pack();
        setResizable(false);
    }

    private void reset() {
        tfName.setText("");
    }

    private void saveOrUpdate() {
        if (kandidat == null)
            save();
        else
            update();
    }

    private void save() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        Date dt = new Date();
        try {
            dt = sdf.parse(comboGeburtstagTag.getSelectedItem() + "/" + (comboGeburtstagMonat.getSelectedIndex() + 1) + "/" + comboGeburtstagJahr.getSelectedItem());
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        kandidat = new Kandidat(tfTitle.getText(), tfName.getText(), new Adresse(tfStrasse.getText(), tfPLZ.getText(), tfOrt.getText()), tfTelefon.getText(), tfEmail.getText(), dt, tfGeburtsort.getText(), cbPassfoto.isSelected(), cbKursunterlagen.isSelected(), cbAnwesend.isSelected());
        frame.getKandidatPanel().getKandidatTable().add(kandidat);
        log.info("Added item '" + kandidat.getTitleAndName() + "' to KandidatTable.");
    }

    private void update() {
        kandidat.setTitle(tfTitle.getText());
        kandidat.setName(tfName.getText());
        kandidat.setAdresse(new Adresse(tfStrasse.getText(), tfPLZ.getText(), tfOrt.getText()));
        kandidat.setGeburtsort(tfGeburtsort.getText());
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        Date dt = new Date();
        try {
            dt = sdf.parse(comboGeburtstagTag.getSelectedItem() + "/" + (comboGeburtstagMonat.getSelectedIndex() + 1) + "/" + comboGeburtstagJahr.getSelectedItem());
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        kandidat.setGeburtstag(dt);
        kandidat.setGeburtsort(tfGeburtsort.getText());
        kandidat.setTelephone(tfTelefon.getText());
        kandidat.setEmail(tfEmail.getText());
        kandidat.setAnwesend(cbAnwesend.isSelected());
        kandidat.setPassPhoto(cbPassfoto.isSelected());
        kandidat.setKursgebuehrBezahlt(cbKursunterlagen.isSelected());
        // update the model and tell the editor that editing has stopped, let the cell
        // be handled by the renderer again
        frame.getKandidatPanel().getKandidatTable().getCellEditor().stopCellEditing();

        log.info("Updated item '" + kandidat.getTitleAndName() + "' in  KandidatTable.");
    }

    private void close() {
        saveOrUpdate();
        reset();
        dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (SAVE.equals(e.getActionCommand())) {
            close();
        } else if (CANCEL.equals(e.getActionCommand())) {
            dispose();
        }
    }

    private void fillValues() {
        // set Name
        labelTitle.setText(kandidat.getTitleAndName());
        tfTitle.setText(kandidat.getTitle());
        tfName.setText(kandidat.getName());
        tfStrasse.setText(kandidat.getStrasse());
        tfPLZ.setText(kandidat.getPLZ() + "");
        tfOrt.setText(kandidat.getOrt());
        comboGeburtstagTag.setSelectedItem(new SimpleDateFormat(DATE_DAY).format(kandidat.getGeburtstag()));
        comboGeburtstagMonat.setSelectedItem(rh.getString(PROPERTY_NAME, MONTHS).split(",")[Integer.valueOf(new SimpleDateFormat(DATE_MONTH).format(kandidat.getGeburtstag())) - 1]);
        comboGeburtstagJahr.setSelectedItem(new SimpleDateFormat(DATE_YEAR).format(kandidat.getGeburtstag()));
        tfGeburtsort.setText(kandidat.getGeburtsort());
        cbAnwesend.setSelected(kandidat.isAnwesend());
        cbPassfoto.setSelected(kandidat.hasPassPhoto());
        cbKursunterlagen.setSelected(kandidat.hasKursgebuehrBezahlt());
    }
}
