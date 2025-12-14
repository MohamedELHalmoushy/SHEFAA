import javax.net.ssl.*;
import java.security.cert.X509Certificate;

public class DisableSSLVerification {
    public static void disableSSLVerification() {
        try {
            // Set up a TrustManager that accepts all certificates
            TrustManager[] trustAllCertificates = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCertificates, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Now you can make HTTPS requests without SSL validation
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        disableSSLVerification();
        // Your existing HTTP/HTTPS code here
    }
}
