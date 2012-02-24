package at.oefg1880.swing.dialog;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.document.FilterTextDocument;
import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.list.AntwortList;
import at.oefg1880.swing.list.Fragebogen;
import at.oefg1880.swing.list.FragebogenList;
import at.oefg1880.swing.panel.AntwortPanel;
import at.oefg1880.swing.panel.GradientPanel;
import at.oefg1880.swing.utils.PropertyHandler;
import at.oefg1880.swing.utils.ResourceHandler;
import com.jgoodies.forms.builder.PanelBuilder;
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
  public final String PROPERTY_NAME = "at.oefg1880.swing.dialog.FragebogenDialog";
  protected ResourceHandler rh = ResourceHandler.getInstance();
  private PropertyHandler props = PropertyHandler.getInstance();
  protected TestToolFrame frame;
  protected Fragebogen fragebogen;
  protected AntwortList list;
  private JTextField textFieldName;
  private JTextField spinnerEditorField;
  private JSpinner spinner;
  private AntwortPanel answerPanel;
  private PanelBuilder builder;
  private JButton button;
  private final String SAVE = "save";
  private final String UPDATE = "update";
  private final Logger log = Logger.getLogger(FragebogenDialog.class);

  public abstract AntwortPanel getAntwortPanel();

  public abstract AntwortList getAntwortList();

  public FragebogenDialog(TestToolFrame frame, String title) {
    super(frame, title, true);
    this.frame = frame;
    FormLayout layout = new FormLayout(
        "6dlu,pref,6dlu,64dlu,6dlu",
        "6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu");

    builder = new PanelBuilder(layout);
    setup();
  }

  public FragebogenDialog(TestToolFrame frame, String title, Fragebogen fragebogen) {
    super(frame, title, true);
    this.frame = frame;
    FormLayout layout;
    if (fragebogen != null && fragebogen.getSolved() > 0) {
      layout = new FormLayout(
          "6dlu,pref,6dlu,64dlu,6dlu",
          "6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu");
    } else {
      layout = new FormLayout(
          "6dlu,pref,6dlu,64dlu,6dlu",
          "6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu");
    }

    loadProps();

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        storeProps();
      }
    });

    builder = new PanelBuilder(layout);
    this.fragebogen = fragebogen;
    setup();
    answerPanel.setValues(fragebogen.getSolutions());
    textFieldName.setText(fragebogen.getTitle());
    spinner.setValue(fragebogen.getExisting());

    button.setActionCommand(UPDATE);
    button.setEnabled(true);
    getAntwortList().requestFocus();
  }

  public void loadProps() {
    if (props.getProperty(PROPERTY_NAME + "." + POS_X, "").length() > 0) {
      int x = Integer.valueOf(props.getProperty(PROPERTY_NAME + "." + POS_X, ""));
      int y = Integer.valueOf(props.getProperty(PROPERTY_NAME + "." + POS_Y, ""));

      Point p = new Point(x, y);
      setLocation(p);
    }
  }

  private void setup() {
    GradientPanel gradientPanel = new GradientPanel();
    CellConstraints cc = new CellConstraints();

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
    button = new JButton(rh.getString(getClass(), BUTTON_SAVE));
    button.addActionListener(this);
    button.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) saveOrUpdate();
//        else if (e.getKeyChar() == KeyEvent.VK_SPACE) saveOrUpdate();
      }
    });
    button.setActionCommand(SAVE);
    button.setEnabled(false);
    answerPanel = getAntwortPanel();

    builder.addSeparator(rh.getString(getClass(), LABEL_FRAGEBOGEN), cc.xywh(2, 2, 3, 1));
    builder.addLabel(rh.getString(getClass(), LABEL_NAME), cc.xy(2, 4));
    builder.addLabel(rh.getString(getClass(), QUESTION_FRAGEBOGEN), cc.xy(2, 6));
    builder.add(textFieldName, cc.xy(4, 4));
    builder.add(spinner, cc.xy(4, 6));
    builder.addSeparator(rh.getString(getClass(), LABEL_SOLUTION), cc.xywh(2, 8, 3, 1));
    builder.add(answerPanel, cc.xywh(2, 10, 3, 1));
    builder.add(button, cc.xy(4, 12));

    if (fragebogen != null && fragebogen.getSolved() > 0) {
      // add Antworten to Table
      builder.addSeparator(rh.getString(getClass(), LABEL_ANSWER), cc.xywh(2, 14, 3, 1));
      JScrollPane scrollPane = new JScrollPane(getAntwortList(),
          JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
          JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      scrollPane.setPreferredSize(new Dimension((int) getWidth(), 150));
      builder.add(scrollPane, cc.xywh(2, 16, 3, 1));
    }

    builder.setBorder(BorderFactory.createLineBorder(Color.black));
    gradientPanel.add(builder.getPanel());
    getContentPane().add(gradientPanel);
    pack();
    setResizable(true);
  }


  private void reset() {
    fragebogen = null;
    textFieldName.setText("");
    spinner.setValue(5);
    answerPanel.reset();
  }

  private void storeProps() {
    props.setProperty(PROPERTY_NAME + "." + POS_X, getX() + "");
    props.setProperty(PROPERTY_NAME + "." + POS_Y, getY() + "");
  }

  private void saveOrUpdate() {
    if (SAVE.equals(button.getActionCommand())) {
      save();
      storeProps();
    } else {
      if (UPDATE.equals(button.getActionCommand())) {
        update();
      }
    }
  }

  private void save() {
    FragebogenList list = frame.getFragebogenPanel().getFragebogenList();
    list.add(
        textFieldName.getText(),
        Integer.valueOf(spinner.getValue().toString()),
        answerPanel.getValues()
    );
    log.info("Added item '" + textFieldName.getText() + "' to list.");
    if (frame.getFragebogenPanel().getFragebogenList().getModel().getSize() > 0) {
      frame.getFragebogenPanel().getButtonSave().setEnabled(true);
    }
    close();
  }

  private void update() {
    fragebogen.setTitle(textFieldName.getText());
    fragebogen.setExisting(Integer.valueOf(spinner.getValue().toString()));
    fragebogen.setSolutions(answerPanel.getValues());
    FragebogenList list = frame.getFragebogenPanel().getFragebogenList();
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
}
