package at.oefg1880.swing;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: schremse
 * Date: 21.04.2010
 * Time: 14:57:54
 * To change this template use File | Settings | File Templates.
 */
public interface IConfig {
  // SAR GLOBAL CONFIGURATION PROPERTIES
  public final static String NUM_ANSWERS = "numAnswers";
  public final static String NUM_QUESTIONS = "numQuestions";
  public final static String NUM_CATEGORIES = "numCategories";
  public final static String NUM_SPREADS = "numSpreads";
  public final static String LICENSE_FILE = "license.file";
  public final static String PUBLIC_KEY = "public.key";

  public final static String LICENSE_ISSUE_DATE = "issue-date";
  public final static String LICENSE_VALID_DATE = "valid-date";

  public final Color color_1 = new Color(255, 251, 234), color_2 = new Color(219, 216, 201);
  public final Color selectedTextForeground = new Color(136, 181, 232), selectedListForeground = new Color(199, 215, 234), finishedColor = new Color(0, 100, 55);
  public final static int VERTICAL = 0, HORIZONTAL = 1, DIAGONAL = 2, PLAIN_1 = 3, PLAIN_2 = 4;
  public final static String OK = "OK", CANCEL = "CANCEL";
}