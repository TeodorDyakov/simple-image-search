package cbir;

import java.util.Arrays;

public class MathUtils {

	public static int[] flatten(int[][][] threeDHistogram) {
		int dim = threeDHistogram.length;
		int size = dim * dim * dim;
		int[] histogram = new int[size];
		int idx = 0;
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				for (int k = 0; k < dim; k++) {
					histogram[idx++] = threeDHistogram[i][j][k];
				}
			}
		}
		return histogram;
	}

	static double[] normalize(int[] a) {
		int sum = Arrays.stream(a).sum();
		double[] res = new double[a.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = (double) a[i] / sum;
		}
		return res;
	}

	public static double L1(double[] vectorA, double[] vectorB) {
		double res = 0;
		for (int i = 0; i < vectorA.length; i++) {
			double diff = vectorA[i] - vectorB[i];
			res += Math.abs(diff);
		}
		return res;
	}

	public static double distance(int[] vectorA, int[] vectorB) {
		return 1 / L1(normalize(vectorA), normalize(vectorB));
	}

	public static int[] concat(int[] vecA, int[] vecB) {
		int[] vecC = new int[vecA.length + vecB.length];
		int idx = 0;
		for (int i = 0; i < vecA.length; i++) {
			vecC[idx++] = vecA[i];
		}
		for (int i = 0; i < vecB.length; i++) {
			vecC[idx++] = vecB[i];
		}
		return vecC;
	}
}
