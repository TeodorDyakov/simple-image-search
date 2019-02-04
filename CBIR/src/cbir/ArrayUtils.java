package cbir;

import java.util.Arrays;

public final class ArrayUtils {

	static final float[] concat(float[] desc, float[] fs) {
		final float[] vecC = new float[desc.length + fs.length];
		System.arraycopy(desc, 0, vecC, 0, desc.length);
		System.arraycopy(fs, 0, vecC, desc.length, fs.length);
		return vecC;
	}

	static final int[] flatten(int[][][] threeDHistogram) {
		return Arrays.stream(threeDHistogram).flatMap(Arrays::stream).flatMapToInt(Arrays::stream).toArray();
	}

	static final int argmax(float[] arr) {
		float max = Float.NEGATIVE_INFINITY;
		int argmax = 0;
		for (int i = 0; i < arr.length; i++) {
			if (max < arr[i]) {
				max = arr[i];
				argmax = i;
			}
		}
		return argmax;
	}

}
