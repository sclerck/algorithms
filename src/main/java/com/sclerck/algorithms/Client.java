package com.sclerck.algorithms;

import com.sclerck.algorithms.protos.AlgorithmServerGrpc;
import com.sclerck.algorithms.protos.AlgorithmType;
import com.sclerck.algorithms.protos.Parameters;
import com.sclerck.algorithms.protos.Volatility;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.concurrent.TimeUnit;

@Slf4j
public class Client {

    private final AlgorithmServerGrpc.AlgorithmServerBlockingStub blockingStub;

    public Client(Channel channel) {
        blockingStub = AlgorithmServerGrpc.newBlockingStub(channel);
    }

    public void getTicks(Parameters parameters) {
        LOG.info("Requesting ticks with algoritm {}, volatility {}, tick rate changes{} and seed {}",
                parameters.getAlgorithm(),
                parameters.getVolatility(),
                parameters.getTickRateChanges(),
                parameters.getSeed());

        try {
            blockingStub.connect(parameters).forEachRemaining(t -> LOG.info("Tick: {} @ {}", t.getLevel(), t.getTime().getNanos()));
        } catch (StatusRuntimeException e) {
            LOG.error("RPC failed {}", e.getStatus(), e);
            return;
        }

        LOG.info("Complete");
    }

    public static void main(String[] args) {

        try {
            Options options = new Options();

            Option hostOption = new Option("h", "host", true, "hostname");
            hostOption.setRequired(true);
            options.addOption(hostOption);

            Option portOption = new Option("p", "port", true, "port number");
            portOption.setRequired(true);
            options.addOption(portOption);

            Option algorithmOption = new Option("a", "algorithm", true, "algorithm (MIDPOINT_DISPLACEMENT|PERLIN_NOISE)");
            algorithmOption.setRequired(true);
            options.addOption(algorithmOption);

            Option volatilityOption = new Option("v", "volatility", true, "volatility (STABLE|NORMAL|ABNORMAL)");
            volatilityOption.setRequired(true);
            options.addOption(volatilityOption);

            Option tickRateChangesOption = new Option("t", "tickRateChanges", true, "number of tick rate changes");
            options.addOption(tickRateChangesOption);

            Option seedOption = new Option("s", "seed", true, "seed");
            options.addOption(seedOption);

            CommandLineParser parser = new DefaultParser();
            HelpFormatter formatter = new HelpFormatter();
            CommandLine cmd = null;

            try {
                cmd = parser.parse(options, args);
            } catch (ParseException e) {
                formatter.printHelp(Server.class.getName(), options);

                System.exit(-1);
            }

            String host = cmd.getOptionValue("host");
            int port = Integer.parseInt(cmd.getOptionValue("port"));
            AlgorithmType algorithmType = AlgorithmType.valueOf(cmd.getOptionValue("algorithm"));
            Volatility volatility = Volatility.valueOf(cmd.getOptionValue("volatility"));

            int tickRateChanges = 100;
            if (cmd.hasOption("tickRateChanges")) {
                tickRateChanges = Integer.parseInt(cmd.getOptionValue("tickRateChanges"));
            }

            int seed = 50;
            if (cmd.hasOption("seed")) {
                seed = Integer.parseInt(cmd.getOptionValue("seed"));
            }

            String target = host + ":" + port;

            ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

            Parameters parameters = Parameters.newBuilder()
                    .setAlgorithm(algorithmType)
                    .setVolatility(volatility)
                    .setTickRateChanges(tickRateChanges)
                    .setSeed(seed)
                    .build();

            try {
                Client client = new Client(channel);
                client.getTicks(parameters);
            } finally {
                channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
            }

        } catch (Exception e) {
            LOG.error("Exception caught starting up", e);
            System.exit(-1);
        }
    }

}
