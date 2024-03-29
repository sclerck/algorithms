/**
 * $Header:
 * $Id:
 * $Name:
 */
package com.sclerck.algorithms;

import com.sclerck.algorithms.protos.AlgorithmType;
import com.sclerck.algorithms.protos.Volatility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;

import static com.sclerck.algorithms.protos.Volatility.UNRECOGNIZED;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Purpose: Test both types of algorithm
 * 
 * @author sclerck
 * @date 12 Sep 2016
 *
 */
@Slf4j
public class TestAlgorithms {

	private static final int POINT_RATE_CHANGES = 100;

	private static final double SEED = 50.0;

	@Test
	public void testMidPointDisplacement() {
		LOG.info("Testing Midpoint Displacement");
		test(calculateStats(AlgorithmBuilder.builder(AlgorithmType.MIDPOINT_DISPLACEMENT), POINT_RATE_CHANGES, SEED,
				Volatility.values()));
	}

	@Test
	public void testPerlinNoise() {
		LOG.info("Testing Perlin Noise");
		test(calculateStats(AlgorithmBuilder.builder(AlgorithmType.PERLIN_NOISE), POINT_RATE_CHANGES, SEED,
				Volatility.values()));
	}

	private void test(Map<Volatility, Stats> stats) {
		Stats baseline = stats.get(Volatility.STABLE);

		Stats normal = stats.get(Volatility.NORMAL);

		Stats abnormal = stats.get(Volatility.ABNORMAL);

		assertTrue((normal.peaks + normal.troughs) > (baseline.peaks + baseline.troughs));

		assertTrue(normal.max() >= baseline.max());

		assertTrue(normal.mean() > baseline.mean());

		assertTrue((abnormal.peaks + abnormal.troughs) > (normal.peaks + normal.troughs));

		assertTrue(abnormal.max() >= normal.max());

		assertTrue(abnormal.mean() > normal.mean());
	}

	private Map<Volatility, Stats> calculateStats(Algorithm a, int pointRateChanges, double seed, Volatility[] vols) {

		Map<Volatility, Stats> results = new HashMap<>();

		for (Volatility vol : vols) {

			if (UNRECOGNIZED == vol) {
				continue;
			}

			Stats s = new Stats();

			boolean direction = false; // false = down, true = up. Assume down
										// for the start

			int lastValue = 0;

			int peakTroughValue = 0;

			NavigableMap<Double, Double> values = a.generateCurve(pointRateChanges, vol, seed);

			for (int i = 0; i < pointRateChanges; i++) {
				int v = values.floorEntry((double) i).getValue().intValue();

				if (lastValue == 0) {
					lastValue = v;

					peakTroughValue = v;

					continue;
				}

				if (v > lastValue && !direction) {
					s.troughs++;

					direction = true;

					lastValue = v;

					s.ds.addValue(Math.abs(peakTroughValue - v));

					peakTroughValue = v;

					continue;
				}

				if (v < lastValue && direction) {
					s.peaks++;

					direction = false;

					lastValue = v;

					s.ds.addValue(Math.abs(peakTroughValue - v));

					continue;
				}

				lastValue = v;
			}

			results.put(vol, s);
		}

		return results;
	}

	private class Stats {
		private int peaks;
		private int troughs;
		private final DescriptiveStatistics ds;

		private Stats() {
			peaks = 0;
			troughs = 0;
			ds = new DescriptiveStatistics();
		}

		private double mean() {
			return ds.getMean();
		}

		private double max() {
			return ds.getMax();
		}

		@Override
		public String toString() {
			return peaks + " " + troughs + " " + mean() + " " + max();
		}

	}
}
