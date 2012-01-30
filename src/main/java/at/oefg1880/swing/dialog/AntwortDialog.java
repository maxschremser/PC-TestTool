package at.oefg1880.swing.dialog;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.list.Antwort;
import at.oefg1880.swing.list.Fragebogen;
import at.oefg1880.swing.panel.AntwortPanel;
import at.oefg1880.swing.panel.GradientPanel;
import at.oefg1880.swing.text.AntwortTextField;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 14.10.2010
 * Time: 13:21:52
 * To change this template use File | Settings | File Templates.
 */
public class AntwortDialog extends JDialog implements ActionListener, PropertyChangeListener, IConfig {
    TestToolFrame frame;
    JTextField tfName;
    AntwortPanel antwortPanel;
    Fragebogen fragebogen;
    Antwort antwort;
    ButtonGroup bgGeschlecht, bgAlter;
    DefaultPieDataset dataset;
    int correctAnswers = 0;
    JFreeChart chart;
    JButton saveButton;
    JRadioButton rbM, rbW, rb1115, rb1620, rb2130, rb30;

    final String SAVE = "update";
    final String CORRECT = "Richtig";
    final String WRONG = "Falsch";

    final String MASCULIN = "Männlich";
    final String FEMININ = "Weiblich";

    final String _1115 = "11-15";
    final String _1620 = "16-20";
    final String _2130 = "21-30";
    final String _30 = "30+  ";

    // New Answers given by user

    public AntwortDialog(TestToolFrame frame, String title, Fragebogen fragebogen) {
        super(frame, title, true);
        this.frame = frame;
        this.fragebogen = fragebogen;
        setup();
    }

    // Edit Answers given by user

    public AntwortDialog(TestToolFrame frame, String title, Fragebogen fragebogen, Antwort antwort) {
        super(frame.getFragebogenPane().getFragebogenDialog(), title, true);
        this.frame = frame;
        this.fragebogen = fragebogen;
        this.antwort = antwort;
        setup();
        fillValues();
    }

