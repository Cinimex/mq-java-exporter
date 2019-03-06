package ru.cinimex.exporter.mq;

import com.ibm.mq.MQException;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.pcf.PCFMessage;
import com.ibm.mq.pcf.PCFMessageAgent;
import ru.cinimex.exporter.mq.pcf.PCFFieldReference;
import ru.cinimex.exporter.prometheus.metrics.MetricsManager;
import ru.cinimex.exporter.prometheus.metrics.MetricsReference;

import java.io.IOException;
import java.util.*;

public class MQPCFSubscriber implements Runnable {
    private MQConnection connection;
    private String queueManagerName;
    private MQObject object;
    private String[] labels;
    private PCFMessage pcfMessage;
    private PCFMessageAgent agent;
    private boolean useWildcards;
    private int intervalSeconds = 10;
    private ArrayList<String> objectNames;

    public MQPCFSubscriber(String queueManagerName, Hashtable<String, Object> connectionProperties, MQObject object) {
        if (connection == null) {
            connection = new MQConnection();
            connection.establish(queueManagerName, connectionProperties);
        }
        //TODO: error handling
        try {
            this.agent = new PCFMessageAgent(connection.getQueueManager());
        } catch (MQException e) {
            e.printStackTrace();
        }
        this.object = object;
        this.queueManagerName = queueManagerName;

    }

    public static PCFMessage getPCFCommand(MQObject object) {
        PCFMessage pcfCmd = new PCFMessage(object.getPCFCmd());
        HashMap<Integer, Object> parameters = object.getPCFParameters();
        Iterator it = parameters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            pcfCmd.addParameter((Integer) pair.getKey(), (String) pair.getValue());
        }
        return pcfCmd;
    }

    @Override
    public void run() {
        try {
            PCFMessage request = getPCFCommand(object);
            PCFMessage[] pcfResponse = agent.send(request);
            PCFMessage response = pcfResponse[0];

            Object result = response.getParameterValue(object.getPCFHeader());
            if (result.getClass() == Integer.class) {
                double prometheusValue = PCFFieldReference.getMetricValue(object.getType(), (Integer) result);
                MetricsManager.updateMetric(MetricsReference.getMetricName(object), prometheusValue, queueManagerName,
                        object.getName());
            } else {
                System.err.println("Unknown PCF metric: " + result.toString());
            }

        } catch (MQException e) {
            //TODO: error handling
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
