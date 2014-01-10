package RDF;

public class Window {

	ResultsWindow results;
	SearchWindow search;
	RDF entry;

	public Window() {
		entry = new RDF(this);
		search = new SearchWindow(entry);
		results = new ResultsWindow(this);

		entry.start();
	}

	public static void main(String[] args) {
		new Window();
	}

}
