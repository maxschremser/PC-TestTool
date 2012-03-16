package at.oefg1880.swing.dialog;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.list.Antwort;
import at.oefg1880.swing.list.FilteredList;
import at.oefg1880.swing.list.Fragebogen;
import at.oefg1880.swing.list.Kandidat;
import at.oefg1880.swing.panel.AntwortPanel;
import at.oefg1880.swing.panel.FragebogenPanel;
import at.oefg1880.swing.panel.GradientPanel;
import at.oefg1880.swing.text.AntwortTextField;
import at.oefg1880.swing.utils.PropertyHandler;
import at.oefg1880.swing.utils.ResourceHandler;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 14.10.2010
 * Time: 13:21:52
 * To change this template use File | Settings | File Templates.
 */
public abstract class AntwortDialog extends JDialog implements ActionListener, PropertyChangeListener, IConfig, ITexts {
  public final static String PROPERTY_NAME = "at.oefg1880.swing.dialog.AntwortDialog";
  private ResourceHandler rh = ResourceHandler.getInstance();
  private PropertyHandler props = PropertyHandler.getInstance();
  private TestToolFrame frame;
  private FilteredList tfName;
  private AntwortPanel antwortPanel;
  protected Fragebogen fragebogen;
  private Antwort antwort;
  private DefaultPieDataset dataset;
  private int correctAnswers = 0;
  private JFreeChart chart;
  private JButton saveButton;

  private final String SAVE = "update";
  private final String CORRECT = rh.getString(PROPERTY_NAME, GRAPH_CORRECT);
  private final String WRONG = rh.getString(PROPERTY_NAME, GRAPH_WRONG);

  public abstract AntwortPanel getAntwortPanel();

  // New Answers given by user

  public AntwortDialog(TestToolFrame frame, String title, Fragebogen fragebogen) {
    super(frame, title, true);
    this.frame = frame;
    this.fragebogen = fragebogen;
    setup();
  }

  // Edit Answers given by user

  public AntwortDialog(TestToolFrame frame, String title, Fragebogen fragebogen, Antwort antwort) {
    super(((FragebogenPanel) frame.getFragebogenPanel()).getFragebogenDialog(), title, true);
    this.frame = frame;
    this.fragebogen = fragebogen;
    this.antwort = antwort;
    setup();
    fillValues();
  }

  private void setup() {
    FormLayout layout = new FormLayout(
        "6dlu,100dlu,6dlu,pref,6dlu,pref,6dlu",
        "6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,12dlu,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu");
    GradientPanel gradientPanel = new GradientPanel();
    PanelBuilder builder = new PanelBuilder(layout);
    CellConstraints cc = new CellConstraints();

    loadProps();

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        storeProps();
      }
    });

    saveButton = new JButton(rh.getString(PROPERTY_NAME, BUTTON_SAVE));
    saveButton.addActionListener(this);
    saveButton.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) close();
