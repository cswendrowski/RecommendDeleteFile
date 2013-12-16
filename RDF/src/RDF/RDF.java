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

	public RDF() {
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
		Window.addResult(String.format("Files over %.2f GB and older than %d days in C:/",size / 1073741824.0,time / 86400000));
		search("C:/");
		Window.clearText();
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
		Window.addText("Searching " + path);
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
						Window.addResult(String.format("%-25s", fc.name()) + "\tSize: "
								+ String.format("%.2f GB", fc.size() / 1073741824.0)
								+ "\tLast Accessed:"
								+ String.format(" %d days ago",((currentTime - fc.lastAccess()) / 86400000))
								+ "\tLocation: " + fc.location());
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
