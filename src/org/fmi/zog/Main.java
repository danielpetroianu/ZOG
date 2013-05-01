package org.fmi.zog;

import java.awt.EventQueue;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.JFrame;

import org.fmi.zog.presentation.Zog;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;

public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				try {
					
					UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());

					JFrame.setDefaultLookAndFeelDecorated(true);
					new Zog().setVisible(true);

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, ex, "Error", JOptionPane.ERROR_MESSAGE, null);

					System.exit(0);
				}
			}
		});

	}

}
