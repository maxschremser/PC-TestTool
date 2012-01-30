package at.oefg1880.swing.frame;

/**
 * Created by IntelliJ IDEA.
 * User: maxschremser
 * Date: 30.01.12
 * Time: 22:53
 * To change this template use File | Settings | File Templates.
 */
public class WFATestToolFrame extends TestToolFrame {
  public WFATestToolFrame(String title) {
    super(title);
  }

  @Override
  public String getImageName() {
    return "resources/wfa_logo.gif";
  }

  @Override
  public String getFavicon() {
    return "resources/wfa_favicon.gif";
  }
}
