package org.fmi.zog.helpers;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SavePopup extends JFrame implements ActionListener, ChangeListener {

	BufferedImage image;
	JPanel panel;
	JLabel sliderLabel;
	JButton ok;
	JSlider slide;
	int compresie;
	File imFile;

	public SavePopup(BufferedImage im, File fl) {
		super("Compresie ");
		image = im;
		compresie = 1;
		imFile = fl;

		panel = new JPanel();

		sliderLabel = new JLabel("Compresie(0/100): ");

		Font font = new Font("Serif", Font.ITALIC, 15);
		slide = new JSlider(0, 100, 0);
		slide.addChangeListener(this);
		slide.setMajorTickSpacing(50);
		slide.setMinorTickSpacing(10);
		slide.setPaintTicks(true);
		slide.setPaintLabels(true);
		slide.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		slide.setFocusable(false);
		slide.setToolTipText("High compression means low quality.");
		slide.setFont(font);
		slide.setValue(80);

		ok = new JButton("OK");
		ok.setVerticalTextPosition(AbstractButton.CENTER);
		ok.setHorizontalTextPosition(AbstractButton.LEADING);
		ok.setActionCommand("OK");
		ok.addActionListener(this);

		panel.add(sliderLabel, "2,1,4,1");
		panel.add(slide, "1,4,5,4");
		panel.add(ok, "3,5");
		panel.setVisible(true);

		this.getContentPane().add(panel);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setSize(300, 170);
		this.setAlwaysOnTop(true);
		this.setLocationByPlatform(true);
		this.setResizable(false);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("OK")) {
			compresie = slide.getValue() / 100;
			Iterator iter = ImageIO.getImageWritersByFormatName("jpg");
			ImageWriter writer = (ImageWriter) iter.next();
			ImageWriteParam iwp = writer.getDefaultWriteParam();
			iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			iwp.setCompressionQuality(1 - compresie);
			// an integer between 0 and 1
			// 1 specifies minimum compression and maximum quality
			FileImageOutputStream output;
			try {
				output = new FileImageOutputStream(imFile);
				writer.setOutput(output);
				writer.write(null, new IIOImage(image, null, null), iwp);
				output.close();

			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			writer.dispose();
			this.setVisible(false);
			this.dispose();
		}
	}

	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		if (!source.getValueIsAdjusting()) {
			sliderLabel.setText("Compresie(" + source.getValue() + "/100): ");
		}
	}
}
