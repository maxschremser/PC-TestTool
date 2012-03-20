package at.oefg1880.swing.dialog;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.document.FilterTextDocument;
import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.list.AntwortList;
import at.oefg1880.swing.list.Fragebogen;
import at.oefg1880.swing.list.FragebogenList;
import at.oefg1880.swing.panel.AntwortPanel;
import at.oefg1880.swing.panel.FragebogenPanel;
import at.oefg1880.swing.panel.GradientPanel;
import at.oefg1880.swing.utils.PropertyHandler;
import at.oefg1880.swing.utils.ResourceHandler;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 14.10.2010
 * Time: 13:21:52
 * To change this template use File | Settings | File Templates.
 */
public abstract class FragebogenDialog extends JDialog implements ActionListener, IConfig, ITexts {
  public final static String PROPERTY_NAME = "at.oefg1880.swing.dialog.FragebogenDialog";
  protected ResourceHandler rh = ResourceHandler.getInstance();
  private PropertyHandler props = PropertyHandler.getInstance();
  protected TestToolFrame frame;
  protected Fragebogen fragebogen;
  protected AntwortList list;
  private JTextField textFieldName;
  private JTextField spinnerEditorField;
  private JSpinner spinner;
  private AntwortPanel answerPanel;
  private JButton button;
  private final String SAVE = "save";
  private final String UPDATE = "update";
  private final Logger log = Logger.getLogger(FragebogenDialog.class);

  public abstract AntwortPanel getAntwortPanel();

  public abstract AntwortList getAntwortList();

  // New Fragebogen
  public FragebogenDialog(TestToolFrame frame, String title) {
    this(frame, title, null);
  }

  // Update Fragebogen
  public FragebogenDialog(TestToolFrame frame, String title, Fragebogen fragebogen) {
    super(frame, title, true);
    this.frame = frame;
    this.fragebogen = fragebogen;
    
    setup();
    
    if (fragebogen != null) 
      fillValues();
  }
  
  private void initComponents() {
    textFieldName = new JTextField();
    textFieldName.setSelectionColor(selectedTextForeground);
    textFieldName.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
          reset();
          storeProps();
          dispose();
        }
      }
    });
    textFieldName.setDocument(new FilterTextDocument());
    int minValue = 1;
    if (fragebogen != null) {
      minValue = fragebogen.getSolved();
    }
    spinner = new JSpinner(new SpinnerNumberModel(5, minValue, 100, 1));
    spinnerEditorField = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
    spinnerEditorField.setSelectionColor(selectedTextForeground);
    spinnerEditorField.addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(FocusEvent e) {
        if (spinnerEditorField.hasFocus()) {
          String sText = spinnerEditorField.getText();
          spinnerEditorField.setText(sText);
          spinnerEditorField.setCaretPosition(0);
          spinnerEditorField.moveCaretPosition(spinnerEditorField.getText().length());
        }
      }
    });
    button = new JButton(rh.getString(PROPERTY_NAME, BUTTON_SAVE));
    button.setActionCommand(SAVE);
    button.setEnabled(false);
    button.addActionListener(this);
    button.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) saveOrUpdate();
//        else if (e.getKeyChar() == KeyEvent.VK_SPACE) saveOrUpdate();
      }
    });

    answerPanel = getAntwortPanel();
  }
  
  private JPanel buildPanel() {
    loadProps();

    initComponents();

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        storeProps();
      }
    });

    getAntwortList().requestFocus();

    FormLayout layout;
    if (fragebogen != null && fragebogen.getSolved() > 0) {
      layout = new FormLayout(
          "6dlu,pref,6dlu,64dlu",
          "6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref");
    } else {
      layout = new FormLayout(
          "6dlu,pref,6dlu,64dlu",
          "6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref");
    }

    JPanel panel = new GradientPanel();
//    FormDebugPanel panel = new FormDebugPanel(layout);
    PanelBuilder builder = new PanelBuilder(layout, panel);
    CellConstraints cc = new CellConstraints();

    builder.addSeparator(rh.getString(PROPERTY_NAME, LABEL_FRAGEBOGEN), cc.xywh(2, 2, 3, 1));
    builder.addLabel(rh.getString(PROPERTY_NAME, LABEL_NAME), cc.xy(2, 4));
    builder.addLabel(rh.getString(PROPERTY_NAME, QUESTION_FRAGEBOGEN), cc.xy(2, 6));
    builder.add(textFieldName, cc.xy(4, 4));
    builder.add(spinner, cc.xy(4, 6));
    builder.addSeparator(rh.getString(PROPERTY_NAME, LABEL_SOLUTION), cc.xywh(2, 8, 3, 1));
    builder.add(answerPanel, cc.xywh(2, 10, 3, 1));
    builder.add(button, cc.xy(4, 12));

    if (fragebogen != null && fragebogen.getSolved() > 0) {
      // add Antworten to Table
      builder.addSeparator(rh.getString(PROPERTY_NAME, LABEL_ANSWER), cc.xywh(2, 14, 3, 1));
      JScrollPane scrollPane = new JScrollPane(getAntwortList(),
          JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
          JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      scrollPane.setPreferredSize(new Dimension((int) getWidth(), 150));
      builder.add(scrollPane, cc.xywh(2, 16, 3, 1));
    }

    builder.setBorder(BorderFactory.createLineBorder(Color.black));

    return builder.getPanel();
  }
  
  private void setup() {
    getContentPane().add(buildPanel());
    pack();
    setResizable(false);
  }

  private void reset() {
    fragebogen = null;
    textFieldName.setText("");
    spinner.setValue(5);
    answerPanel.reset();
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

  private void saveOrUpdate() {
    if (fragebogen == null) {
      save();
    } else {
      update();
    }
    storeProps();
  }

  private void save() {
    FragebogenList list = ((FragebogenPanel) frame.getFragebogenPanel()).getFragebogenList();
    list.add(
        textFieldName.getText(),
        Integer.valueOf(spinner.getValue().toString()),
        answerPanel.getValues()
    );
    log.info("Added item '" + textFieldName.getText() + "' to list.");
    if (((FragebogenPanel) frame.getFragebogenPanel()).getFragebogenList().getModel().getSize() > 0) {
      frame.enableButtonSave(true);
      frame.enableMenuItemSave(true);
    }
    close();
  }

  private void update() {
    fragebogen.setTitle(textFieldName.getText());
    fragebogen.setExisting(Integer.valueOf(spinner.getValue().toString()));
    fragebogen.setSolutions(answerPanel.getValues());
    FragebogenList list = ((FragebogenPanel) frame.getFragebogenPanel()).getFragebogenList();
    list.update(fragebogen);
    close();
  }

  private void close() {
    reset();
    dispose();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (SAVE.equals(e.getActionCommand())) {
      save();
    } else if (UPDATE.equals(e.getActionCommand())) {
      update();
    }
  }
  
  private void fillValues() {
    answerPanel.setValues(fragebogen.getSolutions());
    textFieldName.setText(fragebogen.getTitle());
    spinner.setValue(fragebogen.getExisting());
    button.setActionCommand(UPDATE);
    button.setEnabled(true);
  }
  
  public JButton getSaveButton() {
    return button;
  }
}
