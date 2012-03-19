package at.oefg1880.swing.dialog;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.list.Kandidat;
import at.oefg1880.swing.panel.GradientPanel;
import at.oefg1880.swing.panel.KandidatPanel;
import at.oefg1880.swing.utils.PropertyHandler;
import at.oefg1880.swing.utils.ResourceHandler;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
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
public class KandidatDialog extends JDialog implements ActionListener, IConfig, ITexts {
  public final static String PROPERTY_NAME = "at.oefg1880.swing.dialog.KandidatDialog";
  private ResourceHandler rh = ResourceHandler.getInstance();
  private PropertyHandler props = PropertyHandler.getInstance();
  private TestToolFrame frame;
  private Kandidat kandidat;
  private JLabel labelTitle;
  private JTextField tfName, tfStrasse, tfPLZ, tfOrt, tfGeburtsort, tfTelefon, tfEmail;
  private JComboBox comboGeburtstagTag, comboGeburtstagMonat, comboGeburtstagJahr;
  private JCheckBox cbKursunterlagen, cbPassfoto, cbAnwesend;
  private JButton saveButton;
  private final String SAVE = "update";

  // New Kandidat
  public KandidatDialog(TestToolFrame frame, String title) {
    this(frame, title, null);
  }

  // Update Kandidat
  public KandidatDialog(TestToolFrame frame, String title, Kandidat kandidat) {
    super(frame, title, true);
    this.frame = frame;
    this.kandidat = kandidat;
    setup();
    if (kandidat != null)
      fillValues();
  }

  private void initComponents() {
    saveButton = new JButton(rh.getString(PROPERTY_NAME, BUTTON_SAVE));
    saveButton.addActionListener(this);
    saveButton.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) close();
      }
    });
    saveButton.setActionCommand(SAVE);
    saveButton.setEnabled(false);

    labelTitle = new JLabel(" ");
    Font font = labelTitle.getFont().deriveFont(Font.PLAIN, 21);
    labelTitle.setFont(font);

    tfName = new JTextField(rh.getString(PROPERTY_NAME, FIRST_LAST_NAME), 20);
    tfName.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
          reset();
          storeProps();
          dispose();
        }
      }
    });
    tfName.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        labelTitle.setText(tfName.getText());
      }
    });
    tfStrasse = new JTextField(rh.getString(PROPERTY_NAME, STREET), 20);
    tfPLZ = new JFormattedTextField(new DecimalFormat("####"));
    tfPLZ.setText(rh.getString(PROPERTY_NAME, PLZ));
    tfPLZ.setHorizontalAlignment(JTextField.RIGHT);
    tfOrt = new JTextField(rh.getString(PROPERTY_NAME, CITY), 20);
    tfGeburtsort = new JTextField(rh.getString(PROPERTY_NAME, BIRTHPLACE), 20);
    tfTelefon = new JTextField(rh.getString(PROPERTY_NAME, TELEPHONE), 20);
    tfEmail = new JTextField(rh.getString(PROPERTY_NAME, EMAIl), 20);

    Vector<String> vDays = new Vector<String>();
    for (int i = 1; i <= 31; i++) {
      vDays.add(i + "");
    }
    comboGeburtstagTag = new JComboBox(vDays);
    comboGeburtstagMonat = new JComboBox(rh.getString(PROPERTY_NAME, MONTHS).split(","));
    Vector<String> vYears= new Vector<String>();
    int thisYear = new GregorianCalendar().get(GregorianCalendar.YEAR);
    for (int i = 1900; i <= thisYear-10; i++) {
      vYears.add(i + "");
    }
    comboGeburtstagJahr = new JComboBox(vYears);
    
    cbKursunterlagen = new JCheckBox();
    cbPassfoto = new JCheckBox();
    cbAnwesend = new JCheckBox();

  }

  private void setup() {
    loadProps();
    initComponents();

    FormLayout layout = new FormLayout(
        "6dlu,right:pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu",
        "6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu");
//    GradientPanel gradientPanel = new GradientPanel();
    FormDebugPanel gradientPanel = new FormDebugPanel(layout);
    PanelBuilder builder = new PanelBuilder(layout, gradientPanel);
    CellConstraints cc = new CellConstraints();

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        storeProps();
      }
    });

    // Title
    builder.add(labelTitle, cc.xywh(2, 2, 5, 1));
    // Name
    builder.addSeparator(rh.getString(PROPERTY_NAME, NAME), cc.xywh(2, 4, 7, 1));
    builder.add(new JLabel(rh.getString(PROPERTY_NAME, FIRST_LAST_NAME)), cc.xy(2, 6));
    builder.add(tfName, cc.xywh(4, 6, 5, 1));
    // Strasse
    builder.add(new JLabel(rh.getString(PROPERTY_NAME, STREET)), cc.xy(2, 8));
    builder.add(tfStrasse, cc.xywh(4, 8, 5, 1));
    // PLZ / Ort
    builder.add(new JLabel(rh.getString(PROPERTY_NAME, PLZ_ORT)), cc.xy(2, 10));
    builder.add(tfPLZ, cc.xy(4, 10));
    builder.add(tfOrt, cc.xywh(6, 10, 3, 1));

    // Personal Data
    builder.addSeparator(rh.getString(PROPERTY_NAME, PERSONAL_DATA), cc.xywh(2, 12, 7, 1));
    // Birthday
    builder.add(new JLabel(rh.getString(PROPERTY_NAME, BIRTHDAY)), cc.xy(2,14));
    builder.add(comboGeburtstagTag, cc.xy(4, 14));
    builder.add(comboGeburtstagMonat, cc.xy(6, 14));
    builder.add(comboGeburtstagJahr, cc.xy(8, 14));
    // Birthplace
    builder.add(new JLabel(rh.getString(PROPERTY_NAME, BIRTHPLACE)), cc.xy(2, 16));
    builder.add(tfGeburtsort, cc.xywh(4, 16, 5, 1));
    // Telephone
    builder.add(new JLabel(rh.getString(PROPERTY_NAME, TELEPHONE)), cc.xy(2, 18));
    builder.add(tfTelefon, cc.xywh(4, 18, 5, 1));
    // E-Mail
    builder.add(new JLabel(rh.getString(PROPERTY_NAME, EMAIl)), cc.xy(2, 20));
    builder.add(tfEmail, cc.xywh(4, 20, 5, 1));

    // Test Data
    builder.addSeparator(rh.getString(PROPERTY_NAME, TEST_DATA), cc.xywh(2, 22, 7, 1));
    // Kursunterlagen bezahlt
    builder.addLabel("Kursunterlagen bezahlt", cc.xywh(2, 24, 5, 1));
