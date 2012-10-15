/**
 * TestToolServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.oefg1880.ws.client;

public interface TestToolServiceService extends javax.xml.rpc.Service {
    public java.lang.String getTestToolServiceAddress();

    public at.oefg1880.ws.client.TestToolService_PortType getTestToolService() throws javax.xml.rpc.ServiceException;

    public at.oefg1880.ws.client.TestToolService_PortType getTestToolService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
