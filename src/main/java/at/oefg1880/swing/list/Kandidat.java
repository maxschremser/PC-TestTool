package at.oefg1880.swing.list;

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
  private String telephon;
  private String email;

  private Date geburtstag;
  private String geburtsort;
  private boolean passPhoto;
  private boolean kursunterlagen;
  private boolean pruefungsgebuehr;

  public Kandidat() {
    super();
  }

  public Kandidat(int index, String name, Date geburtstag) {
    this();
    this.index = index;
    this.name = name;
    this.geburtstag = geburtstag;
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

  public Adresse getAdresse() {
    return adresse;
  }

  public void setAdresse(Adresse adresse) {
    this.adresse = adresse;
  }

  public String getTelephon() {
    return telephon;
  }

  public void setTelephon(String telephon) {
    this.telephon = telephon;
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

  public void setBirthDate(Date geburtstag) {
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

  public boolean isPruefungsgebuehr() {
    return pruefungsgebuehr;
  }

  public void setPruefungsgebuehr(boolean pruefungsgebuehr) {
    this.pruefungsgebuehr = pruefungsgebuehr;
  }

  protected class Adresse {
    private String strasse;
    private int PLZ;
    private String ort;

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
}
