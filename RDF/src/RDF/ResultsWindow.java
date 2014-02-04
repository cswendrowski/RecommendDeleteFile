package RDF;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

public class ResultsWindow implements ActionListener {

	JTable found;
	String[] names = { "Name", "Size", "Last Accessed", "Location" };
	Object[][] data = new Object[1][4];
	Window window;
	int index = 0;
	JFrame results;

	public ResultsWindow(Window w) {
		window = w;
		results = new JFrame();
		JButton showSearch = new JButton("Show Search Window");
		showSearch.addActionListener(this);
		showSearch.setActionCommand("Show");

		JButton open = new JButton("Open File Location");
		open.setActionCommand("Open");
		open.addActionListener(this);

		JButton blacklist = new JButton("Blacklist File");
		blacklist.setActionCommand("Blacklist");
		blacklist.addActionListener(this);

		JPanel resultButtons = new JPanel(new GridLayout(0, 3));

		DefaultTableModel defTableModel = new DefaultTableModel(data, names);
		found = new JTable(defTableModel);
		// found.setModel(new DefaultTableModel());
		found.setShowGrid(false);
		found.setIntercellSpacing(new Dimension(0, 0));

		JScrollPane scrollPane2 = new JScrollPane(found);

		found.setFillsViewportHeight(true);

		resultButtons.add(showSearch);
		resultButtons.add(open);
		resultButtons.add(blacklist);

		results.add(scrollPane2);
		results.add(BorderLayout.SOUTH, resultButtons);
		results.setSize(900, 400);
		results.setVisible(false);

		results.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Show")) {
			window.search.window.setVisible(true);
		} else if (e.getActionCommand().equals("Open")) {
			/*
			 * try { String s = (String)
			 * found.getModel().getValueAt(found.getSelectedRow(), 3); s =
			 * s.replace("\\", "\\\\");
			 * System.out.println("Trying to run: explorer.exe /select, " + s);
			 * new ProcessBuilder("explorer.exe /select, " + s).start(); } catch
			 * (IOException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); }
			 */

			if (Desktop.isDesktopSupported()) {
				String path = (String) found.getModel().getValueAt(
						found.getSelectedRow(), 3);

				final File f = new File(path);
				final Desktop desktop = Desktop.getDesktop();
				AccessController.doPrivileged(new PrivilegedAction() {
					public Object run() {
						try {
							desktop.open(f);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						return null;
					}
				});
			}
		} else if (e.getActionCommand().equals("Blacklist")) {
			try {
				String file = (String) found.getModel().getValueAt(
						found.getSelectedRow(), 0);
				File list = new File("Blacklist.txt");
				PrintWriter writer = new PrintWriter(list);

				window.entry.getBlackFiles().add(file);

				writer.write("FILES\r\n");

				for (String s : window.entry.getBlackFiles())
					writer.write("\n" + s + "\r\n");

				writer.write("\nEXTENSIONS\r\n");

				for (String s : window.entry.getBlackTypes())
					writer.write("\n" + s + "\r\n");

				writer.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}

	}

	public void addResult(String s) {

		ensureCapacity(index + 1);

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
		// m.addRow(a);
		found.setModel(m);
		m.fireTableDataChanged();
	}

	private void ensureCapacity(int i) {
		if (data.length > i)
			return;

		// else
		// Copies data over to new array
		Object[][] temp = new Object[data.length * 2][4];
		for (int x = 0; x < data.length; x++)
			for (int y = 0; y < data[x].length; y++)
				temp[x][y] = data[x][y];

		data = temp;

	}

	public void setTitle(String s) {
		results.setTitle(s);
	}

	public void show() {
		results.setVisible(true);

	}
}
