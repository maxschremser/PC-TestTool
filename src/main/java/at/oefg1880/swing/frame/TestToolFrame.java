package at.oefg1880.swing.frame;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.list.Antwort;
import at.oefg1880.swing.list.Fragebogen;
import at.oefg1880.swing.panel.FragebogenPane;
import at.oefg1880.swing.panel.GradientPanel;
import at.oefg1880.swing.panel.ImagePanel;
import at.oefg1880.swing.text.AntwortTextField;
import at.oefg1880.swing.utils.PropertyHandler;
import at.oefg1880.swing.utils.ResourceHandler;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 09.02.2010
 * Time: 10:41:56
 * To change this template use File | Settings | File Templates.
 */
public abstract class TestToolFrame extends SheetableFrame implements ITexts, IConfig {
  private ImagePanel imagePanel;
  private FragebogenPane fragebogenPane;
  private PropertyHandler props = PropertyHandler.getInstance();
  private ResourceHandler rh = ResourceHandler.getInstance();
  private final Logger log = Logger.getLogger(TestToolFrame.class);
  private int returnValue;
  private JDialog dialog;

  public abstract String getImageName();
  public abstract String getFavicon();

  public TestToolFrame(String title) throws HeadlessException {
    super(title);
    props.setOwner(this);
    setup();
//    test();
    pack();
  }


  private void test() {
    getFragebogenPane().getFragebogenList().add("LFV I", 7, new int[]{3, 2, 3, 2, 1, 3, 2, 1, 3, 2, 1, 3, 2, 2, 3, 2, 1, 2, 3, 2});
    getFragebogenPane().getFragebogenList().add("LFV II", 5, new int[]{3, 2, 3, 2, 1, 3, 2, 1, 3, 2, 1, 3, 2, 2, 3, 2, 1, 2, 3, 2});
    getFragebogenPane().getFragebogenList().add("LFV III", 2, new int[]{3, 2, 3, 2, 1, 3, 2, 1, 3, 2, 1, 3, 2, 2, 3, 2, 1, 2, 3, 2});
    getFragebogenPane().getFragebogenList().add("LFV IV", 3, new int[]{3, 2, 3, 2, 1, 3, 2, 1, 3, 2, 1, 3, 2, 2, 3, 2, 1, 2, 3, 2});
    getFragebogenPane().getFragebogenList().add("LFV V", 5, new int[]{3, 2, 3, 2, 1, 3, 2, 1, 3, 2, 1, 3, 2, 2, 3, 2, 1, 2, 3, 2});
  }

  private void setup() {
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        if (fragebogenPane.getFragebogenList().getModel().getSize() > 0) {
          int a = JOptionPane.showConfirmDialog(getParent(), "Möchten Sie die Daten speichern ?");
          if (JOptionPane.YES_OPTION == a) {
            exportData();
            dispose();
            return;
          } else if (JOptionPane.NO_OPTION == a) {
            dispose();
            return;
          } else {
            return;
          }
        }
        dispose();
      }
    });
    FormLayout layout = new FormLayout(
            "6dlu,pref,6dlu",
            "6dlu,pref,6dlu,pref,6dlu");
    CellConstraints cc = new CellConstraints();
    GradientPanel panel = new GradientPanel();
    panel.setLayout(layout);
    panel.add(getImagePane(), cc.xy(2, 2));
    panel.add(getFragebogenPane(), cc.xy(2, 4));
