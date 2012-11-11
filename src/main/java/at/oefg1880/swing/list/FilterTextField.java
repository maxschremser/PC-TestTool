package at.oefg1880.swing.list;

import at.oefg1880.swing.dialog.AntwortDialog;
import at.oefg1880.swing.io.Kandidat;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FilterTextField extends JTextField implements DocumentListener {
    private final FilterKandidatTable filterKandidatTable;
    private boolean foundKandidat = false;

    public FilterTextField(FilterKandidatTable filterKandidatTable, int width) {
        super(width);
        this.filterKandidatTable = filterKandidatTable;
        addDocumentListener();
    }

    public void addDocumentListener() {
        getDocument().addDocumentListener(this);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        filterKandidatTable.getModel().refilter();
        if (filterKandidatTable.getModel().getFilterItems().size() == 1 && !foundKandidat) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    Kandidat kandidat = filterKandidatTable.getModel().getFilterItems().get(0);
                    filterKandidatTable.setKandidat(kandidat);
                    setText(kandidat.getTitleAndName());
                    foundKandidat = true;
                    AntwortDialog antwortDialog = filterKandidatTable.getFrame().getFragebogenPanel().getAntwortDialog();
                    antwortDialog.getAntwortPanel(antwortDialog).getAntwortTextField(0).requestFocus();
                }
            });
        }
        foundKandidat = false;
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        filterKandidatTable.getModel().refilter();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        filterKandidatTable.getModel().refilter();
    }
}