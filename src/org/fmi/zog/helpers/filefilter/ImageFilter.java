package org.fmi.zog.helpers.filefilter;

import java.io.File;

import org.fmi.zog.helpers.UtilData;
import org.fmi.zog.helpers.Utils;

public class ImageFilter extends javax.swing.filechooser.FileFilter {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String extension = Utils.getFileExtension(f);
		
		return extension != null
			&& extension.equals(UtilData.JPEG) 
			|| extension.equals(UtilData.JPG)
			|| extension.equals(UtilData.JPE)
			|| extension.equals(UtilData.JFIF)
			|| extension.equals(UtilData.BMP);
	}

	@Override
	public String getDescription() {
		return "BMP and JPEG(*.bmp; *.jpg; *.jpeg; *.jpe; *.jfif)";
	}
}
