package cbir;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageUtils {

	static int[][] imagetoGreycale2dArray(BufferedImage img) {
		final int[][] arr = new int[img.getHeight()][img.getWidth()];

		for (int i = 0; i < arr.length; i++)
			for (int j = 0; j < arr[0].length; j++) {
				final Color color = new Color(img.getRGB(j, i));
				arr[i][j] = colorToGreyscale(color);
			}
		return arr;
	}

	static final int colorToGreyscale(final Color c) {
		// Y = 0.2989 R + 0.5870 G + 0.1140 B
		return (int) (0.2989 * c.getRed() + 0.5870 * c.getGreen() + 0.1140 * c.getBlue());
	}

	public static BufferedImage convertToBufferedImage(Image image) {
		BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}
}
