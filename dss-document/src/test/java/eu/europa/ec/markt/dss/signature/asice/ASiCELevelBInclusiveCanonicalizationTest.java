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
package eu.europa.ec.markt.dss.signature.asice;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.xml.crypto.dsig.CanonicalizationMethod;

import org.junit.Before;

import eu.europa.ec.markt.dss.SignatureAlgorithm;
import eu.europa.ec.markt.dss.parameter.SignatureParameters;
import eu.europa.ec.markt.dss.service.CertificateService;
import eu.europa.ec.markt.dss.signature.AbstractTestSignature;
import eu.europa.ec.markt.dss.signature.DSSDocument;
import eu.europa.ec.markt.dss.signature.DocumentSignatureService;
import eu.europa.ec.markt.dss.signature.InMemoryDocument;
import eu.europa.ec.markt.dss.signature.MimeType;
import eu.europa.ec.markt.dss.signature.SignatureLevel;
import eu.europa.ec.markt.dss.signature.SignaturePackaging;
import eu.europa.ec.markt.dss.signature.asic.ASiCService;
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.validation102853.CertificateVerifier;
import eu.europa.ec.markt.dss.validation102853.CommonCertificateVerifier;
import eu.europa.ec.markt.dss.validation102853.report.DiagnosticData;

public class ASiCELevelBInclusiveCanonicalizationTest extends AbstractTestSignature {

	private DocumentSignatureService service;
	private SignatureParameters signatureParameters;
	private DSSDocument documentToSign;
	private DSSPrivateKeyEntry privateKeyEntry;

	@Before
	public void init() throws Exception {
		documentToSign = new InMemoryDocument("Hello Wolrd !".getBytes(), "test.text");

		CertificateService certificateService = new CertificateService();
		privateKeyEntry = certificateService.generateCertificateChain(SignatureAlgorithm.RSA_SHA256);

		signatureParameters = new SignatureParameters();
		signatureParameters.bLevel().setSigningDate(new Date());
		signatureParameters.setSigningCertificate(privateKeyEntry.getCertificate());
		signatureParameters.setCertificateChain(privateKeyEntry.getCertificateChain());
		signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
		signatureParameters.setSignatureLevel(SignatureLevel.ASiC_E_BASELINE_B);
		signatureParameters.setSignedInfoCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE);
		signatureParameters.setSignedPropertiesCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE);

		CertificateVerifier certificateVerifier = new CommonCertificateVerifier();
		service = new ASiCService(certificateVerifier);
	}

	@Override
	protected void checkSignatureLevel(DiagnosticData diagnosticData) {
		assertEquals(SignatureLevel.XAdES_BASELINE_B.name(), diagnosticData.getSignatureFormat(diagnosticData.getFirstSignatureId()));
	}

	@Override
	protected DocumentSignatureService getService() {
		return service;
	}

	@Override
	protected SignatureParameters getSignatureParameters() {
		return signatureParameters;
	}

	@Override
	protected MimeType getExpectedMime() {
		return MimeType.ASICE;
	}

	@Override
	protected boolean isBaselineT() {
		return false;
	}

	@Override
	protected boolean isBaselineLTA() {
		return false;
	}

	@Override
	protected DSSDocument getDocumentToSign() {
		return documentToSign;
	}

	@Override
	protected DSSPrivateKeyEntry getPrivateKeyEntry() {
		return privateKeyEntry;
	}

}
