package com.example.springbootcassandra;

import com.datastax.driver.core.RemoteEndpointAwareJdkSSLOptions;
import com.datastax.driver.core.SSLOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.net.ssl.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

@Component
public class SslOptionsFactoryBean extends AbstractFactoryBean<SSLOptions> {

    @Value("classpath:${cassandra.ssl.keyStore.classpath:ssl/cassandra.keystore}")
    private Resource keyStore;
    @Value("${cassandra.ssl.keyStore.password}")
    private String keyStorePassword;

    @Value("classpath:${cassandra.ssl.trustStore.classpath:ssl/cassandra.truststore}")
    private Resource trustStore;
    @Value("${cassandra.ssl.trustStore.password}")
    private String trustStorePassword;

    @Override
    public Class<?> getObjectType() {
        return SSLOptions.class;
    }

    @Override
    protected SSLOptions createInstance() throws Exception {

        if (!keyStore.exists()) throw new RuntimeException(new FileNotFoundException("keyStore"));
        if (!trustStore.exists()) throw new RuntimeException(new FileNotFoundException("trustStore"));

        KeyManager[] keyManagers = getKeyStore() != null
                ? createKeyManagerFactory(getKeyStore(), getKeyStorePassword()).getKeyManagers() : null;

        TrustManager[] trustManagers = getTrustStore() != null
                ? createTrustManagerFactory(getTrustStore(), getTrustStorePassword()).getTrustManagers() : null;

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, trustManagers, null);

        return RemoteEndpointAwareJdkSSLOptions.builder().withSSLContext(sslContext).build();
    }

    private static KeyManagerFactory createKeyManagerFactory(Resource keystoreFile, String storePassword)
            throws GeneralSecurityException, IOException {

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

        try (InputStream inputStream = keystoreFile.getInputStream()) {
            keyStore.load(inputStream, StringUtils.hasText(storePassword) ? storePassword.toCharArray() : null);
        }

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, StringUtils.hasText(storePassword) ? storePassword.toCharArray() : new char[0]);

        return keyManagerFactory;
    }

    private static TrustManagerFactory createTrustManagerFactory(Resource trustFile, String storePassword)
            throws GeneralSecurityException, IOException {

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());

        try (InputStream inputStream = trustFile.getInputStream()) {
            trustStore.load(inputStream, StringUtils.hasText(storePassword) ? storePassword.toCharArray() : null);
        }

        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        return trustManagerFactory;
    }

    public Resource getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(Resource keyStore) {
        this.keyStore = keyStore;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public Resource getTrustStore() {
        return trustStore;
    }

    public void setTrustStore(Resource trustStore) {
        this.trustStore = trustStore;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }
}
