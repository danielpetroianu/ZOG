package org.fmi.zog.presentation;

import java.awt.CardLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;

import org.fmi.zog.helpers.UtilData;
import org.fmi.zog.helpers.ZDialog;
import org.fmi.zog.helpers.ZSpinner;
import org.fmi.zog.helpers.filefilter.BmpFilter;
import org.fmi.zog.helpers.filefilter.ImageFilter;
import org.fmi.zog.helpers.filefilter.ImagePreview;
import org.fmi.zog.helpers.filefilter.JpegFilter;
import info.clearthought.layout.TableLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.JFrame;


public final class Zog 
				extends JFrame 
				implements ActionListener, ItemListener, ChangeListener, UtilData {

	private final Dimension ScreenSize;
	public static TabbedPanel paint;
	static ColorPanel colors;
	private int index = 0;

	public Zog() {
		super("ZOG Image Editor");
		ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (ScreenSize.width < 800 && ScreenSize.height < 600) {
			JOptionPane.showMessageDialog(
							this,
							"The screen resolution is to low, visual performance may be altered",
							"Warning", JOptionPane.WARNING_MESSAGE);
		}
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setIconImage(new javax.swing.ImageIcon(Zog.class.getResource("resources/zog_logo.png")).getImage());
		// set layout
		setLayout(new TableLayout(new double[][] {
				{ 4, BUTTON_WIDTH, 4, COLOR_PANEL_WIDTH - BUTTON_WIDTH - 4, 10, PROPERTIES_PANEL_WIDTH, TableLayout.FILL },
				{ 4, COLOR_PANEL_HEIGHT, 10, BUTTON_HEIGHT, 2, BUTTON_HEIGHT, 2, BUTTON_HEIGHT, 
					2, BUTTON_HEIGHT, 2, BUTTON_HEIGHT, 2, BUTTON_HEIGHT, 2, BUTTON_HEIGHT, 2, BUTTON_HEIGHT,
					2, BUTTON_HEIGHT, 2, BUTTON_HEIGHT, 2, BUTTON_HEIGHT, TableLayout.FILL, 4 } 
		}));
		setPreferredSize(FRAME_SIZE);
		setMaximumSize(FRAME_MINIMUM_SIZE);

		// add menu to frame
		createMenu();
		setJMenuBar(MENUBAR);

		paint = new TabbedPanel();

		// add color panel to frame
		colors = new ColorPanel();
		add(colors, "1,1,3,1");

		// add properties panel to frame
		createProperties();
		add(propCard, "5,1");

		// add buttons to frame
		createButtons();

		// add TabbedPanel to frame
		add(paint, "3,2,6,24");

		pack();
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

	}

	// <editor-fold defaultstate="collapsed" desc="MENIU">
	private JMenuBar MENUBAR;
	private final JMenu file = new JMenu("File");
	// private final JMenu edit = new JMenu("Edit");
	private final JMenuItem[] fileItem = new JMenuItem[FILE_ITEM];
	private final String[] fileItemName = { "New", "Open", "Save", "Save as", "About", "Exit" };

	// private final JMenuItem[] editItem = new JMenuItem[EDIT_ITEM];
	// private final String[] editItemName = {"Undo", "Redo", "Cut", "Copy",
	// "Paste"};

	private void createMenu() {
		MENUBAR = new JMenuBar();

		for (int i = 0; i < FILE_ITEM; i++) {
			fileItem[i] = new JMenuItem(fileItemName[i]);
			fileItem[i].addActionListener(this);
		}

		fileItem[NEW_FILE].setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_N,
				java.awt.event.InputEvent.CTRL_MASK));
		fileItem[NEW_FILE].setIcon(new javax.swing.ImageIcon(Zog.class
				.getResource("resources/IconMenu/newfile.png")));
		file.add(fileItem[NEW_FILE]);

		fileItem[OPEN].setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_O,
				java.awt.event.InputEvent.CTRL_MASK));
		fileItem[OPEN].setIcon(new javax.swing.ImageIcon(Zog.class
				.getResource("resources/IconMenu/open.png")));
		file.add(fileItem[OPEN]);

		file.add(new javax.swing.JPopupMenu.Separator());

		fileItem[SAVE].setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_S,
				java.awt.event.InputEvent.CTRL_MASK));
		fileItem[SAVE].setIcon(new javax.swing.ImageIcon(Zog.class
				.getResource("resources/IconMenu/save.png")));
		file.add(fileItem[SAVE]);

		fileItem[SAVE_AS].setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_S,
				java.awt.event.InputEvent.SHIFT_MASK
						| java.awt.event.InputEvent.CTRL_MASK));
		fileItem[SAVE_AS].setIcon(new javax.swing.ImageIcon(Zog.class
				.getResource("resources/IconMenu/saveas.png")));
		file.add(fileItem[SAVE_AS]);

		file.add(new javax.swing.JPopupMenu.Separator());

		fileItem[ABOUT].setIcon(new javax.swing.ImageIcon(Zog.class
				.getResource("resources/IconMenu/about.png")));
		file.add(fileItem[ABOUT]);

		file.add(new javax.swing.JPopupMenu.Separator());

		fileItem[EXIT].setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_Q,
				java.awt.event.InputEvent.CTRL_MASK));
		fileItem[EXIT].setIcon(new javax.swing.ImageIcon(Zog.class
				.getResource("resources/IconMenu/exit.png")));
		file.add(fileItem[EXIT]);

		file.setMnemonic(java.awt.event.KeyEvent.VK_F);
		file.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if (e.getSource() == file) {
					if (paint.tabPanel.getTabCount() > 0) {
						fileItem[SAVE].setEnabled(true);
						fileItem[SAVE_AS].setEnabled(true);
					} else {
						fileItem[SAVE].setEnabled(false);
						fileItem[SAVE_AS].setEnabled(false);
					}
				}
			}
		});

		MENUBAR.add(file);

		// ********************
		// EDIT

		// for (int i = 0; i < EDIT_ITEM; i++) {
		// editItem[i] = new JMenuItem(editItemName[i]);
		// editItem[i].addActionListener(this);
		// }
		//
		// editItem[UNDO].setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z,
		// java.awt.event.InputEvent.CTRL_MASK));
		// editItem[UNDO].setIcon(new
		// javax.swing.ImageIcon(Zog.class.getResource("resources/IconMenu/undo.png")));
		// edit.add(editItem[UNDO]);
		//
		// editItem[REDO].setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y,
		// java.awt.event.InputEvent.CTRL_MASK));
		// editItem[REDO].setIcon(new
		// javax.swing.ImageIcon(Zog.class.getResource("resources/IconMenu/redo.png")));
		// edit.add(editItem[REDO]);
		//
		// edit.add(new javax.swing.JPopupMenu.Separator());
		//
		// editItem[CUT].setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X,
		// java.awt.event.InputEvent.CTRL_MASK));
		// editItem[CUT].setIcon(new
		// javax.swing.ImageIcon(Zog.class.getResource("resources/IconMenu/cut.png")));
		// edit.add(editItem[CUT]);
		//
		// editItem[COPY].setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,
		// java.awt.event.InputEvent.CTRL_MASK));
		// editItem[COPY].setIcon(new
		// javax.swing.ImageIcon(Zog.class.getResource("resources/IconMenu/copy.png")));
		// edit.add(editItem[COPY]);
		//
		// editItem[PASTE].setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V,
		// java.awt.event.InputEvent.CTRL_MASK));
		// editItem[PASTE].setIcon(new
		// javax.swing.ImageIcon(Zog.class.getResource("resources/IconMenu/paste.png")));
		// edit.add(editItem[PASTE]);
		//
		//
		// edit.setMnemonic(java.awt.event.KeyEvent.VK_E);
		//
		// MENUBAR.add(edit);
	}

	// </editor-fold>
	
	
	// <editor-fold defaultstate="collapsed" desc="Properies Pannels">
	
	private ButtonGroup buttonGrup;
	private final JPanel propCard = new JPanel(new CardLayout());
	private JToggleButton transE, transR;
	private JToggleButton opacE, opacR;
	private JComboBox lineCB;
	private JComboBox brushCB;
	private JSlider zoom;
	private ZSpinner sizeBrush;
	private ZSpinner sizeLine;
	private ZSpinner sizeElipse;
	private ZSpinner sizeRectan;
	private ZSpinner sizePen;
	private ZSpinner sizeEr;

	private void createProperties() {

		propCard.add(new JPanel(), EMPTY_PANEL);
		propCard.add(createPencilPropPanel(), PENCIL_PANEL);
		propCard.add(createBrushPropPanel(), BRUSH_PANEL);
		propCard.add(createRectanglePropPanel(), RECTANGLE_PANEL);
		propCard.add(createElipsePropPanel(), ELIPSE_PANEL);
		propCard.add(createLinePropPanel(), LINE_PANEL);
		propCard.add(createEraserPropPanel(), ERASER_PANEL);
		propCard.add(createMagnifierPropPanel(), ZOOM_PANEL);

	}

	private JPanel createElipsePropPanel() {
		buttonGrup = new ButtonGroup();
		JPanel shapepanel = new JPanel();
		shapepanel.setLayout(new TableLayout(new double[][] {
				{ BUTTON_WIDTH, 4, BUTTON_WIDTH, 15, 12 },
				{ TableLayout.FILL, BUTTON_WIDTH, 4, TableLayout.MINIMUM,TableLayout.FILL }
		}));

		transE = new JToggleButton(new ImageIcon(Zog.class.getResource("resources/IconButtons/trans.png")));
		transE.setPreferredSize(BUTTON_SIZE);
		transE.setMinimumSize(BUTTON_SIZE);
		transE.setMaximumSize(BUTTON_SIZE);
		transE.addActionListener(this);
		shapepanel.add(transE, "0,1");
		buttonGrup.add(transE);
		transE.setSelected(true);

		opacE = new JToggleButton(new ImageIcon(Zog.class.getResource("resources/IconButtons/opac.png")));
		opacE.setPreferredSize(BUTTON_SIZE);
		opacE.setMinimumSize(BUTTON_SIZE);
		opacE.setMaximumSize(BUTTON_SIZE);
		opacE.addActionListener(this);
		shapepanel.add(opacE, "2,1");
		buttonGrup.add(opacE);

		shapepanel.add(new JLabel("size :"), "0,3");

		sizeElipse = new ZSpinner(4, 1, 100, 1);
		shapepanel.add(sizeElipse, "2,3,3,3");

		shapepanel.add(new JLabel("px"), "4,3");

		return shapepanel;
	}

	private JPanel createRectanglePropPanel() {
		buttonGrup = new ButtonGroup();
		JPanel shapepanel = new JPanel();
		shapepanel.setLayout(new TableLayout(new double[][] {
				{ BUTTON_WIDTH, 4, BUTTON_WIDTH, 15, 12 },
				{ TableLayout.FILL, BUTTON_WIDTH, 4, TableLayout.MINIMUM,TableLayout.FILL } 
		}));

		transR = new JToggleButton(new ImageIcon(Zog.class.getResource("resources/IconButtons/trans.png")));
		transR.setPreferredSize(BUTTON_SIZE);
		transR.setMinimumSize(BUTTON_SIZE);
		transR.setMaximumSize(BUTTON_SIZE);
		transR.addActionListener(this);
		shapepanel.add(transR, "0,1");
		buttonGrup.add(transR);
		transR.setSelected(true);

		opacR = new JToggleButton(new ImageIcon(Zog.class.getResource("resources/IconButtons/opac.png")));
		opacR.setPreferredSize(BUTTON_SIZE);
		opacR.setMinimumSize(BUTTON_SIZE);
		opacR.setMaximumSize(BUTTON_SIZE);
		opacR.addActionListener(this);
		shapepanel.add(opacR, "2,1");
		buttonGrup.add(opacR);

		shapepanel.add(new JLabel("size :"), "0,3");

		sizeRectan = new ZSpinner(4, 1, 100, 1);
		shapepanel.add(sizeRectan, "2,3,3,3");

		shapepanel.add(new JLabel("px"), "4,3");

		return shapepanel;
	}

	private JPanel createLinePropPanel() {

		JPanel linepanel = new JPanel();
		linepanel.setLayout(new TableLayout(new double[][] {
				{ TableLayout.MINIMUM, 2, TableLayout.MINIMUM, 2, TableLayout.MINIMUM },
				{ TableLayout.FILL, TableLayout.MINIMUM, 4,TableLayout.MINIMUM, TableLayout.FILL } 
		}));

		linepanel.add(new JLabel("size :"), "0,1");

		Vector<Integer> item = new Vector<Integer>();
		item.add(1);
		for (int i = 2; i <= 20; i += 2) {
			item.add(i);
		}
		sizeLine = new ZSpinner(4, 1, 100, 1);
		linepanel.add(sizeLine, "2,1");

		linepanel.add(new JLabel("px"), "4,1");

		Vector<ImageIcon> itemLine = new Vector<ImageIcon>();
		for (int i = 0; i < 3; i++) {
			itemLine.add(new ImageIcon(Zog.class
					.getResource("resources/styles/line" + i + ".png")));
		}
		lineCB = new JComboBox(itemLine);
		lineCB.addItemListener(this);
		linepanel.add(lineCB, "0,3,3,3");

		return linepanel;
	}

	private JPanel createBrushPropPanel() {
		JPanel brushPro = new JPanel(new FlowLayout());
		brushPro.setLayout(new TableLayout(new double[][] {
				{ TableLayout.MINIMUM, 2, TableLayout.MINIMUM, 2,TableLayout.MINIMUM },
				{ TableLayout.FILL, TableLayout.MINIMUM, 4, TableLayout.MINIMUM, TableLayout.FILL }
		}));

		brushPro.add(new JLabel("size   :"), "0, 1");

		sizeBrush = new ZSpinner(4, 1, 100, 1);

		brushPro.add(sizeBrush, "2,1");

		brushPro.add(new JLabel("px"), "4,1");
		brushPro.add(new JLabel("type :"), "0,3");

		Vector<ImageIcon> item = new Vector<ImageIcon>();
		for (int i = 0; i < 3; i++) {
			item.add(new ImageIcon(Zog.class
					.getResource("resources/styles/brush" + i + ".png")));
		}
		brushCB = new JComboBox(item);
		brushCB.addItemListener(this);

		brushPro.add(brushCB, "2,3,4,3");
		return brushPro;
	}

	private JPanel createPencilPropPanel() {
		JPanel pencilProp = new JPanel();
		pencilProp.setLayout(new TableLayout(new double[][] {
				{ TableLayout.MINIMUM, 4.0d, TableLayout.MINIMUM },
				{ TableLayout.FILL, TableLayout.MINIMUM, TableLayout.FILL }
		}));

		pencilProp.add(new JLabel("size :"), "0,1");
		sizePen = new ZSpinner(4, 1, 100, 1);
		pencilProp.add(sizePen, "2,1");
		return pencilProp;
	}

	private JPanel createEraserPropPanel() {
		JPanel eraserProp = new JPanel();
		eraserProp.setLayout(new TableLayout(new double[][] {
				{ TableLayout.MINIMUM, 4.0d, TableLayout.MINIMUM },
				{ TableLayout.FILL, TableLayout.MINIMUM, TableLayout.FILL } }));

		eraserProp.add(new JLabel("size :"), "0,1");
		sizeEr = new ZSpinner(4, 1, 100, 1);

		eraserProp.add(sizeEr, "2,1");
		return eraserProp;
	}

	private JPanel createMagnifierPropPanel() {
		JPanel mp = new JPanel();
		mp.setLayout(new TableLayout(new double[][] {
				{ 120 },
				{ TableLayout.FILL, 40, TableLayout.FILL }
		}));

		zoom = new JSlider(JSlider.HORIZONTAL, 1, 8, 1);
		zoom.setMajorTickSpacing(1);
		zoom.setPaintTicks(true);
		Hashtable<Integer, JLabel> label = new Hashtable<Integer, JLabel>();
		label.put(new Integer(zoom.getMinimum()), new JLabel("  100%"));
		label.put(new Integer(zoom.getMaximum()), new JLabel("   800%"));
		zoom.setLabelTable(label);
		zoom.setPaintLabels(true);
		zoom.addChangeListener(this);
		zoom.setFocusable(false);
		mp.add(zoom, "0,1");

		return mp;

	}
	
	// </editor-fold>

	
	// <editor-fold defaultstate="collapsed" desc="JTogleButtons">

	private final JToggleButton[] buttons = new JToggleButton[BUTTON_COUNT];
	private final String buttonToolTip[] = { "Pencil", "Brush",
			"Fill with color", "Rectangle", "Elipse", "Line", "Eraser",
			"Get color from (x,y)", "Zoom", "Select" };

	private void createButtons() {
		buttonGrup = new ButtonGroup();
		for (int i = 0, j = 3; i < buttons.length; i++, j += 2) {

			buttons[i] = new JToggleButton(new ImageIcon(Zog.class.getResource("resources/IconButtons/b" + i + ".png")));
			buttons[i].setPreferredSize(BUTTON_SIZE);
			buttons[i].setMinimumSize(BUTTON_SIZE);
			buttons[i].setMaximumSize(BUTTON_SIZE);
			buttons[i].addActionListener(this);
			buttons[i].setToolTipText(buttonToolTip[i]);
			add(buttons[i], "1," + j);
			buttonGrup.add(buttons[i]);
		}
	}
	
	// </editor-fold>

	
	// <editor-fold defaultstate="collapsed" desc="Events">
	
	public void actionPerformed(ActionEvent e) {
		if(e == null) {
			return;
		}
		
		if (e.getSource() instanceof JToggleButton) {
			// <editor-fold defaultstate="collapsed" desc="ButtonAction">
			if (e.getSource() == buttons[PENCIL]) {

				CardLayout cl = (CardLayout) propCard.getLayout();
				cl.show(propCard, PENCIL_PANEL);

				TabbedPanel.TOOL = PENCIL;
				TabbedPanel.TOOL_Thicknes = (Integer) sizePen.getValue();

			} else if (e.getSource() == buttons[BRUSH]) {

				CardLayout cl = (CardLayout) propCard.getLayout();
				cl.show(propCard, BRUSH_PANEL);

				TabbedPanel.TOOL = BRUSH;
				TabbedPanel.TOOL_Thicknes = (Integer) sizeBrush.getValue();

			} else if (e.getSource() == buttons[BUCKET]) {

				CardLayout cl = (CardLayout) propCard.getLayout();
				cl.show(propCard, EMPTY_PANEL);

				TabbedPanel.TOOL = BUCKET;

			} else if (e.getSource() == buttons[RECTANGLE]) {

				CardLayout cl = (CardLayout) propCard.getLayout();
				cl.show(propCard, RECTANGLE_PANEL);

				TabbedPanel.TOOL = RECTANGLE;
				TabbedPanel.TOOL_Thicknes = (Integer) sizeRectan.getValue();
				if (transR.isSelected()) {
					TabbedPanel.TOOL_PropType = TRANSPARENT;
				} else if (opacR.isSelected()) {
					TabbedPanel.TOOL_PropType = OPAQUE;
				}

			} else if (e.getSource() == buttons[ELIPSE]) {

				CardLayout cl = (CardLayout) propCard.getLayout();
				cl.show(propCard, ELIPSE_PANEL);

				TabbedPanel.TOOL = ELIPSE;
				TabbedPanel.TOOL_Thicknes = (Integer) sizeElipse.getValue();
				if (transE.isSelected()) {
					TabbedPanel.TOOL_PropType = TRANSPARENT;
				} else if (opacE.isSelected()) {
					TabbedPanel.TOOL_PropType = OPAQUE;
				}

			} else if (e.getSource() == buttons[LINE]) {

				CardLayout cl = (CardLayout) propCard.getLayout();
				cl.show(propCard, LINE_PANEL);

				TabbedPanel.TOOL = LINE;
				TabbedPanel.TOOL_Thicknes = (Integer) sizeLine.getValue();
				TabbedPanel.TOOL_PropType = lineCB.getSelectedIndex();

			} else if (e.getSource() == buttons[ERASER]) {

				CardLayout cl = (CardLayout) propCard.getLayout();
				cl.show(propCard, ERASER_PANEL);

				TabbedPanel.TOOL = ERASER;
				TabbedPanel.TOOL_Thicknes = (Integer) sizeEr.getValue();

			} else if (e.getSource() == buttons[COLOR_PICKER]) {

				CardLayout cl = (CardLayout) propCard.getLayout();
				cl.show(propCard, EMPTY_PANEL);

				TabbedPanel.TOOL = COLOR_PICKER;

			} else if (e.getSource() == buttons[MAGNIFIER]) {

				CardLayout cl = (CardLayout) propCard.getLayout();
				cl.show(propCard, ZOOM_PANEL);

				TabbedPanel.TOOL = MAGNIFIER;
				TabbedPanel.TOOL_Thicknes = (int) zoom.getValue();

			} else if (e.getSource() == buttons[SELECT]) {

				CardLayout cl = (CardLayout) propCard.getLayout();
				cl.show(propCard, EMPTY_PANEL);

				TabbedPanel.TOOL = SELECT;

			} else if (e.getSource() == transE || e.getSource() == transR) {
				TabbedPanel.TOOL_PropType = TRANSPARENT;
			} else if (e.getSource() == opacE || e.getSource() == opacR) {
				TabbedPanel.TOOL_PropType = OPAQUE;
			}
			// </editor-fold>
		} else if (e.getSource() instanceof JMenuItem) {
			// <editor-fold defaultstate="collapsed" desc="MeniuAction">
			if (e.getSource() == fileItem[NEW_FILE]) {
				try {
					ZDialog size = new ZDialog(this);
					if (size.getChoice() == ZDialog.OK) {
						size.dispose();
						paint.newTab("Untitled" + (index++),
								size.getDimensions());
						fileItem[SAVE].setEnabled(true);
						fileItem[SAVE_AS].setEnabled(true);
					}
				} catch (OutOfMemoryError ex) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(this,
							"Can't create image, insuficient memory", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			} else if (e.getSource() == fileItem[OPEN]) {
				// <editor-fold defaultstate="collapsed" desc="open">
				final JFileChooser fC = new JFileChooser();
				fC.setAcceptAllFileFilterUsed(false);
				fC.addChoosableFileFilter(new BmpFilter());
				fC.addChoosableFileFilter(new JpegFilter());
				fC.addChoosableFileFilter(new ImageFilter());

				fC.setAccessory(new ImagePreview(fC));

				int choice = fC.showOpenDialog(Zog.this);

				switch (choice) {
				case JFileChooser.APPROVE_OPTION:
					try {
						paint.newTab(fC.getSelectedFile());
						fileItem[SAVE].setEnabled(true);
						fileItem[SAVE_AS].setEnabled(true);
					} catch (OutOfMemoryError ex) {
						Toolkit.getDefaultToolkit().beep();
						JOptionPane.showMessageDialog(this,
								"Can't open image insuficient memory", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
					break;
				case JFileChooser.ERROR_OPTION:
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(this, "Can't open file",
							"Error", JOptionPane.ERROR_MESSAGE);
					break;
				}
				// </editor-fold>
			} else if (e.getSource() == fileItem[SAVE]) {
				paint.save();
			} else if (e.getSource() == fileItem[SAVE_AS]) {
				paint.saveAs();
			} else if (e.getSource() == fileItem[ABOUT]) {
				new About(this);
			} else if (e.getSource() == fileItem[EXIT]) {
				exit();
			}
			// </editor-fold>
		}
	}

	private void exit() {
		int tc = paint.tabPanel.getTabCount();
		boolean unsave = false;
		int i;
		for (i = 0; i < tc; i++) {
			if (!unsave) {
				unsave = ((DrawPanel) ((JScrollPane) paint.tabPanel
						.getComponentAt(i)).getViewport().getView())
						.getStatus();
			} else {
				break;
			}
		}
		if (unsave) {
			int ret = JOptionPane
					.showConfirmDialog(
							null,
							"One or more image are not saved.\nDo you want to save them ?",
							"Save", JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
			if (ret == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
		}
		if (!unsave) {
			System.exit(0);
		}
	}

	public void itemStateChanged(ItemEvent e) {
		JComboBox tmp = (JComboBox) e.getSource();
		TabbedPanel.TOOL_PropType = tmp.getSelectedIndex();
	}

	public void stateChanged(ChangeEvent e) {
		JSlider tmp = (JSlider) e.getSource();
		TabbedPanel.TOOL_Thicknes = tmp.getValue();
	}

	// </editor-fold>

}
