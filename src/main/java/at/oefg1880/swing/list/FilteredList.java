package at.oefg1880.swing.list;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 13.03.12
 * Time: 11:55
 * To change this template use File | Settings | File Templates.
 */
public class FilteredList extends JList {
  private FilterField filterField;
  private int DEFAULT_FIELD_WIDTH = 20;

  public FilteredList() {
    this(new Vector<Kandidat>());
  }

  public FilteredList(Vector<Kandidat> items) {
    super(items);
    Iterator<Kandidat> iter = items.iterator();
    setModel(new FilterModel());
    filterField = new FilterField(DEFAULT_FIELD_WIDTH);
    while (iter.hasNext()) {
      addItem(iter.next());
    }
  }

  @Override
  public void setModel(ListModel model) {
    if (!(model instanceof FilterModel)) throw new IllegalArgumentException();
    super.setModel(model);
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

  private class FilterModel extends AbstractListModel {
    ArrayList<Kandidat> items, filterItems;

    public FilterModel() {
      super();
      items = new ArrayList<Kandidat>();
      filterItems = new ArrayList<Kandidat>();
    }

    @Override
    public int getSize() {
      return filterItems.size();
    }

    @Override
    public Object getElementAt(int index) {
      if (index < filterItems.size()) {
        return filterItems.get(index);
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
        if (items.get(i).toString().indexOf(term, 0) != -1) {
          filterItems.add(items.get(i));
        }
      }
      fireContentsChanged(this, 0, getSize());
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