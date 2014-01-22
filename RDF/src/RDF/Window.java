package RDF;

public class Window {

	ResultsWindow results;
	SearchWindow search;
	RDF entry;
	UserOptionsWindow params;

	public Window() {
		
		
		params = new UserOptionsWindow(this);
		
	}

	public static void main(String[] args) {
		new Window();
	}

	public double fileSize() {
		return params.getUserFileSize();
	}

	public int lastAccessTime() {
		return params.getUserLastAccessTime();
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
