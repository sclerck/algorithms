/**
 * $Header:
 * $Id:
 * $Name:
 */
package com.sclerck.algorithms.perlinnoise;

/**
 * Purpose: A cosine based version of an interpolater
 * 
 * @author sclerck
 * @date 7 Sep 2016
 *
 */
public class CosineInterpolater implements Interpolater {

	public float interpolate(float a, float b, float x) {
		float ft = (float) (x * Math.PI);
		float f = (float) ((1 - Math.cos(ft)) * 0.5);

		return a * (1 - f) + b * f;
	}

}
