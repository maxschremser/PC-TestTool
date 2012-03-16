package at.oefg1880.swing.list;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.panel.FragebogenPanel;
import at.oefg1880.swing.panel.GradientPanel;
import at.oefg1880.swing.panel.KandidatPanel;
import at.oefg1880.swing.utils.ResourceHandler;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 12.10.2010
 * Time: 14:32:28
 * To change this template use File | Settings | File Templates.
 */
public class KandidatList extends JList implements ActionListener, IConfig, ITexts {
  public final static String PROPERTY_NAME = "at.oefg1880.swing.list.KandidatList";
  private ResourceHandler rh = ResourceHandler.getInstance();
  private GradientPanel cell;
  private JLabel labelName, labelGeburtsdatum, labelGeburtsort, labelPLZ,
      labelOrt, labelStrasse;
  private static Color listForeground, listBackground, listSelectionForeground, listSelectionBackground;
  private DefaultListModel model;
  private TestToolFrame frame;
  private JPopupMenu menu;
  private JMenuItem menuEdit, menuDelete;
  private static Logger log = Logger.getLogger(FragebogenList.class);

  static {
    UIDefaults uid = UIManager.getLookAndFeel().getDefaults();
    listBackground = uid.getColor("List.background");
    listForeground = uid.getColor("List.foreground");
    listSelectionBackground = new Color(24, 49, 73);
    listSelectionForeground = new Color(255, 255, 255);
  }

  public KandidatList(TestToolFrame frame) {
    super();
    this.frame = frame;
    setup();
  }

  private void setup() {
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
          ((KandidatPanel) frame.getKandidatPanel()).updateKandidatDialog((Kandidat) getSelectedValue());
        }
      }
    });

    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        int key = e.getKeyCode();
        // 49=1, 50=2,...57=9   on keyboard
        // 97=1, 98=2,...105=9  on numpad
        if (key == KeyEvent.VK_ENTER) {            // handle enter key pressed
          // open FragebogenDialog
          if (getSelectedIndex() >= 0)
            ((FragebogenPanel) frame.getFragebogenPanel()).createNewAntwortDialog((Fragebogen) getSelectedValue());
        } else if (key == KeyEvent.VK_N) {
          ((FragebogenPanel) frame.getFragebogenPanel()).getButtonNew().doClick();
        } else if (key == KeyEvent.VK_S) {
          if (((FragebogenPanel) frame.getFragebogenPanel()).getButtonSave().isEnabled()) {
            ((FragebogenPanel) frame.getFragebogenPanel()).getButtonSave().doClick();
          }
        } else if (key == KeyEvent.VK_DELETE) {
          String title = ((Fragebogen) getSelectedValue()).getTitle();
          int n = frame.showDeleteFragebogenDialog(((FragebogenPanel) frame.getFragebogenPanel()).getFragebogenList(), rh.getString(PROPERTY_NAME, QUESTION_DELETE, new String[]{title}), rh.getString(PROPERTY_NAME, DELETE));
          if (n == JOptionPane.OK_OPTION) // JA
            model.remove(getSelectedIndex());
        } else {
          int index = (key >= 49 && key <= 57) ? key - 49 :
              (key >= 97 && key <= 105) ? key - 97 : -1;
          if (index >= 0 && index <= 9) {
            setSelectedIndex(index);
            ensureIndexIsVisible(index);
          }
        }
      }
    });

    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    setCellRenderer(new KandidatRenderer());
    model = new DefaultListModel();
    setModel(model);
    setBorder(BorderFactory.createLineBorder(Color.black));

    FormLayout layout = new FormLayout("6dlu,pref,3dlu,pref,6dlu,pref,3dlu,pref,6dlu,pref,3dlu,pref,6dlu",
        "6dlu,pref,3dlu,pref,6dlu");
    CellConstraints cc = new CellConstraints();
    cell = new GradientPanel(IConfig.HORIZONTAL);
    cell.setLayout(layout);
    labelName = new JLabel();
    Font defaultFont = labelName.getFont();
    Font titleFont = defaultFont.deriveFont(Font.BOLD, defaultFont.getSize() + 4);
    labelName.setFont(titleFont);

    labelGeburtsdatum = new JLabel();
    labelGeburtsort = new JLabel();
    labelPLZ = new JLabel();

    labelOrt = new JLabel();
    labelStrasse = new JLabel();

    cell.add(labelName, cc.xywh(2, 2, 11, 1));
    cell.add(labelOrt, cc.xy(2, 4));
    cell.add(labelGeburtsdatum, cc.xy(4, 4));
    cell.add(labelStrasse, cc.xy(6, 4));
    cell.add(labelGeburtsort, cc.xy(8, 4));
    cell.add(labelPLZ, cc.xy(12, 4));

    cell.setOpaque(true);
  }

  public void add(String name, String strasse, int PLZ, String ort, Date geburtstag) {
    int index = model.getSize();
    Kandidat kandidat = new Kandidat(index, name, geburtstag);
    Kandidat.Adresse adresse = kandidat.new Adresse(strasse, PLZ, ort);
    kandidat.setAdresse(adresse);
    add(kandidat);
  }

  public void add(Kandidat kandidat) {
    model.addElement(kandidat);
  }

  public void update(Kandidat kandidat) {
    model.setElementAt(kandidat, kandidat.getIndex());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == menuEdit) {
      ((FragebogenPanel) frame.getFragebogenPanel()).editFragebogenDialog((Fragebogen) getSelectedValue());
    } else if (e.getSource() == menuDelete) {
      String title = ((Fragebogen) getSelectedValue()).getTitle();
      int n = frame.showDeleteFragebogenDialog(this, rh.getString(PROPERTY_NAME, QUESTION_DELETE, new String[]{title}), rh.getString(PROPERTY_NAME, DELETE));
      if (n == 0) // JA
        model.remove(getSelectedIndex());
      if (model.getSize() <= 0)
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

  private class KandidatRenderer implements ListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      if (value instanceof Kandidat) {
        Kandidat kandidat = (Kandidat) value;
        kandidat.setIndex(index);
        labelName.setText(kandidat.getName());
        labelGeburtsdatum.setText(new SimpleDateFormat("dd.mm.yyyy").format(kandidat.getGeburtstag()));
        labelGeburtsort.setText(kandidat.getGeburtsort());
        labelPLZ.setText(kandidat.getAdresse().getPLZ() + "");
        labelOrt.setText(kandidat.getAdresse().getOrt());
        labelStrasse.setText(kandidat.getAdresse().getStrasse());
        log.debug(isSelected ? "Cell selected: " + cell : "Cell: " + cell);
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
        return cell;
      }
      return new JPanel();
    }
  }
}