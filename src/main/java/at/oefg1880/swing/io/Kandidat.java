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
    private int index;
    private String name;
    private Adresse adresse;
    private String telephone;
    private String email;

    private Date geburtstag;
    private String geburtsort;
    private boolean passPhoto;
    private boolean kursunterlagen;
    private boolean anwesend;

    public Kandidat() {
        super();
    }

    public Kandidat(int index, String name, String strasse, int PLZ, String ort, String telephone, String email, Date geburtstag, String geburtsort) {
        this(index, name, new Adresse(strasse, PLZ, ort), telephone, email, geburtstag, geburtsort);
    }

    public Kandidat(int index, String name, Adresse adresse, String telephone, String email, Date geburtstag, String geburtsort) {
        this();
        this.index = index;
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

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdresse() {
        return adresse.getStrasse() + " " + adresse.getPLZ() + " " + adresse.getOrt();
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getGeburtstag() {
        return geburtstag;
    }

    public void setGeburtstag(Date geburtstag) {
        this.geburtstag = geburtstag;
    }

    public String getGeburtsort() {
        return geburtsort;
    }

    public void setGeburtsort(String geburtsort) {
        this.geburtsort = geburtsort;
    }

    public boolean isPassPhoto() {
        return passPhoto;
    }

    public void setPassPhoto(boolean passPhoto) {
        this.passPhoto = passPhoto;
    }

    public boolean isKursunterlagen() {
        return kursunterlagen;
    }

    public void setKursunterlagen(boolean kursunterlagen) {
        this.kursunterlagen = kursunterlagen;
    }

    public boolean isAnwesend() {
        return anwesend;
    }

    public void setAnwesend(boolean anwesend) {
        this.anwesend = anwesend;
    }
}
