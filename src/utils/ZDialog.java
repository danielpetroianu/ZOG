package utils;

import info.clearthought.layout.TableLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ZDialog extends JDialog implements UtilData, ActionListener,
		KeyListener {

	public static final boolean OK = true;
	public static final boolean CANCEL = false;
	private int height;
	private int width;
	private boolean choice = false;
	private JTextField widthField;
	private JTextField heightField;
	private JButton ok;
	private JButton cancel;

	public ZDialog(JFrame parent) {
		super(parent, "Image size", true);

		setLayout(new TableLayout(new double[][] { { 10, 15, 45, 5, 60, 10 },
				{ 10, 15, 5, 20, 10, 23, 10 } }));
		setSize(160, 130);
		JLabel wl = new JLabel("width :");
		JLabel hl = new JLabel("height  :");

		widthField = new JTextField("" + DRAW_AREA_WIDTH);
		widthField.addKeyListener(this);

		heightField = new JTextField("" + DRAW_AREA_HEIGHT);
		heightField.addKeyListener(this);

		ok = new JButton("OK");
		ok.addActionListener(this);
		ok.setActionCommand("ok");

		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		cancel.setActionCommand("cancel");

		add(wl, "1,1,2,1");
		add(widthField, "1,3,2,3");
		add(hl, "4,1");
		add(heightField, "4,3");
		add(ok, "2,5");
		add(cancel, "4,5");

		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}

	public Dimension getDimensions() {
		return new Dimension(width, height);
	}

	public boolean getChoice() {
		return choice;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok") && valid()) {
			choice = OK;
			width = Integer.parseInt(widthField.getText());
			height = Integer.parseInt(heightField.getText());
			setVisible(false);
		} else if (e.getActionCommand().equals("cancel")) {
			choice = CANCEL;
			setVisible(false);
		}
	}

	public void keyTyped(KeyEvent e) {
		// throw new UnsupportedOperationException("Not supported yet.");
	}

	public void keyPressed(KeyEvent e) {
		JTextField tmp = (JTextField) e.getSource();
		tmp.setBackground(Color.WHITE);

		if (Character.isLetter(e.getKeyChar())) {
			Toolkit.getDefaultToolkit().beep();
			tmp.setBackground(Color.red);
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER && valid()) {
			choice = OK;
			width = Integer.parseInt(widthField.getText());
			height = Integer.parseInt(heightField.getText());
			setVisible(false);
		}
	}

	public void keyReleased(KeyEvent e) {
		// throw new UnsupportedOperationException("Not supported yet.");
	}

	private boolean valid() {
		boolean b = true;
		int w, h;
		try {
			w = Integer.parseInt(widthField.getText());
			if (w == 0) {
				widthField.setBackground(Color.red);
				b = false;
			}
		} catch (NumberFormatException ex) {
			b = false;
			widthField.setBackground(Color.red);
		}
		try {
			h = Integer.parseInt(heightField.getText());
			if (h == 0) {
				heightField.setBackground(Color.red);
				b = false;
			}
		} catch (NumberFormatException ex) {
			b = false;
			heightField.setBackground(Color.red);
		}

		return b;
	}
}
