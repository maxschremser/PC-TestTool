package at.oefg1880.swing.list;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.io.Fragebogen;
import at.oefg1880.swing.io.Kandidat;
import at.oefg1880.swing.panel.GradientPanel;
import at.oefg1880.swing.panel.KandidatPanel;
import at.oefg1880.swing.utils.ResourceHandler;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * !!! MAKE IT TO JTABLE !!!
 * http://pekalicious.com/blog/custom-jpanel-cell-with-jbuttons-in-jtable/
 *
 *
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 12.10.2010
 * Time: 14:32:28
 * To change this template use File | Settings | File Templates.
 */
public class KandidatTable extends JTable implements IConfig, ITexts {
    public final static String PROPERTY_NAME = "at.oefg1880.swing.list.KandidatTable";
    private ResourceHandler rh = ResourceHandler.getInstance();

    private static Color listForeground, listBackground, listSelectionForeground, listSelectionBackground;
//    private AbstractTableModel model;
    private TestToolFrame frame;
    private ArrayList<Kandidat> items;
    private static Logger log = Logger.getLogger(FragebogenList.class);

    static {
        UIDefaults uid = UIManager.getLookAndFeel().getDefaults();
        listBackground = uid.getColor("List.background");
        listForeground = uid.getColor("List.foreground");
        listSelectionBackground = new Color(24, 49, 73);
        listSelectionForeground = new Color(255, 255, 255);
    }

    public KandidatTable(TestToolFrame frame) {
        this(frame, new ArrayList<Kandidat>());
    }

    public KandidatTable(TestToolFrame frame, ArrayList<Kandidat> items) {
        super();
        this.frame = frame;
        this.items = items;
        setup();
    }

    private void setup() {
        setModel(new KandidatTableModel(items));
//        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setDefaultRenderer(Kandidat.class, new KandidatCell());
        setDefaultEditor(Kandidat.class, new KandidatCell());
        setRowHeight(120);

//        addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                // open KandidatDialog
//                menu.setVisible(false);
//                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
//                    ((KandidatPanel) frame.getKandidatPanel()).editKandidatDialog((Kandidat) getSelectedValue());
//                } else if (SwingUtilities.isRightMouseButton(e) && !isSelectionEmpty() &&
//                        locationToIndex(e.getPoint()) == getSelectedIndex()) {
//                    // right click, open edit menu
//                    menu.show((JList) e.getSource(), e.getX(), e.getY());
//                } else if (SwingUtilities.isLeftMouseButton(e)) {
////                    int index = locationToIndex(e.getPoint());
//                    if (e.getPoint().x > checkBoxAnwesend.getLocation().x &&
//                            e.getPoint().x < (checkBoxAnwesend.getLocation().x + checkBoxAnwesend.getPreferredSize().width) &&
//                        e.getPoint().y > checkBoxAnwesend.getLocation().y &&
//                            e.getPoint().y < (checkBoxAnwesend.getLocation().y + checkBoxAnwesend.getPreferredSize().height))
//                    checkBoxAnwesend.setSelected(!checkBoxAnwesend.isSelected());
//                }
//            }
//        });

//        addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                super.keyPressed(e);
//                int key = e.getKeyCode();
//                // 49=1, 50=2,...57=9   on keyboard
//                // 97=1, 98=2,...105=9  on numpad
//                if (key == KeyEvent.VK_ENTER) {            // handle enter key pressed
//                    // open KandidatDialog
//                    if (getSelectedIndex() >= 0)
//                        ((KandidatPanel) frame.getKandidatPanel()).editKandidatDialog((Kandidat) getSelectedValue());
//                } else if (key == KeyEvent.VK_N) {
//                    ((KandidatPanel) frame.getKandidatPanel()).getButtonNew().doClick();
//                } else if (key == KeyEvent.VK_S) {
//                    if (((KandidatPanel) frame.getKandidatPanel()).getButtonSave().isEnabled()) {
//                        ((KandidatPanel) frame.getKandidatPanel()).getButtonSave().doClick();
//                    }
//                } else if (key == KeyEvent.VK_DELETE) {
//                    String title = ((Fragebogen) getSelectedValue()).getTitle();
//                    int n = frame.showDeleteDialog(((KandidatPanel) frame.getKandidatPanel()).getKandidatTable(), rh.getString(PROPERTY_NAME, QUESTION_DELETE, new String[]{title}), rh.getString(PROPERTY_NAME, DELETE));
//                    if (n == JOptionPane.OK_OPTION) // JA
//                        model.remove(getSelectedIndex());
//                } else {
//                    int index = (key >= 49 && key <= 57) ? key - 49 :
//                            (key >= 97 && key <= 105) ? key - 97 : -1;
//                    if (index >= 0 && index <= 9) {
//                        setSelectedIndex(index);
//                        ensureIndexIsVisible(index);
//                    }
//                }
//            }
//        });

//        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        setModel(model);
//        setCellRenderer(new KandidatRenderer());

    }

