/**
 * $Header:
 * $Id:
 * $Name:
 */
package com.sclerck.algorithms;

/**
 * Purpose: Volatility is a common concept but different algorithms have
 * different interpretations
 * 
 * @author sclerck
 * @date 7 Sep 2016
 *
 */
public interface Volatility {

	float getVolatility(AlgorithmEnum algorithm);
}