    private void setup() {
        setLocation(((int) frame.getLocation().getX()) + 50, ((int) frame.getLocation().getY()) + 50);
        FormLayout layout = new FormLayout(
                "6dlu,100dlu,6dlu,pref,6dlu,pref,6dlu",
                "6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,12dlu,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu,pref,6dlu");
        GradientPanel gradientPanel = new GradientPanel();
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        saveButton = new JButton("Speichern");
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

        antwortPanel = new AntwortPanel();
        antwortPanel.addPropertyChangeListener(this);

        JLabel labelTitle = new JLabel(fragebogen.getTitle());
        Font font = labelTitle.getFont().deriveFont(Font.PLAIN, 21);
        labelTitle.setFont(font);

        tfName = new JTextField();
        tfName.setSelectionColor(selectedTextForeground);
        tfName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                    reset();
                    dispose();
                }
            }
        });

        bgGeschlecht = new ButtonGroup();
        rbM = new JRadioButton(MASCULIN);
        rbM.setActionCommand(MASCULIN);
        rbM.addActionListener(this);
        rbM.setSelected(true);
        rbM.setMnemonic('m');
        rbW = new JRadioButton(FEMININ);
        rbW.setActionCommand(FEMININ);
        rbW.setMnemonic('w');
        bgGeschlecht.add(rbM);
        bgGeschlecht.add(rbW);
        bgAlter = new ButtonGroup();
        rb1115 = new JRadioButton(_1115);
        rb1115.setActionCommand(_1115);
        rb1115.setSelected(true);
        rb1115.setMnemonic('1');
        rb1620 = new JRadioButton(_1620);
        rb1620.setActionCommand(_1620);
        rb1620.setMnemonic('2');
        rb2130 = new JRadioButton(_2130);
        rb2130.setActionCommand(_2130);
        rb2130.setMnemonic('3');
        rb30 = new JRadioButton(_30);
        rb30.setActionCommand(_30);
        rb30.setMnemonic('4');
        bgAlter.add(rb1115);
        bgAlter.add(rb1620);
        bgAlter.add(rb2130);
        bgAlter.add(rb30);
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
        builder.addSeparator("Name", cc.xywh(2, 4, 3, 1));
        builder.add(tfName, cc.xywh(2, 6, 3, 1));
        builder.addSeparator("Geschlecht", cc.xywh(2, 8, 3, 1));
        builder.add(rbM, cc.xy(2, 10));
        builder.add(rbW, cc.xy(4, 10));
        builder.addSeparator("Alter", cc.xywh(2, 12, 3, 1));
        builder.add(rb1115, cc.xy(2, 14));
        builder.add(rb1620, cc.xy(4, 14));
        builder.add(rb2130, cc.xy(2, 16));
        builder.add(rb30, cc.xy(4, 16));
        builder.addSeparator("Antworten", cc.xywh(2, 18, 5, 1));
        builder.add(antwortPanel, cc.xywh(2, 20, 5, 1));
        builder.add(saveButton, cc.xy(6, 22));
        builder.add(chartPanel, cc.xywh(6, 4, 1, 13));

        builder.setBorder(BorderFactory.createLineBorder(Color.black));
        gradientPanel.add(builder.getPanel());
        getContentPane().add(gradientPanel);
        pack();
        setResizable(false);
    }

    private void reset() {
        correctAnswers = 0;
        tfName.setText("");
        rbM.setSelected(true);
        rb1115.setSelected(true);
        dataset.setValue(CORRECT, 0);
        dataset.setValue(WRONG, 100);
        antwortPanel.reset();
    }

    private void setGeschlechtSelected(String sex) {
        if (sex.length() <= 0) return;
        if (sex.equals(MASCULIN)) rbM.setSelected(true);
        else rbW.setSelected(true);
    }

    private void update() {
        String sGeschlecht = bgGeschlecht.getSelection().getActionCommand();
        String sAlter = bgAlter.getSelection().getActionCommand();
        antwort.setName(tfName.getText());
        antwort.setAlter(sAlter);
        antwort.setGeschlecht(sGeschlecht);
        antwort.setPercentages(dataset.getValue(1).intValue());
        antwort.setAnswers(antwortPanel.getValues());
        fragebogen.setAntwort(antwort);
        ((DefaultListModel) frame.getFragebogenPane().getFragebogenDialog().
                getAntwortList().getModel()).setElementAt(antwort, antwort.getIndex());
    }

    private void save() {
        String sGeschlecht = bgGeschlecht.getSelection().getActionCommand();
        String sAlter = bgAlter.getSelection().getActionCommand();
        Antwort antwort = new Antwort(fragebogen.getSolved(), tfName.getText(), sAlter,
                sGeschlecht, dataset.getValue(1).intValue(), antwortPanel.getValues());
        fragebogen.addAntwort(antwort);
    }

    private void saveOrUpdate() {
        if (antwort == null)
            save();
        else
            update();
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
        // setSex
        if (antwort.getGeschlecht().equals(MASCULIN)) setGeschlechtSelected(MASCULIN);
        else setGeschlechtSelected(FEMININ);
        // setAge
        if (antwort.getAlter().equals(_1115)) rb1115.setSelected(true);
        else if (antwort.getAlter().equals(_1620)) rb1620.setSelected(true);
        else if (antwort.getAlter().equals(_2130)) rb2130.setSelected(true);
        else if (antwort.getAlter().equals(_30)) rb30.setSelected(true);
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
                dataset.setValue(CORRECT, correctAnswers * 5);
                dataset.setValue(WRONG, (20 - correctAnswers) * 5);
                chart.setTitle(correctAnswers * 5 + "%");
//                if (antwortPanel.isFullyFilled())
                if (index == (solutions.length-1)) {
                    saveButton.setEnabled(true);
                    saveButton.requestFocus();
                }

            }
        }
    }
}
