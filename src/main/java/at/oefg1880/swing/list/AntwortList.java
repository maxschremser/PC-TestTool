package at.oefg1880.swing.list;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.dialog.AntwortDialog;
import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.panel.FragebogenPanel;
import at.oefg1880.swing.panel.GradientPanel;
import at.oefg1880.swing.utils.ResourceHandler;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 12.10.2010
 * Time: 14:32:28
 * To change this template use File | Settings | File Templates.
 */
public abstract class AntwortList extends JList implements ActionListener, ITexts {
  protected ResourceHandler rh = ResourceHandler.getInstance();
  public final static String PROPERTY_NAME = "at.oefg1880.swing.list.AntwortList";
  protected TestToolFrame frame;
  protected AntwortDialog dialog;
  protected Fragebogen fragebogen;
  private GradientPanel cell;
  private static Color listForeground, listBackground, listSelectionForeground, listSelectionBackground;
  private JLabel labelName, labelAlter, labelGeschlecht, labelPercentages;
  private DefaultListModel model;
  private JPopupMenu menu;
  private JMenuItem menuEdit, menuDelete;

  static {
    UIDefaults uid = UIManager.getLookAndFeel().getDefaults();
    listBackground = uid.getColor("List.background");
    listForeground = uid.getColor("List.foreground");
    listSelectionBackground = new Color(24, 49, 73);
    listSelectionForeground = new Color(255, 255, 255);
  }

  protected abstract void createEditAntwortDialog(Antwort antwort);

  public AntwortList(TestToolFrame frame, Fragebogen fragebogen) {
    super();
    this.frame = frame;
    this.fragebogen = fragebogen;
    menu = new JPopupMenu();
    setup();
    init();
  }

  private void setup() {
    menu = new JPopupMenu();
    menu.setBorderPainted(true);
    menu.add(menuEdit = new JMenuItem(rh.getString(PROPERTY_NAME, EDIT)));
    menuEdit.addActionListener(this);
//    menu.add(new JPopupMenu.Separator());
    menu.add(menuDelete = new JMenuItem(rh.getString(PROPERTY_NAME, DELETE)));
    menuDelete.addActionListener(this);
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // open FragebogenDialog
        menu.setVisible(false);
        if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
          createEditAntwortDialog((Antwort) getSelectedValue());
        } else if (SwingUtilities.isRightMouseButton(e) && !isSelectionEmpty() &&
            locationToIndex(e.getPoint()) == getSelectedIndex()) {
          // right click, open edit menu
          menu.show((JList) e.getSource(), e.getX(), e.getY());
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
            createEditAntwortDialog((Antwort) getSelectedValue());
        } else if (key == KeyEvent.VK_DELETE) {
          String title = ((Antwort) getSelectedValue()).getName();
          int n = frame.showDeleteFragebogenDialog(((FragebogenPanel) frame.getFragebogenPanel()).getAntwortDialog(fragebogen), rh.getString(PROPERTY_NAME, QUESTION_DELETE, new String[]{title}), rh.getString(PROPERTY_NAME, DELETE));
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
    setCellRenderer(new AntwortenRenderer());
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
    Font nameFont = defaultFont.deriveFont(Font.BOLD, defaultFont.getSize() + 4);
    labelName.setFont(nameFont);
    labelGeschlecht = new JLabel();
    labelAlter = new JLabel();
    labelPercentages = new JLabel();

    cell.add(labelName, cc.xywh(2, 2, 11, 1));
    cell.add(labelGeschlecht, cc.xy(2, 4));
    cell.add(labelAlter, cc.xy(4, 4));
    cell.add(labelPercentages, cc.xy(6, 4));

    cell.setOpaque(true);
  }

  private void init() {
    if (fragebogen == null) return;
    for (Antwort antwort : fragebogen.getAntworten()) {
      model.addElement(antwort);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == menuEdit) {
      createEditAntwortDialog((Antwort) getSelectedValue());
    } else if (e.getSource() == menuDelete) {
      Antwort antwort = (Antwort) getSelectedValue();
      String name = antwort.getName();
      int n = JOptionPane.showOptionDialog(frame, rh.getString(PROPERTY_NAME, QUESTION_DELETE, new String[]{name}), rh.getString(PROPERTY_NAME, DELETE),
          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Ja", "Nein"}, "Ja");
      if (n == 0) { // JA
        model.remove(getSelectedIndex());
        fragebogen.removeAntwort(antwort);
      }
    }
  }

  private class AntwortenRenderer implements ListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      if (value instanceof Antwort) {
        Antwort antwort = (Antwort) value;
        labelName.setText(antwort.getName());
        labelAlter.setText(antwort.getAlter() + "");
        labelPercentages.setText(antwort.getPercentages() + "%");
        for (Component c : cell.getComponents()) {
          if (isSelected) {
            c.setBackground(listSelectionBackground);
            cell.setBackground(listSelectionBackground);
            cell.setDirection(IConfig.PLAIN_2);
            cell.setColor2(new Color(199, 215, 234));
          } else {
            c.setBackground(listBackground);
            c.setForeground(listForeground);
            cell.setBackground(listBackground);
            cell.setDirection(IConfig.HORIZONTAL);
            cell.setColor2(IConfig.color_2);
          }
        }
        return cell;
      }
      return new JPanel();
    }
  }
}