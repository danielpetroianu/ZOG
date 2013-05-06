package org.fmi.zog.presentation;

import info.clearthought.layout.TableLayout;

import org.fmi.zog.helpers.UtilData;

public final class StatusBar extends javax.swing.JPanel implements UtilData {
	final javax.swing.JLabel xy;
	final javax.swing.JLabel rgb;
	final javax.swing.JLabel size;
	final javax.swing.JProgressBar progresBar;

	public StatusBar() {
		xy = new javax.swing.JLabel("XY : 0 x 0");
		rgb = new javax.swing.JLabel("RGB :255,255,255");
		size = new javax.swing.JLabel("SIZE :");
		progresBar = new javax.swing.JProgressBar(0, 100);
		// setBorder(new LineBorder(Color.red, 1));
		double[][] table = {
				{ 100, 100, 100, 100, 100, TableLayout.FILL, 100, 4 },
				{ STATUS_BAR_HEIGHT } };
		setLayout(new TableLayout(table));
		add(xy, "0,0");
		add(rgb, "2,0");
		add(size, "4,0");
		add(progresBar, "6,0");
	}
}
