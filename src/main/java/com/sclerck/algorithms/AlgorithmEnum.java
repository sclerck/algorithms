/**
 * $Header:
 * $Id:
 * $Name:
 */
package com.sclerck.algorithms;

import com.sclerck.algorithms.midpointdisplacement.MidPointDisplacementAlgorithm;
import com.sclerck.algorithms.perlinnoise.PerlinNoiseAlgorithm;

/**
 * Purpose: Catalogue of available algorithms
 * 
 * @author sclerck
 * @date 12 Sep 2016
 *
 */
public enum AlgorithmEnum {

	MIDPOINT_DISPLACEMENT(new MidPointDisplacementAlgorithm()), PERLIN_NOISE(new PerlinNoiseAlgorithm());

	private Algorithm algorithm;

	private AlgorithmEnum(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	public Algorithm get() {
		return algorithm;
	}

}
