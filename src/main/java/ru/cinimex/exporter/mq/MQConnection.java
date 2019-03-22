package ru.cinimex.exporter.mq;

import com.ibm.mq.MQException;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.MQTopic;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.MQConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Hashtable;

/**
 * Class represents MQ connection.
 */
public class MQConnection {
    private static final Logger logger = LogManager.getLogger(MQConnection.class);
    private MQQueueManager queueManager;

    /**
     * Method creates connection properties Hashtable from connection parameters.
     *
     * @param host     - host, where queue manager is located.
     * @param port     - queue manager's port.
     * @param channel  - queue manager's channel.
     * @param user     - user, which has enough privilege on the queue manager (optional).
     * @param password - password, which is required to establish connection with queue manager (optional).
     * @param useMQCSP - flag, which indicates, if MQCSP auth should be used.
     * @return - returns prepared structure with all parameters transformed into queue manager's format.
     */
    protected static Hashtable<String, Object> createMQConnectionParams(String host, int port, String channel, String user, String password, boolean useMQCSP) {
        Hashtable<String, Object> properties = new Hashtable<>();
        properties.put(MQConstants.TRANSPORT_PROPERTY, host == null ? MQConstants.TRANSPORT_MQSERIES_BINDINGS : MQConstants.TRANSPORT_MQSERIES_CLIENT);
        if (host != null) properties.put(MQConstants.HOST_NAME_PROPERTY, host);
        if (port != 0) properties.put(MQConstants.PORT_PROPERTY, port);
        if (channel != null) properties.put(MQConstants.CHANNEL_PROPERTY, channel);
        if (user != null || password != null) {
            if (useMQCSP) properties.put(MQConstants.USE_MQCSP_AUTHENTICATION_PROPERTY, true);
            if (user != null) properties.put(MQConstants.USER_ID_PROPERTY, user);
            if (password != null) properties.put(MQConstants.PASSWORD_PROPERTY, password);
        }
        return properties;
    }

    /**
     * Method establishes connection with queue manager.
     *
     * @param host     - host, where queue manager is located.
     * @param port     - queue manager's port.
     * @param channel  - queue manager's channel.
     * @param qmName   - queue manager's name.
     * @param user     - user, which has enough privilege on the queue manager (optional).
     * @param password - password, which is required to establish connection with queue manager (optional).
     * @param useMQCSP - flag, which indicates, if MQCSP auth should be used.
     */
    public void establish(String host, int port, String channel, String qmName, String user, String password, boolean useMQCSP) throws MQException {
        Hashtable<String, Object> connectionProperties = createMQConnectionParams(host, port, channel, user, password, useMQCSP);
        queueManager = new MQQueueManager(qmName, connectionProperties);
    }

    /**
     * Method establishes connection with queue manager.
     *
     * @param qmNqme               - queue manager's name.
     * @param connectionProperties - prepared structure with all parameters transformed into queue manager's format. See {@link #createMQConnectionParams(String, int, String, String, String, boolean)} for more info.
     */
    public void establish(String qmNqme, Hashtable<String, Object> connectionProperties) throws MQException {
        queueManager = new MQQueueManager(qmNqme, connectionProperties);
    }

    /**
     * Method closes connection.
     */
    public void close() {
        if (queueManager != null && queueManager.isConnected()) {
            try {
                queueManager.disconnect();
            } catch (MQException e) {
                logger.error("Failed!", e);
            }
        }
    }

    /**
     * Returns MQTopic object, which was retrieved from queue manager.
     *
     * @param topic - topic string.
     * @return - MQTopic object, which is described @see <a href="https://www.ibm.com/support/knowledgecenter/en/SSFKSJ_9.0.0/com.ibm.mq.javadoc.doc/WMQJavaClasses/com/ibm/mq/MQTopic.html">here</a>.
     * @throws MQException - MQ exception, which contains mqrc error code. More info <a href="https://www.ibm.com/support/knowledgecenter/en/SSFKSJ_9.0.0/com.ibm.mq.javadoc.doc/WMQJMSClasses/com/ibm/mq/MQException.html">here</a>.
     */
    public MQTopic createTopic(String topic) throws MQException {
        return queueManager.accessTopic(topic, "", CMQC.MQTOPIC_OPEN_AS_SUBSCRIPTION, CMQC.MQSO_CREATE | CMQC.MQSO_NON_DURABLE | CMQC.MQSO_MANAGED);
    }

    /**
     * Returns MQQueueManager object.
     *
     * @return - MQQueueManager object.
     */
    public MQQueueManager getQueueManager() {
        return this.queueManager;
    }
}
