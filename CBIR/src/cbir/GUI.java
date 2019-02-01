package cbir;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GUI() {
		JLabel labelQ = new JLabel("query image");
		add(labelQ);
		int count = 0;
		for (BufferedImage bi : bil) {
			add(new JLabel("" + count++));
			ImageIcon imageIcon = new ImageIcon(
					bi.getScaledInstance(bi.getWidth() / 2, bi.getHeight() / 2, Image.SCALE_DEFAULT));
			JLabel label = new JLabel(imageIcon);
			add(label);
		}
	}

	static List<BufferedImage> bil = new ArrayList<>();
	static File defaultIndexFile = new File("index/index.txt");
	static boolean loadIndexFromFile = false;
	static String imagesFolderPath = "image.orig";

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		/*
		 * different options for image descriptors - 3d color histogram and
		 * local binary patterns histogram.
		 * 
		 * binsPerColor - each color channel is quantized into N equally sized
		 * bins of size 256/N. The total number of bins in the 3d color
		 * histogram is N^3.
		 * 
		 * The two image descriptors are concatenated in one vector which is
		 * then used to compare two images.
		 * 
		 * //TODO Change the user interface so it is easier to use. Maybe create
		 * a form to type path of image add button for saving and building
		 * index.
		 */
		Index index = new Index(8, true, true);
		// loadIndexFromFile = true;
		boolean checkPrecision = false;
		// checkPrecision = true;
		int numberOfResults = 10;

		if (loadIndexFromFile) {
			index = Index.loadIndexFromFile(defaultIndexFile);
		} else {
			index.buildIndex(imagesFolderPath);
		}

		BufferedImage img = ImageIO.read(new File(imagesFolderPath + "\\214.jpg"));
		List<File> im = index.getTopKMatches(img, numberOfResults);

		bil.add(img);
		for (File f : im) {
			BufferedImage bi = ImageIO.read(f);
			bil.add(bi);
		}
		if (checkPrecision) {
			int corr = 0;
			for (File f : new File(imagesFolderPath).listFiles()) {

				for (File f1 : index.getTopKMatches(ImageIO.read(f), 2).subList(1, 2)) {
					int l = Integer.valueOf(f.getName().replaceAll("\\.jpg", ""));
					int l1 = Integer.valueOf(f1.getName().replaceAll("\\.jpg", ""));
					if (l / 100 == l1 / 100) {
						corr++;
					}
				}
			}
			System.out.println((double) corr / 1000);
		}
		JPanel jp = new GUI();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(jp);
		frame.pack();
		frame.setSize(1000, 600);
		frame.setVisible(true);
		index.saveToFile(defaultIndexFile);
	}

}
