package at.oefg1880.swing.io;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: sensi
 * Date: 17.10.2010
 * Time: 16:58:50
 * To change this template use File | Settings | File Templates.
 */
public class Kandidat {
    private static int index;
    private String title;
    private String name;
    private Adresse adresse;
    private String telephone;
    private String email;

    private Date geburtstag;
    private String geburtsort;
    private boolean bPassPhoto;
    private boolean bKursunterlagen;
    private boolean bAnwesend;

    public Kandidat() {
        super();
    }

    public Kandidat(String title, String name, String strasse, int PLZ, String ort, String telephone, String email, Date geburtstag, String geburtsort) {
        this(title, name, new Adresse(strasse, PLZ, ort), telephone, email, geburtstag, geburtsort);
    }

    public Kandidat(String title, String name, Adresse adresse, String telephone, String email, Date geburtstag, String geburtsort) {
        this();
        this.index++;
        this.title = title;
        this.name = name;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.geburtstag = geburtstag;
        this.geburtsort = geburtsort;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return (title + " " + name).trim();
    }

    public int getPLZ() {
        return adresse.getPLZ();
    }

    public String getStrasse() {
        return adresse.getStrasse();
    }

    public String getOrt() {
        return adresse.getOrt();
    }

    public Date getGeburtstag() {
        return geburtstag;
    }

    public String getGeburtsort() {
        return geburtsort;
    }

    public boolean hasPassPhoto() {
        return bPassPhoto;
    }

    public void setPassPhoto(boolean bPassPhoto) {
        this.bPassPhoto = bPassPhoto;
    }

    public boolean hasKursunterlagen() {
        return bKursunterlagen;
    }

    public void setbKursunterlagen(boolean bKursunterlagen) {
        this.bKursunterlagen = bKursunterlagen;
    }

    public boolean isAnwesend() {
        return bAnwesend;
    }

    public void setAnwesend(boolean bAnwesend) {
        this.bAnwesend = bAnwesend;
    }
}
