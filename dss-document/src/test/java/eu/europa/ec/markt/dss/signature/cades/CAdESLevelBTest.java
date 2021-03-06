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
package eu.europa.ec.markt.dss.signature.cades;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.xml.security.utils.Base64;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.asn1.cms.SignerIdentifier;
import org.bouncycastle.asn1.cms.SignerInfo;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.DigestInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.markt.dss.DSSUtils;
import eu.europa.ec.markt.dss.DigestAlgorithm;
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
import eu.europa.ec.markt.dss.signature.token.DSSPrivateKeyEntry;
import eu.europa.ec.markt.dss.validation102853.CertificateVerifier;
import eu.europa.ec.markt.dss.validation102853.CommonCertificateVerifier;

public class CAdESLevelBTest extends AbstractTestSignature {

	private static final String HELLO_WORLD = "Hello World";

	private static Logger logger = LoggerFactory.getLogger(CAdESLevelBTest.class);

	private DocumentSignatureService service;
	private SignatureParameters signatureParameters;
	private DSSDocument documentToSign;
	private DSSPrivateKeyEntry privateKeyEntry;

	@Before
	public void init() throws Exception {
		documentToSign = new InMemoryDocument(HELLO_WORLD.getBytes());

		CertificateService certificateService = new CertificateService();
		privateKeyEntry = certificateService.generateCertificateChain(SignatureAlgorithm.RSA_SHA256);

		signatureParameters = new SignatureParameters();
		signatureParameters.bLevel().setSigningDate(new Date());
		signatureParameters.setSigningCertificate(privateKeyEntry.getCertificate());
		signatureParameters.setCertificateChain(privateKeyEntry.getCertificateChain());
		signatureParameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
		signatureParameters.setSignatureLevel(SignatureLevel.CAdES_BASELINE_B);

		CertificateVerifier certificateVerifier = new CommonCertificateVerifier();
		service = new CAdESService(certificateVerifier);

	}

