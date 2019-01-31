package cbir;

import java.util.Arrays;

public class ArrayUtils {

	static int[] concat(int[] vecA, int[] vecB) {
		final int[] vecC = new int[vecA.length + vecB.length];
		System.arraycopy(vecA, 0, vecC, 0, vecA.length);
		System.arraycopy(vecB, 0, vecC, vecA.length, vecB.length);
		return vecC;
	}

	static int[] flatten(int[][][] threeDHistogram) {
		return Arrays.stream(threeDHistogram).flatMap(Arrays::stream).flatMapToInt(Arrays::stream).toArray();
	}

}
