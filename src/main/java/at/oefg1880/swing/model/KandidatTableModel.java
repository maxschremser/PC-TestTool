package at.oefg1880.swing.model;

import at.oefg1880.swing.io.Kandidat;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: AT003053
 * Date: 01.06.12
 * Time: 15:18
 * To change this template use File | Settings | File Templates.
 */
public class KandidatTableModel extends DefaultTableModel {
    ArrayList<Kandidat> items;

    public KandidatTableModel(ArrayList<Kandidat> items) {
        this.items = items;
    }

    @Override
    public int getRowCount() {
        return (items == null) ? 0 : items.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return (items == null) ? null : items.get(rowIndex);
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (aValue != null && aValue instanceof Kandidat)
            items.set(row, (Kandidat) aValue);
    }

    @Override
    public String getColumnName(int column) {
        return "Kandidaten";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Kandidat.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
}