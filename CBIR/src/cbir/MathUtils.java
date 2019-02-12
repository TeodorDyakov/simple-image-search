package cbir;

import java.util.Arrays;

public class MathUtils {

	/**
	 * @param vector
	 * @return a new vector with the same length where each element is scaled
	 *         down by the sum of all elements in the argument.
	 */
	public static float[] normalize(int[] vector) {
		final int sum = Arrays.stream(vector).sum();
		final float[] normalized = new float[vector.length];
		for (int i = 0; i < normalized.length; i++) {
			normalized[i] = (float) vector[i] / sum;
		}
		return normalized;
	}

	/**
	 * @param vectorA
	 * @param vectorB
	 * @return L1 distance between the two vectors
	 */
	static float L1distance(float[] vectorA, float[] vectorB) {
		float distance = 0;
		for (int i = 0; i < vectorA.length; i++) {
			final float diff = vectorA[i] - vectorB[i];
			distance += Math.abs(diff);
		}
		return distance;
	}

	/**
	 * @param vectorA
	 * @param vectorB
	 * @return the l1 similarity between the two vectors
	 */
	static float similarity(float[] vectorA, float[] vectorB) {
		return 1 / (1 + L1distance(vectorA, vectorB));
	}

	public static float[] normalize(float[] desc) {
		float sum = 0;
		for (float i : desc) {
			sum += i;
		}
		final float[] normalized = new float[desc.length];
		for (int i = 0; i < normalized.length; i++) {
			normalized[i] = (float) desc[i] / sum;
		}
		return normalized;
	}
}
