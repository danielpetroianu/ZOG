package org.fmi.zog.helpers;

import java.io.File;

public class Utils {
	private static final String DOT_SEPARATOR = ".";
	
	public static String getFileExtension(File file) {
        if (file == null) {
            return null;
        }

        String name = file.getName();
        int extIndex = name.lastIndexOf(Utils.DOT_SEPARATOR);

        return extIndex == -1
        		? "" 
        		: name.substring(extIndex + 1).toLowerCase();
    }
	
	public static boolean isJPEG(File file)
	{
		return Utils.isJPEG(Utils.getFileExtension(file));
	}
	
	public static boolean isBMP(File file)
	{
		return Utils.isBMP(Utils.getFileExtension(file));
	}
	
	public static boolean isJPEG(String fileExt)
	{
		return fileExt != null
			&& fileExt.equals(UtilData.JPEG) 
			|| fileExt.equals(UtilData.JPG)
			|| fileExt.equals(UtilData.JPE)
			|| fileExt.equals(UtilData.JFIF);
	}
	
	public static boolean isBMP(String fileExt)
	{
		return fileExt != null
			&& fileExt.equals(UtilData.BMP);
	}
}