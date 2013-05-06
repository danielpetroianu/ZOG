package org.fmi.zog.presentation;

import info.clearthought.layout.TableLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.fmi.zog.helpers.CloseTabButton;
import org.fmi.zog.helpers.SavePopup;
import org.fmi.zog.helpers.UtilData;
import org.fmi.zog.helpers.Utils;
import org.fmi.zog.helpers.filefilter.BmpFilter;
import org.fmi.zog.helpers.filefilter.ImageFilter;
import org.fmi.zog.helpers.filefilter.JpegFilter;

public final class TabbedPanel extends JPanel implements UtilData {

	public static int TOOL;
	public static int TOOL_PropType;
	public static int TOOL_Thicknes;
	public static Vector<File> data;

	public final JTabbedPane tabPanel;
	private final StatusBar sb;
	private JScrollPane scroll;
	private BufferedImage image;

	public TabbedPanel() {
		tabPanel = new JTabbedPane();
		tabPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		sb = new StatusBar();
		data = new Vector<File>();

		setLayout(new TableLayout(new double[][] { { TableLayout.FILL, 10 },
				{ TableLayout.FILL, STATUS_BAR_HEIGHT } }));

		add(tabPanel, "0,0");
		add(sb, "0,1");
	}

	public void newTab(final String name, final Dimension dim) throws OutOfMemoryError {
		image = new BufferedImage((int) dim.getWidth(), (int) dim.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, (int) dim.getWidth(), (int) dim.getHeight());

		data.add(null);

		scroll = new JScrollPane(new DrawPanel(dim, sb, image));
		scroll.getVerticalScrollBar().setUnitIncrement(15);
		scroll.getHorizontalScrollBar().setUnitIncrement(15);

		tabPanel.addTab(name, scroll);
		tabPanel.setSelectedIndex(tabPanel.getTabCount() - 1);
		tabPanel.setTabComponentAt(tabPanel.getSelectedIndex(),
				new CloseTabButton(tabPanel));
		((DrawPanel) ((JScrollPane) tabPanel.getComponentAt(tabPanel
				.getSelectedIndex())).getViewport().getView()).setStatus(true);
	}

	public void newTab(File file) throws OutOfMemoryError {
		try {
			image = toBufferedImage(ImageIO.read(file));

			String name = file.getName().substring(0,
					file.getName().lastIndexOf('.'));
			Dimension size = new Dimension(image.getWidth(), image.getHeight());

			data.add(file);

			scroll = new JScrollPane(new DrawPanel(size, sb, image));
			scroll.getVerticalScrollBar().setUnitIncrement(15);
			scroll.getHorizontalScrollBar().setUnitIncrement(15);

			tabPanel.addTab(name, scroll);
			tabPanel.setSelectedIndex(tabPanel.getTabCount() - 1);
			tabPanel.setTabComponentAt(tabPanel.getSelectedIndex(),
					new CloseTabButton(tabPanel));
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "Can't open file:\n" + file,
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private static BufferedImage toBufferedImage(Image src)
			throws OutOfMemoryError {
		int w = src.getWidth(null);
		int h = src.getHeight(null);
		int type = BufferedImage.TYPE_INT_RGB;

		BufferedImage dest = new BufferedImage(w, h, type);

		Graphics2D g2 = dest.createGraphics();
		g2.drawImage(src, 0, 0, null);
		g2.dispose();

		return dest;
	}

	public void save(int i) {
		tabPanel.setSelectedIndex(i);
		save();
	}

	public void save() {
		if (tabPanel.getTabCount() <= 0) {
			return;
		}
		
		File file = (File) data.get(tabPanel.getSelectedIndex());
		
		if (file == null) {
			saveAs();
			return;
		}
		
		DrawPanel drawPanel = ((DrawPanel) ((JScrollPane) tabPanel.getSelectedComponent()).getViewport().getView());
		BufferedImage image = drawPanel.getImage();
		
		try {
			
			ImageIO.write(image, Utils.getFileExtension(file), file);
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this,
					"Write error for \n" + file.getPath() + ": "+ e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		drawPanel.setStatus(false);
	}

	public void saveAs() {
		final JFileChooser fC = new JFileChooser();
		fC.setAcceptAllFileFilterUsed(false);
		fC.addChoosableFileFilter(new BmpFilter());
		fC.addChoosableFileFilter(new JpegFilter());
		fC.addChoosableFileFilter(new ImageFilter());

		int returnVal = fC.showSaveDialog(null);
		
		if (returnVal != JFileChooser.APPROVE_OPTION)
			return;
		
		File file = fC.getSelectedFile();
		String ext = Utils.getFileExtension(file);
		
		
		if (Utils.isJPEG(ext))
		{
			new SavePopup(image, file);
		}
		else if (Utils.isBMP(ext))
		{
			try 
			{
				ImageIO.write(image, "bmp", file);
			}
			catch (IOException e) 
			{
				JOptionPane.showMessageDialog(this, 
						"Write error for \n" + file.getPath() + ": " + e.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		} 
		else 
		{
			try
			{	
				if (fC.getFileFilter() instanceof JpegFilter) 
				{				
					new SavePopup(image, new File(file.getCanonicalPath() + ".jpg"));
				} 
				else 
				{
					ImageIO.write(image, "bmp", new File(file.getCanonicalPath() + ".bmp"));
				}
			}
			catch (IOException e) 
			{
				JOptionPane.showMessageDialog(this, 
						"Write error for \n"+ file.getPath() + ": " + e.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		((DrawPanel) ((JScrollPane) tabPanel.getSelectedComponent())
				.getViewport().getView()).setStatus(false);
		
	}
}
