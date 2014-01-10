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

	private long size, time, currentTime;
	Window window;

	public RDF(Window w) {
		window = w;
		
		size = /* 1048576; */1073741824; // 1 GB
		currentTime = System.currentTimeMillis();
		time = 2629740000l; // 1 Month
		
		double userSize = Double.valueOf(JOptionPane.showInputDialog("What size should files be over? (GB)", 1));
		int userTime = Integer.valueOf(JOptionPane.showInputDialog("How many days since last access should files be over?", 30));
		
		System.out.println("Size: " + userSize + " Time: " + userTime);
		
		if (userSize != 0)
			size = (long) (userSize*1073741824.0);
		
		if (userTime != 0)
			time = userTime*86400000l;
		
		//System.out.println("Size final: " + size);
	}

	public void start() {
		window.results.setTitle(String.format("Files over %.2f GB and older than %d days in C:/",size / 1073741824.0,time / 86400000));
		search("C:/");
		window.search.clearText();
	}

	private ArrayList<FileContainer> filesFound = new ArrayList<FileContainer>();

	private void search(String path) {
		if (shouldExit)
			return;
		
		while (!shouldRun) {
			//do nothing
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
				if (f.length() >= size) {
					FileContainer fc = new FileContainer(f);
					if (currentTime - fc.lastAccess() >= time) {
						filesFound.add(fc);
						window.results.addResult(String.format("%-25s", fc.name()) + "\t "
								+ String.format("%.2f GB", fc.size() / 1073741824.0)
								+ "\t"
								+ String.format(" %d days ago",((currentTime - fc.lastAccess()) / 86400000))
								+ "\t" + fc.location());
					}
				}
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
