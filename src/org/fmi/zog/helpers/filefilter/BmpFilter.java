package org.fmi.zog.helpers.filefilter;

import java.io.File;

import org.fmi.zog.helpers.Utils;

public class BmpFilter extends javax.swing.filechooser.FileFilter {

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		
		return Utils.isBMP(f);
	}

	@Override
	public String getDescription() {
		return "BMP(*.bmp)";
	}
}
