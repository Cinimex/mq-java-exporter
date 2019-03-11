package ru.cinimex.exporter;

import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Class is used for parsing config file.
 */
public class Config {
    private String qmgrName;
    private String qmgrHost;
    private int qmgrPort;
    private String qmgrChannel;
    private String user;
    private String password;
    private boolean mqscp;
    private int endpPort;
    private String endpURL;
    private ArrayList<String> queues;
    private ArrayList<String> channels;
    private ArrayList<String> listeners;
    private boolean sendPCFCommands;
    private boolean usePCFWildcards;
    private int scrapeInterval;

    public Config(String path) {
        Yaml file = new Yaml();
        File rawFile = new File(path);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(rawFile));
        } catch (FileNotFoundException e) {
            System.err.println(String.format("Unable to locate config file. Make sure %s is valid path.", path));
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
        queues = (ArrayList<String>) config.get("queues");
        listeners = (ArrayList<String>) config.get("listeners");
        channels = (ArrayList<String>) config.get("channels");
        this.endpPort = (Integer) prometheusEndpointParams.get("port");
        this.endpURL = (String) (prometheusEndpointParams.get("url"));
        this.sendPCFCommands = (boolean) pcfParameters.get("sendPCFCommands");
        this.usePCFWildcards = (boolean) pcfParameters.get("usePCFWildcards");
        this.scrapeInterval = (Integer) pcfParameters.get("scrapeInterval");
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

    public boolean usePCFWildcards() {
        return usePCFWildcards;
    }

    public ArrayList<String> getChannels() {
        return channels;
    }

    public ArrayList<String> getListeners() {
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

    public int getEndpPort() {
        return endpPort;
    }

    public String getEndpURL() {
        return endpURL;
    }

    public ArrayList<String> getQueues() {
        return queues;
    }

}
