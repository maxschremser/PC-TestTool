package at.oefg1880.swing.frame;

import at.oefg1880.swing.io.Antwort;
import at.oefg1880.swing.io.Fragebogen;
import at.oefg1880.swing.list.FragebogenList;
import at.oefg1880.swing.panel.FragebogenPanel;
import at.oefg1880.swing.panel.KandidatPanel;
import at.oefg1880.swing.panel.OEFGAntwortPanel;
import at.oefg1880.swing.panel.OEFGFragebogenPanel;
import at.oefg1880.swing.text.AntwortTextField;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.util.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: maxschremser
 * Date: 30.01.12
 * Time: 22:53
 * To change this template use File | Settings | File Templates.
 */
public class OEFGTestToolFrame extends TestToolFrame {
    public OEFGTestToolFrame(String title) {
        super(title);

    }

    @Override
    public String getImageName() {

        return "resources/oefg1880_logo.gif";
    }

    @Override
    public String getFavicon() {

        return "resources/oefg_favicon.gif";
    }

    @Override
    public FragebogenPanel getFragebogenPanel() {

        if (bottomFragebogenPane == null) {
            bottomFragebogenPane = new OEFGFragebogenPanel(this);
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

        return OEFG_TEST_TOOL;
    }

    @Override
    public char[] getAllowedValues() {

        return new OEFGAntwortPanel(false, null).getAllowedValues();
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
        char[] allowedValues = getFragebogenPanel().getAntwortDialog(fragebogen).getAntwortPanel(null).getAllowedValues();
        int[] solutions = fragebogen.getSolutions();
        int i = 4;

        // create the message for the QRCode
        StringBuffer fBuffer = new StringBuffer();
        String title = fragebogen.getTitle();
        fBuffer.append(title);
        fBuffer.append("{");
        fBuffer.append(fragebogen.getExisting());
        fBuffer.append(",");

        for (int v : fragebogen.getSolutions()) {
            row.createCell(i++).setCellValue(new String(new char[]{AntwortTextField.translate(allowedValues, v)}));
            // add the solutions to the QRCode message
            fBuffer.append(AntwortTextField.translate(getAllowedValues(), v));
        }
        fBuffer.append("}");
        // generate the QRCode image of type PNG
        File f = QRCode.from(fBuffer.toString()).to(ImageType.PNG).withSize(128, 128).file();
        // draw the picture in the sheet
        try {
            InputStream is = new FileInputStream(f);
            byte[] bytes = IOUtils.toByteArray(is);
            int picIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            Drawing drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = wb.getCreationHelper().createClientAnchor();
            anchor.setCol1(0);
            anchor.setCol2(2);
            anchor.setRow1(4);
            anchor.setRow2(12);
            drawing.createPicture(anchor, picIdx);
        } catch (FileNotFoundException fnfne) {
        } catch (IOException ioe) {
        }
        row = sheet.createRow(14);
        cell = row.createCell(0);
        cell.setCellValue(rh.getString(PROPERTY_NAME, LABEL_ANSWER));
        cell.setCellStyle(boldStyle);
        // Antworten
        int r = 16;
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
