package RDF;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * 
 */

/**
 * @author Cody
 * 
 */
public class RDF {

	private long size, time, startTime, foundSpace, totalSpace;
	Window window;

	public RDF(Window w) {
		window = w;

		size = /* 1048576; */1073741824; // 1 GB
		startTime = System.currentTimeMillis();
		time = 2629740000l; // 1 Month
		totalSpace = new File("C:/").getTotalSpace()
				- new File("C:/").getFreeSpace();
		foundSpace = 0;

		// System.out.println("Size final: " + size);
	}

	public void start() {
		double userSize = window.fileSize();
		// Double.valueOf(JOptionPane.showInputDialog("What size should files be over? (GB)",
		// 1));
		int userTime = window.lastAccessTime();
		// Integer.valueOf(JOptionPane.showInputDialog("How many days since last access should files be over?",
		// 30));

		System.out.println("Size: " + userSize + " Time: " + userTime);

		if (userSize != 0)
			size = (long) (userSize * 1073741824.0);

		if (userTime != 0)
			time = userTime * 86400000l;

		window.results.setTitle(String.format(
				"Files over %.2f GB and older than %d days in C:/",
				size / 1073741824.0, time / 86400000));

		Thread repainter = new Thread(new Runnable() {
			@Override
			public void run() {
				search("C:/");
				window.search.clearText();
			}
		});
		repainter.setName("Searcher");
		repainter.setPriority(Thread.MIN_PRIORITY);
		repainter.start();

		// search("C:/");

	}

	private ArrayList<FileContainer> filesFound = new ArrayList<FileContainer>();

	private void search(String path) {
		if (shouldExit)
			return;

		while (!shouldRun) {
			// do nothing
		}
		File root = new File(path);
		// System.out.println("Searching " + path);
		window.search.addText("Searching " + path);
		File[] list = root.listFiles();

		if (list == null)
			return;

		for (File f : list) {
			if (f.isDirectory()) {
				search(f.getAbsolutePath());
			} else {
				FileContainer fc = new FileContainer(f);
				if (f.length() >= size) {
					if (startTime - fc.lastAccess() >= time) {
						filesFound.add(fc);
						window.results
								.addResult(String.format("%-25s", fc.name())
										+ "\t "
										+ String.format("%.2f GB",
												fc.size() / 1073741824.0)
										+ "\t"
										+ String.format(
												" %d days ago",
												((startTime - fc.lastAccess()) / 86400000))
										+ "\t" + fc.location());

					}
				}
				foundSpace += fc.size();
				double percent = ((double) foundSpace / (double) totalSpace) * 100;
				int passedTime = (int) (System.currentTimeMillis() - startTime) / 1000; // Seconds
				double dataRate = ((double) foundSpace / 1048576) / passedTime; // MB/s
				int eta = (int) (dataRate * (((double) totalSpace - (double) foundSpace) / 1048576));
				System.out.println("Passed: " + passedTime + " Found data:" +
						(foundSpace/1048576) +  " dataRate: " + dataRate);
				window.search.window.setTitle(String.format("%.1f", percent)
						+ "%    ETA: " + (eta/60) + " minutes " + (eta%60) + " seconds");
			}
		}
	}

	public void resume() {
		shouldRun = true;

	}

	private boolean shouldRun = true;

	public void pause() {
		shouldRun = false;

	}

	private boolean shouldExit = false;

	public void stop() {
		shouldExit = true;
	}
}
