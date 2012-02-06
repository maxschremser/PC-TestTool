package at.oefg1880.swing.list;

/**
 * Created by IntelliJ IDEA.
 * User: sensi
 * Date: 17.10.2010
 * Time: 16:34:37
 * To change this template use File | Settings | File Templates.
 */
public class Antwort {
  private String name;
  private String alter;
  private String geschlecht;
  private int percentages;
  private int[] answers;
  private int index;

  public Antwort(int index, String name, String alter, String geschlecht, int percentages, int[] answers) {
    this.index = index;
    this.name = name;
    this.alter = alter;
    this.geschlecht = geschlecht;
    this.percentages = percentages;
    this.answers = answers;
  }

  public int getIndex() {
    return index;
  }

  public String getName() {
    return name;
  }

  public String getAlter() {
    return alter;
  }

  public String getGeschlecht() {
    return geschlecht;
  }

  public int[] getAnswers() {
    return answers;
  }

  public int getPercentages() {
    return percentages;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAlter(String alter) {
    this.alter = alter;
  }

  public void setGeschlecht(String geschlecht) {
    this.geschlecht = geschlecht;
  }

  public void setPercentages(int percentages) {
    this.percentages = percentages;
  }

  public void setAnswers(int[] answers) {
    this.answers = answers;
  }
}

