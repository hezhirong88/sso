/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hw.it.welink.sso.proxy.po;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import com.google.gson.Gson;


/**
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public class JWKParser {

	private Gson gson = new Gson();
	private JWK jwk;

	private JWKParser() {
	}

	public JWKParser(JWK jwk) {
		this.jwk = jwk;
	}

	public static JWKParser create() {
		return new JWKParser();
	}

	public static JWKParser create(JWK jwk) {
		return new JWKParser(jwk);
	}

	public JWKParser parse(String jwk) {
		try {
			// this.jwk = JsonSerialization.mapper.readValue(jwk, JWK.class);
			this.jwk = gson.fromJson(jwk, JWK.class);
			return this;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public JWK getJwk() {
		return jwk;
	}

	public PublicKey toPublicKey() {
		String keyType = jwk.getKty();
		if (isKeyTypeSupported(keyType)) {
			BigInteger modulus = new BigInteger(1, Base64Url.decode(jwk.getN()));
			BigInteger publicExponent = new BigInteger(1, Base64Url.decode(jwk
					.getE()));

			try {
				return KeyFactory.getInstance("RSA").generatePublic(
						new RSAPublicKeySpec(modulus, publicExponent));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new RuntimeException("Unsupported keyType " + keyType);
		}
	}

	public boolean isKeyTypeSupported(String keyType) {
		return RSAPublicJWK.RSA.equals(keyType);
	}

}
