package com.sclerck.algorithms;

import com.sclerck.algorithms.midpointdisplacement.MidPointDisplacementAlgorithm;
import com.sclerck.algorithms.perlinnoise.PerlinNoiseAlgorithm;
import com.sclerck.algorithms.protos.AlgorithmType;

public class AlgorithmBuilder {

    public static Algorithm builder(AlgorithmType algorithmType) {
        switch(algorithmType) {
            case MIDPOINT_DISPLACEMENT:
                return new MidPointDisplacementAlgorithm();
            case PERLIN_NOISE:
                return new PerlinNoiseAlgorithm();
            default:
                throw new IllegalArgumentException("Unimplemented algorithm");
        }
    }
}
