package at.oefg1880.swing.list;

import at.oefg1880.swing.frame.TestToolFrame;
import at.oefg1880.swing.io.Kandidat;
import at.oefg1880.swing.model.KandidatTableModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 13.03.12
 * Time: 11:55
 * To change this template use File | Settings | File Templates.
 */
public class FilteredTable extends KandidatTable {
    private FilterField filterField;
    private int DEFAULT_FIELD_WIDTH = 20;

    public FilteredTable(TestToolFrame frame) {
        this(frame, new ArrayList<Kandidat>());
    }

    public FilteredTable(TestToolFrame frame, ArrayList<Kandidat> items) {
        super(frame, items);
        Iterator<Kandidat> iter = items.iterator();
        setModel(new FilterModel());
        filterField = new FilterField(DEFAULT_FIELD_WIDTH);
        while (iter.hasNext()) {
            addItem(iter.next());
        }
        setText("");
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    setText(((FilterModel) getModel()).getFilterItems().get(rowAtPoint(e.getPoint())).getName());
                }
            }
        });
    }

    public void addItem(Kandidat item) {
        ((FilterModel) getModel()).addElement(item);
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

    public JTextField getFilterField() {
        return filterField;
    }

    private class FilterModel extends KandidatTableModel {
        private ArrayList<Kandidat> filterItems;

        public FilterModel() {
            super(new ArrayList<Kandidat>());
            this.filterItems = new ArrayList<Kandidat>();
        }

        public ArrayList<Kandidat> getFilterItems() {
          return filterItems;
        }

        public int getRowCount() {
            return filterItems != null ? filterItems.size() : 0;
        }

        public int getColumnCount() {
            return 1;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex < filterItems.size()) {
                return filterItems.get(rowIndex);
            } else {
                return null;
            }
        }

        public void addElement(Kandidat item) {
            items.add(item);
            refilter();
        }

        private void refilter() {
            filterItems.clear();
            String term = getFilterField().getText();
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getName().indexOf(term, 0) != -1) {
                    filterItems.add(items.get(i));
                }
            }
            fireTableDataChanged();
        }

    }

    private class FilterField extends JTextField implements DocumentListener {
        public FilterField(int width) {
            super(width);
            getDocument().addDocumentListener(this);
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            ((FilterModel) getModel()).refilter();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            ((FilterModel) getModel()).refilter();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            ((FilterModel) getModel()).refilter();
        }
    }
}
