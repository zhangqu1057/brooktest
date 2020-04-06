package certs;

import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;

public class RootCertificateAndCRL {
    /**
     * Downloaded root certificates
     */
    final private X509Certificate[] certificates;

    /**
     * Downloaded CRL
     */
    final private X509CRL crl;


    /**
     * accept multi certificates here.
     * @param certificates
     * @param crl
     */
    public RootCertificateAndCRL(X509Certificate[] certificates, X509CRL crl) {
        this.certificates = certificates;
        this.crl = crl;
    }

    /**
     * Obtain certificates.
     */
    public X509Certificate[] getCertificate() {
        return certificates;
    }

    /**
     * Obtain CRL.
     */
    public X509CRL getCrl() {
        return crl;
    }
}
