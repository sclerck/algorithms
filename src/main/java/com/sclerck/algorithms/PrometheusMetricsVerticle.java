package com.sclerck.algorithms;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.vertx.MetricsHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class PrometheusMetricsVerticle extends AbstractVerticle {

    private static final Gauge OPEN_CONNECTIONS = Gauge.build()
            .name("open_connections")
            .help("The number of open connections right now")
            .register();

    private static final Counter ALL_CONNECTIONS = Counter.build()
            .name("all_connections")
            .help("All connections so far")
            .register();

    private static final Counter TICKS = Counter.build()
            .name("ticks")
            .help("All ticks sent so far")
            .register();

    private int port;

    @Override
    public void start() {

        Router router = Router.router(vertx);

        router.route("/metrics/").handler(new MetricsHandler());

        vertx.createHttpServer().requestHandler(router::accept).listen(port);

        LOG.info("Started web server, listening on port {}", port);
    }

    public void incrementConnections() {
        OPEN_CONNECTIONS.inc();
        ALL_CONNECTIONS.inc();
    }

    public void decrementConnections() {
        OPEN_CONNECTIONS.dec();
    }

    public void incrementTicks() {
        TICKS.inc();
    }
}
