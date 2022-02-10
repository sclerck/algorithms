/**
 * $Header:
 * $Id:
 * $Name:
 */
package com.sclerck.algorithms.perlinnoise;

import java.util.NavigableMap;
import java.util.TreeMap;

import com.sclerck.algorithms.Algorithm;
import com.sclerck.algorithms.VolatilityMap;
import com.sclerck.algorithms.protos.AlgorithmType;
import com.sclerck.algorithms.protos.Volatility;

/**
 * Purpose: An algorithm based on perlin noise
 * 
 * This class is based on the logic described here:
 * https://rtouti.github.io/graphics/perlin-noise-algorithm
 * 
 * @author sclerck
 * @date 7 Sep 2016
 *
 */
public class PerlinNoiseAlgorithm implements Algorithm {

	private final Interpolater interpolater;

	private final int octaves;

	public PerlinNoiseAlgorithm() {
		interpolater = new CosineInterpolater();
		octaves = 8;
	}

	@Override
	public NavigableMap<Double, Double> generateCurve(int numTickRateChanges, Volatility volatility, double seed) {
		NavigableMap<Double, Double> results = new TreeMap<>();

		float seedF = (float)seed;

		for (int i = 0; i < numTickRateChanges; i++) {
			float value = perlinNoise(i, volatility) * seedF + seedF;
			results.put((double) i, (double) value);
		}

		return results;
	}

	@Override
	public NavigableMap<Double, Double> generateCurve(int numTickRateChanges, Volatility volatility) {
		return generateCurve(numTickRateChanges, volatility, SEED);
	}

	/**
	 * Generate a single data point based on perlin noise model This in turn
	 * calls the provided interpolater which in turn uses a smoothed noise
	 * calculation
	 * 
	 * @param seed
	 *            - different seed value will render a different point
	 * @param volatility
	 * @return
	 */
	public float perlinNoise(float seed, Volatility volatility) {
		return perlinNoise(seed, volatility, octaves);
	}

	/**
	 * Generates a single data point based on perlin noise model This in turn
	 * calls the provided interpolater which in turn uses a smoothed noise
	 * calculation
	 * 
	 * @param seed
	 *            - different seed value will render a different point
	 * @param volatility
	 * @param octaves
	 *            - provide the octave scale
	 * @return
	 */
	public float perlinNoise(float seed, Volatility volatility, int octaves) {
		int i = octaves - 1;

		// Don't iterate as per the webpage, instead just get that octave's data
		// set

		float frequency = (float) Math.pow(2.0, i);
		double amplitude = Math.pow(VolatilityMap.getVolatility(volatility, AlgorithmType.PERLIN_NOISE), i);

		return (float)(interpolatedNoise(seed * frequency, interpolater) * amplitude);
	}

	private float interpolatedNoise(float seed, Interpolater interpolater) {
		int seedInt = (int) seed;
		float fractional_X = seed - seedInt;

		float v1 = smoothedNoise(seedInt);
		float v2 = smoothedNoise(seedInt + 1);

		return interpolater.interpolate(v1, v2, fractional_X);
	}

	private float smoothedNoise(int seed) {
		return noise(seed) / 2 + noise(seed - 1) / 4 + noise(seed + 1) / 4;
	}

	private float noise(int seed) {
		seed = (seed << 13) ^ seed;
		return (float) (1.0 - ((seed * (seed * seed * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0);
	}
}
