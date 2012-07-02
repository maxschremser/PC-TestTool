package at.oefg1880.swing.model;

import at.oefg1880.swing.io.Kandidat;
import at.oefg1880.swing.list.FilterKandidatTable;
import at.oefg1880.swing.list.FilterTextField;

import javax.swing.*;
import java.util.ArrayList;

public class FilterKandidatTableModel extends KandidatTableModel {
    private FilterKandidatTable filterKandidatTable;
    private ArrayList<Kandidat> filterItems;

    public FilterKandidatTableModel(FilterKandidatTable filterKandidatTable, ArrayList<Kandidat> items) {
        super();
        this.filterKandidatTable = filterKandidatTable;
        this.filterItems = new ArrayList<Kandidat>();
        setItems(items);
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
        filterKandidatTable.getItems().add(item);
        refilter();
    }

    public void refilter() {
        filterItems.clear();
        String term = filterKandidatTable.getFilterField().getText();
        for (int i = 0; i < filterKandidatTable.getItems().size(); i++) {
            if (filterKandidatTable.getItems().get(i).getName().toLowerCase().indexOf(term.toLowerCase(), 0) != -1) {
                filterItems.add(filterKandidatTable.getItems().get(i));
            }
        }
        filterKandidatTable.getModel().fireTableStructureChanged();
    }

}