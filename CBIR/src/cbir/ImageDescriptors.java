package cbir;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageDescriptors {

	static int[] lbp(int[][] img) {
		final int[] histogram = new int[256];

		final int[] dx = { 0, 0, -1, -1, -1, 1, 1, 1 };
		final int[] dy = { 1, -1, 0, 1, -1, 0, 1, -1 };

		for (int i = 1; i < img.length - 1; i++) {
			for (int j = 1; j < img[0].length - 1; j++) {
				int pow = 1;
				int pattern = 0;
				final int center = img[i][j];
				for (int k = 0; k < dx.length; k++) {
					if (center > img[i + dx[k]][j + dy[k]]) {
						pattern += pow;
					}
					pow *= 2;
				}
				histogram[pattern]++;
			}
		}
		return histogram;

	}

	static int[] lbp(BufferedImage img) {
		return lbp(ImageUtils.imagetoGreycale2dArray(img));
	}

	static int[] colorHistogram(BufferedImage img, int binsPerColor) {
		final int[][][] histogram = new int[binsPerColor][binsPerColor][binsPerColor];
		final int div = (int) Math.ceil(256d / binsPerColor);
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				Color c = new Color(img.getRGB(j, i));
				histogram[c.getRed() / div][c.getGreen() / div][c.getBlue() / div]++;
			}
		}
		return (ArrayUtils.flatten(histogram));
	}

	static float[] downsampledGrayscale(BufferedImage img1, int sz) {
		float[] f = new float[sz * sz];
		BufferedImage img = ImageUtils
				.convertToBufferedImage(img1.getScaledInstance(sz, sz, Image.SCALE_FAST));
		for (int i = 0; i < sz; i++) {
			for (int j = 0; j < sz; j++) {
				f[j * sz + i] = ImageUtils.colorToGreyscale(new Color(img.getRGB(j, i)));
			}
		}
		return f;
	}

	static float[] computeImageDescriptor(BufferedImage img, int binsPerColor, boolean useLbp,
			boolean useColor) {
		float[] desc = new float[0];
		if (useLbp) {
			desc = MathUtils.normalize(lbp(img));
		}
		if (useColor) {
			desc = ArrayUtils.concat(desc, MathUtils.normalize(colorHistogram(img, binsPerColor)));
		}
		return desc;
	}

}
