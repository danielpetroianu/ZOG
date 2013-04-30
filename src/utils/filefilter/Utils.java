package utils.filefilter;

import java.io.File;

public class Utils {
	public final static String jpeg = "jpeg";
	public final static String jpg = "jpg";
	public final static String jpe = "jpe";
	public final static String jfif = "jfif";
	public final static String bmp = "bmp";

	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}
}