package cbir;

import java.util.Arrays;

public class MathUtils {

	static float[] normalize(int[] a) {
		final int sum = Arrays.stream(a).sum();
		final float[] res = new float[a.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = (float) a[i] / sum;
		}
		return res;
	}

	static float L1distance(float[] vectorA, float[] vectorB) {
		float res = 0;
		for (int i = 0; i < vectorA.length; i++) {
			final float diff = vectorA[i] - vectorB[i];
			res += Math.abs(diff);
		}
		return res;
	}

	static float similiraty(float[] vectorA, float[] vectorB) {
		return 1 / L1distance(vectorA, vectorB);
	}
}
