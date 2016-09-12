/**
 * $Header:
 * $Id:
 * $Name:
 */
package com.sclerck.algorithms.utils;

import java.util.Random;

/**
 * Purpose: Math functions to support the algorithms
 * 
 * @author sclerck
 * @date 7 Sep 2016
 *
 */
public class MathFunctions {

	public static float randFloat(int seed, float min, float max) {
		Random rand = new Random(seed);
		return calcNextRandFloat(rand, min, max);
	}

	public static float randFloat(float min, float max) {
		Random rand = new Random();

		return calcNextRandFloat(rand, min, max);
	}

	private static float calcNextRandFloat(Random rand, float min, float max) {
		return rand.nextFloat() * (max - min) + min;
	}

	public static int randInt(int min, int max) {
		return new Random().nextInt((max - min) + 1) + min;
	}

}
