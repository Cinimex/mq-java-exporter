package ru.cinimex.exporter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.cinimex.exporter.mq.MQSecurityProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigTest {
    private Config config;

    @BeforeEach
    void initConfigFile() {
        config = new Config("src/test/resources/valid_config.yaml");
    }

    @Test
    void testValidConfigFile() {
        Map<String, List<String>> queues = new HashMap<>();
        List<String> include = new ArrayList<>();
        List<String> exclude = new ArrayList<>();

        include.add("*");
        exclude.add("SYSTEM.*");

        queues.put("include", include);
        queues.put("exclude", exclude);

        Map<String, List<String>> listeners = new HashMap<>();
        include = null;
        exclude = new ArrayList<>();

        exclude.add("SYSTEM.*");

        listeners.put("include", include);
        listeners.put("exclude", exclude);

        Map<String, List<String>> channels = new HashMap<>();
        include = null;

        channels.put("include", include);

        Assertions.assertAll(

                //Asserts for MQ connection params

                () -> assertEquals("QM", config.getQmgrName()),
                () -> assertEquals("hostname", config.getQmgrHost()),
                () -> assertEquals(1414, config.getQmgrPort()),
                () -> assertEquals("SYSTEM.DEF.SVRCONN", config.getQmgrChannel()),
                () -> assertEquals("mqm", config.getUser()),
                () -> assertEquals("mqmpass", config.getPassword()),
                () -> assertTrue(config.useMqscp()),
                () -> assertEquals(12000, config.getConnTimeout()),

                //Asserts for prometheus endpoint params

                () -> assertEquals("/metrics", config.getEndpURL()),
                () -> assertEquals(8080, config.getEndpPort()),

                //Asserts for PCFParameters

                () -> assertTrue(config.sendPCFCommands()),
                () -> assertEquals(10, config.getScrapeInterval()),

                () -> assertEquals(30 , config.getUpdateInterval()),

                //Asserts for monitored objects

                () -> assertEquals(queues, config.getQueues()),
                () -> assertEquals(listeners, config.getListeners()),
                () -> assertEquals(channels, config.getChannels())
        );

    }

    @Test
    void getMqSecurityProperties() {
        MQSecurityProperties props = config.getMqSecurityProperties();
        Assertions.assertAll(
                () -> assertTrue(props.isUseTLS()),
                () -> assertEquals("src/test/resources/keystores/keystore.jks", props.getKeystorePath()),
                () -> assertEquals("testpass2", props.getKeystorePassword()),
                () -> assertEquals("src/test/resources/keystores/truststore.jks", props.getTruststorePath()),
                () -> assertEquals("testpass2", props.getTruststorePassword()),
                () -> assertEquals("TLSv1.2", props.getSslProtocol()),
                () -> assertEquals("TLS_RSA_WITH_AES_256_CBC_SHA256", props.getCipherSuite())
        );

    }
}
