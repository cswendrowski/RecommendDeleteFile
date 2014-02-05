package RDF;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Cody Swendrowski
 * 
 */
public class RDF {

	private long size, time, startTime, foundSpace, totalSpace;
	private ArrayList<String> blackFiles, blackTypes, queue;
	Window window;

	public RDF(Window w) {
		window = w;

		size = /* 1048576; */1073741824; // 1 GB
		startTime = System.currentTimeMillis();
		time = 2629740000l; // 1 Month
		File c = new File("C:/");
		/*
		 * System.out.println("C:/ total: " + c.getTotalSpace() + " free: " +
		 * c.getFreeSpace() + " used:" + (c.getTotalSpace() -
		 * c.getFreeSpace()));
		 */
		totalSpace = c.getTotalSpace() - c.getUsableSpace();

		foundSpace = 0;

		blackFiles = new ArrayList<String>();
		blackTypes = new ArrayList<String>();
		queue = new ArrayList<String>();

		// System.out.println("Size final: " + size);
	}

	public void start() {
		loadBlacklist();

		if (queue.size() == 0) {
			String locationList = window.locations();

			// Read in locations from the list
			Scanner reader = new Scanner(locationList);
			reader.useDelimiter(",");

			while (reader.hasNext())
				queue.add(reader.next());

			reader.close();
		}

		double userSize = 0;
		int userTime = 0;

		if (size == 0)
			userSize = window.fileSize();
		// Double.valueOf(JOptionPane.showInputDialog("What size should files be over? (GB)",
		// 1));
		if (time == 0)
			userTime = window.lastAccessTime();
		// Integer.valueOf(JOptionPane.showInputDialog("How many days since last access should files be over?",
		// 30));

		System.out.println("Size: " + userSize + " Time: " + userTime);

		if (userSize != 0)
			size = (long) (userSize * 1073741824.0);

		if (userTime != 0)
			time = userTime * 86400000l;

		String list = "";
		for (String s : queue)
			list = list + s + ",";

		list = list.substring(0, list.length() - 1);
		window.results.setTitle(String.format(
				"Files over %.2f GB and older than %d days in " + list,
				size / 1073741824.0, time / 86400000));

		Thread repainter = new Thread(new Runnable() {
			@Override
			public void run() {
				for (String s : queue) {
					search(s);
					window.search.clearText();
				}
				window.search.window.setTitle("100%    ETA: DONE");
			}
		});
		repainter.setName("Searcher");
		repainter.setPriority(Thread.MIN_PRIORITY);
		repainter.start();
		startTime = System.currentTimeMillis();
	}

	private void loadBlacklist() {
		File list = new File("blacklist.txt");
		if (!list.exists())
			try {
				list.createNewFile();
				System.out.println("File created.");
			} catch (IOException e) {
				e.printStackTrace();
			}

		try {
			System.out.println("Reading file.");
			Scanner scan = new Scanner(list);

			// Don't start loading until we hit the first section
			while (scan.hasNext()) {
				String found = scan.next();
				System.out.println("Found extra: " + found);
				if (found.equals("FILES"))
					break;
			}

			while (scan.hasNext()) {
				String found = scan.next();
				if (found == null)
					found = "null";
				if (found.equals("EXTENSIONS"))
					break;
				blackFiles.add(found);

				System.out.println("BL'ing file " + found);
			}

			// Should only be entered when second section is found
			while (scan.hasNext()) {
				String found = scan.next();
				blackTypes.add(found);
				System.out.println("BL'ing type " + found);
			}

			scan.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<String> getBlackFiles() {
		return blackFiles;
	}

	public ArrayList<String> getBlackTypes() {
		return blackTypes;
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
						boolean blackFlag = false;
						for (String s : blackFiles)
							if (s.equals(fc.name()))
								blackFlag = true;

						for (String s : blackTypes) {
							System.out.println("Extension: "
									+ fc.name().substring(
											fc.name().indexOf("."),
											fc.name().length()));
							if (s.equals(fc.name().substring(
									fc.name().indexOf("."), fc.name().length())))
								blackFlag = true;

						}

						if (!blackFlag) {
							filesFound.add(fc);
							window.results
									.addResult(String.format("%-25s", fc.name())
											+ "\t "
											+ String.format("%.2f GB",
													fc.size() / 1073741824.0)
											+ "\t"
											+ String.format(
													" %d days ago",
													((startTime - fc
															.lastAccess()) / 86400000))
											+ "\t" + fc.location());
						}

					}
				}
				foundSpace += fc.size();
				double percent = ((double) foundSpace / (double) totalSpace) * 100;
				int passedTime = (int) (System.currentTimeMillis() - startTime) / 1000; // Seconds
				double dataRate = ((double) foundSpace / 1048576) / passedTime; // MB/s
				int eta = (int) ((1 / dataRate) * (((double) totalSpace - (double) foundSpace) / 1048576));
				window.search.window.setTitle(String.format("%.1f", percent)
						+ "%    ETA: " + (eta / 60) + " minutes " + (eta % 60)
						+ " seconds");

				if (percent > 100)
					window.search.window.setTitle("100%    ETA: DONE");

				/*
				 * System.out.println("Found: " + foundSpace + "  Total: " +
				 * totalSpace);
				 */
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

	public void setParams(String locs, double s, int t) {
		Scanner scan = new Scanner(locs);
		scan.useDelimiter(",");
		while (scan.hasNext())
			queue.add(scan.next());

		scan.close();

		size = (long) (s * 1073741824l);
		time = t * 86400000l;
	}
}
