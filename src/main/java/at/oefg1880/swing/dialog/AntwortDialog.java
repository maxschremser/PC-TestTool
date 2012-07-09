package at.oefg1880.swing.dialog;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.io.Antwort;
import at.oefg1880.swing.io.Fragebogen;
import at.oefg1880.swing.io.Kandidat;
import at.oefg1880.swing.list.FilterKandidatTable;
import at.oefg1880.swing.panel.AntwortPanel;
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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

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
    private FilterKandidatTable tfName;
    private AntwortPanel antwortPanel;
    protected Fragebogen fragebogen;
    private Antwort antwort;
    private DefaultPieDataset dataset;
    private int correctAnswers = 0;
    private JFreeChart chart;
    private JButton saveButton;
    protected AntwortPanel panel;
    private JLabel labelTitle;

    private final static String SAVE = "update";
    private final String CORRECT = rh.getString(PROPERTY_NAME, GRAPH_CORRECT);
    private final String WRONG = rh.getString(PROPERTY_NAME, GRAPH_WRONG);
    private ChartPanel chartPanel;

    public abstract AntwortPanel getAntwortPanel(AntwortDialog dialog);

    // New Answers given by user

    public AntwortDialog(TestToolFrame frame, String title, Fragebogen fragebogen) {
        super(frame, title, true);
        this.frame = frame;
        this.fragebogen = fragebogen;
        setup();
    }

    // Edit Answers given by user

    public AntwortDialog(TestToolFrame frame, String title, Fragebogen fragebogen, Antwort antwort) {
        super(frame.getFragebogenPanel().getFragebogenDialog(), title, true);
        this.frame = frame;
        this.fragebogen = fragebogen;
        this.antwort = antwort;
        setup();
        fillValues();
    }

    private void initComponents() {
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

        antwortPanel = getAntwortPanel(this);
        antwortPanel.addPropertyChangeListener(this);

        labelTitle = new JLabel(fragebogen.getTitle());
        Font font = labelTitle.getFont().deriveFont(Font.PLAIN, 21);
        labelTitle.setFont(font);

        // if Kandidat has given answer, don't add him to the list of objects
        ArrayList<Kandidat> objects = new ArrayList<Kandidat>();
        ArrayList<Kandidat> vKandidat = frame.getKandidatPanel().getKandidatTable().getModel().getItems();
        Kandidat kandidat = null;
        Iterator<Kandidat> iterKandidat = vKandidat.iterator();
        while (iterKandidat.hasNext()) {
            boolean bFound = false;
            kandidat = iterKandidat.next();
            Enumeration enumFragebogen = ((DefaultListModel) frame.getFragebogenPanel().getFragebogenList().getModel()).elements();
            while (enumFragebogen.hasMoreElements()) {
                Fragebogen fb = (Fragebogen) enumFragebogen.nextElement();
                Iterator<Antwort> iter = fb.getAntworten().iterator();
                while (iter.hasNext()) {
                    String name = iter.next().getName();
                    if (name.equals(kandidat.getName())) {
                        bFound = !bFound;
                        break;
                    }
                }
            }
            if (!bFound)
                objects.add(kandidat);
        }


        tfName = new FilterKandidatTable(frame, objects);
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
        chartPanel = new ChartPanel(chart);
        chartPanel.setOpaque(true);
        chartPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        chartPanel.setPreferredSize(new Dimension(200, 200));
    }

    private void setup() {
        getContentPane().add(buildPanel());
        pack();
        setResizable(false);
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

        FormLayout layout = new FormLayout(
                "6dlu,100dlu,6dlu,pref,6dlu,pref",
                "6dlu,pref,6dlu,pref,6dlu,pref,6dlu,86dlu,6dlu,pref,6dlu,pref,6dlu,12dlu");
        PanelBuilder builder = new PanelBuilder(layout, new GradientPanel());
        CellConstraints cc = new CellConstraints();

        builder.add(labelTitle, cc.xywh(2, 2, 5, 1));
        builder.addSeparator(rh.getString(PROPERTY_NAME, NAME), cc.xywh(2, 4, 3, 1));
        builder.add(tfName.getFilterField(), cc.xywh(2, 6, 3, 1));
        builder.add(new JScrollPane(tfName, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED), cc.xywh(2, 8, 3, 1));
        builder.addSeparator(rh.getString(PROPERTY_NAME, ANSWERS), cc.xywh(2, 10, 5, 1));
        builder.add(antwortPanel, cc.xywh(2, 12, 5, 1));
        builder.add(saveButton, cc.xy(6, 14));
        builder.add(chartPanel, cc.xywh(6, 6, 1, 3));

        builder.setBorder(BorderFactory.createLineBorder(Color.black));
        return builder.getPanel();
    }

    private void reset() {
        correctAnswers = 0;
        tfName.getFilterField().setText("");
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
//        antwort.setKandidat((Kandidat) tfName.getSelectedValue());
        antwort.setPercentages(dataset.getValue(1).intValue());
        antwort.setAnswers(antwortPanel.getValues());
//        fragebogen.setAntwort(antwort);
//        ((DefaultListModel)frame.getFragebogenPanel().getFragebogenDialog().
//                getAntwortList().getModel()).setElementAt(antwort, antwort.getIndex());
    }

    private void save() {
        Antwort antwort = new Antwort((Kandidat) tfName.getModel().getValueAt(0, 0), dataset.getValue(1).intValue(), antwortPanel.getValues());
        fragebogen.addAntwort(antwort);

        ((Kandidat) tfName.getModel().getValueAt(0, 0)).setAntwort(fragebogen, antwort);
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
        tfName.getValueAt(0, 0);
        antwortPanel.setValues(antwort.getAnswers());
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public boolean hasKandidat() {
        return tfName.getValueAt(0, 0) != null || antwort.getKandidat() != null;
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
                if (antwortPanel.isFullyFilled(fragebogen == null) && hasKandidat()) {
                    if (index == (solutions.length - 1)) {
                        saveButton.setEnabled(true);
                        saveButton.requestFocus();
                    }
                }
            }
        }
    }
}
