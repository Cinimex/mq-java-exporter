package ru.cinimex.exporter.prometheus;


import io.prometheus.client.CollectorRegistry;

/**
 * Class stores the only instance of CollectorRegistry.
 */
public class Registry {
    private static final CollectorRegistry exporterRegistry = CollectorRegistry.defaultRegistry;

    private Registry() {
    }

    public static CollectorRegistry getRegistry() {
        return exporterRegistry;
    }
}