//    builder.add(cbKursunterlagen, cc.xywh(4, 24, 7, 1));
    // Passfoto
    builder.addLabel("Passfoto vorhanden", cc.xywh(2, 26, 5, 1));
//    builder.add(cbPassfoto, cc.xywh(2, 26, 3, 1));
    // anwesend
    builder.addLabel("anwesend", cc.xywh(2, 28, 5, 1));
//    builder.add(cbAnwesend, cc.xywh(2, 28, 3, 1));

    JPanel buttonOKBar = ButtonBarFactory.buildOKBar(saveButton);
    buttonOKBar.setOpaque(false);
    builder.add(buttonOKBar, cc.xywh(2, 30, 7, 1));
    builder.setBorder(BorderFactory.createLineBorder(Color.black));
//    gradientPanel.add(builder.getPanel());
    getContentPane().add(new JPanel().add(gradientPanel));
    pack();
    setResizable(false);
  }

  private void reset() {
    tfName.setText("");
  }

  public void loadProps() {
    if (props.getProperty(PROPERTY_NAME + "." + POS_X, "").length() > 0) {
      int x = Integer.valueOf(props.getProperty(PROPERTY_NAME + "." + POS_X, ""));
      int y = Integer.valueOf(props.getProperty(PROPERTY_NAME + "." + POS_Y, ""));

      Point p = new Point(x, y);
      setLocation(p);
    }
  }

  private void storeProps() {
    props.setProperty(PROPERTY_NAME + "." + POS_X, getX() + "");
    props.setProperty(PROPERTY_NAME + "." + POS_Y, getY() + "");
  }

  private void update() {
    ((DefaultListModel) ((KandidatPanel) frame.getKandidatPanel()).getKandidatList().getModel()).setElementAt(kandidat, kandidat.getIndex());
  }

  private void save() {
    Kandidat kandidat = new Kandidat(((KandidatPanel) frame.getKandidatPanel()).getKandidatList().getModel().getSize(), tfName.getText(), new Date(1979, 8, 7));
    ((KandidatPanel) frame.getKandidatPanel()).getKandidatList().add(kandidat);
  }

  private void saveOrUpdate() {
    if (kandidat == null)
      save();
    else
      update();
    storeProps();
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
    } else if (OK.equals(e.getActionCommand())) {
      frame.setReturnValue(JOptionPane.OK_OPTION);
      frame.getDialog().dispose();
    } else if (CANCEL.equals(e.getActionCommand())) {
      frame.setReturnValue(JOptionPane.CANCEL_OPTION);
      frame.getDialog().dispose();
    } else if (NO.equals(e.getActionCommand())) {
      frame.setReturnValue(JOptionPane.NO_OPTION);
      frame.getDialog().dispose();
    }
  }

  private void fillValues() {
    // set Name
    tfName.setText(kandidat.getName());
    tfStrasse.setText(kandidat.getStrasse());
    tfPLZ.setText(kandidat.getPLZ() + "");
    tfOrt.setText(kandidat.getOrt());
    tfGeburtsort.setText(kandidat.getGeburtsort());
  }
}
