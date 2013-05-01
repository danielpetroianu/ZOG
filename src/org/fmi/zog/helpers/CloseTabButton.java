package org.fmi.zog.helpers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

import org.fmi.zog.presentation.DrawPanel;
import org.fmi.zog.presentation.TabbedPanel;

public final class CloseTabButton extends JPanel {

	private JTabbedPane tab;

	public CloseTabButton(final JTabbedPane tab) {
		super(new FlowLayout(0, 0, 0));
		if (tab == null) {
			throw new NullPointerException("TabbedPanel is null");
		} else {
			this.tab = tab;
			setOpaque(false);

			JLabel label = new JLabel() {

				@Override
				public synchronized String getText() {
					int i = tab.indexOfTabComponent(CloseTabButton.this);
					if (i != -1) {
						String name = tab.getTitleAt(i);
						if (name.length() > 9) {
							name = name.substring(0, 9) + "...";
						}
						return name;
					} else {
						return null;
					}
				}
			};
			label.addMouseListener(new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					int i = tab.indexOfTabComponent(CloseTabButton.this);
					if (i != -1) {
						tab.setSelectedIndex(i);
					}
				}
			});

			String name = tab.getTitleAt(tab.getSelectedIndex());
			label.setToolTipText(name);
			label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

			add(label);
			JButton button = new TabButton();
			add(button);
			setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
			return;
		}

	}

	private final class TabButton extends JButton implements ActionListener {

		int buttonSize = 17;

		public TabButton() {
			setPreferredSize(new Dimension(buttonSize, buttonSize));
			setToolTipText("Close");
			// Make the button looks the same for all Laf's
			setUI(new BasicButtonUI());
			// Make it transparent
			setContentAreaFilled(false);
			// No need to be focusable
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);
			// Making nice rollover effect
			// we use the same listener for all buttons
			// addMouseListener(buttonMouseListener);
			setRolloverEnabled(true);
			// Close the proper tab by clicking the button
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			int i = tab.indexOfTabComponent(CloseTabButton.this);
			if (i != -1 && testStatus(i)) {
				tab.remove(i);
				TabbedPanel.data.remove(i);
			}
		}

		@Override
		public void updateUI() {
		}

		@Override
		protected void paintComponent(Graphics g0) {
			super.paintComponent(g0);

			Graphics2D g = (Graphics2D) g0;

			int size = 6;
			if (getModel().isPressed()) {
				size = 7;
			}

			g.setStroke(new BasicStroke(2));
			if (getModel().isRollover()) {
				g.setColor(Color.RED);
			}
			g.drawLine(size, size, getWidth() - size, getHeight() - size);
			g.drawLine(getWidth() - size, size, size, getHeight() - size);
			g.dispose();
		}

		public boolean testStatus(int i) {
			boolean b = ((DrawPanel) ((JScrollPane) tab.getComponentAt(i))
					.getViewport().getView()).getStatus();
			if (b) {
				int ret = JOptionPane.showConfirmDialog(null,
						"Do you want to save ?", "Save",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				switch (ret) {
				case JOptionPane.YES_OPTION:
					org.fmi.zog.presentation.Zog.paint.save(i);
					return true;
				case JOptionPane.NO_OPTION:
					return true;
				case JOptionPane.CANCEL_OPTION:
					return false;
				}
			}
			return true;
		}
	}
}
