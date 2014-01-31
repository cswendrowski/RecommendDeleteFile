package RDF;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UserOptionsWindow implements ActionListener {

	Window window;
	JTextField size, time;
	JFrame frame;
	
	public UserOptionsWindow(Window w) {
		window = w;
		
		frame = new JFrame();
		JPanel masterPanel = new JPanel();
		masterPanel.setLayout(new BoxLayout(masterPanel,BoxLayout.PAGE_AXIS));

		JLabel sizeLabel = new JLabel("What size should files be over? (GB)");
		size = new JTextField("1");
		
		JPanel sizePanel = new JPanel(new BorderLayout());
		sizePanel.add(sizeLabel);
		sizePanel.add(size, BorderLayout.SOUTH);
		
		JLabel timeLabel = new JLabel("How many days since last access should files be over?");
		time = new JTextField("30");
		
		JPanel timePanel = new JPanel(new BorderLayout());
		timePanel.add(timeLabel);
		timePanel.add(time, BorderLayout.SOUTH);
		
		masterPanel.add(sizePanel);
		masterPanel.add(timePanel);
		
		frame.add(masterPanel);
		
		JPanel buttons = new JPanel(new GridLayout(0,3));
		JButton ok = new JButton("Ok");
		ok.setActionCommand("Ok");
		JButton cancel = new JButton("Cancel");
		JButton blacklist = new JButton("Blacklist");
		buttons.add(ok);
		buttons.add(cancel);
		buttons.add(blacklist);
		
		ok.addActionListener(this);
		cancel.addActionListener(this);
		blacklist.addActionListener(this);
		
		frame.add(buttons, BorderLayout.SOUTH);
		frame.setSize(800, 500);
		frame.setVisible(true);
		
		window.createFrames();
		//window.reportDone();
	}

	public int getUserLastAccessTime() {
		return Integer.valueOf(time.getText());
	}

	public double getUserFileSize() {
		return Double.valueOf(size.getText());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Ok")) {
			frame.dispose();
			window.reportDone();
		}
		else if (e.getActionCommand().equals("Cancel"))
			System.exit(0);
		else if (e.getActionCommand().equals("Blacklist")) {
			File f = new File("blacklist.txt");
			try {
				Runtime.getRuntime().exec("notepad " + f.getAbsolutePath());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}

}
