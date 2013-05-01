package org.fmi.zog.helpers;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ZSpinner extends JSpinner implements FocusListener, KeyListener,
		ChangeListener {

	private int maxim, minim;
	private JFormattedTextField editor;

	public ZSpinner(int s, int min, int max, int step) {
		super(new SpinnerNumberModel(s, min, max, step));
		this.minim = min;
		this.maxim = max;
		addChangeListener(this);

		editor = ((JSpinner.NumberEditor) getEditor()).getTextField();
		editor.setBackground(Color.WHITE);
		editor.addFocusListener(this);
		editor.addKeyListener(this);
	}

	public void stateChanged(ChangeEvent e) {
		org.fmi.zog.presentation.TabbedPanel.TOOL_Thicknes = (Integer) this.getValue();
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		valid();
	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		char c = e.getKeyChar();
		int t = Integer.parseInt(editor.getText());

		if (Character.isLetter(c)) {
			error();
			editor.setText("" + t);

		} else {
			if (e.getKeyCode() == KeyEvent.VK_UP && e.isControlDown()) {
				if (t < maxim - 4) {
					editor.setText("" + (t + 4));
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN && e.isControlDown()) {
				if (t > minim + 4) {
					editor.setText("" + (t - 4));
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				valid();
			}
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	private void valid() {
		try {
			int tmp = Integer.parseInt(editor.getText());

			if (tmp < minim || tmp > maxim) {
				if (tmp <= minim) {
					editor.setText("" + minim);
				} else if (tmp >= maxim) {
					editor.setText("" + maxim);
				}
				error();
			}
			org.fmi.zog.presentation.TabbedPanel.TOOL_Thicknes = (Integer) this.getValue();
		} catch (NumberFormatException ex) {
		}
	}

	private void error() {
		JOptionPane.showMessageDialog(null,
				"Invalid value inserted.\nMust be between [" + minim + " , "
						+ maxim + "]", "Insert error",
				JOptionPane.ERROR_MESSAGE);
	}
}
