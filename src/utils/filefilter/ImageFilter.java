package utils.filefilter;

import java.io.File;

public class ImageFilter extends javax.swing.filechooser.FileFilter {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String extension = Utils.getExtension(f);
		if (extension != null) {
			if (extension.equals(Utils.jpeg) || extension.equals(Utils.jpg)
					|| extension.equals(Utils.jpe)
					|| extension.equals(Utils.jfif)
					|| extension.equals(Utils.bmp)) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	@Override
	public String getDescription() {
		return "BMP and JPEG(*.bmp; *.jpg; *.jpeg; *.jpe; *.jfif)";
	}
}
