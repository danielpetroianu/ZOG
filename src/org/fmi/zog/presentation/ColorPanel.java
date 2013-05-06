package org.fmi.zog.presentation;

import com.bric.swing.ColorPicker;
import info.clearthought.layout.TableLayout;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import javax.swing.border.Border;

import org.fmi.zog.helpers.UtilData;


public final class ColorPanel extends javax.swing.JPanel implements UtilData {

	private static final Dimension COLOR_SIZE = new Dimension(45, 50);
	private static final Dimension LS_SIZE = new Dimension(15, 15);
	private static final Dimension SLIDER_SIZE = new Dimension(45, 15);
	private final Color[] Colors = { 
			Color.BLACK, Color.WHITE, Color.RED.darker(), Color.ORANGE, Color.YELLOW, Color.DARK_GRAY,
			randomColor(), Color.RED, Color.PINK, Color.GREEN, Color.GRAY, Color.CYAN, Color.BLUE,
			randomColor(), randomColor(), Color.LIGHT_GRAY, randomColor().brighter(),
			randomColor().brighter(), randomColor().brighter(), randomColor().brighter()
	};
	private ColorArea[] staticColor;
	private ColorArea[] lastColor;
	private ColorArea[] color;
	private JSlider[] opacitySlider;
	private float lastOpacity = 1;
	private int adjustingOpacity;
	private int lastColorIdx = 0;
	private final MouseListener lc;
	private final ChangeListener osl;
	private final MouseListener cl;
	private final MouseListener scl;

