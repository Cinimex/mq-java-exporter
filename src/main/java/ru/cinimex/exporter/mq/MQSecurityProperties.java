package ru.cinimex.exporter.mq;

public class MQSecurityProperties {
    private final boolean useTLS;
    private final String keystorePath;
    private final String keystorePassword;
    private final String truststorePath;
    private final String truststorePassword;
    private final String sslProtocol;
    private final String cipherSuite;

    public MQSecurityProperties(boolean useTLS, String keystorePath, String keystorePassword, String truststorePath, String truststorePassword, String sslProtocol, String cipherSuite) {
        this.useTLS = useTLS;
        this.keystorePath = keystorePath;
        this.keystorePassword = keystorePassword;
        this.truststorePath = truststorePath;
        this.truststorePassword = truststorePassword;
        this.sslProtocol = sslProtocol;
        this.cipherSuite = cipherSuite;
    }

    public String getKeystorePath() {
        return keystorePath;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public String getTruststorePath() {
        return truststorePath;
    }

    public String getTruststorePassword() {
        return truststorePassword;
    }

    public String getSslProtocol() {
        return sslProtocol;
    }

    public String getCipherSuite() {
        return cipherSuite;
    }

    public boolean isUseTLS() {
        return useTLS;
    }
}