    public void add(String title, String name, String strasse, int PLZ, String ort, String telephone, String email, Date geburtstag, String geburtsort) {
        Kandidat kandidat = new Kandidat(title, name, strasse, PLZ, ort, telephone, email, geburtstag, geburtsort);
        add(kandidat);
    }

    public void add(Kandidat kandidat) {
        items.add(kandidat);
        ((AbstractTableModel)getModel()).fireTableDataChanged();
        repaint();
    }

    public void update(Kandidat kandidat) {
        for (Kandidat item : items) {
            if (kandidat.getIndex() == item.getIndex()) {
                items.set(kandidat.getIndex(), kandidat);
            }
        }
    }

//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == menuEdit) {
//            ((KandidatPanel) frame.getKandidatPanel()).editKandidatDialog((Kandidat) getSelectedValue());
//        } else if (e.getSource() == menuDelete) {
//            String title = ((Kandidat) getSelectedValue()).getName();
//            int n = frame.showDeleteDialog(this, rh.getString(PROPERTY_NAME, QUESTION_DELETE, new String[]{title}), rh.getString(PROPERTY_NAME, DELETE));
//            if (n == 0) // JA
//                model.remove(getSelectedIndex());
//            if (model.getSize() <= 0)
//                frame.enableButtonSave(false);
//        } else if (OK.equals(e.getActionCommand())) {
//            frame.setReturnValue(JOptionPane.OK_OPTION);
//            frame.getDialog().dispose();
//        } else if (CANCEL.equals(e.getActionCommand())) {
//            frame.setReturnValue(JOptionPane.CANCEL_OPTION);
//            frame.getDialog().dispose();
//        } else if (NO.equals(e.getActionCommand())) {
//            frame.setReturnValue(JOptionPane.NO_OPTION);
//            frame.getDialog().dispose();
//        }
//    }

    private class KandidatTableModel extends AbstractTableModel {
        ArrayList<Kandidat> items;

        public KandidatTableModel(ArrayList<Kandidat> items) {
            this.items = items;
        }

        @Override
        public int getRowCount() {
            return (items == null) ? 0 : items.size();
        }

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return (items == null) ? null : items.get(rowIndex);
        }

