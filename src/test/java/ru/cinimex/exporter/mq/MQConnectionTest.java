package ru.cinimex.exporter.mq;

import com.ibm.mq.MQException;
import com.ibm.mq.constants.MQConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.cinimex.exporter.Config;

import java.util.Hashtable;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Although we do not have the ability to mock IBM MQ, we'll just check exceptions correctness.
 */

class MQConnectionTest {
    private MQConnection connection;
    private Config config;
    private String host;
    private int port;
    private String channel;
    private String qmName;
    private String user;
    private String password;
    private boolean useMQCSP;


    @BeforeEach
    void setConnection() {
        connection = new MQConnection();
        config = new Config("src/test/resources/valid_config.yaml");
        qmName = config.getQmgrName();
        host = config.getQmgrHost();
        port = config.getQmgrPort();
        channel = config.getQmgrChannel();
        user = config.getUser();
        password = config.getPassword();
        useMQCSP = config.useMqscp();
    }

    @Test
    void createMQConnectionParams() {
        Map<String, Object> params = MQConnection.createMQConnectionParams(config);
        Assertions.assertAll(
                () -> assertEquals(MQConstants.TRANSPORT_MQSERIES_CLIENT, params.get(MQConstants.TRANSPORT_PROPERTY)),
                () -> assertEquals(host, params.get(MQConstants.HOST_NAME_PROPERTY)),
                () -> assertEquals(port, params.get(MQConstants.PORT_PROPERTY)),
                () -> assertEquals(channel, params.get(MQConstants.CHANNEL_PROPERTY)),
                () -> assertEquals(user, params.get(MQConstants.USER_ID_PROPERTY)),
                () -> assertEquals(password, params.get(MQConstants.PASSWORD_PROPERTY)),
                () -> assertEquals(useMQCSP, params.get(MQConstants.USE_MQCSP_AUTHENTICATION_PROPERTY))
        );
    }

    @Test
    void establish() {
        Map<String, Object> params = MQConnection.createMQConnectionParams(config);
        Assertions.assertThrows(MQException.class, () -> connection.establish(qmName, params));
    }

    @Test
    void establish1() {
        Map<String, Object> params = MQConnection.createMQConnectionParams(config);
        Assertions.assertThrows(MQException.class, () -> connection.establish(qmName, params));
    }


}