	public ColorPanel() {
		color = new ColorArea[2];
		opacitySlider = new JSlider[2];
		lastColor = new ColorArea[4];
		staticColor = new ColorArea[20];
		// setBorder(new LineBorder(Color.red, 1));
		cl = new java.awt.event.MouseAdapter() {

			int idx;

			public void mousePressed(java.awt.event.MouseEvent evt) {
				if (evt.getSource() == color[0]) {
					idx = 0;
				} else {
					idx = 1;
				}
				Color tmpColor = ColorPicker.showDialog(null,
						color[idx].getForeground(), true);
				if (tmpColor != null) {
					color[idx].setForeground(tmpColor);
					opacitySlider[idx].setValue(color[idx].getForeground()
							.getAlpha());

					if (lastColorIdx > 3) {
						lastColorIdx = 0;
					}
					lastColor[lastColorIdx++].setForeground(tmpColor);
				}
			}
		};
		osl = new javax.swing.event.ChangeListener() {

			int idx;

			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				if (evt.getSource() == opacitySlider[0]) {
					idx = 0;
				} else {
					idx = 1;
				}
				if (adjustingOpacity > 0) {
					return;
				}
				int v = opacitySlider[idx].getValue();
				setOpacity(((float) v) / 255f, idx);
			}
		};
		lc = new java.awt.event.MouseAdapter() {

			int idx;

			public void mousePressed(java.awt.event.MouseEvent evt) {
				for (idx = 0; idx < lastColor.length; idx++) {
					if (evt.getSource() == lastColor[idx]) {
						break;
					}
				}
				System.out.println(idx);
				if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
					color[0].setForeground(lastColor[idx].getForeground());
					opacitySlider[0].setValue(color[0].getForeground()
							.getAlpha());
					System.out.println("test1");
				} else if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
					color[1].setForeground(lastColor[idx].getForeground());
					opacitySlider[1].setValue(color[1].getForeground()
							.getAlpha());
					System.out.println("test2");
				}
			}
		};
		scl = new java.awt.event.MouseAdapter() {

			int idx;

			public void mousePressed(java.awt.event.MouseEvent evt) {
				for (idx = 0; idx < staticColor.length; idx++) {
					if (evt.getSource() == staticColor[idx]) {
						break;
					}
				}

				if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
					color[0].setForeground(staticColor[idx].getForeground());
					opacitySlider[0].setValue(color[0].getForeground()
							.getAlpha());
				} else if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
					color[1].setForeground(staticColor[idx].getForeground());
					opacitySlider[1].setValue(color[1].getForeground()
							.getAlpha());
				}
			}
		};

		color[0] = new ColorArea(COLOR_SIZE, Color.BLACK, new CompoundBorder(
				new LineBorder(Color.BLACK), new LineBorder(Color.WHITE)));
		color[0].addMouseListener(cl);

		color[1] = new ColorArea(COLOR_SIZE, Color.WHITE, new CompoundBorder(
				new LineBorder(Color.BLACK), new LineBorder(Color.WHITE)));
		color[1].addMouseListener(cl);

		opacitySlider[0] = new JSlider(SwingConstants.HORIZONTAL, 0, 255, 255);
		opacitySlider[0].setPreferredSize(SLIDER_SIZE);
		opacitySlider[0].setFocusable(false);
		opacitySlider[0].addChangeListener(osl);

		opacitySlider[1] = new JSlider(SwingConstants.HORIZONTAL, 0, 255, 255);
		opacitySlider[1].setPreferredSize(SLIDER_SIZE);
		opacitySlider[1].setFocusable(false);
		opacitySlider[1].addChangeListener(osl);

		lastColor[0] = new ColorArea(LS_SIZE, new Color(0, 0, 0, 0),
				new LineBorder(Color.BLACK, 1, true));
		lastColor[0].addMouseListener(lc);

		lastColor[1] = new ColorArea(LS_SIZE, new Color(0, 0, 0, 0),
				new LineBorder(Color.BLACK, 1, true));
		lastColor[1].addMouseListener(lc);

		lastColor[2] = new ColorArea(LS_SIZE, new Color(0, 0, 0, 0),
				new LineBorder(Color.BLACK, 1, true));
		lastColor[2].addMouseListener(lc);

		lastColor[3] = new ColorArea(LS_SIZE, new Color(0, 0, 0, 0),
				new LineBorder(Color.BLACK, 1, true));
		lastColor[3].addMouseListener(lc);

		double[][] tabel = {
				{ 50, 4, 50, 4, 15, 8, 15, 2, 15, 2, 15, 2, 15, 2, 15 },
				{ 15, 2, 15, 2, 15, 2, 15, 5 } };
		TableLayout layout = new TableLayout(tabel);
		setLayout(layout);

		add(color[0], "0,0,0,5");
		add(color[1], "2,0,2,5");
		add(opacitySlider[0], "0,6,0,7");
		add(opacitySlider[1], "2,6,2,7");
		add(lastColor[0], "4,0");
		add(lastColor[1], "4,2");
		add(lastColor[2], "4,4");
		add(lastColor[3], "4,6");

		int c = 6, r = 0;
		for (int i = 0; i < staticColor.length; i++) {
			staticColor[i] = new ColorArea(LS_SIZE, Colors[i], new LineBorder(
					Color.BLACK, 1, true));
			add(staticColor[i], "" + c + "," + r + "");
			staticColor[i].addMouseListener(scl);
			if (c < 14) {
				c += 2;
			} else {
				c = 6;
				r += 2;
			}

		}
	}

	public void setOpacity(final float v, final int idx) {
		if (v < 0 || v > 1) {
			throw new IllegalArgumentException("The opacity (" + v
					+ ") must be between 0 and 1.");
		}
		adjustingOpacity++;
		try {
			int i = (int) (255 * v);
			if (lastOpacity != v) {
				Color c = color[idx].getForeground();
				color[idx].setForeground(new Color(c.getRed(), c.getGreen(), c
						.getBlue(), i));
			}
			lastOpacity = v;
		} finally {
			adjustingOpacity--;
		}
	}

	private Color randomColor() {
		return new Color((int) (Math.random() * 255),
				(int) (Math.random() * 255), (int) (Math.random() * 255));
	}

	public Color getColor1() {
		return color[0].getForeground();
	}

	public Color getColor2() {
		return color[1].getForeground();
	}

	public void setColor1(Color c) {
		color[0].setForeground(c);
	}

	public void setColor2(Color c) {
		color[1].setForeground(c);
	}

	private final class ColorArea extends javax.swing.JPanel {

		private TexturePaint checkerPaint = null;
		private Border border;

		private ColorArea(final java.awt.Dimension size,
				final java.awt.Color c, Border b) {
			border = b;
			setPreferredSize(size);
			setMaximumSize(size);
			setMinimumSize(size);
			setForeground(c);
			// setBorder(border);
		}

		private TexturePaint getCheckerPaint() {
			if (checkerPaint == null) {
				int t = 8;
				BufferedImage bi = new BufferedImage(t * 2, t * 2,
						BufferedImage.TYPE_INT_RGB);
				Graphics g = bi.createGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, 2 * t, 2 * t);
				g.setColor(Color.lightGray);
				g.fillRect(0, 0, t, t);
				g.fillRect(t, t, t, t);
				checkerPaint = new TexturePaint(bi, new Rectangle(0, 0,
						bi.getWidth(), bi.getHeight()));
			}
			return checkerPaint;
		}

		@Override
		public void paintComponent(Graphics g0) {
			super.paintComponent(g0);

			Graphics2D g = (Graphics2D) g0;

			Color c = getForeground();
			Rectangle r = new Rectangle(0, 0, getWidth(), getHeight());

			if (c.getAlpha() < 255) {
				TexturePaint checkers = getCheckerPaint();
				g.setPaint(checkers);
				g.fillRect(r.x, r.y, r.width, r.height);
			}
			g.setColor(c);
			g.fillRect(r.x, r.y, r.width, r.height);
			setBorder(border);
		}
	}
}
