package org.fmi.zog.helpers;

import java.awt.Dimension;

public interface UtilData {

	/*
	 * *********************** BUTTON PANEL CONSTANTS
	 */
	public static final int BUTTON_COUNT = 10;
	public static final int PENCIL = 0;
	public static final int BRUSH = 1;
	public static final int BUCKET = 2;
	public static final int RECTANGLE = 3;
	public static final int ELIPSE = 4;
	public static final int LINE = 5;
	public static final int ERASER = 6;
	public static final int COLOR_PICKER = 7;
	public static final int MAGNIFIER = 8;
	public static final int SELECT = 9;
	
	/*
	 * *********************** BUTTON PROPERTIES
	 */
	public static final int PROPERTIES_OB_NR = 5;
	public static final String BRUSH_PANEL = "brush";
	public static final String TEXT_PANEL = "text";
	public static final String ELIPSE_PANEL = "elipse";
	public static final String RECTANGLE_PANEL = "rectangle";
	public static final String LINE_PANEL = "line";
	public static final String PENCIL_PANEL = "pencil";
	public static final String ERASER_PANEL = "eraser";
	public static final String ZOOM_PANEL = "zoom";
	public static final String EMPTY_PANEL = "empty";
	public static final int LINE_STYLE1 = 0;
	public static final int LINE_STYLE2 = 1;
	public static final int LINE_STYLE3 = 2;
	public static final int BRUSH_STYLE1 = 0;
	public static final int BRUSH_STYLE2 = 1;
	public static final int BRUSH_STYLE3 = 2;
	public static final int TRANSPARENT = 0;
	public static final int OPAQUE = 1;
	
	/*
	 * *********************** FRAME CONSTANTS
	 */
	public static final int TAB_LIMIT = 22;
	public static final int COLOR_PANEL_WIDTH = 215;
	public static final int COLOR_PANEL_HEIGHT = 75;
	public static final int PROPERTIES_PANEL_WIDTH = 130;
	public static final int PROPERTIES_PANEL_HEIGHT = COLOR_PANEL_HEIGHT;
	public static final int DRAW_AREA_WIDTH = 500;
	public static final int DRAW_AREA_HEIGHT = 500;
	public static final int FILE_WIDTH = DRAW_AREA_WIDTH + 200;
	public static final int FILE_HEIGHT = DRAW_AREA_HEIGHT + 200;
	public static final int BUTTON_WIDTH = 30;
	public static final int BUTTON_HEIGHT = 30;
	public static final int BUTTON_PANEL_WIDTH = BUTTON_WIDTH;
	public static final int BUTTON_PANEL_HEIGHT = 11 * BUTTON_HEIGHT;
	public static final int MENU_HEIGHT = 22;
	public static final double STATUS_BAR_HEIGHT = 15;
	public static final int FRAME_WIDTH = FILE_WIDTH + COLOR_PANEL_WIDTH
			+ PROPERTIES_PANEL_WIDTH;
	public static final int FRAME_HEIGHT = MENU_HEIGHT + FILE_HEIGHT
			+ (int) STATUS_BAR_HEIGHT;
	/*
	 * *********************** MENU CONSTANTS
	 */
	public static final int FILE_ITEM = 6;
	public static final int EDIT_ITEM = 5;
	public static final int NEW_FILE = 0;
	public static final int OPEN = 1;
	public static final int SAVE = 2;
	public static final int SAVE_AS = 3;
	public static final int ABOUT = 4;
	public static final int EXIT = 5;
	public static final int UNDO = 0;
	public static final int REDO = 1;
	public static final int CUT = 2;
	public static final int COPY = 3;
	public static final int PASTE = 4;
	public static final Dimension COLOR_PANEL_SIZE = new java.awt.Dimension(
			COLOR_PANEL_WIDTH, COLOR_PANEL_HEIGHT);
	public static final Dimension DRAW_AREA_SIZE = new java.awt.Dimension(
			DRAW_AREA_WIDTH, DRAW_AREA_HEIGHT);
	public static final Dimension BUTTON_SIZE = new java.awt.Dimension(
			BUTTON_WIDTH, BUTTON_HEIGHT);
	public static final Dimension FRAME_SIZE = new java.awt.Dimension(
			FRAME_WIDTH, FRAME_HEIGHT);
	public static final Dimension FRAME_MINIMUM_SIZE = new java.awt.Dimension(
			FRAME_WIDTH - 200, FRAME_HEIGHT - 100);
	
	
	
	/*
	 * *********************** Image extension
	 */
	public final static String JPEG = "jpeg";
	public final static String JPG = "jpg";
	public final static String JPE = "jpe";
	public final static String JFIF = "jfif";
	public final static String BMP = "bmp";
}
