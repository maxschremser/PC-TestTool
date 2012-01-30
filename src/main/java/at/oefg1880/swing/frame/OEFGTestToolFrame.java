package at.oefg1880.swing.frame;

/**
 * Created by IntelliJ IDEA.
 * User: maxschremser
 * Date: 30.01.12
 * Time: 22:53
 * To change this template use File | Settings | File Templates.
 */
public class OEFGTestToolFrame extends TestToolFrame {
  public OEFGTestToolFrame(String title) {
    super(title);
  }

  @Override
  public String getImageName() {
    return "resources/oefg1880_logo.gif";
  }

  @Override
  public String getFavicon() {
    return "resources/oefg_favicon.gif";
  }
}
