package zog;

import utils.Fill;
import utils.CompositeStroke;
import utils.Transparency;
import utils.UtilData;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import javax.swing.JPanel;

public final class DrawPanel extends JPanel implements UtilData {

	final DrawArea paintPanel;
	private boolean unsave = false;

	public DrawPanel(final java.awt.Dimension size, final StatusBar sb,
			final BufferedImage image) throws OutOfMemoryError {
		super(new FlowLayout(0, 2, 2));
		paintPanel = new DrawArea(size, sb, image);
		add(paintPanel);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	public BufferedImage getImage() {
		return paintPanel.image;
	}

	public boolean getStatus() {
		return unsave;
	}

	public void setStatus(final boolean status) {
		unsave = status;
	}

	private final class DrawArea extends JPanel implements MouseListener,
			MouseMotionListener {

		BufferedImage image;
		private StatusBar sb;
		private Point S, D, T;
		private int dx, dy, imx, imy, mouseButton;
		private double scale;
		private boolean isSelected, isShiftDown;
		private BufferedImage selectedIm;
		private Image imageTrans;

		// private boolean drag;

		private DrawArea(final Dimension size, final StatusBar sb,
				final BufferedImage imag) throws OutOfMemoryError {
			super(true);
			setPreferredSize(size);
			addMouseListener(this);
			addMouseMotionListener(this);
			this.sb = sb;
			this.image = imag;
			this.scale = 1.0;
			this.isSelected = false;
			this.isShiftDown = false;
			this.selectedIm = null;
			this.imx = 0;
			this.imy = 0;

			sb.size.setText("SIZE : " + size.width + "x" + size.height);
			cursor();

			S = new Point(0, 0);
			D = new Point(0, 0);
			T = new Point(0, 0);
		}

		@Override
		protected void paintComponent(Graphics g0) {
			super.paintComponent(g0);
			Graphics2D g = (Graphics2D) g0;
			g.scale(scale, scale);
			g.drawImage(image, 0, 0, this);

			if (D.x < image.getWidth() && D.y < image.getHeight()
					&& S.x < image.getWidth() && S.y < image.getHeight()) {
				switch (TabbedPanel.TOOL) {
				case SELECT:
					if (!isSelected && (S.x != D.x || S.y != D.y)) {
						g.setStroke(new BasicStroke(1.f, BasicStroke.CAP_BUTT,
								BasicStroke.JOIN_BEVEL, 10.f, new float[] {
										5.f, 5.f }, 0.f));
						g.setColor(Color.black);
						g.drawRect(T.x - 1, T.y - 1, Math.abs(S.x - D.x) + 1,
								Math.abs(D.y - S.y) + 1);
					} else {
						if (selectedIm != null) {
							int ax = D.x - imx, ay = D.y - imy;
							if (ax + selectedIm.getWidth() > image.getWidth()) {
								ax = image.getWidth() - selectedIm.getWidth();
							}
							if (ax < 0) {
								ax = 0;
							}
							if (ay + selectedIm.getHeight() > image.getHeight()) {
								ay = image.getHeight() - selectedIm.getHeight();
							}
							if (ay < 0) {
								ay = 0;
							}
							D.x = ax;
							D.y = ay;
							if (isShiftDown) {
								g.drawImage(imageTrans, ax, ay, this);
							} else {
								g.drawImage(selectedIm, ax, ay, this);
							}
							g.setStroke(new BasicStroke(1.f,
									BasicStroke.CAP_BUTT,
									BasicStroke.JOIN_BEVEL, 10.f, new float[] {
											5.f, 5.f }, 0.f));
							g.drawRect(ax, ay, selectedIm.getWidth(),
									selectedIm.getHeight());
						}
					}
					break;

				case LINE:
					drawStyleLine(g, true);
					break;

				case ELIPSE:
					drawElipse(g, T.x, T.y);
					break;

				case RECTANGLE:
					drawRectangle(g, T.x, T.y);
					break;
				}
			}
		}

		public void mousePressed(MouseEvent e) {
			Graphics2D g = image.createGraphics();
			int sx = S.x, sy = S.y;
			boolean drawTransp = true;
			S.x = e.getX() / (int) scale;
			S.y = e.getY() / (int) scale;
			mouseButton = e.getButton();
			if (e.isShiftDown()) {
				drawTransp = true;
			} else {
				drawTransp = false;
			}
			// drag = true;
			if ((TabbedPanel.TOOL != SELECT) && (isSelected)) {
				isSelected = false;
				if (selectedIm != null) {
					int ax = D.x - imx, ay = D.y - imy;
					if (ax + selectedIm.getWidth() > image.getWidth()) {
						ax = image.getWidth() - selectedIm.getWidth();
					}
					if (ax < 0) {
						ax = 0;
					}
					if (ay + selectedIm.getHeight() > image.getHeight()) {
						ay = image.getHeight() - selectedIm.getHeight();
					}
					if (ay < 0) {
						ay = 0;
					}
					drawImage(ax, ay);
					selectedIm = null;
					dx = 0;
					dy = 0;
				}
			}
			int thicknes;
			switch (TabbedPanel.TOOL) {
			case SELECT:// <editor-fold defaultstate="collapsed" desc="comment">
				T.y = Math.min(D.y, sy);
				T.x = Math.min(D.x, sx);
				if (selectedIm != null) {
					if (drawTransp) {
						imageTrans = Transparency.makeColorTransparent(
								selectedIm, Zog.colors.getColor2());
					}
					if ((S.y < dy || S.y > (dy + selectedIm.getHeight()))
							|| (S.x < dx || S.x > (dx + selectedIm.getWidth()))) {
						isSelected = false;
						int ax = D.x - imx, ay = D.y - imy;
						if (ax + selectedIm.getWidth() > image.getWidth()) {
							ax = image.getWidth() - selectedIm.getWidth();
						}
						if (ax < 0) {
							ax = 0;
						}
						if (ay + selectedIm.getHeight() > image.getHeight()) {
							ay = image.getHeight() - selectedIm.getHeight();
						}
						if (ay < 0) {
							ay = 0;
						}
						drawImage(ax, ay);
						selectedIm = null;
						dx = 0;
						dy = 0;
					} else {
						if (e.isControlDown()) {

							g.setColor(Zog.colors.getColor2());
							g.fillRect(dx, dy, selectedIm.getWidth(),
									selectedIm.getHeight());
						}
						imx = Math.abs(dx - S.x);
						imy = Math.abs(dy - S.y);
					}

				}// </editor-fold>

				unsave = true;

				break;

			case PENCIL:
				thicknes = TabbedPanel.TOOL_Thicknes;
				// setBackground(g);
				setColor(g, false);
				g.fillRect(S.x - thicknes / 2, S.y - thicknes / 2, thicknes,
						thicknes);
				repaint();
				unsave = true;
				break;

			case BRUSH:
				thicknes = TabbedPanel.TOOL_Thicknes;
				setColor(g, false);

				if (TabbedPanel.TOOL_PropType == BRUSH_STYLE1) {
					g.fillOval(S.x - thicknes / 2, S.y - thicknes / 2,
							thicknes, thicknes);
				} else if (TabbedPanel.TOOL_PropType == BRUSH_STYLE2) {
					g.fillOval(S.x - thicknes / 2, S.y - thicknes / 2,
							thicknes, thicknes / 2);
				} else if (TabbedPanel.TOOL_PropType == BRUSH_STYLE3) {
					g.fillOval(S.x - thicknes / 2, S.y - thicknes / 2,
							thicknes / 2, thicknes);
				}
				repaint();
				unsave = true;
				break;

			case ERASER:
				thicknes = TabbedPanel.TOOL_Thicknes;
				setColor(g, true);
				g.clearRect(S.x - thicknes / 2, S.y - thicknes / 2, thicknes,
						thicknes);
				repaint();
				unsave = true;
				break;

			case COLOR_PICKER:
				if (e.getButton() == MouseEvent.BUTTON1) {
					Zog.colors.setColor1(new Color(image.getRGB(S.x, S.y)));
				} else {
					Zog.colors.setColor2(new Color(image.getRGB(S.x, S.y)));
				}
				repaint();
				break;

			case BUCKET:
				Fill f = null;
				if (e.getButton() == MouseEvent.BUTTON1) {
					f = new Fill(image, S.x, S.y, Zog.colors.getColor1()
							.getRGB());
				} else {
					f = new Fill(image, S.x, S.y, Zog.colors.getColor2()
							.getRGB());
				}
				image = f.getBufferedImage();
				repaint();
				unsave = true;
				break;

			case MAGNIFIER:
				scale = TabbedPanel.TOOL_Thicknes;
				revalidate();
				repaint();
				break;
			}
			isShiftDown = drawTransp;
		}

		public void mouseDragged(MouseEvent e) {
			Graphics2D g = image.createGraphics();
			D.x = e.getX() / (int) scale;
			D.y = e.getY() / (int) scale;
			T.y = Math.min(D.y, S.y);
			T.x = Math.min(D.x, S.x);
			// drag = true;
			setColor(g, false);
			switch (TabbedPanel.TOOL) {
			case SELECT:
				this.repaint();
				break;
			case PENCIL:
				g.setStroke(new BasicStroke(TabbedPanel.TOOL_Thicknes));
				g.drawLine(D.x, D.y, S.x, S.y);
				S.x = D.x;
				S.y = D.y;
				break;
			case ERASER:
				g.setStroke(new BasicStroke(TabbedPanel.TOOL_Thicknes));
				setColor(g, true);
				g.drawLine(D.x, D.y, S.x, S.y);
				S.x = D.x;
				S.y = D.y;
				break;
			case BRUSH:
				int thicknes = TabbedPanel.TOOL_Thicknes;
				if (TabbedPanel.TOOL_PropType == BRUSH_STYLE1) {
					g.fillOval(D.x - thicknes / 2, D.y - thicknes / 2,
							thicknes, thicknes);
				} else if (TabbedPanel.TOOL_PropType == BRUSH_STYLE2) {
					g.fillOval(D.x - thicknes / 2, D.y - thicknes / 2,
							thicknes, thicknes / 2);
				} else if (TabbedPanel.TOOL_PropType == BRUSH_STYLE3) {
					g.fillOval(D.x - thicknes / 2, D.y - thicknes / 2,
							thicknes / 2, thicknes);
				}
				break;
			}
			repaint();
			setXY(D.x, D.y);
		}

		public void mouseMoved(MouseEvent e) {
			int X = e.getX() / (int) scale;
			int Y = e.getY() / (int) scale;
			setXY(X, Y);

		}

		public void mouseReleased(MouseEvent e) {
			D.x = e.getX() / (int) scale;
			D.y = e.getY() / (int) scale;
			// drag = false;

			Graphics2D g = image.createGraphics();
			switch (TabbedPanel.TOOL) {
			case SELECT:
				if (!isSelected) {
					T.y = Math.min(D.y, S.y);
					T.x = Math.min(D.x, S.x);
					if (S.x != D.x && S.y != D.y && D.x >= 0 && D.y >= 0
							&& D.x < image.getWidth()
							&& D.y < image.getHeight()
							&& S.x < image.getWidth()
							&& S.y < image.getHeight()) {
						BufferedImage src = image.getSubimage(T.x, T.y,
								Math.abs(S.x - D.x), Math.abs(D.y - S.y));
						selectedIm = newImage(src);
						isSelected = true;
						dx = T.x;
						dy = T.y;
						if (D.x > T.x) {
							imx = selectedIm.getWidth();
						} else {
							imx = 0;
						}
						if (D.y > T.y) {
							imy = selectedIm.getHeight();
						} else {
							imy = 0;
						}
					}
				}
				break;
			case LINE:
				drawStyleLine(g, false);
				repaint();
				unsave = true;
				break;

			case ELIPSE:
				drawElipse(g, T.x, T.y);
				repaint();
				unsave = true;
				break;

			case RECTANGLE:
				drawRectangle(g, T.x, T.y);
				repaint();
				unsave = true;
				break;
			}

		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		private void drawStyleLine(Graphics2D g, boolean fill) {
			Stroke lineStroke = null;
			switch (TabbedPanel.TOOL_PropType) {
			case LINE_STYLE1:
				lineStroke = new BasicStroke(TabbedPanel.TOOL_Thicknes,
						BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

				break;
			case LINE_STYLE2:
				lineStroke = new BasicStroke(TabbedPanel.TOOL_Thicknes,
						BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f,
						new float[] { 20.0f, 20.0f }, 0.0f);
				break;
			case LINE_STYLE3:
				lineStroke = new BasicStroke(TabbedPanel.TOOL_Thicknes,
						BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f,
						new float[] { 20.0f, 10.0f, 5.0f, 10.0f }, 0.0f);
				break;
			}
			setColor(g, false);
			if (fill == true) {
				g.setStroke(new CompositeStroke(lineStroke, new BasicStroke(1f)));
			} else {
				g.setStroke(lineStroke);
			}
			g.drawLine(S.x, S.y, D.x, D.y);
			revalidate();

		}

		private void drawElipse(Graphics2D g, int StartX, int StartY) {

			g.setStroke(new BasicStroke(TabbedPanel.TOOL_Thicknes));

			if (TabbedPanel.TOOL_PropType == TRANSPARENT) {
				setColor(g, false);
				g.draw(new Ellipse2D.Double(StartX, StartY,
						Math.abs(S.x - D.x), Math.abs(D.y - S.y)));
			} else if (TabbedPanel.TOOL_PropType == OPAQUE) {
				setColor(g, false);
				g.draw(new Ellipse2D.Double(StartX, StartY,
						Math.abs(S.x - D.x), Math.abs(D.y - S.y)));
				setColor(g, true);
				g.fill(new Ellipse2D.Double(StartX, StartY,
						Math.abs(S.x - D.x), Math.abs(D.y - S.y)));
			}
		}

		private void drawRectangle(Graphics2D g, int StartX, int StartY) {
			g.setStroke(new BasicStroke(TabbedPanel.TOOL_Thicknes));

			if (TabbedPanel.TOOL_PropType == TRANSPARENT) {
				setColor(g, false);
				g.draw(new Rectangle2D.Double(StartX, StartY, Math.abs(S.x
						- D.x), Math.abs(D.y - S.y)));
			} else if (TabbedPanel.TOOL_PropType == OPAQUE) {
				setColor(g, false);
				g.draw(new Rectangle2D.Double(StartX, StartY, Math.abs(S.x
						- D.x), Math.abs(D.y - S.y)));
				setColor(g, true);
				g.fill(new Rectangle2D.Double(StartX, StartY, Math.abs(S.x
						- D.x), Math.abs(D.y - S.y)));
			}
		}

		private void cursor() {
			int curWidth = 32;
			int curHeight = 32;

			int pix[] = new int[curWidth * curHeight];

			int curCol = Color.BLACK.getRGB();
			pix[7] = curCol;
			pix[7 + 2 * curWidth] = curCol;
			pix[7 + 6 * curWidth] = curCol;
			pix[7 + 8 * curWidth] = curCol;
			pix[7 + 12 * curWidth] = curCol;
			pix[7 + 14 * curWidth] = curCol;
			pix[7 * curWidth] = curCol;
			pix[7 * curWidth + 2] = curCol;
			pix[7 * curWidth + 6] = curCol;
			pix[7 * curWidth + 8] = curCol;
			pix[7 * curWidth + 12] = curCol;
			pix[7 * curWidth + 14] = curCol;

			curCol = Color.WHITE.getRGB();
			pix[7 + curWidth] = curCol;
			pix[7 + 3 * curWidth] = curCol;
			pix[7 + 11 * curWidth] = curCol;
			pix[7 + 13 * curWidth] = curCol;
			pix[7 * curWidth + 1] = curCol;
			pix[7 * curWidth + 3] = curCol;
			pix[7 * curWidth + 11] = curCol;
			pix[7 * curWidth + 13] = curCol;
			pix[6 * curWidth + 6] = curCol;
			pix[6 * curWidth + 8] = curCol;
			pix[8 * curWidth + 6] = curCol;
			pix[8 * curWidth + 8] = curCol;

			Image img = createImage(new MemoryImageSource(curWidth, curHeight,
					pix, 0, curWidth));
			Cursor curCircle = Toolkit.getDefaultToolkit().createCustomCursor(
					img, new Point(7, 7), "plus");
			setCursor(curCircle);

		}

		private void setColor(Graphics2D g, boolean useColor2) {
			if (useColor2) {
				if (mouseButton == MouseEvent.BUTTON1) {
					g.setColor(Zog.colors.getColor2());
				} else if (mouseButton == MouseEvent.BUTTON3) {
					g.setColor(Zog.colors.getColor1());
				}
			} else {
				if (mouseButton == MouseEvent.BUTTON1) {
					g.setColor(Zog.colors.getColor1());
				} else if (mouseButton == MouseEvent.BUTTON3) {
					g.setColor(Zog.colors.getColor2());
				}
			}
		}

		private BufferedImage newImage(BufferedImage src) {
			if (src == null) {
				return null;
			}
			int w = src.getWidth(null);
			int h = src.getHeight(null);
			int type = BufferedImage.TYPE_INT_ARGB; // other options
			BufferedImage dest = new BufferedImage(w, h, type);
			duplicate(src, dest);
			return dest;
		}

		private void duplicate(BufferedImage src, BufferedImage dest) {
			for (int i = 0; i < src.getWidth(); i++) {
				for (int j = 0; j < src.getHeight(); j++) {
					dest.setRGB(i, j, (src.getRGB(i, j)));
				}
			}
		}

		private void drawImage(int a, int b) {
			Graphics2D g = image.createGraphics();
			for (int i = 0; i < selectedIm.getWidth(); i++) {
				for (int j = 0; j < selectedIm.getHeight(); j++) {
					Color color = new Color(selectedIm.getRGB(i, j));
					if (!isShiftDown
							|| (color.getRGB() != Zog.colors.getColor2()
									.getRGB())) {
						g.setColor(color);
						g.drawLine(a + i, b + j, a + i, b + j);
					}
				}
			}
		}

		private void setXY(int X, int Y) {
			if ((X > 0 && Y > 0)
					&& (X < image.getWidth() && Y < image.getHeight())) {
				sb.xy.setText("XY : " + X + "," + Y);
				Color c = new Color(image.getRGB(X, Y));
				sb.rgb.setText("RGB: " + c.getRed() + "," + c.getGreen() + ","
						+ c.getBlue());
			}
		}

		@Override
		public Dimension getPreferredSize() {
			int w = (int) (scale * image.getWidth());
			int h = (int) (scale * image.getHeight());
			return new Dimension(w, h);
		}

		@Override
		public Dimension getMinimumSize() {
			return new Dimension(image.getWidth(), image.getHeight());
		}
		// // <editor-fold defaultstate="collapsed" desc="comment">
		// void cutIm() {
		// if (selectedIm != null) {
		// leg.setImage(selectedIm);
		// isSelected = false;
		// D.x = S.x;
		// D.y = S.y;
		// Graphics2D g = image.createGraphics();
		// g.setColor(Zog.colors.getColor2());
		// g.fillRect(dx, dy, selectedIm.getWidth(), selectedIm.getHeight());
		// selectedIm = null;
		// //unsaved = true;
		// this.repaint();
		// }
		// }
		// void pasteIm(BufferedImage im) {
		// if (selectedIm != null) {
		// int ax = D.x - imx, ay = D.y - imy;
		// if (ax + selectedIm.getWidth() > image.getWidth()) {
		// ax = image.getWidth() - selectedIm.getWidth();
		// }
		// if (ax < 0) {
		// ax = 0;
		// }
		// if (ay + selectedIm.getHeight() > image.getHeight()) {
		// ay = image.getHeight() - selectedIm.getHeight();
		// }
		// if (ay < 0) {
		// ay = 0;
		// }
		// drawImage(ax, ay);
		// dx = 0;
		// dy = 0;
		// // update_undo();
		// }
		// if (im == null) {
		// selectedIm = null;
		// isSelected = false;
		// // undo = true;
		// this.repaint();
		// return;
		// }
		// selectedIm = newImage(im);
		// isSelected = true;
		// // leg.setButton(1);
		// imx = selectedIm.getWidth();
		// imy = selectedIm.getHeight();
		// D.x = 1;
		// D.y = 1;
		// dx = D.x;
		// dy = D.y;
		// this.repaint();
		// }// </editor-fold>
	}
}
