package at.oefg1880.swing.frame;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.ITexts;
import at.oefg1880.swing.io.Antwort;
import at.oefg1880.swing.io.Fragebogen;
import at.oefg1880.swing.io.Kandidat;
import at.oefg1880.swing.panel.FragebogenPanel;
import at.oefg1880.swing.panel.GradientPanel;
import at.oefg1880.swing.panel.ImagePanel;
import at.oefg1880.swing.panel.KandidatPanel;
import at.oefg1880.swing.tabs.FadingTabbedPane;
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
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.io.*;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 09.02.2010
 * Time: 10:41:56
 * To change this template use File | Settings | File Templates.
 */
public abstract class TestToolFrame extends SavingLoggerFrame implements ITexts, IConfig, DropTargetListener, ActionListener {
    public final static String PROPERTY_NAME = "at.oefg1880.swing.frame.TestToolFrame";
    private final static String KANDIDATEN = "Kandidaten";
    protected JTabbedPane bottomPane;
    protected KandidatPanel bottomKandidatPane;
    protected FragebogenPanel bottomFragebogenPane;
    private PropertyHandler props = PropertyHandler.getInstance();
    protected ResourceHandler rh = ResourceHandler.getInstance();
    private ImagePanel imagePanel;
    private int returnValue;
    private JDialog dialog;

    public abstract String getImageName();

    public abstract String getFavicon();

    public abstract FragebogenPanel getFragebogenPanel();

    public abstract KandidatPanel getKandidatPanel();

    public abstract void exportFragebogen(Workbook wb, Fragebogen f);

    public abstract String getFragebogenName();

    public abstract char[] getAllowedValues();

    public TestToolFrame(String title) throws HeadlessException {
        super(title, PROPERTY_NAME);
        props.setOwner(this);
        setup();
    }

    private void createJMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu(rh.getString(PROPERTY_NAME, FILE));
        menu.setMnemonic(rh.getString(PROPERTY_NAME, FILE).toCharArray()[0]);

        JMenuItem menuItem = new JMenuItem(rh.getString(PROPERTY_NAME, OPEN));
        menuItem.addActionListener(this);
        menuItem.setMnemonic(rh.getString(PROPERTY_NAME, OPEN).toCharArray()[0]);
        menuItem.setActionCommand(OPEN);
        menu.add(menuItem);

        JMenu subMenu = new JMenu(rh.getString(PROPERTY_NAME, REOPEN));
        subMenu.setMnemonic(rh.getString(PROPERTY_NAME, REOPEN).toCharArray()[0]);

        String[] files = props.getProperty(PROPERTY_NAME + "." + REOPEN, "").split(",");
        int i = 1;
        for (String f : files) {
            if (new File(f).exists()) {
                menuItem = new JMenuItem(i + " " + f);
                menuItem.addActionListener(this);
                menuItem.setActionCommand(REOPEN + "_" + i);
                subMenu.add(menuItem);
                i++;
            }
        }
        menu.add(subMenu);

        menu.addSeparator();
        menuItem = new JMenuItem(rh.getString(PROPERTY_NAME, SAVE));
        menuItem.addActionListener(this);
        menuItem.setMnemonic(rh.getString(PROPERTY_NAME, SAVE).toCharArray()[0]);
        menuItem.setActionCommand(SAVE);
        menuItem.setEnabled(false);
        menu.add(menuItem);

        menu.addSeparator();
        menuItem = new JMenuItem(rh.getString(PROPERTY_NAME, EXIT));
        menuItem.addActionListener(this);
        menuItem.setMnemonic(rh.getString(PROPERTY_NAME, EXIT).toCharArray()[0]);
        menuItem.setActionCommand(EXIT);
        menu.add(menuItem);

        menuBar.add(menu);

