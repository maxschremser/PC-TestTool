package at.oefg1880.swing.list;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.io.Antwort;
import at.oefg1880.swing.io.Fragebogen;
import at.oefg1880.swing.io.Kandidat;
import at.oefg1880.swing.model.KandidatTableModel;
import at.oefg1880.swing.panel.GradientPanel;
import at.oefg1880.swing.utils.ResourceHandler;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * !!! MAKE IT TO JTABLE !!!
 * http://pekalicious.com/blog/custom-jpanel-cell-with-jbuttons-in-jtable/
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 12.10.2010
 * Time: 14:32:28
 * To change this template use File | Settings | File Templates.
 */
public class KandidatTable extends JTable implements ActionListener, IConfig, ITexts {
  public final static String PROPERTY_NAME = "at.oefg1880.swing.list.KandidatTable";
  private ResourceHandler rh = ResourceHandler.getInstance();

  private static Color listForeground, listBackground, listSelectionForeground, listSelectionBackground;
  private AbstractTableModel model;
  private JPopupMenu menu;
  private JMenuItem menuEdit, menuDelete;
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

  public ArrayList<Kandidat> getItems() {
    return items;
  }

  private void setup() {
    model = new KandidatTableModel(items);
    setModel(model);
    setDefaultRenderer(Kandidat.class, new KandidatCell());
    setDefaultEditor(Kandidat.class, new KandidatCell());
    setRowHeight(120);

    menu = new JPopupMenu();
    menu.setBorderPainted(true);
    menu.add(menuEdit = new JMenuItem(rh.getString(PROPERTY_NAME, EDIT)));
    menuEdit.addActionListener(this);
    menu.add(menuDelete = new JMenuItem(rh.getString(PROPERTY_NAME, DELETE)));
    menuDelete.addActionListener(this);

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // open KandidatDialog
        menu.setVisible(false);
        if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
          frame.getKandidatPanel().editKandidatDialog(items.get(getSelectedRow()));
        } else if (SwingUtilities.isRightMouseButton(e) && getSelectedRow() > -1) {
          // right click, open edit menu
          menu.show((JTable) e.getSource(), e.getX(), e.getY());
        }
      }
    });
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == menuEdit) {
      frame.getKandidatPanel().editKandidatDialog(items.get(getSelectedRow()));
    } else if (e.getSource() == menuDelete) {
      String title = items.get(getSelectedRow()).getName();
      int n = frame.showDeleteDialog(this, rh.getString(PROPERTY_NAME, QUESTION_DELETE, new String[]{title}), rh.getString(PROPERTY_NAME, DELETE));
      if (n == 0) // JA
        model.fireTableRowsDeleted(getSelectedRow(), getSelectedRow());
      if (model.getRowCount() <= 0)
        frame.enableButtonSave(false);
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

  public void add(String title, String name, String strasse, String PLZ, String ort, String telephone, String email, Date geburtstag, String geburtsort, boolean bPassPhoto, boolean bKursgebuehr, boolean bAnwesend) {
    Kandidat kandidat = new Kandidat(title, name, strasse, PLZ, ort, telephone, email, geburtstag, geburtsort, bPassPhoto, bKursgebuehr, bAnwesend);
    add(kandidat);
  }

  public void add(Kandidat kandidat) {
    items.add(kandidat);
    ((AbstractTableModel) getModel()).fireTableDataChanged();
    repaint();
  }

  public void update(Kandidat kandidat) {
    for (Kandidat item : items) {
      if (kandidat.getIndex() == item.getIndex()) {
        items.set(kandidat.getIndex(), kandidat);
      }
    }
  }

  private class KandidatCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {
    private Kandidat kandidat;
    private JPopupMenu menu;
    private GradientPanel cell;
    private JLabel labelName, labelGeburtsdatumOrt, labelStrasse, labelPLZOrt;
    private JLabel labelAnwesend, labelKursgebuehr, labelPassfoto;
    private JCheckBox checkBoxAnwesend, checkBoxKursgebuehr, checkBoxPassfoto;
    private JButton openAnswerPanelButton;

    public KandidatCell() {
      setup();
      buildPanel();
    }

    private void setup() {
      menu = new JPopupMenu();
      menu.setBorderPainted(true);
      menu.add(menuEdit = new JMenuItem(rh.getString(PROPERTY_NAME, EDIT)));
      menu.add(menuDelete = new JMenuItem(rh.getString(PROPERTY_NAME, DELETE)));
      setBorder(BorderFactory.createLineBorder(Color.black));

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

      checkBoxAnwesend = new JCheckBox();
      checkBoxAnwesend.setActionCommand("ANWESEND");
      checkBoxAnwesend.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          kandidat.setAnwesend(checkBoxAnwesend.isSelected());
        }
      });
      checkBoxKursgebuehr = new JCheckBox();
      checkBoxKursgebuehr.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          kandidat.setKursgebuehrBezahlt(checkBoxKursgebuehr.isSelected());
        }
      });
      checkBoxPassfoto = new JCheckBox();
      checkBoxPassfoto.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          kandidat.setPassPhoto(checkBoxPassfoto.isSelected());
        }
      });
        
      openAnswerPanelButton = new JButton("Zeige Test...");
      openAnswerPanelButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              System.out.println("Button: openAnswerPanelButton has been clicked. Fragebogen: " + kandidat.getFragebogen().getTitle() + " - " + kandidat.getAntwort().getPercentages() + "%");
          }
      });
      openAnswerPanelButton.setEnabled(false);
    }

    private void buildPanel() {
      FormLayout layout = new FormLayout("6dlu,pref,6dlu,pref,3dlu,pref,6dlu,pref,6dlu",
              "6dlu,pref,3dlu,pref,3dlu,pref,3dlu,pref,6dlu");
      CellConstraints cc = new CellConstraints();
      cell = new GradientPanel(IConfig.HORIZONTAL);
      cell.setLayout(layout);
      cell.add(labelName, cc.xywh(2, 2, 8, 1));
      cell.add(labelStrasse, cc.xy(2, 4));
      cell.add(labelPLZOrt, cc.xy(2, 6));
      cell.add(labelGeburtsdatumOrt, cc.xy(2, 8));
      cell.add(labelAnwesend, cc.xy(4, 4));
      cell.add(checkBoxAnwesend, cc.xy(6, 4));
      cell.add(labelKursgebuehr, cc.xy(4, 6));
      cell.add(checkBoxKursgebuehr, cc.xy(6, 6));
      cell.add(labelPassfoto, cc.xy(4, 8));
      cell.add(checkBoxPassfoto, cc.xy(6, 8));
      cell.add(openAnswerPanelButton, cc.xy(8, 8));
      cell.setOpaque(true);
    }

    private void updateData(Kandidat kandidat, boolean isSelected, JTable table) {
      this.kandidat = kandidat;

      labelName.setText(kandidat.getName());
      labelStrasse.setText(kandidat.getStrasse());
      labelPLZOrt.setText(kandidat.getPLZ() + " " + kandidat.getOrt());
      labelGeburtsdatumOrt.setText(new SimpleDateFormat("dd.MM.yyyy").format(kandidat.getGeburtstag()) + " in " + kandidat.getGeburtsort());
      checkBoxAnwesend.setSelected(kandidat.isAnwesend());
      checkBoxPassfoto.setSelected(kandidat.hasPassPhoto());
      checkBoxKursgebuehr.setSelected(kandidat.hasKursgebuehrBezahlt());

      if (kandidat.getAntwort() != null && kandidat.getFragebogen() != null)
          openAnswerPanelButton.setEnabled(true);

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