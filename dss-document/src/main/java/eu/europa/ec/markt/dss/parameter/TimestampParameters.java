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
package eu.europa.ec.markt.dss.parameter;

import javax.xml.crypto.dsig.CanonicalizationMethod;

import eu.europa.ec.markt.dss.DSSUtils;
import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.exception.DSSNullException;

/**
 * This class represents the parameters provided when generating specific timestamps in a signature, such as an AllDataObjectsTimestamp or an
 * IndividualDataObjectsTimestamp.
 */
public class TimestampParameters {

	/**
	 * The digest algorithm to provide to the timestamping authority
	 */
	private DigestAlgorithm digestAlgorithm = DigestAlgorithm.SHA256;

	/**
	 * This is the default canonicalization method for XMLDSIG used for timestamps. Another complication arises because of the way that the default canonicalization algorithm
	 * handles namespace declarations; frequently a signed XML document needs to be embedded in another document; in this case the original canonicalization algorithm will not
	 * yield the same result as if the document is treated alone. For this reason, the so-called Exclusive Canonicalization, which serializes XML namespace declarations
	 * independently of the surrounding XML, was created.
	 */
	private String canonicalizationMethod = CanonicalizationMethod.EXCLUSIVE;


	public DigestAlgorithm getDigestAlgorithm() {
		return digestAlgorithm;
	}

	public void setDigestAlgorithm(final DigestAlgorithm digestAlgorithm) {

		if (digestAlgorithm == null) {
			throw new DSSNullException(DigestAlgorithm.class);
		}
		this.digestAlgorithm = digestAlgorithm;
	}

	public String getCanonicalizationMethod() {
		return canonicalizationMethod;
	}

	public void setCanonicalizationMethod(final String canonicalizationMethod) {

		if (DSSUtils.isBlank(canonicalizationMethod)) {
			throw new DSSNullException(String.class, "canonicalizationMethod");
		}
		this.canonicalizationMethod = canonicalizationMethod;
	}

	public String toString() {
		return "TimestampParameters{" +
			  ", digestAlgorithm=" + digestAlgorithm.getName() +
			  ", canonicalizationMethod=" + canonicalizationMethod +
			  "}";
	}
}