//    panel.add(getButtonPane(), cc.xy(2, 6));
    getContentPane().add(panel);

    int x = Integer.valueOf(props.getProperty("x"));
    int y = Integer.valueOf(props.getProperty("y"));
    Point p = new Point(x, y);
    setLocation(p);
    // we are now using the Dissolver to fade out the frame
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    setIconImage(new ImageIcon(getClass().getClassLoader().getResource(getFavicon())).getImage());
    pack();
    setResizable(false);
    setVisible(true);
  }

  public ImagePanel getImagePane() {
    if (imagePanel == null) {
      imagePanel = new ImagePanel(getClass().getClassLoader().getResource(getImageName()));
      imagePanel.setBorder(BorderFactory.createLineBorder(Color.black));
    }
    return imagePanel;
  }

  public FragebogenPane getFragebogenPane() {
    if (fragebogenPane == null) {
      fragebogenPane = new FragebogenPane(this);
      fragebogenPane.setBorder(BorderFactory.createLineBorder(Color.black));
    }
    return fragebogenPane;
  }

  public void setReturnValue(int returnValue) {
    this.returnValue = returnValue;
  }

  public JDialog getDialog() {
    return dialog;
  }

  public int showDeleteFragebogenDialog(ActionListener list, String message, String title) {
    // Ja, Nein, Abbrechen Dialog mit einer Frage und einem Icon
    dialog = new JDialog(this, title, true);
    FormLayout layout = new FormLayout(
            "6dlu,center:pref,6dlu", "6dlu,pref,6dlu,pref,6dlu"
    );
    PanelBuilder builder = new PanelBuilder(layout);
    CellConstraints cc = new CellConstraints();
    builder.addLabel(message.toString(), cc.xy(2, 2));
    JButton okButton = new JButton(OK);
    okButton.setMnemonic('O');
    okButton.addActionListener(list);
    okButton.setActionCommand(OK);
    JButton noButton = new JButton(NO);
    noButton.setMnemonic('N');
    noButton.addActionListener(list);
    noButton.setActionCommand(NO);
    JPanel bar = ButtonBarFactory.buildOKCancelBar(okButton, noButton);
    builder.add(bar, cc.xy(2, 4));
    dialog.getContentPane().add(builder.getPanel());
    dialog.pack();
    dialog.setLocation((getLocation().x + (getWidth() / 2)) - (dialog.getWidth() / 2), (getLocation().y + (getHeight() / 2) - (dialog.getHeight() / 2)));
    dialog.setVisible(true);
    dialog.dispose();

    return returnValue;
  }

  public void exportData() {
    try {
      Workbook wb = new HSSFWorkbook();
      Calendar cal = new GregorianCalendar();
      String date = "" + cal.get(Calendar.YEAR) + cal.get(Calendar.MONTH) + cal.get(Calendar.DAY_OF_MONTH);
      File file = new File("TestTool-" + date + ".xls");
      file.createNewFile();
      log.info("Saved at: " + file.getAbsolutePath());
      FileOutputStream fos = new FileOutputStream(file);
      DefaultListModel model = (DefaultListModel) fragebogenPane.getFragebogenList().getModel();
      Enumeration<Fragebogen> enums = (Enumeration<Fragebogen>) model.elements();
      while (enums.hasMoreElements()) {
        Fragebogen f = enums.nextElement();
        Sheet sheet = wb.createSheet(f.getTitle());
        CellStyle boldStyle = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        boldStyle.setFont(font);

        // Title
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(f.getTitle());
        cell.setCellStyle(boldStyle);
        // Lösungen
        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue("Lösungen");
        cell.setCellStyle(boldStyle);

        int c = 4;
        cell = row.createCell(c++);
        cell.setCellValue("A1");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("A2");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("A3");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("A4");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("A5");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("B1");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("B2");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("B3");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("B4");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("B5");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("C1");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("C2");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("C3");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("C4");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("C5");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("D1");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("D2");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("D3");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("D4");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("D5");
        cell.setCellStyle(boldStyle);

        row = sheet.createRow(3);
        int i = 4;
        for (int v : f.getSolutions()) {
          row.createCell(i++).setCellValue(AntwortTextField.translate(v) + "");
        }

        row = sheet.createRow(5);
        cell = row.createCell(0);
        cell.setCellValue("Antworten");
        cell.setCellStyle(boldStyle);
        // Antworten
        int r = 6;
        for (Antwort a : f.getAntworten()) {
          row = sheet.createRow(r++);
          row.createCell(0).setCellValue(a.getName());
          row.createCell(1).setCellValue(a.getAlter());
          row.createCell(2).setCellValue(a.getGeschlecht());
          row.createCell(3).setCellValue(a.getPercentages() + "%");
          i = 4;
          for (int v : a.getAnswers()) {
            row.createCell(i++).setCellValue((AntwortTextField.translate(v) + ""));
          }
        }
      }
      wb.write(fos);
      fos.close();
    } catch (FileNotFoundException fnfne) {
    } catch (IOException ioe) {
    }
  }

}