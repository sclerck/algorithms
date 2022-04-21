/**
 * $Header:
 * $Id:
 * $Name:
 */
package com.sclerck.algorithms;


import com.sclerck.algorithms.protos.Volatility;

import java.util.NavigableMap;

/**
 * Purpose: Algorithms must be able to generate curves
 * 
 * @author sclerck
 * @date 7 Sep 2016
 *
 */
public interface Algorithm {

	double SEED = 50.0;

	/**
	 * Generate a curve of navigable points, based on the number of point rate changes
	 * and volatility
	 *
	 * @param numPointRateChanges
	 * @param volatility
	 * @return a navigable map of points
	 */
	NavigableMap<Double, Double> generateCurve(final int numPointRateChanges, Volatility volatility);

	/**
	 * Generate a curve of navigable points, based on the number of point rate
	 * changes, volatility and seed
	 *
	 * @param numPointRateChanges
	 * @param volatility
	 * @param seed
	 * @return a navigable map of points
	 */
	NavigableMap<Double, Double> generateCurve(final int numPointRateChanges, Volatility volatility, final double seed);
}
