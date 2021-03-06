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
package eu.europa.ec.markt.dss.cookbook.example.sign;

import java.io.IOException;
import java.util.List;

import eu.europa.ec.markt.dss.DSSUtils;
import eu.europa.ec.markt.dss.DigestAlgorithm;
import eu.europa.ec.markt.dss.cookbook.example.Cookbook;
import eu.europa.ec.markt.dss.exception.DSSException;
import eu.europa.ec.markt.dss.parameter.SignatureParameters;
import eu.europa.ec.markt.dss.signature.DSSDocument;
import eu.europa.ec.markt.dss.signature.SignatureLevel;
import eu.europa.ec.markt.dss.signature.SignaturePackaging;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.signature.token.MSCAPISignatureToken;
import eu.europa.ec.markt.dss.signature.xades.XAdESService;
import eu.europa.ec.markt.dss.validation102853.CommonCertificateVerifier;

/**
 * How to sign using MS-CAPI.
 */
public class SignXmlXadesBWithMSCAPI extends Cookbook {

	public static void main(String[] args) throws DSSException, IOException {
		// GET document to be signed -
		// Return DSSDocument toSignDocument
		prepareXmlDoc();

		// Creation of MS-CAPI signature token
		signingToken = new MSCAPISignatureToken();
		List<DSSPrivateKeyEntry> list = signingToken.getKeys();
		// Chose the right private key entry from store. The index will depend of the number of the certificates on your card.
		System.out.println(signingToken.getKeys().size());
		privateKey = signingToken.getKeys().get(0);

		// Preparing parameters for the PAdES signature
		SignatureParameters parameters = new SignatureParameters();
		// We choose the level of the signature (-B, -T, -LT, -LTA).
		parameters.setSignatureLevel(SignatureLevel.XAdES_BASELINE_B);
		// We choose the type of the signature packaging (ENVELOPING, DETACHED).
		parameters.setSignaturePackaging(SignaturePackaging.ENVELOPED);
		// We set the digest algorithm to use with the signature algorithm. You must use the
		// same parameter when you invoke the method sign on the token.
		parameters.setDigestAlgorithm(DigestAlgorithm.SHA1);
		// We choose the private key with the certificate and corresponding certificate
		// chain.
		parameters.setPrivateKeyEntry(privateKey);

		// Create common certificate verifier
		CommonCertificateVerifier commonCertificateVerifier = new CommonCertificateVerifier();
		// Create CAdES xadesService for signature
		XAdESService xadesService = new XAdESService(commonCertificateVerifier);

		// Get the SignedInfo segment that need to be signed.
		byte[] dataToSign = xadesService.getDataToSign(toSignDocument, parameters);

		// This function obtains the signature value for signed information using the
		// private key and specified algorithm
		DigestAlgorithm digestAlgorithm = parameters.getDigestAlgorithm();
		byte[] signatureValue = signingToken.sign(dataToSign, digestAlgorithm, privateKey);

		// We invoke the xadesService to sign the document with the signature value obtained in
		// the previous step.
		DSSDocument signedDocument = xadesService.signDocument(toSignDocument, parameters, signatureValue);

		//DSSUtils.copy(signedDocument.openStream(), System.out);
		DSSUtils.saveToFile(signedDocument.openStream(), "target/signedXmlXadesMSCapi.xml");
	}
}
