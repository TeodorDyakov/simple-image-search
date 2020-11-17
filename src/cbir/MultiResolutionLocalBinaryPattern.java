package cbir;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class MultiResolutionLocalBinaryPattern {
	/**
	 * @param img
	 * @return an histogram of local binary patterns of size 7 * 256
	 */
	public static float[] mrlbph(BufferedImage img) {
		img = ImageUtils.convertToBufferedImage(img.getScaledInstance(256, 256, Image.SCALE_AREA_AVERAGING));
		float[] des = new float[0];
		while (img.getHeight() >= 3 && img.getWidth() >= 3) {
			int height = img.getHeight();
			int width = img.getWidth();
			des = ArrayUtils.concat(des, MathUtils.normalize(ImageDescriptors.lbp(img)));
			height /= 2;
			width /= 2;
			Image down = img.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
			img = ImageUtils.convertToBufferedImage(down);
		}
		return des;
	}
}
