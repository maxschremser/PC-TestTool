package at.oefg1880.swing.list;

import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.io.Kandidat;
import at.oefg1880.swing.model.FilterKandidatTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 13.03.12
 * Time: 11:55
 * To change this template use File | Settings | File Templates.
 */
public class FilterKandidatTable extends KandidatTable {
    private FilterTextField filterField;
    private int DEFAULT_FIELD_WIDTH = 20;
    private Kandidat kandidat;

    public FilterKandidatTable(TestToolFrame frame, ArrayList<Kandidat> items) {
        super(frame, items);
        model = new FilterKandidatTableModel(this, items);
        setup();
        filterField = new FilterTextField(this, DEFAULT_FIELD_WIDTH);
        getModel().refilter();
        setText("");
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    kandidat = getModel().getFilterItems().get(rowAtPoint(e.getPoint()));
                    setText(kandidat.getTitleAndName());
                }
            }
        });
    }

    public FilterKandidatTableModel getModel() {
        return (FilterKandidatTableModel) model;
    }

    public TestToolFrame getFrame() {
        return frame;
    }

    public void addItem(Kandidat item) {
        getModel().addElement(item);
    }

    public void setText(String text) {
        getFilterField().setText(text);
    }

    public String getText() {
        return getFilterField().getText();
    }

    public void setSelectionColor(Color color) {
        getFilterField().setSelectionColor(color);
    }

    public FilterTextField getFilterField() {
        return filterField;
    }

    public Kandidat getKandidat() {
        return kandidat;
    }

    public void setKandidat(Kandidat kandidat) {
        this.kandidat = kandidat;
    }
}
