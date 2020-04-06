package certs;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CertCrlDownloader {

    /**
     * The location of the certificate zip on the VCSA.  %s is the placeholder
     * for the IP address of the VCSA.
     */
    private static final String CA_CERT_LOCATION = "C:\\Work\\VxRail\\FP_GI_Integration\\cert\\7.0_vc_download.zip";
    private static final String CA_CERT_LOCATION_OLD_VC = "C:\\Work\\VxRail\\FP_GI_Integration\\cert\\old_vc_154_download.zip";

    private static final String CRL_FILE_NAME_PATTERN = "[^\\s]+\\.r[0-9]*$";

    private static final String CERT_FILE_NAME_PATTERN = "[^\\s]+\\.[0-9]+$";

    /**
     * Download and extract certificates and CRLs.
     */
    public static RootCertificateAndCRL downloadFromCloudVM() throws Exception {
        // Connect to VC, download the zip.
//        final SSLSocketFactory sslSocketFactory = MarvinSSLUtil.createNaiveSocketFactory();
//        HttpsURLConnection httpsURLConnection = null;
        File file = new File(CA_CERT_LOCATION);
//        File file = new File(CA_CERT_LOCATION_OLD_VC);
        InputStream inputStream = new FileInputStream(file);
        try {
//            try {
//                httpsURLConnection = (HttpsURLConnection)
//                        new URL(String.format(CA_CERT_LOCATION, vcIP)).openConnection();
//                httpsURLConnection.setSSLSocketFactory(sslSocketFactory);
//                inputStream = httpsURLConnection.getInputStream();
//            } catch (IOException ie) {
//                IOUtils.closeQuietly(inputStream);
//                if (httpsURLConnection != null) {
//                    httpsURLConnection.disconnect();
//                }
//                httpsURLConnection = (HttpsURLConnection)
//                        new URL(String.format(CA_CERT_LOCATION + ".zip", vcIP)).openConnection();
//                httpsURLConnection.setSSLSocketFactory(sslSocketFactory);
//                inputStream = httpsURLConnection.getInputStream();
//            }

            // The CA root ends with ".0"
            // The CA CRL contains the substring ".r"
            final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            List<X509Certificate> certificates = new ArrayList<X509Certificate>();
            X509CRL crl = null;
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (entry.getName().matches(CRL_FILE_NAME_PATTERN)) {
                    // load all files that ned with ".r<number>" as CRL
                    // I don't trust vcenter cert downloader
                    // This is an interesting KB article for anyone interested -
                    // http://blogs.vmware.com/vsphere/2015/03/vmware-certificate-authority-overview-using-vmca-root-certificates-browser.html
                    //crl = MarvinSSLUtil.readCRL(zipInputStream);
                    System.out.println("Find Crl. entry = " + entry.getName());
                } else if (entry.getName().matches(CERT_FILE_NAME_PATTERN)) {
                    // load all files that end with ".<number>" as trusted certificate.
                    //certificates.add(MarvinSSLUtil.readCertificate(zipInputStream));
                    System.out.println("Find Cert Root entry = " + entry.getName());
                }
            }
            // If the crl file format changes then crl member remains null
            // and it cascaded to the caller instead of failing ...
            return new RootCertificateAndCRL(certificates.toArray(new X509Certificate[0]), crl);
        } finally {
            IOUtils.closeQuietly(inputStream);
//            if (httpsURLConnection != null) {
//                httpsURLConnection.disconnect();
//            }
        }
    }

    public static void main(String[] args) {
        try {
            downloadFromCloudVM();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