	@Override
	protected void onDocumentSigned(byte[] byteArray) {
		try {

			ASN1InputStream asn1sInput = new ASN1InputStream(byteArray);
			ASN1Sequence asn1Seq = (ASN1Sequence) asn1sInput.readObject();

			logger.info("SEQ : " + asn1Seq.toString());

			assertEquals(2, asn1Seq.size());

			ASN1ObjectIdentifier oid = ASN1ObjectIdentifier.getInstance(asn1Seq.getObjectAt(0));
			assertEquals(PKCSObjectIdentifiers.signedData, oid);
			logger.info("OID : " + oid.toString());

			ASN1TaggedObject taggedObj = DERTaggedObject.getInstance(asn1Seq.getObjectAt(1));

			logger.info("TAGGED OBJ : " + taggedObj.toString());

			ASN1Primitive object = taggedObj.getObject();
			logger.info("OBJ : " + object.toString());

			SignedData signedData = SignedData.getInstance(object);
			logger.info("SIGNED DATA : " + signedData.toString());

			ASN1Set digestAlgorithms = signedData.getDigestAlgorithms();
			logger.info("DIGEST ALGOS : " + digestAlgorithms.toString());

			ContentInfo encapContentInfo = signedData.getEncapContentInfo();
			logger.info("ENCAPSULATED CONTENT INFO : " + encapContentInfo);

			ASN1Set certificates = signedData.getCertificates();
			logger.info("CERTIFICATES (" + certificates.size() + ") : " + certificates);

			List<X509Certificate> foundCertificates = new ArrayList<X509Certificate>();
			for (int i = 0; i < certificates.size(); i++) {
				ASN1Sequence seqCertif = ASN1Sequence.getInstance(certificates.getObjectAt(i));
				logger.info("SEQ cert " + i + " : " + seqCertif);

				X509CertificateHolder certificateHolder = new X509CertificateHolder(seqCertif.getEncoded());
				X509Certificate certificate = new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getCertificate(
						certificateHolder);

				certificate.checkValidity();

				logger.info("Cert " + i + " : " + certificate);

				foundCertificates.add(certificate);
			}

			ASN1Set crLs = signedData.getCRLs();
			logger.info("CRLs : " + crLs);

			ASN1Set signerInfosAsn1 = signedData.getSignerInfos();
			logger.info("SIGNER INFO ASN1 : " + signerInfosAsn1.toString());
			assertEquals(1, signerInfosAsn1.size());

			ASN1Sequence seqSignedInfo = ASN1Sequence.getInstance(signerInfosAsn1.getObjectAt(0));

			SignerInfo signedInfo = SignerInfo.getInstance(seqSignedInfo);
			logger.info("SIGNER INFO : " + signedInfo.toString());

			SignerIdentifier sid = signedInfo.getSID();
			logger.info("SIGNER IDENTIFIER : " + sid.getId());

			IssuerAndSerialNumber issuerAndSerialNumber = IssuerAndSerialNumber.getInstance(signedInfo.getSID());
			logger.info("ISSUER AND SN : " + issuerAndSerialNumber.toString());

			BigInteger serial = issuerAndSerialNumber.getSerialNumber().getValue();
			X500Name name = issuerAndSerialNumber.getName();

			X509Certificate signerCertificate = null;
			for (X509Certificate x509Certificate : foundCertificates) {
				// TODO check issuer name
				if (serial.equals(x509Certificate.getSerialNumber())) {
					signerCertificate = x509Certificate;
				}
			}
			assertNotNull(signerCertificate);

			ASN1OctetString encryptedDigest = signedInfo.getEncryptedDigest();
			logger.info("ENCRYPT DIGEST : " + encryptedDigest.toString());

			ASN1Sequence seq = ASN1Sequence.getInstance(object);

			ASN1Integer version = ASN1Integer.getInstance(seq.getObjectAt(0));
			logger.info("VERSION : " + version.toString());

			ASN1Set digestManualSet = ASN1Set.getInstance(seq.getObjectAt(1));
			logger.info("DIGEST SET : " + digestManualSet.toString());
			assertEquals(digestAlgorithms, digestManualSet);

			ASN1Sequence seqDigest = ASN1Sequence.getInstance(digestManualSet.getObjectAt(0));
			// assertEquals(1, seqDigest.size());

			ASN1ObjectIdentifier oidDigestAlgo = ASN1ObjectIdentifier.getInstance(seqDigest.getObjectAt(0));
			assertEquals(DigestAlgorithm.SHA256.getOid(), oidDigestAlgo);

			ASN1Sequence seqEncapsulatedInfo = ASN1Sequence.getInstance(seq.getObjectAt(2));
			logger.info("ENCAPSULATED INFO : " + seqEncapsulatedInfo.toString());

			ASN1ObjectIdentifier oidContentType = ASN1ObjectIdentifier.getInstance(seqEncapsulatedInfo.getObjectAt(0));
			logger.info("OID CONTENT TYPE : " + oidContentType.toString());

			ASN1TaggedObject taggedContent = DERTaggedObject.getInstance(seqEncapsulatedInfo.getObjectAt(1));

			ASN1OctetString contentOctetString = ASN1OctetString.getInstance(taggedContent.getObject());
			String content = new String(contentOctetString.getOctets());
			assertEquals(HELLO_WORLD, content);
			logger.info("CONTENT : " + content);

			byte[] digest = DSSUtils.digest(DigestAlgorithm.SHA256, HELLO_WORLD.getBytes());
			String encodeHexDigest = Hex.toHexString(digest);
			logger.info("CONTENT DIGEST COMPUTED : " + encodeHexDigest);

			ASN1Set authenticatedAttributes = signedInfo.getAuthenticatedAttributes();
			logger.info("AUTHENTICATED ATTRIBUTES : " + authenticatedAttributes.toString());

			// ASN1Sequence seqAuthAttrib = ASN1Sequence.getInstance(authenticatedAttributes.getObjectAt(0));

			logger.info("Nb Auth Attributes : " + authenticatedAttributes.size());

			String embeddedDigest = StringUtils.EMPTY;
			for (int i = 0; i < authenticatedAttributes.size(); i++) {
				ASN1Sequence authAttrSeq = ASN1Sequence.getInstance(authenticatedAttributes.getObjectAt(i));
				logger.info(authAttrSeq.toString());
				ASN1ObjectIdentifier attrOid = ASN1ObjectIdentifier.getInstance(authAttrSeq.getObjectAt(0));
				if (PKCSObjectIdentifiers.pkcs_9_at_messageDigest.equals(attrOid)) {
					ASN1Set setMessageDigest = ASN1Set.getInstance(authAttrSeq.getObjectAt(1));
					ASN1OctetString asn1ObjString = ASN1OctetString.getInstance(setMessageDigest.getObjectAt(0));
					embeddedDigest = Hex.toHexString(asn1ObjString.getOctets());
				}
			}
			assertEquals(encodeHexDigest, embeddedDigest);

			ASN1OctetString encryptedInfoOctedString = signedInfo.getEncryptedDigest();
			String signatureValue = Hex.toHexString(encryptedInfoOctedString.getOctets());

			logger.info("SIGNATURE VALUE : " + signatureValue);

			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, signerCertificate);
			byte[] decrypted = cipher.doFinal(encryptedInfoOctedString.getOctets());

			ASN1InputStream inputDecrypted = new ASN1InputStream(decrypted);

			ASN1Sequence seqDecrypt = (ASN1Sequence) inputDecrypted.readObject();
			logger.info("Decrypted : " + seqDecrypt);

			DigestInfo digestInfo = new DigestInfo(seqDecrypt);
			assertEquals(oidDigestAlgo, digestInfo.getAlgorithmId().getAlgorithm());

			String decryptedDigestEncodeBase64 = Base64.encode(digestInfo.getDigest());
			logger.info("Decrypted Base64 : " + decryptedDigestEncodeBase64);

			byte[] encoded = signedInfo.getAuthenticatedAttributes().getEncoded();
			MessageDigest messageDigest = MessageDigest.getInstance(DigestAlgorithm.SHA256.getName());
			byte[] digestOfAuthenticatedAttributes = messageDigest.digest(encoded);

			String computedDigestEncodeBase64 = Base64.encode(digestOfAuthenticatedAttributes);
			logger.info("Computed Base64 : " + computedDigestEncodeBase64);

			assertEquals(decryptedDigestEncodeBase64, computedDigestEncodeBase64);

			IOUtils.closeQuietly(asn1sInput);
			IOUtils.closeQuietly(inputDecrypted);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			fail(e.getMessage());
		}
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
		return MimeType.PKCS7;
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
