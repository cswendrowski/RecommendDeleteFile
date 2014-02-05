package RDF;

/**
 * @author Cody Swendrowski
 * 
 */
public class Window {

	ResultsWindow results;
	SearchWindow search;
	RDF entry;
	UserOptionsWindow params;
	static double size;
	static int time;
	static String locations;

	public Window() {
		params = new UserOptionsWindow(this);
	}

	public Window(double size2, int time2) {
		createFrames();
		entry.setParams(locations, size2, time2);
		reportDone();
	}

	public static void main(String[] args) {
		if (args.length == 3) {
			locations = args[0];
			size = Double.valueOf(args[1]);
			time = Integer.valueOf(args[2]);
			new Window(size, time);
		} else {
			new Window();
		}
	}

	public double fileSize() {
		return params.getUserFileSize();
	}

	public int lastAccessTime() {
		return params.getUserLastAccessTime();
	}

	public String locations() {
		return params.getLocations();
	}

	public void reportDone() {
		search.show();
		results.show();
		entry.start();
	}

	public void createFrames() {
		entry = new RDF(this);
		search = new SearchWindow(entry);
		results = new ResultsWindow(this);

	}

}
