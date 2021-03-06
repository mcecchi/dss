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
package eu.europa.ec.markt.dss.signature.token;

import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.IOUtils;

import eu.europa.ec.markt.dss.exception.DSSException;

/**
 * Class holding all Java KeyStore file access logic.
 *
 */
public class JKSSignatureToken extends AbstractSignatureTokenConnection {

	private char[] password;

	protected KeyStore keyStore = null;

	/**
	 * Creates a SignatureTokenConnection with the provided path to Java KeyStore file and password.
	 *
	 * @param ksUrlLocation
	 * @param ksPassword
	 */
	public JKSSignatureToken(String ksUrlLocation, String ksPassword) {

		InputStream ksStream = null;
		try {

			final URL ksLocation = new URL(ksUrlLocation);
			keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			ksStream = ksLocation.openStream();
			password = (ksPassword == null) ? null : ksPassword.toCharArray();
			keyStore.load(ksStream, password);
		} catch (Exception e) {
			throw new DSSException(e);
		} finally {
			IOUtils.closeQuietly(ksStream);
		}
	}

	@Override
	public void close() {
		for (int ii = 0; ii < password.length; ii++) {
			password[ii] = 0;
		}
	}

	/**
	 * Retrieves all the available keys (private keys entries) from the Java KeyStore.
	 *
	 * @return
	 * @throws DSSException
	 */
	@Override
	public List<DSSPrivateKeyEntry> getKeys() throws DSSException {

		final List<DSSPrivateKeyEntry> list = new ArrayList<DSSPrivateKeyEntry>();

		try {
			final PasswordProtection pp = new KeyStore.PasswordProtection(password);
			final Enumeration<String> aliases = keyStore.aliases();
			while (aliases.hasMoreElements()) {

				final String alias = aliases.nextElement();
				if (keyStore.isKeyEntry(alias)) {

					final PrivateKeyEntry entry = (PrivateKeyEntry) keyStore.getEntry(alias, pp);
					list.add(new KSPrivateKeyEntry(entry));
				}
			}
		} catch (Exception e) {
			throw new DSSException(e);
		}
		return list;
	}
}
