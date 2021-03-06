//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.17 at 03:26:09 PM CET 
//


/**
 * DSS - Digital Signature Services
 * Copyright (C) 2015 European Commission, provided under the CEF programme
 *
 * This file is part of the "DSS - Digital Signature Services" project.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package eu.europa.ec.markt.dss.validation102853.data.diagnostic;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * 
 *
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="QCP" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="QCPPlus" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="QCC" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="QCSSCD" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "qcp",
    "qcpPlus",
    "qcc",
    "qcsscd"
})
public class XmlQCStatement {

    @XmlElement(name = "QCP")
    protected boolean qcp;
    @XmlElement(name = "QCPPlus")
    protected boolean qcpPlus;
    @XmlElement(name = "QCC")
    protected boolean qcc;
    @XmlElement(name = "QCSSCD")
    protected boolean qcsscd;

    /**
     * Gets the value of the qcp property.
     * 
     */
    public boolean isQCP() {
        return qcp;
    }

    /**
     * Sets the value of the qcp property.
     * 
     */
    public void setQCP(boolean value) {
        this.qcp = value;
    }

    /**
     * Gets the value of the qcpPlus property.
     * 
     */
    public boolean isQCPPlus() {
        return qcpPlus;
    }

    /**
     * Sets the value of the qcpPlus property.
     * 
     */
    public void setQCPPlus(boolean value) {
        this.qcpPlus = value;
    }

    /**
     * Gets the value of the qcc property.
     * 
     */
    public boolean isQCC() {
        return qcc;
    }

    /**
     * Sets the value of the qcc property.
     * 
     */
    public void setQCC(boolean value) {
        this.qcc = value;
    }

    /**
     * Gets the value of the qcsscd property.
     * 
     */
    public boolean isQCSSCD() {
        return qcsscd;
    }

    /**
     * Sets the value of the qcsscd property.
     * 
     */
    public void setQCSSCD(boolean value) {
        this.qcsscd = value;
    }

}
