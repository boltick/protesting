package org.protesting.atfwk.trust;

/**
 * MiTM class
 * User: AB83625
 * Date: 15/02/13
 * To change this template use File | Settings | File Templates.
 */
public class MiTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
    public java.security.cert.X509Certificate[] getAcceptedIssuers()
    {
        return null;
    }

    public boolean isServerTrusted(
            java.security.cert.X509Certificate[] certs)
    {
        return true;
    }

    public boolean isClientTrusted(
            java.security.cert.X509Certificate[] certs)
    {
        return true;
    }

    public void checkServerTrusted(
            java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException
    {
        return;
    }

    public void checkClientTrusted(
            java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException
    {
        return;
    }
}
