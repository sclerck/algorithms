package com.sclerck.algorithms.server;

import com.google.protobuf.Timestamp;
import com.sclerck.algorithms.Algorithm;
import com.sclerck.algorithms.AlgorithmBuilder;
import com.sclerck.algorithms.PrometheusMetricsVerticle;
import com.sclerck.algorithms.protos.AlgorithmServerGrpc;
import com.sclerck.algorithms.protos.AlgorithmType;
import com.sclerck.algorithms.protos.Parameters;
import com.sclerck.algorithms.protos.Point;
import com.sclerck.algorithms.protos.Volatility;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AlgorithmServer extends AlgorithmServerGrpc.AlgorithmServerImplBase {

    private Server server;
    private final PrometheusMetricsVerticle prometheusMetricsVerticle;

    public AlgorithmServer(PrometheusMetricsVerticle prometheusMetricsVerticle) {
        this.prometheusMetricsVerticle = prometheusMetricsVerticle;
    }

    public void start(int port) throws IOException {
        server = ServerBuilder.forPort(port).addService(this).build().start();
        LOG.info("Started server, listening on port {}", port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.error("Shutting down server since JVM is shutting down");
            try {
                AlgorithmServer.this.stop();
            } catch (InterruptedException e) {
                LOG.error("Unable to stop", e);
            }
            LOG.error("Server shut down");
        }));
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    @Override
    public void connect(Parameters request, StreamObserver<Point> responseObserver) {

        prometheusMetricsVerticle.incrementConnections();

        AlgorithmType algorithmType = request.getAlgorithm();
        Volatility volatility = request.getVolatility();
        int seed = request.getSeed();
        int pointRateChanges = request.getPointRateChanges();

        Algorithm algorithm = AlgorithmBuilder.builder(algorithmType);
        algorithm.generateCurve(pointRateChanges, volatility, seed).values().forEach(d -> {
            int now = Instant.now().getNano();
            Point point = Point.newBuilder()
                    .setLevel(d.floatValue())
                    .setTime(Timestamp.newBuilder().setNanos(now).build())
                    .build();

            prometheusMetricsVerticle.incrementPoints();

            responseObserver.onNext(point);
        });

        responseObserver.onCompleted();

        prometheusMetricsVerticle.decrementConnections();
    }
}