//                else if (e.getKeyChar() == KeyEvent.VK_SPACE) close();
      }
    });
    saveButton.setActionCommand(SAVE);
    saveButton.setEnabled(false);

    antwortPanel = getAntwortPanel();
    antwortPanel.addPropertyChangeListener(this);

    JLabel labelTitle = new JLabel(fragebogen.getTitle());
    Font font = labelTitle.getFont().deriveFont(Font.PLAIN, 21);
    labelTitle.setFont(font);

    Vector<Kandidat> v = new Vector<Kandidat>();
    v.add(new Kandidat(0, "Maximilian", new Date(1979, 8, 7)));
    v.add(new Kandidat(0, "Michael", new Date(1975, 2, 12)));
    v.add(new Kandidat(0, "Markus", new Date(1972, 6, 23)));
    tfName = new FilteredList(v);
    tfName.setSelectionColor(selectedTextForeground);
    tfName.setBorder(new LineBorder(Color.black));
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

    dataset = new DefaultPieDataset();
    dataset.setValue(WRONG, 100);
    dataset.setValue(CORRECT, 0);
    chart = ChartFactory.createPieChart("", dataset, true, false, false);
    chart.setTitle("0%");
    PiePlot plot = (PiePlot) chart.getPlot();
    plot.setForegroundAlpha(0.7f);
    ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setOpaque(true);
    chartPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    chartPanel.setPreferredSize(new Dimension(200, 200));

    builder.add(labelTitle, cc.xywh(2, 2, 5, 1));
    builder.addSeparator(rh.getString(PROPERTY_NAME, NAME), cc.xywh(2, 4, 3, 1));
    builder.add(tfName.getFilterField(), cc.xywh(2, 6, 3, 1));
    builder.add(new JScrollPane(tfName, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), cc.xywh(2, 8, 3, 1));
    builder.addSeparator(rh.getString(PROPERTY_NAME, ANSWERS), cc.xywh(2, 10, 5, 1));
    builder.add(antwortPanel, cc.xywh(2, 12, 5, 1));
    builder.add(saveButton, cc.xy(6, 14));
    builder.add(chartPanel, cc.xywh(6, 6, 1, 3));

    builder.setBorder(BorderFactory.createLineBorder(Color.black));
    gradientPanel.add(builder.getPanel());
    getContentPane().add(gradientPanel);
    pack();
    setResizable(false);
  }

  private void reset() {
    correctAnswers = 0;
    tfName.setText("");
    dataset.setValue(CORRECT, 0);
    dataset.setValue(WRONG, 100);
    antwortPanel.reset();
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
    antwort.setKandidat((Kandidat) tfName.getSelectedValue());
    antwort.setPercentages(dataset.getValue(1).intValue());
    antwort.setAnswers(antwortPanel.getValues());
    fragebogen.setAntwort(antwort);
    ((DefaultListModel) ((FragebogenPanel) frame.getFragebogenPanel()).getFragebogenDialog().
        getAntwortList().getModel()).setElementAt(antwort, antwort.getIndex());
  }

  private void save() {
    Antwort antwort = new Antwort(fragebogen.getAntworten().size(), (Kandidat) tfName.getSelectedValue(), dataset.getValue(1).intValue(), antwortPanel.getValues());
    fragebogen.addAntwort(antwort);
  }

  private void saveOrUpdate() {
    if (antwort == null)
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
    tfName.setText(antwort.getName());
    antwortPanel.setValues(antwort.getAnswers());
  }

  @Override
  /*
 handles the answers inserted into the answertextfield on the answerpanel
  */
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().startsWith(AntwortTextField.VALUE_CHANGED)) {
      correctAnswers = 0;
      if (fragebogen != null) {
        int[] solutions = fragebogen.getSolutions();
        int[] answers = antwortPanel.getValues();
        String propName = evt.getPropertyName();
        int index = Integer.valueOf(propName.substring(AntwortTextField.VALUE_CHANGED.length(), propName.length()));
        AntwortTextField atf = antwortPanel.getAntwortTextField(index);
        for (int i = 0; i < solutions.length; i++) {
          if (answers[i] == solutions[i]) {
            correctAnswers++;
            if (index == i) {
              atf.setOldColor(Color.green);
              atf.setBackground(Color.green);
            }
          } else {
            if (index == i) {
              atf.setOldColor(Color.red);
              atf.setBackground(Color.red);
            }
          }
        }
        int iNumAnswers = antwortPanel.getNumAnswers();
        int iPercentage = 100 / iNumAnswers;
        dataset.setValue(CORRECT, correctAnswers * iPercentage);
        dataset.setValue(WRONG, (iNumAnswers - correctAnswers) * iPercentage);
        chart.setTitle(correctAnswers * iPercentage + "%");
        // if is in create mode then ' ' is not allowed
        if (antwortPanel.isFullyFilled(fragebogen == null)) {
          if (index == (solutions.length - 1)) {
            saveButton.setEnabled(true);
            saveButton.requestFocus();
          }
        }
      }
    }
  }
}
