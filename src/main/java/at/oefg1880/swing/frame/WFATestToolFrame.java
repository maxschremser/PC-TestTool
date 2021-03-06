package at.oefg1880.swing.frame;

import at.oefg1880.swing.io.Antwort;
import at.oefg1880.swing.io.Fragebogen;
import at.oefg1880.swing.list.FragebogenList;
import at.oefg1880.swing.panel.FragebogenPanel;
import at.oefg1880.swing.panel.KandidatPanel;
import at.oefg1880.swing.panel.WFAAntwortPanel;
import at.oefg1880.swing.panel.WFAFragebogenPanel;
import at.oefg1880.swing.text.AntwortTextField;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: maxschremser
 * Date: 30.01.12
 * Time: 22:53
 * To change this template use File | Settings | File Templates.
 */
public class WFATestToolFrame extends TestToolFrame {

    public WFATestToolFrame(String title) {
        super(title);

    }

    @Override
    public String getImageName() {

        return "resources/wfa_logo.gif";
    }

    @Override
    public String getFavicon() {

        return "resources/wfa_favicon.gif";
    }

    @Override
    public FragebogenPanel getFragebogenPanel() {

        if (bottomFragebogenPane == null) {
            bottomFragebogenPane = new WFAFragebogenPanel(this);
            bottomFragebogenPane.setBorder(BorderFactory.createLineBorder(Color.black));
        }
        return bottomFragebogenPane;
    }

    @Override
    public KandidatPanel getKandidatPanel() {

        if (bottomKandidatPane == null) {
            bottomKandidatPane = new KandidatPanel(this);
            bottomKandidatPane.setBorder(BorderFactory.createLineBorder(Color.black));
        }
        return bottomKandidatPane;
    }

    @Override
    public String getFragebogenName() {

        return WFA_TEST_TOOL;
    }

    @Override
    public char[] getAllowedValues() {

        return new WFAAntwortPanel(false, null).getAllowedValues();
    }


    @Override
    public void exportFragebogen(Workbook wb, Fragebogen fragebogen) {

        Sheet sheet = wb.createSheet(fragebogen.getTitle());
        CellStyle boldStyle = wb.createCellStyle();
        CellStyle greenStyle = wb.createCellStyle();
        CellStyle redStyle = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        boldStyle.setFont(font);

        redStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        redStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        greenStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        greenStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        // Title
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(fragebogen.getTitle());
        cell.setCellStyle(boldStyle);

        // Existing
        cell = row.createCell(4);
        cell.setCellStyle(boldStyle);
        cell.setCellValue(rh.getString(FragebogenList.PROPERTY_NAME, AVAILABLE));
        cell = row.createCell(5);
        cell.setCellValue(fragebogen.getExisting());

        // Solved
        cell = row.createCell(6);
        cell.setCellStyle(boldStyle);
        cell.setCellValue(rh.getString(FragebogenList.PROPERTY_NAME, SOLVED));
        cell = row.createCell(7);
        cell.setCellValue(fragebogen.getSolved());

        // Opened
        cell = row.createCell(8);
        cell.setCellStyle(boldStyle);
        cell.setCellValue(rh.getString(FragebogenList.PROPERTY_NAME, OPENED));
        cell = row.createCell(9);
        cell.setCellValue(fragebogen.getUnsolved());

        // Lösungen
        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue(rh.getString(PROPERTY_NAME, LABEL_SOLUTION));
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
        cell.setCellValue("A6");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("A7");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("A8");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("A9");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("A10");
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
        cell.setCellValue("B6");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("B7");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("B8");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("B9");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("B10");
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
        cell.setCellValue("C6");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("C7");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("C8");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("C9");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("C10");
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
        cell = row.createCell(c++);
        cell.setCellValue("D6");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("D7");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("D8");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("D9");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("D10");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("E1");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("E2");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("E3");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("E4");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("E5");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("E6");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("E7");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("E8");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("E9");
        cell.setCellStyle(boldStyle);
        cell = row.createCell(c++);
        cell.setCellValue("E10");
        cell.setCellStyle(boldStyle);

        row = sheet.createRow(3);
        char[] allowedValues = getFragebogenPanel().getAntwortDialog(fragebogen).getAntwortPanel(null).getAllowedValues();
        int[] solutions = fragebogen.getSolutions();
        int i = 4;
        for (int v : fragebogen.getSolutions()) {
            row.createCell(i++).setCellValue(AntwortTextField.translate(allowedValues, v) + "");
        }

        row = sheet.createRow(5);
        cell = row.createCell(0);
        cell.setCellValue(rh.getString(PROPERTY_NAME, LABEL_ANSWER));
        cell.setCellStyle(boldStyle);
        // Antworten
        int r = 6;
        for (Antwort a : fragebogen.getAntworten()) {
            row = sheet.createRow(r++);
            row.createCell(0).setCellValue(a.getKandidatName());
            row.createCell(2).setCellValue(a.getPercentages() + "%");
            i = 4;
            int j = 0;
            for (int iAnswer : a.getAnswers()) {
                Cell cellAnswer = row.createCell(i++);
                cellAnswer.setCellValue(new String(new char[]{AntwortTextField.translate(allowedValues, iAnswer)}));
                if (iAnswer == solutions[j++]) {
                    cellAnswer.setCellStyle(greenStyle);
                } else {
                    cellAnswer.setCellStyle(redStyle);
                }
            }
        }
    }
}
