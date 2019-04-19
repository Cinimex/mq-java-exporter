package ru.cinimex.exporter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.cinimex.exporter.mq.MQSecurityProperties;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigTest {
    private Config config;

    @BeforeEach
    void initConfigFile() {
        config = new Config("src/test/resources/valid_config.yaml");
    }

    @Test
    void testValidConfigFile() {
        List<String> queues = new ArrayList<>();
        queues.add("QUEUE1");
        queues.add("QUEUE2");

        List<String> listeners = new ArrayList<>();
        listeners.add("LISTENER01");

        List<String> channels = new ArrayList<>();
        channels.add("MANAGEMENT.CHANNEL");

        Assertions.assertAll(

                //Asserts for MQ connection params

                () -> assertEquals("QM", config.getQmgrName()),
                () -> assertEquals("hostname", config.getQmgrHost()),
                () -> assertEquals(1414, config.getQmgrPort()),
                () -> assertEquals("SYSTEM.DEF.SVRCONN", config.getQmgrChannel()),
                () -> assertEquals("mqm", config.getUser()),
                () -> assertEquals("mqmpass", config.getPassword()),
                () -> assertEquals(true, config.useMqscp()),
                () -> assertEquals(12000, config.getConnTimeout()),

                //Asserts for prometheus endpoint params

                () -> assertEquals("/metrics", config.getEndpURL()),
                () -> assertEquals(8080, config.getEndpPort()),

                //Asserts for PCFParameters

                () -> assertEquals(true, config.sendPCFCommands()),
                () -> assertEquals(true, config.usePCFWildcards()),
                () -> assertEquals(10, config.getScrapeInterval()),

                //Asserts for monitored objects

                () -> assertEquals(config.getQueues(), queues),
                () -> assertEquals(config.getListeners(), listeners),
                () -> assertEquals(config.getChannels(), channels)
        );

    }

    @Test
    void getMqSecurityProperties() {
        MQSecurityProperties props = config.getMqSecurityProperties();
        Assertions.assertAll(
                () -> assertEquals(true, props.isUseTLS()),
                () -> assertEquals("src/test/resources/keystores/keystore.jks", props.getKeystorePath()),
                () -> assertEquals("testpass2", props.getKeystorePassword()),
                () -> assertEquals("src/test/resources/keystores/truststore.jks", props.getTruststorePath()),
                () -> assertEquals("testpass2", props.getTruststorePassword()),
                () -> assertEquals("TLSv1.2", props.getSslProtocol()),
                () -> assertEquals("TLS_RSA_WITH_AES_256_CBC_SHA256", props.getCipherSuite())
        );

    }
}
