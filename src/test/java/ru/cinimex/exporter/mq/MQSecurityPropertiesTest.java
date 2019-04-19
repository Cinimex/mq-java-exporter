package ru.cinimex.exporter.mq;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.cinimex.exporter.Config;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MQSecurityPropertiesTest {
    private static MQSecurityProperties properties;

    @BeforeAll
    static void initProperties() {
        properties = new Config("src/test/resources/valid_config.yaml").getMqSecurityProperties();
    }

    @Test
    void getKeystorePath() {
        assertEquals("src/test/resources/keystores/keystore.jks", properties.getKeystorePath());
    }

    @Test
    void getKeystorePassword() {
        assertEquals("testpass2", properties.getKeystorePassword());
    }

    @Test
    void getTruststorePath() {
        assertEquals("src/test/resources/keystores/truststore.jks", properties.getTruststorePath());
    }

    @Test
    void getTruststorePassword() {
        assertEquals("testpass2", properties.getTruststorePassword());
    }

    @Test
    void getSslProtocol() {
        assertEquals("TLSv1.2", properties.getSslProtocol());
    }

    @Test
    void getCipherSuite() {
        assertEquals("TLS_RSA_WITH_AES_256_CBC_SHA256", properties.getCipherSuite());
    }

    @Test
    void isUseTLS() {
        assertEquals(true, properties.isUseTLS());
    }
}