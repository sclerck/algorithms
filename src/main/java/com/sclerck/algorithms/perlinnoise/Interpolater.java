/**
 * $Header:
 * $Id:
 * $Name:
 */
package com.sclerck.algorithms.perlinnoise;

/**
 * Purpose: Perlin noise can be generated using different interpolater
 * algorithms
 * 
 * @author sclerck
 * @date 7 Sep 2016
 *
 */
public interface Interpolater {

	float interpolate(float a, float b, float x);
}
