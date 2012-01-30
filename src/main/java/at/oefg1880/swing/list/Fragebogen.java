package at.oefg1880.swing.list;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: sensi
 * Date: 17.10.2010
 * Time: 16:58:50
 * To change this template use File | Settings | File Templates.
 */
public class Fragebogen {
    private String title;
    private int existing;
    private int[] solutions;
    private ArrayList<Antwort> antworten;
    private int index;

    public Fragebogen(int index, String title, int existing, int[] solutions) {
        this.index = index;
        this.title = title;
        this.existing = existing;
        this.solutions = solutions;
        this.antworten = new ArrayList<Antwort>();
    }

    public String getTitle() {
        return title;
    }

    public int getExisting() {
        return existing;
    }

    public int getSolved() {
        return antworten.size();
    }

    public int getUnsolved() {
        int offen = existing - antworten.size();
        return offen > 0 ? offen : 0;
    }

    public int[] getSolutions() {
        return this.solutions;
    }

    public void addAntwort(Antwort antwort) {
        this.antworten.add(antwort);
    }

    public void setAntwort(Antwort antwort) {
        this.antworten.set(antwort.getIndex(), antwort);
    }

    public void removeAntwort(Antwort antwort) {
        this.antworten.remove(antwort);
    }

    public ArrayList<Antwort> getAntworten() {
        return this.antworten;
    }

    public void setExisting(int existing) {
        this.existing = existing;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSolutions(int[] solutions) {
        this.solutions = solutions;
    }
}
