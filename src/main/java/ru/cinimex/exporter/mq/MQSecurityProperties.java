package ru.cinimex.exporter.mq;

public class MQSecurityProperties {
    private boolean useTLS;
    private String keystorePath;
    private String keystorePassword;
    private String truststorePath;
    private String truststorePassword;
    private String sslProtocol;
    private String cipherSuite;

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
