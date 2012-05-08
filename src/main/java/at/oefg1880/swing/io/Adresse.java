package at.oefg1880.swing.io;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 21.03.12
 * Time: 07:52
 * To change this template use File | Settings | File Templates.
 */
public class Adresse {
    private String strasse;
    private int PLZ;
    private String ort;

    public Adresse() {
        super();
    }

    public Adresse(String strasse, int PLZ, String ort) {
        this.strasse = strasse;
        this.PLZ = PLZ;
        this.ort = ort;
    }

    public String getStrasse() {
        return strasse;
    }

    public int getPLZ() {
        return PLZ;
    }

    public String getOrt() {
        return ort;
    }
}
