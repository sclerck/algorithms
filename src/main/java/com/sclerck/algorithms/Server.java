package com.sclerck.algorithms;

import com.sclerck.algorithms.server.AlgorithmServer;
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

            int port = Integer.parseInt(cmd.getOptionValue("port"));

            final AlgorithmServer server = new AlgorithmServer();
            server.start(port);
            server.blockUntilShutdown();
        } catch (Exception e) {
            LOG.error("Exception caught starting up", e);
            System.exit(-1);
        }
    }
}
