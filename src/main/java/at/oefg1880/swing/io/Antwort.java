package at.oefg1880.swing.io;

/**
 * Created by IntelliJ IDEA.
 * User: sensi
 * Date: 17.10.2010
 * Time: 16:34:37
 * To change this template use File | Settings | File Templates.
 */
public class Antwort {
    private int percentages;
    private Kandidat kandidat;
    private int[] answers;
    private int index;

    public Antwort(Kandidat kandidat, int percentages, int[] answers) {
        this.index++;
        this.kandidat = kandidat;
        this.percentages = percentages;
        this.answers = answers;
    }

    public int getIndex() {
        return index;
    }

    public String getKandidatName() {
        return kandidat.getTitleAndName();
    }

    public int[] getAnswers() {
        return answers;
    }

    public int getPercentages() {
        return percentages;
    }

    public Kandidat getKandidat() {
        return kandidat;
    }

    public void setPercentages(int percentages) {
        this.percentages = percentages;
    }

    public void setKandidat(Kandidat kandidat) {
        this.kandidat = kandidat;
    }

    public void setAnswers(int[] answers) {
        this.answers = answers;
    }
}

