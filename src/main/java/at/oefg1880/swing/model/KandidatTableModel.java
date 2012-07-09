package at.oefg1880.swing.model;

import at.oefg1880.swing.IConfig;
import at.oefg1880.swing.io.Kandidat;
import at.oefg1880.swing.utils.ResourceHandler;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: AT003053
 * Date: 01.06.12
 * Time: 15:18
 * To change this template use File | Settings | File Templates.
 */
public class KandidatTableModel extends DefaultTableModel implements IConfig {
    protected ArrayList<Kandidat> items;

    public final static String PROPERTY_NAME = "at.oefg1880.swing.model.KandidatTableModel";
    private ResourceHandler rh = ResourceHandler.getInstance();

    public KandidatTableModel() {
        super();
    }

    public KandidatTableModel(ArrayList<Kandidat> items) {
        super();
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
        return rh.getString(PROPERTY_NAME, COLUMN);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Kandidat.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    public ArrayList<Kandidat> getItems() {
        return items;
    }

    public void setItems(ArrayList<Kandidat> items) {
        this.items = items;
    }
}