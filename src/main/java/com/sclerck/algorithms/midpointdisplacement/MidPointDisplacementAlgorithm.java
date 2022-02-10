/**
 * $Header:
 * $Id:
 * $Name:
 */
package com.sclerck.algorithms.midpointdisplacement;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.sclerck.algorithms.Algorithm;
import com.sclerck.algorithms.VolatilityMap;
import com.sclerck.algorithms.protos.AlgorithmType;
import com.sclerck.algorithms.protos.Volatility;

/**
 * Purpose: An algorithm based on midpoint displacement
 * 
 * See http://introcs.cs.princeton.edu/java/23recursion
 * 
 * For details (Brownian)
 * 
 * @author sclerck
 * @date 7 Sep 2016
 *
 */
public class MidPointDisplacementAlgorithm implements Algorithm {

	@Override
	public NavigableMap<Double, Double> generateCurve(final int numTickRateChanges, Volatility volatility) {
		return generateCurve(numTickRateChanges, volatility, SEED);
	}

	public NavigableMap<Double, Double> generateCurve(final int numTickRateChanges, Volatility volatility,
			final double seed) {
		final NavigableMap<Double, Double> results = new TreeMap<>();

		final double s = Math.pow(2,
				2 * Float.valueOf(VolatilityMap.getVolatility(volatility, AlgorithmType.MIDPOINT_DISPLACEMENT)).doubleValue());

		curve(0.0, seed, numTickRateChanges, seed, 5.0, s, results);

		return results;
	}

	private void curve(double x0, double y0, double x1, double y1, double var, double s, Map<Double, Double> results) {
		// stop if interval is sufficiently small
		if (Math.abs(x1 - x0) < 1.0) {
			if (results.isEmpty()) {
				results.put(x0, y0);
			}

			results.put(x1, y1);

			return;
		}

		double xm = (x0 + x1) / 2;
		double ym = (y0 + y1) / 2 + StdRandom.gaussian(0, Math.sqrt(var));
		curve(x0, y0, xm, ym, var / s, s, results);
		curve(xm, ym, x1, y1, var / s, s, results);
	}
}
