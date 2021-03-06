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
    private String title;
    private String name;
    private Adresse adresse;
    private String telephone;
    private String email;

    private Date geburtstag;
    private String geburtsort;
    private boolean bPassPhoto;
    private boolean bKursgebuehr;
    private boolean bAnwesend;

    private Antwort antwort;
    private Fragebogen fragebogen;

    public Kandidat() {
        super();
    }

    public Kandidat(String title, String name, String strasse, String PLZ, String ort, String telephone, String email, Date geburtstag, String geburtsort, boolean bPassPhoto, boolean bKursgebuehr, boolean bAnwesend) {
        this(title, name, new Adresse(strasse, PLZ, ort), telephone, email, geburtstag, geburtsort, bPassPhoto, bKursgebuehr, bAnwesend);
    }

    public Kandidat(String title, String name, Adresse adresse, String telephone, String email, Date geburtstag, String geburtsort, boolean bPassPhoto, boolean bKursgebuehr, boolean bAnwesend) {
        this();
        this.title = title;
        this.name = name;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.geburtstag = geburtstag;
        this.geburtsort = geburtsort;
        this.bPassPhoto = bPassPhoto;
        this.bKursgebuehr = bKursgebuehr;
        this.bAnwesend = bAnwesend;
    }

    public void setAntwort(Fragebogen fragebogen, Antwort antwort) {
        this.fragebogen = fragebogen;
        this.antwort = antwort;
    }

    public Fragebogen getFragebogen() {
        return fragebogen;
    }

    public Antwort getAntwort() {
        return antwort;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getTitleAndName() {
        return title != null && title.length() > 0 ? title + " " + name : name;
    }

    public String getPLZ() {
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

    public boolean hasKursgebuehrBezahlt() {
        return bKursgebuehr;
    }

    public void setKursgebuehrBezahlt(boolean bKursgebuehrBezahlt) {
        this.bKursgebuehr = bKursgebuehrBezahlt;
    }

    public boolean isAnwesend() {
        return bAnwesend;
    }

    public void setAnwesend(boolean bAnwesend) {
        this.bAnwesend = bAnwesend;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGeburtstag(Date geburtstag) {
        this.geburtstag = geburtstag;
    }

    public void setGeburtsort(String geburtsort) {
        this.geburtsort = geburtsort;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("Kandidat(");
        stringBuffer.append("title:" + getTitle());
        stringBuffer.append(",name:" + getName());
        stringBuffer.append(",plz:" + getPLZ());
        stringBuffer.append(",ort:" + getOrt());
        stringBuffer.append(")");
        return stringBuffer.toString();
    }
}
