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
    public final static String VERSION = "VERSION";
    public final static String MAJOR = "MAJOR";
    public final static String MINOR = "MINOR";

    public final static String POS_X = "x";
    public final static String POS_Y = "y";

    public final String DATE_PATTERN = "d/M/yyyy";
    public final Color color_1 = new Color(252, 253, 254), color_2 = new Color(224, 229, 245);
    public final Color selectedTextForeground = new Color(136, 181, 232), selectedListForeground = new Color(199, 215, 234), finishedColor = new Color(0, 100, 55);
    public final static int VERTICAL = 0, HORIZONTAL = 1, DIAGONAL = 2, PLAIN_1 = 3, PLAIN_2 = 4;
    public final static String OK = "OK", CANCEL = "CANCEL";
}