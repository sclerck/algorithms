package com.sclerck.algorithms.server;

import com.sclerck.algorithms.protos.AlgorithmServerGrpc;
import com.sclerck.algorithms.protos.Parameters;
import com.sclerck.algorithms.protos.Tick;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AlgorithmServer extends AlgorithmServerGrpc.AlgorithmServerImplBase {

    private Server server;

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
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    @Override
    public void connect(Parameters request, StreamObserver<Tick> responseObserver) {
        request.getAlgorithm();
    }
}
