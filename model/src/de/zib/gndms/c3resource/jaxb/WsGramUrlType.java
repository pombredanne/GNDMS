//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-661 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.12.11 at 02:32:25 PM CET 
//


package de.zib.gndms.c3resource.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import org.apache.cxf.jaxb.JAXBToStringBuilder;
import org.apache.cxf.jaxb.JAXBToStringStyle;


/**
 * 
 *         Web service endpoint of the WS-Gram installation. Optionally the
 *         attribute jobManager specifies the job manager to be used. This
 *         corresponds to the "-Ft <factory-type>" option of of the
 *         globusrun-ws command.
 *       
 * 
 * <p>Java class for wsGramUrlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wsGramUrlType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>anyURI">
 *       &lt;attribute name="jobManager" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "wsGramUrlType", namespace = "http://c3grid.de/language/resouces/2007-05-16-resources.xsd", propOrder = {
    "value"
})
public class WsGramUrlType {

    @XmlValue
    @XmlSchemaType(name = "anyURI")
    protected String value;
    @XmlAttribute
    @XmlSchemaType(name = "anySimpleType")
    protected String jobManager;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the jobManager property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobManager() {
        return jobManager;
    }

    /**
     * Sets the value of the jobManager property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobManager(String value) {
        this.jobManager = value;
    }

    /**
     * Generates a String representation of the contents of this type.
     * This is an extension method, produced by the 'ts' xjc plugin
     * 
     */
    @Override
    public String toString() {
        return JAXBToStringBuilder.valueOf(this, JAXBToStringStyle.MULTI_LINE_STYLE);
    }

}
