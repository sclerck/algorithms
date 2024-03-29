package com.sclerck.algorithms;

import com.sclerck.algorithms.protos.AlgorithmType;
import com.sclerck.algorithms.protos.Volatility;
import org.javatuples.Pair;

import java.util.Map;

public class VolatilityMap {

    // A map of volatility type with their midpoint and noise volatility values
    private static Map<Volatility, Pair<Float, Float>> volatilities = Map.of(
            Volatility.STABLE, new Pair<>(0.5f, 0.25f),
            Volatility.NORMAL, new Pair<>(0.2f, 0.708f),
            Volatility.ABNORMAL, new Pair<>(0.002f, 1.00f)
    );

    public static float getVolatility(Volatility volatility, AlgorithmType algorithmType) {

        Pair<Float, Float> values = volatilities.get(volatility);

        if (values == null) {
            throw new IllegalArgumentException("Unrecognized volatility");
        }

        if (algorithmType == AlgorithmType.MIDPOINT_DISPLACEMENT) {
            return values.getValue0();
        }

        if (algorithmType == AlgorithmType.PERLIN_NOISE) {
            return values.getValue1();
        }

        throw new IllegalArgumentException("Unrecognized algorithm");
    }
}

