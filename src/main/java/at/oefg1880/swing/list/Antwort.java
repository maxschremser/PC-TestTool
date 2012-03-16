package at.oefg1880.swing.list;

import java.util.Calendar;
import java.util.GregorianCalendar;

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

  public Antwort(int index, Kandidat kandidat, int percentages, int[] answers) {
    this.index = index;
    this.kandidat = kandidat;
    this.percentages = percentages;
    this.answers = answers;
  }

  public int getIndex() {
    return index;
  }

  public String getName() {
    return kandidat.getName();
  }

  public int getAlter() {
    Calendar cal = new GregorianCalendar(kandidat.getGeburtstag().getYear(), kandidat.getGeburtstag().getMonth(), kandidat.getGeburtstag().getDay());
    Calendar now = new GregorianCalendar();
    int res = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
    if ((cal.get(Calendar.MONTH) > now.get(Calendar.MONTH))
        || (cal.get(Calendar.MONTH) == now.get(Calendar.MONTH)
        && cal.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH))) {
      res--;
    }
    return res;
  }


  public int[] getAnswers() {
    return answers;
  }

  public int getPercentages() {
    return percentages;
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

