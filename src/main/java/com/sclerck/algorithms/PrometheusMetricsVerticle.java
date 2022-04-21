package com.sclerck.algorithms;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.micrometer.PrometheusScrapingHandler;
import io.vertx.micrometer.backends.BackendRegistries;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class PrometheusMetricsVerticle extends AbstractVerticle {

    private final int port;

    private AtomicInteger openConnections;
    private Counter allConnections;
    private Counter points;

    public PrometheusMetricsVerticle(int port) {
        this.port = port;
    }

    @Override
    public void start() {
        MeterRegistry registry = BackendRegistries.getDefaultNow();

        openConnections = new AtomicInteger(0);

        Gauge.builder("open_connections", openConnections, f -> (double) openConnections.get())
                .description("The number of open connections right now")
                .register(registry);

        allConnections = Counter.builder("all_connections")
                .description("All connections so far")
                .register(registry);

        points = Counter.builder("points")
                .description("All points sent so far")
                .register(registry);

        // Later on, creating a router
        Router router = Router.router(vertx);
        router.route("/metrics").handler(PrometheusScrapingHandler.create());
        vertx.createHttpServer().requestHandler(router).listen(port);

        LOG.info("Started web server on port {}", port);
    }

    public void incrementConnections() {

        allConnections.increment();
        openConnections.incrementAndGet();
    }

    public void decrementConnections() {
        openConnections.decrementAndGet();
    }

    public void incrementPoints() {
        points.increment();
    }
}
