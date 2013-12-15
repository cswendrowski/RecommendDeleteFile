package RDF;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;


public class Window {

	static JTextArea text, found;
	
	public static void main(String[] args) {
		RDF entry = new RDF();
		
		JFrame window = new JFrame();
		text = new JTextArea(5, 20);
		DefaultCaret caret = (DefaultCaret)text.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scrollPane = new JScrollPane(text);
		text.setEditable(false);
		window.add(scrollPane);
		window.setSize(800,600);
		window.setVisible(true);
		
		JFrame results = new JFrame();
		found = new JTextArea(5,20);
		DefaultCaret caret2 = (DefaultCaret)found.getCaret();
		caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scrollPane2 = new JScrollPane(found);
		found.setEditable(false);
		results.add(scrollPane2);
		results.setSize(900,400);
		results.setVisible(true);
		
		entry.start();
		
	}
	
	public static void addText(String s) {
		text.append(s + "\n");
	}
	
	public static void addResult(String s) {
		found.append(s + "\n");
	}

	public static void clearText() {
		text.setText("Search complete!");
		
	}
}
