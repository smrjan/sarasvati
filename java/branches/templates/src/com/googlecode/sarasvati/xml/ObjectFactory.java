//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.0 in JDK 1.6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2008.09.12 at 10:39:12 AM EDT
//

package com.googlecode.sarasvati.xml;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.googlecode.sarasvati.xml package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory
{

  /**
   * Create a new ObjectFactory that can be used to create new instances of
   * schema derived classes for package: com.googlecode.sarasvati.xml
   * 
   */
  public ObjectFactory ()
  {
    // default constructor
  }

  /**
   * Create an instance of {@link XmlExternal }
   */
  public XmlExternal createExternal ()
  {
    return new XmlExternal();
  }

  /**
   * Create an instance of {@link XmlExternalArc }
   */
  public XmlExternalArc createExternalArc ()
  {
    return new XmlExternalArc();
  }

  /**
   * Create an instance of {@link XmlArc }
   */
  public XmlArc createArc ()
  {
    return new XmlArc();
  }

  /**
   * Create an instance of {@link XmlNode }
   */
  public XmlNode createNode ()
  {
    return new XmlNode();
  }

  /**
   * Create an instance of {@link XmlProcessDefinition }
   * 
   */
  public XmlProcessDefinition createProcessDefinition ()
  {
    return new XmlProcessDefinition();
  }
}