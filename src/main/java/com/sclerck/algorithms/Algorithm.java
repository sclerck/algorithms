/**
 * $Header:
 * $Id:
 * $Name:
 */
package com.sclerck.algorithms;


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
	 * Generate a curve of navigable points, based on the number of tick rate
	 * changes and volatility
	 * 
	 * @param numTickRateChanges
	 * @param volatility
	 * @return a navigable map of points
	 */
	NavigableMap<Double, Double> generateCurve(final int numTickRateChanges, Volatility volatility);

	/**
	 * Generate a curve of navigable points, based on the number of tick rate
	 * changes, volatility a seed
	 * 
	 * @param numTickRateChanges
	 * @param volatility
	 * @param seed
	 * @return a navigable map of points
	 */
	NavigableMap<Double, Double> generateCurve(final int numTickRateChanges, Volatility volatility, final double seed);
}
