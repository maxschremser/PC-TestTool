/**
 * TestToolServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package at.oefg1880.ws.client;

public class TestToolServiceServiceLocator extends org.apache.axis.client.Service implements at.oefg1880.ws.client.TestToolServiceService {

    public TestToolServiceServiceLocator() {
    }


    public TestToolServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TestToolServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TestToolService
    private java.lang.String TestToolService_address = "http://localhost:8080/services/TestToolService";

    public java.lang.String getTestToolServiceAddress() {
        return TestToolService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TestToolServiceWSDDServiceName = "TestToolService";

    public java.lang.String getTestToolServiceWSDDServiceName() {
        return TestToolServiceWSDDServiceName;
    }

    public void setTestToolServiceWSDDServiceName(java.lang.String name) {
        TestToolServiceWSDDServiceName = name;
    }

    public at.oefg1880.ws.client.TestToolService_PortType getTestToolService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TestToolService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTestToolService(endpoint);
    }

    public at.oefg1880.ws.client.TestToolService_PortType getTestToolService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            at.oefg1880.ws.client.TestToolServiceSoapBindingStub _stub = new at.oefg1880.ws.client.TestToolServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getTestToolServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTestToolServiceEndpointAddress(java.lang.String address) {
        TestToolService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (at.oefg1880.ws.client.TestToolService_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                at.oefg1880.ws.client.TestToolServiceSoapBindingStub _stub = new at.oefg1880.ws.client.TestToolServiceSoapBindingStub(new java.net.URL(TestToolService_address), this);
                _stub.setPortName(getTestToolServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("TestToolService".equals(inputPortName)) {
            return getTestToolService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://example", "TestToolServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://example", "TestToolService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("TestToolService".equals(portName)) {
            setTestToolServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
