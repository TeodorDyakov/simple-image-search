package cbir;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

public class Index implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<IndexedImage> index;

	boolean useLBP = false;
	boolean useColor = false;

	int binsPerColor = 8;

	Index(int binsPerColor, boolean useLBP, boolean useColor) {
		this.useColor = useColor;
		this.binsPerColor = binsPerColor;
		this.useLBP = useLBP;
	}

	void saveToFile(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream out = new ObjectOutputStream(fos);
		out.writeObject(this);
		out.close();
		fos.close();
	}

	static Index loadIndexFromFile(File file) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream in = new ObjectInputStream(fis);
		Index index = (Index) in.readObject();
		in.close();
		fis.close();
		return index;
	}

	IndexedImage createIndexedImage(File file) throws IOException {
		BufferedImage img = ImageIO.read(file);
		return new IndexedImage(file,
				ImageDescriptors.computeImageDescriptor(img, binsPerColor, useLBP, useColor));
	}

	List<IndexedImage> buildIndex(String pathToImageFolder) throws IOException, ClassNotFoundException {

		int count = 0;
		long tic = System.currentTimeMillis();

		File[] listOfFiles = new File(pathToImageFolder).listFiles();
		List<IndexedImage> indexedImages = new ArrayList<>();

		for (File f : listOfFiles) {

			System.out.println(count++ + "/" + listOfFiles.length + " images indexed");

			indexedImages.add(createIndexedImage(f));
		}

		System.out.println("index built in: " + (System.currentTimeMillis() - tic) / 1000f + "seconds");

		return index = indexedImages;
	}

	/*
	 * O(k*n) algorithm to return the k most similar images to the query image (
	 * this is fast when k << n due to locality of reference)
	 */
	List<File> getTopKMatches(BufferedImage queryImg, int k) {
		long tic = System.currentTimeMillis();

		float[] imageQueryDescriptor = ImageDescriptors.computeImageDescriptor(queryImg, binsPerColor, useLBP,
				useColor);

		Set<File> results = new LinkedHashSet<>();
		/*
		 * Precompute the similarity between the query and all the images in the
		 * index
		 */
		float[] similarityCache = new float[index.size()];

		for (int i = 0; i < index.size(); i++) {
			similarityCache[i] = MathUtils.similarity(imageQueryDescriptor, index.get(i).imageDescriptor);
		}

		for (int i = 0; i < k; i++) {

			float maxSimilarity = 0;
			File argmin = null;
			int idx = 0;

			for (IndexedImage img : index) {
				float similarity = similarityCache[idx++];

				if (!results.contains(img.file) && maxSimilarity < similarity) {
					maxSimilarity = similarity;
					argmin = img.file;
				}
			}

			System.out.println(argmin.getName() + " similarity: " + String.format("%.3f", maxSimilarity));

			results.add(argmin);
		}

		System.out.println("Search done in: " + (System.currentTimeMillis() - tic) / 1000f + "s");

		return new ArrayList<>(results);
	}

}
