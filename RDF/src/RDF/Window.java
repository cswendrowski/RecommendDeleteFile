package RDF;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultCaret;

public class Window implements ActionListener {

	static JTextArea text, found;
	static JButton resume, pause;
	static RDF entry;
	static JFrame window;

	public Window() {
		entry = new RDF();

		window = new JFrame();
		text = new JTextArea(5, 20);
		DefaultCaret caret = (DefaultCaret) text.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		JScrollPane scrollPane = new JScrollPane(text);
		text.setEditable(false);

		resume = new JButton("Resume");
		pause = new JButton("Pause");
		JButton stop = new JButton("Stop");

		resume.setEnabled(false);

		JPanel buttons = new JPanel(new GridLayout(0, 3));

		buttons.add(resume);
		buttons.add(pause);
		buttons.add(stop);

		resume.addActionListener(this);
		pause.addActionListener(this);
		stop.addActionListener(this);

		window.add(BorderLayout.CENTER, scrollPane);
		window.add(BorderLayout.SOUTH, buttons);
		window.setSize(800, 600);
		window.setVisible(true);

		JFrame results = new JFrame();
		JButton showSearch = new JButton("Show Search Window");
		showSearch.addActionListener(this);
		showSearch.setActionCommand("Show");
		
		window.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		results.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		found = new JTextArea(5, 20);
		
		DefaultCaret caret2 = (DefaultCaret) found.getCaret();
		caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane scrollPane2 = new JScrollPane(found);
		found.setEditable(false);
		
		results.add(scrollPane2);
		results.add(BorderLayout.SOUTH, showSearch);
		results.setSize(900, 400);
		results.setVisible(true);

		entry.start();
	}

	public static void main(String[] args) {
		new Window();

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

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Pause")) {
			pause.setEnabled(false);
			resume.setEnabled(true);
			entry.pause();
		} else if (e.getActionCommand().equals("Resume")) {
			pause.setEnabled(true);
			resume.setEnabled(false);
			entry.resume();
		} else if (e.getActionCommand().equals("Stop")) {
			int n = JOptionPane.showConfirmDialog(null,
					"Are you sure you would like to stop searching?",
					"Confirmation", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.NO_OPTION)
				return;
			entry.stop();
			window.setVisible(false);
		}
		else if (e.getActionCommand().equals("Show")) {
			window.setVisible(true);
		}
	}
}
