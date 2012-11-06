package at.oefg1880.swing.model;

import at.oefg1880.swing.io.Kandidat;
import at.oefg1880.swing.list.FilterKandidatTable;

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
        log.debug(item.toString());
        filterKandidatTable.getModel().getItems().add(item);
        refilter();
    }

    public void refilter() {
        filterItems.clear();
        String term = filterKandidatTable.getFilterField().getText();
        for (int i = 0; i < filterKandidatTable.getModel().getItems().size(); i++) {
            if (filterKandidatTable.getModel().getItems().get(i).getTitleAndName().toLowerCase().indexOf(term.toLowerCase(), 0) != -1) {
                filterItems.add(filterKandidatTable.getModel().getItems().get(i));
            }
        }
//        if (filterItems.size() == 1) {
//            filterKandidatTable.setKandidat(filterItems.get(0));
//        }
        filterKandidatTable.getModel().fireTableDataChanged();
    }

}