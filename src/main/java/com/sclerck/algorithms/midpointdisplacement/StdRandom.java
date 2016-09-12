/**
 * $Header:
 * $Id:
 * $Name:
 */
package com.sclerck.algorithms.midpointdisplacement;

import java.util.Random;

/**
 * Purpose: Random number generater methods required for this algorithm
 * 
 * This class is effectively copied from:
 * http://introcs.cs.princeton.edu/java/22library/StdRandom.java.html
 * 
 * @author sclerck
 * @date 7 Sep 2016
 *
 */
public class StdRandom {

	private static Random random;
	private static long seed;

	static {
		seed = System.currentTimeMillis();
		random = new Random(seed);
	}

	private StdRandom() {
	}

	/**
	 * Returns a real number for a gaussian distribution with given mean and std
	 * dev
	 * 
	 * @param mean
	 * @param stddev
	 * @return
	 */
	public static double gaussian(double mean, double stddev) {
		return mean + stddev * gaussian();
	}

	/**
	 * Returns a real number with a standard Gaussian distribution
	 * 
	 * @return
	 */
	public static double gaussian() {
		// use the polar form of the Box-Muller transform
		double r, x, y;

		do {
			x = uniform(-1.0, 1.0);
			y = uniform(-1.0, 1.0);
			r = x * x + y * y;
		} while (r >= 1 || r == 0);

		return x * Math.sqrt(-2 * Math.log(r) / r);
	}

	/**
	 * Returns a real number uniformly in [a, b).
	 * 
	 * @param a
	 * @param b
	 * @return
	 * 
	 * @throws IllegalArgumentException
	 *             unless <tt>a < b</tt>
	 */
	public static double uniform(double a, double b) {
		if (!(a < b))
			throw new IllegalArgumentException("Invalid range");

		return a + uniform() * (b - a);
	}

	/**
	 * Return a real number uniformly in [0, 1).
	 * 
	 * @return
	 */
	public static double uniform() {
		return random.nextDouble();
	}
}
