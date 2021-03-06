//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.16 at 10:07:03 PM CET 
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
package eu.europa.ec.markt.tsl.jaxb.ecc;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.europa.ec.markt.tsl.jaxb.ecc package. 
 *
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Qualifications_QNAME = new QName("http://uri.etsi.org/TrstSvc/SvcInfoExt/eSigDir-1999-93-EC-TrustedList/#", "Qualifications");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.europa.ec.markt.tsl.jaxb.ecc
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link QualificationsType }
     * 
     */
    public QualificationsType createQualificationsType() {
        return new QualificationsType();
    }

    /**
     * Create an instance of {@link QualificationElementType }
     * 
     */
    public QualificationElementType createQualificationElementType() {
        return new QualificationElementType();
    }

    /**
     * Create an instance of {@link CriteriaListType }
     * 
     */
    public CriteriaListType createCriteriaListType() {
        return new CriteriaListType();
    }

    /**
     * Create an instance of {@link KeyUsageBitType }
     * 
     */
    public KeyUsageBitType createKeyUsageBitType() {
        return new KeyUsageBitType();
    }

    /**
     * Create an instance of {@link QualifierType }
     * 
     */
    public QualifierType createQualifierType() {
        return new QualifierType();
    }

    /**
     * Create an instance of {@link QualifiersType }
     * 
     */
    public QualifiersType createQualifiersType() {
        return new QualifiersType();
    }

    /**
     * Create an instance of {@link KeyUsageType }
     * 
     */
    public KeyUsageType createKeyUsageType() {
        return new KeyUsageType();
    }

    /**
     * Create an instance of {@link PoliciesListType }
     * 
     */
    public PoliciesListType createPoliciesListType() {
        return new PoliciesListType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QualificationsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uri.etsi.org/TrstSvc/SvcInfoExt/eSigDir-1999-93-EC-TrustedList/#", name = "Qualifications")
    public JAXBElement<QualificationsType> createQualifications(QualificationsType value) {
        return new JAXBElement<QualificationsType>(_Qualifications_QNAME, QualificationsType.class, null, value);
    }

}
