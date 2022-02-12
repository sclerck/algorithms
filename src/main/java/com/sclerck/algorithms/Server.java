package com.sclerck.algorithms;

import com.sclerck.algorithms.server.AlgorithmServer;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

@Slf4j
public class Server {

    public static void main(String[] args) {

        try {
            Options options = new Options();

            Option webPortOption = new Option("w", "webport", true, "web port number");
            options.addOption(webPortOption);

            Option portOption = new Option("p", "port", true, "port number");
            portOption.setRequired(true);
            options.addOption(portOption);

            CommandLineParser parser = new DefaultParser();
            HelpFormatter formatter = new HelpFormatter();
            CommandLine cmd = null;

            try {
                cmd = parser.parse(options, args);
            } catch (ParseException e) {
                formatter.printHelp(Server.class.getName(), options);

                System.exit(-1);
            }

            // Start a webserver for the prometheus metrics
            int webport = 80;

            if (cmd.hasOption("webport")) {
                webport = Integer.parseInt(cmd.getOptionValue("webport"));
            }

            VertxOptions vertxOptions = new VertxOptions();
            vertxOptions.setMetricsOptions(
                    new MicrometerMetricsOptions()
                            .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true))
                            .setEnabled(true));
            Vertx vertx = Vertx.vertx(vertxOptions);

            PrometheusMetricsVerticle prometheusMetricsVerticle = new PrometheusMetricsVerticle(webport);

            vertx.deployVerticle(prometheusMetricsVerticle);

            // Start the GRPC server
            int port = Integer.parseInt(cmd.getOptionValue("port"));

            final AlgorithmServer server = new AlgorithmServer(prometheusMetricsVerticle);
            server.start(port);
            server.blockUntilShutdown();


        } catch (Exception e) {
            LOG.error("Exception caught starting up", e);
            System.exit(-1);
        }
    }
}
