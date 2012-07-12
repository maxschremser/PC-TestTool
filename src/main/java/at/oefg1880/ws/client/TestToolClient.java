package at.oefg1880.ws.client;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: AT003053
 * Date: 12.07.12
 * Time: 14:53
 * To change this template use File | Settings | File Templates.
 */
public class TestToolClient {
  public static void main(String[] argv) {
      try {
    TestToolService_PortType service = new TestToolServiceServiceLocator().getTestToolService();
          String fragebogen = service.getFragebogen("OEFG_1");
           System.out.println(fragebogen);
      } catch (ServiceException se) {
          se.printStackTrace();
      }catch (RemoteException re) {
          re.printStackTrace();
      }
  }
}