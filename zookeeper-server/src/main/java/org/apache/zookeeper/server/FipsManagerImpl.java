/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.zookeeper.server;

import java.security.Security;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.fips.FipsStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides management functions for FIPS operation.
 */
public class FipsManagerImpl implements FipsManager {

    private static final Logger LOG = LoggerFactory.getLogger(FipsManagerImpl.class);

    /**
     * Ensure FIPS configuration is eady for use.
     */
    public void configure() {

        // FIPS startup and test.
        LOG.info("FIPS: FipsStatus isReady: " + FipsStatus.isReady());
        LOG.info("FIPS: FipsStatus status message: " + FipsStatus.getStatusMessage());
        LOG.info("FIPS: isInApprovedOnlyMode: " + CryptoServicesRegistrar.isInApprovedOnlyMode());

        // List currently installed providers.
        LOG.info("FIPS: Java security providers");
        java.security.Provider[] providers = Security.getProviders();
        for (int i = 0; i < providers.length; i++) {
            LOG.info("FIPS: [" + i + "] " + providers[i]);
        }

        // List algorithms for keystore and trust manager.
        LOG.info("FIPS: KeyManagerFactory default algorithm: " + KeyManagerFactory.getDefaultAlgorithm());
        LOG.info("FIPS: TrustManagerFactory default algorithm: " + TrustManagerFactory.getDefaultAlgorithm());
    }
}