        @Override
        public String getColumnName(int column) {
            return "Kandidaten";
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return Kandidat.class;
        }
    }

    private class KandidatCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
        private Kandidat kandidat;
        private JPopupMenu menu;
        private JMenuItem menuEdit, menuDelete;
        private GradientPanel cell;
        private JLabel labelName, labelGeburtsdatumOrt, labelStrasse, labelPLZOrt;
        private JLabel labelAnwesend, labelKursgebuehr, labelPassfoto, labelFischerkarte;
        private JCheckBox checkBoxAnwesend, checkBoxKursgebuehr, checkBoxPassfoto, checkBoxFischerkarte;

        public KandidatCell() {
            menu = new JPopupMenu();
            menu.setBorderPainted(true);
            menu.add(menuEdit = new JMenuItem(rh.getString(PROPERTY_NAME, EDIT)));
//            menuEdit.addActionListener(this);
            menu.add(menuDelete = new JMenuItem(rh.getString(PROPERTY_NAME, DELETE)));
//            menuDelete.addActionListener(this);

//            setModel(model);
            setBorder(BorderFactory.createLineBorder(Color.black));

            FormLayout layout = new FormLayout("6dlu,pref,6dlu,pref,3dlu,pref,6dlu",
                    "6dlu,pref,3dlu,pref,3dlu,pref,3dlu,pref,3dlu,pref,6dlu");
            CellConstraints cc = new CellConstraints();
            cell = new GradientPanel(IConfig.HORIZONTAL);
//    cell = new FormDebugPanel(layout);
            cell.setLayout(layout);
            labelName = new JLabel();
            Font defaultFont = labelName.getFont();
            Font titleFont = defaultFont.deriveFont(Font.BOLD, defaultFont.getSize() + 4);
            labelName.setFont(titleFont);

            labelGeburtsdatumOrt = new JLabel();
            labelPLZOrt = new JLabel();
            labelStrasse = new JLabel();

            labelAnwesend = new JLabel("Anwesend");
            labelKursgebuehr = new JLabel("Kursgebühr");
            labelPassfoto = new JLabel("Passfoto");
            labelFischerkarte = new JLabel("Fischerkarte");

            checkBoxAnwesend = new JCheckBox();
            checkBoxAnwesend.setActionCommand("ANWESEND");
//            checkBoxAnwesend.addActionListener(this);
            checkBoxKursgebuehr = new JCheckBox();
            checkBoxPassfoto = new JCheckBox();
            checkBoxFischerkarte = new JCheckBox();

            cell.add(labelName, cc.xywh(2, 2, 6, 1));
            cell.add(labelStrasse, cc.xy(2, 4));
            cell.add(labelPLZOrt, cc.xy(2, 6));
            cell.add(labelGeburtsdatumOrt, cc.xy(2, 8));
            cell.add(labelAnwesend, cc.xy(4, 4));
            cell.add(checkBoxAnwesend, cc.xy(6, 4));
            cell.add(labelKursgebuehr, cc.xy(4, 6));
            cell.add(checkBoxKursgebuehr, cc.xy(6, 6));
            cell.add(labelPassfoto, cc.xy(4, 8));
            cell.add(checkBoxPassfoto, cc.xy(6, 8));
            cell.add(labelFischerkarte, cc.xy(4, 10));
            cell.add(checkBoxFischerkarte, cc.xy(6, 10));

            cell.setOpaque(true);
        }

        private void updateData(Kandidat kandidat, boolean isSelected, JTable table) {
            this.kandidat = kandidat;

            labelName.setText(kandidat.getName());
            labelStrasse.setText(kandidat.getStrasse());
            labelPLZOrt.setText(kandidat.getPLZ() + " " + kandidat.getOrt());
            labelGeburtsdatumOrt.setText(new SimpleDateFormat("dd.MM.yyyy").format(kandidat.getGeburtstag()) + " in " + kandidat.getGeburtsort());

            for (Component c : cell.getComponents()) {
                if (isSelected) {
                    c.setBackground(listSelectionBackground);
                    cell.setBackground(listSelectionBackground);
                    cell.setDirection(PLAIN_2);
                    cell.setColor2(selectedListForeground);
                } else {
                    c.setBackground(listBackground);
                    c.setForeground(listForeground);
                    cell.setBackground(listBackground);
                    cell.setDirection(HORIZONTAL);
                    cell.setColor2(color_2);
                }
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof Kandidat) {
                Kandidat kandidat = (Kandidat) value;
                updateData(kandidat, isSelected, table);
                return cell;
            }
            return new JPanel();
        }

        @Override
        public Object getCellEditorValue() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Kandidat) {
                Kandidat kandidat = (Kandidat) value;
                updateData(kandidat, isSelected, table);
                return cell;
            }
            return new JPanel();
        }
    }
}