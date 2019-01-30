package cbir;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.BitSet;

public class CardinalityFast {
	static final int SIZE = 5;
	static final double[] entropyLUT = new double[SIZE * SIZE + 1];
	static {
		for (int k = 0; k < entropyLUT.length; k++) {
			double p = (double) k / (entropyLUT.length - 1); // calculate p
			entropyLUT[k] = -p * Math.log(p) / Math.log(2.0);
		}
	}

	static int[] entropyFilter(BufferedImage source) {
		int[] des = new int[128];
		int sz = SIZE / 2;
		int width = source.getWidth();
		int height = source.getHeight();

		int[] histogram = new int[256];
		for (int i = sz + 1; i < width - sz - 1; i++) {
			for (int j = sz + 1; j < height - sz - 1; j++) {
				if (j == sz + 1) {
					Arrays.fill(histogram, 0);
					for (int ii = -sz; ii <= sz; ii++) {
						for (int jj = -sz; jj <= sz; jj++) {
							int red = new Color(source.getRGB(i + ii, j + jj)).getRed();
							histogram[red]++;
						}
					}
				} else {
					for (int jj = -sz; jj <= sz; jj++) {
						int red = new Color(source.getRGB(i + jj, j + sz)).getRed();
						int red1 = new Color(source.getRGB(i + jj, j - sz - 1)).getRed();
						histogram[red]++;
						histogram[red1]--;
					}
				}
				double entropy = 0;
				for (int k = 0; k < histogram.length; k++) {
					if (histogram[k] > 0) {
						entropy += entropyLUT[histogram[k]];
					}
				}
				des[(int) ((entropy * des.length) / (entropyLUT.length))]++;
			}
		}
		return des;
	}

	static float lerp(float v0, float v1, float t) {
		return v0 + t * (v1 - v0);
	}

	static int[] cardinality(int[][] source) {
		int sz = SIZE / 2;
		double area = (SIZE + 1) * (SIZE + 1) + 1;
		BitSet bs = new BitSet(257);
		int width = source.length;
		int height = source[0].length;
		int[] histogram = new int[(int) area];
		for (int i = sz; i < width - sz; i++) {
			for (int j = sz; j < height - sz; j++) {
				bs.clear();
				for (int ii = -sz; ii <= sz; ii++) {
					for (int jj = -sz; jj <= sz; jj++) {
						bs.set(source[i + ii][j + jj]);
					}
				}
				histogram[bs.cardinality()]++;
			}
		}
		return histogram;
	}
}