        setJMenuBar(menuBar);

    }

    public void enableMenuItemSave(boolean bEnable) {
        log.debug(bEnable);
        getJMenuBar().getMenu(0).getItem(3).setEnabled(bEnable);
    }

    public void enableButtonSave(boolean bEnable) {
        log.debug(bEnable);
        getFragebogenPanel().getButtonSave().setEnabled(bEnable);
        getKandidatPanel().getButtonSave().setEnabled(bEnable);
    }

    public JComponent getBottomComponent() {
        if (bottomPane == null) {
            bottomPane = new JTabbedPane();
            bottomPane.addTab(rh.getString(KandidatPanel.PROPERTY_NAME, LABEL), getKandidatPanel());
            bottomPane.addTab(rh.getString(FragebogenPanel.PROPERTY_NAME, LABEL), getFragebogenPanel());
        }
        return bottomPane;
    }

    private void setup() {
        createJMenuBar();
        new DropTarget(this, this);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                doWindowClosing();
            }
        });

        FormLayout layout = new FormLayout(
                "6dlu,pref",
                "6dlu,pref,6dlu,pref");
        CellConstraints cc = new CellConstraints();
        GradientPanel panel = new GradientPanel();
        panel.setLayout(layout);
        panel.add(getImagePane(), cc.xy(2, 2));
        panel.add(getBottomComponent(), cc.xy(2, 4));
        getContentPane().add(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    public void setReturnValue(int returnValue) {
        this.returnValue = returnValue;
    }

    public JDialog getDialog() {
        return dialog;
    }

    private void doWindowClosing() {
        if (getFragebogenPanel().getFragebogenList().getModel().getSize() > 0) {
            int a = JOptionPane.showConfirmDialog(getParent(), rh.getString(PROPERTY_NAME, QUESTION_SAVE));
            if (JOptionPane.YES_OPTION == a) {
                getFragebogenPanel().getButtonSave().doClick();
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

    public int showDeleteDialog(ActionListener list, String message, String title) {
        // Ja, Nein, Abbrechen Dialog mit einer Frage und einem Icon
        dialog = new JDialog(this, title, true);
        FormLayout layout = new FormLayout(
                "6dlu,center:pref",
                "6dlu,pref,6dlu,pref"
        );
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        builder.addLabel(message, cc.xy(2, 2));
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

    private void exportKandidaten(Workbook wb) {
        Sheet kandidatSheet = wb.createSheet(KANDIDATEN);
        // Title
        CellStyle boldStyle = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        boldStyle.setFont(font);

        Row row = kandidatSheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(KANDIDATEN);
        cell.setCellStyle(boldStyle);

        row = kandidatSheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellStyle(boldStyle);
        cell.setCellValue("Titel");

        cell = row.createCell(1);
        cell.setCellStyle(boldStyle);
        cell.setCellValue("Vor- und Zuname");

        cell = row.createCell(2);
        cell.setCellStyle(boldStyle);
        cell.setCellValue("Strasse");

        cell = row.createCell(3);
        cell.setCellStyle(boldStyle);
        cell.setCellValue("PLZ");

        cell = row.createCell(4);
        cell.setCellStyle(boldStyle);
        cell.setCellValue("Ort");

        ArrayList<Kandidat> kandidatList = getKandidatPanel().getKandidatTable().getModel().getItems();
        int r = 3;
        for (Kandidat kandidat : kandidatList) {
            row = kandidatSheet.createRow(r++);
            cell = row.createCell(0);
            cell.setCellValue(kandidat.getTitle());

            cell = row.createCell(1);
            cell.setCellValue(kandidat.getName());

            cell = row.createCell(2);
            cell.setCellValue(kandidat.getStrasse());

            cell = row.createCell(3);
            cell.setCellValue(kandidat.getPLZ());

            cell = row.createCell(4);
            cell.setCellValue(kandidat.getOrt());
        }
    }

    public String exportData() {
        File file;
        try {
            Workbook wb = new HSSFWorkbook();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(FILE_DATE_PATTERN);
            String date = sdf.format(cal.getTime());
            file = new File(getFragebogenName() + "-" + date + ".xls");
            log.info("Saved at: " + file.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(file);
            DefaultListModel model = (DefaultListModel) getFragebogenPanel().getFragebogenList().getModel();
            Enumeration<Fragebogen> enums = (Enumeration<Fragebogen>) model.elements();
            while (enums.hasMoreElements()) {
                Fragebogen f = enums.nextElement();
                exportFragebogen(wb, f);
            }
            exportKandidaten(wb);
            wb.write(fos);
            fos.close();
            return file.getAbsolutePath().replaceAll("\\\\", "/");
        } catch (FileNotFoundException fnfne) {
        } catch (IOException ioe) {
        }
        return "";
    }

    private String getValue(Cell cell) {
      if (cell == null) return "";
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return Double.valueOf(cell.getNumericCellValue()).intValue()+"";
        }
        return "";
    }

    public boolean importData(File file) throws Exception {
        try {
            Workbook wb = new HSSFWorkbook(new FileInputStream(file));
            log.info("Importing from: " + file.getAbsolutePath());
            int numSheets = wb.getNumberOfSheets();
            DefaultListModel model = (DefaultListModel) getFragebogenPanel().getFragebogenList().getModel();
            char[] allowedValues = getAllowedValues();

            // import Kandidaten into KandidatTable
            ArrayList<Kandidat> kandidatList = new ArrayList<Kandidat>();
            Sheet kandidatSheet = wb.getSheet(KANDIDATEN);
            for (int i = 3; i <= kandidatSheet.getLastRowNum(); i++) {
                Row kandidatRow = kandidatSheet.getRow(i);
                Cell cell = kandidatRow.getCell(0);
                String title = getValue(cell);
                cell = kandidatRow.getCell(1);
                String name = getValue(cell);
                cell = kandidatRow.getCell(2);
                String strasse = getValue(cell);
                cell = kandidatRow.getCell(3);
                String PLZ = getValue(cell);
                cell = kandidatRow.getCell(4);
                String ort = getValue(cell);
                kandidatList.add(new Kandidat(title, name, strasse, PLZ, ort, "", "", new Date(), "", false, false, false));
            }
            getKandidatPanel().getKandidatTable().getModel().setItems(kandidatList);

            for (int s = 0; s < numSheets; s++) {
                Sheet sheet = wb.getSheetAt(s);
                Row row = sheet.getRow(3);

                // add Solution
                int numSolutions = row.getLastCellNum() - 4;
                int[] solutions = new int[numSolutions];

                if (!sheet.getSheetName().equals(KANDIDATEN)) {
                    for (int i = 4; i < row.getLastCellNum(); i++) {
                        char cellValue = row.getCell(i).getStringCellValue().toCharArray()[0];
                        solutions[i - 4] = AntwortTextField.translate(allowedValues, cellValue);
                    }
                    Fragebogen fragebogen = new Fragebogen(sheet.getSheetName(), Double.valueOf(sheet.getRow(0).getCell(5).toString()).intValue(), solutions);

                    for (int a = 6; a <= sheet.getLastRowNum(); a++) {
                        row = sheet.getRow(a);
                        for (int i = 4; i < row.getLastCellNum(); i++) {
                            char cellValue = row.getCell(i).getStringCellValue().toCharArray()[0];
                            solutions[i - 4] = AntwortTextField.translate(allowedValues, cellValue);
                        }
                        String kandidatName = getValue(row.getCell(0));
                        String p = getValue(row.getCell(2));
                        p = p.substring(0, p.length()-1);
                        int percentages = Integer.valueOf(p).intValue();
                        Kandidat kandidat = findKandidat(kandidatList, kandidatName);
                        Antwort antwort = new Antwort(kandidat, percentages, solutions);
                        kandidat.setAntwort(fragebogen, antwort);
                        fragebogen.addAntwort(antwort);
                    }
                    model.addElement(fragebogen);
                }
            }
            if (model.getSize() > 0) {
                enableButtonSave(true);
                enableMenuItemSave(true);
            }
            String reopenConfigString = props.getProperty(PROPERTY_NAME + "." + REOPEN, "");

            if (reopenConfigString.length() > 0) {
                if (reopenConfigString.contains(file.getAbsolutePath())) { // if the file is not already in the list
                    reopenConfigString = file.getAbsolutePath() + "," + reopenConfigString;
                    // maximum files to reopen is limited to 10
                    String[] files = reopenConfigString.split(",");
                    reopenConfigString = "";
                    for (int i = 0; i < 10 && i < files.length; i++) {
                        reopenConfigString += files[i];
                        if (i < 10 && ((i + 1) < files.length)) {
                            reopenConfigString += ",";
                        }
                    }
                }
            } else {
                reopenConfigString = file.getAbsolutePath();
            }
            props.setProperty(PROPERTY_NAME + "." + REOPEN, reopenConfigString);
        } catch (FileNotFoundException fnfne) {
        } catch (IOException ioe) {
        }
        return true;
    }

    private Kandidat findKandidat(ArrayList<Kandidat> kandidatList, String kandidatName) {
        for (Kandidat k : kandidatList) {
            if (k.getTitleAndName().equals(kandidatName)) return k;
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (OPEN.equals(e.getActionCommand())) {
            log.debug("actionPerformed.Command = OPEN");
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().endsWith(".xls");
                }

                @Override
                public String getDescription() {
                    return "Excel Files (*.xls)";
                }
            });
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    importData(fileChooser.getSelectedFile());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if (SAVE.equals(e.getActionCommand())) {
            log.debug("actionPerformed.Command = SAVE");
            props.propertyChange(new PropertyChangeEvent(this, JOptionPane.VALUE_PROPERTY, 0, 0));
            String filePath = exportData();
            int selectedOption = JOptionPane.showConfirmDialog(getParent(), rh.getString(FragebogenPanel.PROPERTY_NAME, DIALOG_SAVED, new String[]{filePath}), UIManager.getString("OptionPane.titleText"), JOptionPane.YES_NO_OPTION);
            if (JOptionPane.OK_OPTION == selectedOption) {
                try {
                    URI uri = new URI(filePath);
                    Desktop.getDesktop().browse(uri);
                } catch (Exception exp) {
                    log.info(exp.getMessage());
                }
            }
        } else if (EXIT.equals(e.getActionCommand())) {
            log.debug("actionPerformed.Command = EXIT");
            doWindowClosing();
        } else if (e.getActionCommand().startsWith(REOPEN)) {
            log.debug("actionPerformed.Command = REOPEN");
            int index = Integer.valueOf(e.getActionCommand().substring(REOPEN.length() + 1));
            String file = props.getProperty(PROPERTY_NAME + "." + REOPEN, "").split(",")[index];
            File f;
            if ((f = new File(file)).exists()) {
                try {
                    importData(f);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {

        // handle Document dropped
        log.info(dtde.getSource());
        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
        Transferable transferable = dtde.getTransferable();
        try {
            if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                List list = (List) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                File f = (File) list.get(0);
                log.info(f.getAbsolutePath());
                importData(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
