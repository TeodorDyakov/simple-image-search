package cbir;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageDescriptors {

	static int[] lbp(BufferedImage image) {
		final int[][] img = ImageDescriptors.imagetoGreycale2dArray(image);
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

	static int[] colorHistogram(BufferedImage img, int binsPerColor) {
		final int[][][] histogram = new int[binsPerColor][binsPerColor][binsPerColor];
		final int div = (int) Math.ceil(256d / binsPerColor);
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				Color c = new Color(img.getRGB(j, i));
				histogram[c.getRed() / div][c.getGreen() / div][c.getBlue() / div]++;
			}
		}
		return ArrayUtils.flatten(histogram);
	}

	static float[] computeImageDescriptor(BufferedImage img, int binsPerColor, boolean useLbp,
			boolean useColor) {
		if (useLbp && useColor) {
			return MathUtils.normalize(ArrayUtils.concat(lbp(img), colorHistogram(img, binsPerColor)));
		}
		if (useLbp) {
			return MathUtils.normalize(lbp(img));
		}
		return MathUtils.normalize(colorHistogram(img, binsPerColor));
	}

	static int[][] imagetoGreycale2dArray(BufferedImage img) {
		int[][] arr = new int[img.getHeight()][img.getWidth()];

		for (int i = 0; i < arr.length; i++)
			for (int j = 0; j < arr[0].length; j++) {
				final Color c = new Color(img.getRGB(j, i));
				// Y = 0.2989 R + 0.5870 G + 0.1140 B
				final int grey = (int) (0.2989 * c.getRed() + 0.5870 * c.getGreen() + 0.1140 * c.getBlue());
				arr[i][j] = grey;
			}
		return arr;
	}

}
