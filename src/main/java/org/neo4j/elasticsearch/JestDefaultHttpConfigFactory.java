package org.neo4j.elasticsearch;

import io.searchbox.client.config.HttpClientConfig;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.nio.conn.SchemeIOSessionStrategy;
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.*;

public class JestDefaultHttpConfigFactory {
  public static HttpClientConfig getConfigFor(final String hostName, final Boolean discovery) throws URISyntaxException, GeneralSecurityException {
    HttpClientConfig.Builder clientConfig = new HttpClientConfig.Builder(hostName)
            .multiThreaded(true)
            .defaultSchemeForDiscoveredNodes(new URI(hostName).getScheme())
            .sslSocketFactory(getSyncHttpsHandler())
            .httpsIOSessionStrategy(getAsyncHttpsHandler());
    if (discovery == true) {
      clientConfig.discoveryFrequency(1L, TimeUnit.MINUTES).discoveryEnabled(true);
    }

    return clientConfig.build();
  }

  private static SSLConnectionSocketFactory getSyncHttpsHandler() throws GeneralSecurityException {
    return new SSLConnectionSocketFactory(getSSLContext(), NoopHostnameVerifier.INSTANCE);
  }

  private static SchemeIOSessionStrategy getAsyncHttpsHandler() throws GeneralSecurityException {
    return new SSLIOSessionStrategy(getSSLContext(), NoopHostnameVerifier.INSTANCE);
  }

  private static SSLContext getSSLContext() throws GeneralSecurityException {
    return new SSLContextBuilder().loadTrustMaterial(null, new TrustEverythingStrategy()).build();
  }

  static class TrustEverythingStrategy implements TrustStrategy {
    @Override
      public boolean isTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {
        return true;
      }
  }
}
