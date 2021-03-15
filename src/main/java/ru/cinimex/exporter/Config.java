package ru.cinimex.exporter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import ru.cinimex.exporter.mq.MQSecurityProperties;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Class is used for parsing config file.
 */
public class Config {
    private static final Logger logger = LogManager.getLogger(Config.class);
    private String qmgrName;
    private String qmgrHost;
    private int qmgrPort;
    private String qmgrChannel;
    private String user;
    private String password;
    private boolean mqscp;
    private int connTimeout;
    private int endpPort;
    private String endpURL;
    private int updateInterval;
    private Map<String, List<String>> queues;
    private Map<String, List<String>> channels;
    private Map<String, List<String>> listeners;
    private boolean sendPCFCommands;
    private int scrapeInterval;
    private MQSecurityProperties mqSecurityProperties;

    public Config(String path) {
        Yaml file = new Yaml();
        File rawFile = new File(path);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(rawFile));
        } catch (FileNotFoundException e) {
            logger.error("Unable to locate config file. Make sure \"{}\" is a valid path.", path);
            System.exit(1);
        }
        LinkedHashMap<String, Object> config = file.load(br);
        HashMap<String, Object> qmgrConnectionParams = (HashMap<String, Object>) config.get("qmgrConnectionParams");
        HashMap<String, Object> prometheusEndpointParams = (HashMap<String, Object>) config.get("prometheusEndpointParams");
        HashMap<String, Object> pcfParameters = (HashMap<String, Object>) config.get("PCFParameters");
        this.qmgrName = (String) qmgrConnectionParams.get("qmgrName");
        this.qmgrHost = (String) qmgrConnectionParams.get("qmgrHost");
        this.qmgrPort = (Integer) qmgrConnectionParams.get("qmgrPort");
        this.qmgrChannel = (String) qmgrConnectionParams.get("qmgrChannel");
        this.user = (String) qmgrConnectionParams.get("user");
        this.password = (String) qmgrConnectionParams.get("password");
        this.mqscp = (boolean) qmgrConnectionParams.get("mqscp");
        this.connTimeout = (Integer) qmgrConnectionParams.get("connTimeout");
        this.updateInterval = (Integer) config.get("updateInterval");
        this.queues = (Map<String, List<String>>) config.get("queues");
        this.listeners = (Map<String, List<String>>) config.get("listeners");
        this.channels = (Map<String, List<String>>) config.get("channels");
        this.endpPort = (Integer) prometheusEndpointParams.get("port");
        this.endpURL = (String) (prometheusEndpointParams.get("url"));
        this.sendPCFCommands = (boolean) pcfParameters.get("sendPCFCommands");
        this.scrapeInterval = (Integer) pcfParameters.get("scrapeInterval");
        boolean useTLS = (boolean) qmgrConnectionParams.get("useTLS");
        if (useTLS) {
            logger.debug("Secured connection to queue manager will be used");
            mqSecurityProperties = new MQSecurityProperties(
                    useTLS,
                    (String) qmgrConnectionParams.get("keystorePath"),
                    (String) qmgrConnectionParams.get("keystorePassword"),
                    (String) qmgrConnectionParams.get("truststorePath"),
                    (String) qmgrConnectionParams.get("truststorePassword"),
                    (String) qmgrConnectionParams.get("sslProtocol"),
                    (String) qmgrConnectionParams.get("cipherSuite")
            );
        } else {
            logger.debug("Unsecured connection to queue manager will be used");
        }
        logger.info("Successfully parsed configuration file!");
    }

    public boolean useMqscp() {
        return mqscp;
    }

    public String getQmgrName() {
        return qmgrName;
    }

    public boolean sendPCFCommands() {
        return sendPCFCommands;
    }

    public Map<String, List<String>> getChannels() {
        return channels;
    }

    public Map<String, List<String>> getListeners() {
        return listeners;
    }

    public String getQmgrHost() {
        return qmgrHost;
    }

    public int getQmgrPort() {
        return qmgrPort;
    }

    public int getScrapeInterval() {
        return scrapeInterval;
    }

    public String getQmgrChannel() {
        return qmgrChannel;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public int getConnTimeout() {
        return connTimeout;
    }

    public int getEndpPort() {
        return endpPort;
    }

    public String getEndpURL() {
        return endpURL;
    }

    public int getUpdateInterval() { return updateInterval; }

    public Map<String, List<String>> getQueues() {
        return queues;
    }

    public MQSecurityProperties getMqSecurityProperties() {
        return mqSecurityProperties;
    }
}
