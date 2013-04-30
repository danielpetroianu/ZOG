package utils;

import java.awt.Point;
import java.awt.image.*;

public class Fill {

	BufferedImage bim;
	static int old;
	static int fill;
	static int index;
	static int eastBound;
	static int southBound;
	Point[] pixels;
	Point cur;

	public Fill(BufferedImage in, int x1, int y1, int filler) {
		fill = filler;
		bim = in;
		index = 0;
		old = in.getRGB(x1, y1);

		eastBound = in.getWidth();
		southBound = in.getHeight();

		pixels = new Point[eastBound * southBound];
		pixels[index] = new Point(x1, y1);
		bim.setRGB(x1, y1, fill);
		bim.flush();
		in.flush();
		try {
			fillIt();
		} catch (Exception e) {
		}
	}

	public BufferedImage getBufferedImage() {
		bim.flush();
		return bim;
	}

	public void fillIt() {
		while (index != -1) {
			cur = pixels[index];
			--index;
			if (cur.y + 1 < bim.getHeight()
					&& bim.getRGB(cur.x, cur.y + 1) == old) {
				bim.setRGB(cur.x, cur.y + 1, fill);
				++index;
				pixels[index] = new Point(cur.x, cur.y + 1);
			}
			if (cur.y - 1 >= 0 && bim.getRGB(cur.x, cur.y - 1) == old) {
				bim.setRGB(cur.x, cur.y - 1, fill);
				++index;
				pixels[index] = new Point(cur.x, cur.y - 1);
			}
			if (cur.x + 1 < bim.getWidth()
					&& bim.getRGB(cur.x + 1, cur.y) == old) {
				bim.setRGB(cur.x + 1, cur.y, fill);
				++index;
				pixels[index] = new Point(cur.x + 1, cur.y);
			}
			if (cur.x - 1 >= 0 && bim.getRGB(cur.x - 1, cur.y) == old) {
				bim.setRGB(cur.x - 1, cur.y, fill);
				++index;
				pixels[index] = new Point(cur.x - 1, cur.y);
			}
		}
		bim.flush();
	}
}