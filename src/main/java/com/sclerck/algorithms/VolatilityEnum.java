/**
 * $Header:
 * $Id:
 * $Name:
 */
package com.sclerck.algorithms;

/**
 * Purpose: Identifies the frequencies for each algorithm under different
 * scenarios
 * 
 * @author sclerck
 * @date 12 Sep 2016
 *
 */
public enum VolatilityEnum implements Volatility {

	STABLE(0.5f, 0.25f), NORMAL(0.2f, 0.708f), ABNORMAL(0.002f, 1.00f);

	private float midPointVolatility;
	private float noiseVolatility;

	VolatilityEnum(float midPointVolatility, float noiseVolatility) {
		this.midPointVolatility = midPointVolatility;
		this.noiseVolatility = noiseVolatility;
	}

	public float getVolatility(AlgorithmEnum a) {
		if (a == AlgorithmEnum.MIDPOINT_DISPLACEMENT) {
			return midPointVolatility;
		}

		if (a == AlgorithmEnum.PERLIN_NOISE) {
			return noiseVolatility;
		}

		throw new IllegalArgumentException("Unrecognized algorithm");
	}

}
