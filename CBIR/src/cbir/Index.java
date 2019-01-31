package cbir;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Index {

	static int binsPerColor = 8;
	static List<IndexedImage> index;

	static boolean useLBP = false;
	static boolean useColor = false;

	static void saveIndexToFile(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream out = new ObjectOutputStream(fos);
		out.writeObject(index);
		out.close();
		fos.close();
	}

	@SuppressWarnings("unchecked")
	static void loadIndexFromFile(File file) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream in = new ObjectInputStream(fis);
		index = (List<IndexedImage>) in.readObject();
		in.close();
		fis.close();
	}

	static IndexedImage createIndexedImage(File file) throws IOException {
		BufferedImage img = ImageIO.read(file);
		IndexedImage indexedImage = new IndexedImage();
		indexedImage.imageDescriptor = ImageDescriptors.computeImageDescriptor(img, binsPerColor, useLBP,
				useColor);

		indexedImage.file = file;
		return indexedImage;
	}

	static List<IndexedImage> buildIndex(String pathToImageFolder)
			throws IOException, ClassNotFoundException {
		int count = 0;
		long tic = System.currentTimeMillis();

		File folder = new File(pathToImageFolder);
		File[] listOfFiles = folder.listFiles();
		List<IndexedImage> indexedImages = new ArrayList<>();
		for (File f : listOfFiles) {
			System.out.println(count++ + "/" + listOfFiles.length + " images indexed");
			indexedImages.add(createIndexedImage(f));
		}
		System.out.println("index built in: " + (System.currentTimeMillis() - tic) / 1000f + "seconds");
		return index = indexedImages;
	}

	static List<File> getTopKMatches(BufferedImage queryImg, int k) {
		long tic = System.currentTimeMillis();

		float[] imageQueryDescriptor = ImageDescriptors.computeImageDescriptor(queryImg, binsPerColor,
				useLBP, useColor);

		List<File> results = new ArrayList<>();
		for (int i = 0; i < k; i++) {

			float maxSimilarity = Float.NEGATIVE_INFINITY;
			File argmin = null;

			for (IndexedImage img : index) {
				float similarity = MathUtils.similiraty(imageQueryDescriptor, img.imageDescriptor);
				if (!results.contains(img.file) && maxSimilarity < similarity) {
					maxSimilarity = similarity;
					argmin = img.file;
				}
			}
			System.out.println(argmin.getName() + " similarity: " + String.format("%.3f", maxSimilarity));
			results.add(argmin);
		}
		System.out.println("Search done in: " + (System.currentTimeMillis() - tic) / 1000f + "s");
		return results;
	}

	// static String imagesFolderPath = "image.vary.jpg/image.vary.jpg";
	static String imagesFolderPath = "image.orig";
	static File defaultIndexFile = new File("index/index.txt");

}
