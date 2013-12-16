package RDF;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;

public class Window implements ActionListener {

	static JTextArea text;
	static JTable found;
	static JButton resume, pause;
	static RDF entry;
	static String[] names = { "Name", "Size", "Last Modified","Location" };
	static JFrame window;
	static Object[][] data = new Object[0][4];

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

		DefaultTableModel defTableModel = new DefaultTableModel(data, names);
		found = new JTable(defTableModel);
		// found.setModel(new DefaultTableModel());

		JScrollPane scrollPane2 = new JScrollPane(found);

		found.setFillsViewportHeight(true);

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

	static int index = 0;

	public static void addResult(String s) {

		//Copies data over to new array for proper sizing
		Object[][] temp = new Object[data.length+1][4];
		for (int x=0; x<data.length; x++)
			for (int y=0; y<data[x].length; y++)
				temp[x][y] = data[x][y];
		
		data = temp;
		
		Scanner scan = new Scanner(s);
		scan.useDelimiter("\t");
		int cnt = 0;
		while (scan.hasNext()) {
			String f = scan.next();
			System.out.println("Found: " + f);
			data[index][cnt] = f;
			cnt++;
		}
		scan.close();
		index++;
		DefaultTableModel m = new DefaultTableModel(data, names);
		//m.addRow(a);
		found.setModel(m);
		m.fireTableDataChanged();
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
		} else if (e.getActionCommand().equals("Show")) {
			window.setVisible(true);
		}
	}
}
