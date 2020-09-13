package sig;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;

public class DisplayManager extends JPanel implements MouseListener,ListSelectionListener,ItemListener{
	public static JFrame f = new JFrame();
	static GridBagConstraints g = new GridBagConstraints();
	static Font[] fontList = null;
	static ColorButton colorButton;
	static ColorButton colorButton2;
	static JTextField fontSizeInput;
	static JTextField widthInput;
	static JTextField heightInput;
	static JTextField delayInput;
	static DefaultListModel model = new DefaultListModel();
	static DefaultListModel model2 = new DefaultListModel();
	static JComboBox fonts;
	JList labels = new JList(model);
	JList displayedLabels = new JList(model2);
	public static Display selectedDisplay;
	static String[] AVAILABLELABELS = new String[] {
			"Best Play",
			"Overall Rating",
			"Song Difficulty",
			"Song Title (Japanese)",
			"Song Title (Romanized)",
			"Song Title (Japanese+Romanized)",
			"Song Title (English)",
			"Song Title (Japanese+Romanized+ENG)",
			"Play Count",
			"Pass/Play Count",
			"Pass/Play Count (+%)",
			"FC Count",
			"FC Count (+%)",
			"Song Artist",
	};
	
	DisplayManager() throws IOException {

        f.setIconImage(ImageIO.read(new File("cross.png")));
        f.setTitle("DivaBot - Create Display");
        
		List<Font> tempFontList = new ArrayList(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()));
		
		for (int i=0;i<tempFontList.size();i++) {
			//System.out.println(tempFontList.get(i));
			if (!tempFontList.get(i).canDisplay(12479) || !tempFontList.get(i).canDisplay(12540) || tempFontList.get(i).getFontName().equals("Dialog.plain")) {
				tempFontList.remove(i--);
			}
		}
		
		fontList = tempFontList.toArray(new Font[tempFontList.size()]);
		
		this.setLayout(new GridBagLayout());
		g.anchor=GridBagConstraints.EAST;
		addComponent(1,1,3,1,new JLabel("Background Color"));
		g.anchor=GridBagConstraints.WEST;
		colorButton = (ColorButton)addComponent(4,1,4,1,new ColorButton("Color","background"));
		fonts = new JComboBox(fontList) {
			@Override 
			public Dimension getPreferredSize() {
				return new Dimension(300,40);
			}	
		};
		if (MyRobot.p.configData.containsKey("LAST_BACKGROUND")) {
			colorButton.setBackground(new Color(Integer.parseInt(MyRobot.p.configData.get("LAST_BACKGROUND"))));
		}
		addComponent(9,1,2,1,new JLabel(" "));
		fonts.setRenderer(new FontRenderer());
		fonts.setMaximumRowCount(5);
		fonts.addItemListener(this);
		
		g.anchor=GridBagConstraints.EAST;
		addComponent(1,3,3,1,new JLabel("Font"));
		g.anchor=GridBagConstraints.WEST;
		addComponent(4,3,4,1,fonts);
		if (MyRobot.p.configData.containsKey("LAST_FONT")) {
			for (int i=0;i<fontList.length;i++) {
				if (MyRobot.p.configData.get("LAST_FONT").equals(fontList[i].getFontName())) {
					fonts.setSelectedItem(fontList[i]);
					break;
				}
			}
			//fonts.setSelectedItem(new Font(MyRobot.p.configData.get("LAST_FONT"),Font.PLAIN,32));
		}
		g.anchor=GridBagConstraints.EAST;
		addComponent(1,5,3,1,new JLabel("Font Color"));
		g.anchor=GridBagConstraints.WEST;
		colorButton2 = (ColorButton)addComponent(4,5,2,1,new ColorButton("Color","foreground"));
		colorButton2.setColor(Color.WHITE);
		if (MyRobot.p.configData.containsKey("LAST_TEXT")) {
			colorButton.setBackground(new Color(Integer.parseInt(MyRobot.p.configData.get("LAST_TEXT"))));
		}
		g.anchor=GridBagConstraints.EAST;
		addComponent(7,5,1,1,new JLabel("Size"));
		g.anchor=GridBagConstraints.WEST;
		fontSizeInput = (JTextField)addComponent(8,5,1,1,new JTextField() {
			@Override 
			public Dimension getPreferredSize() {
				return new Dimension(50,20);
			}	
		});
		if (MyRobot.p.configData.containsKey("LAST_FONTSIZE")) {
			fontSizeInput.setText(MyRobot.p.configData.get("LAST_FONTSIZE"));
		}
		fontSizeInput.getDocument().addDocumentListener(new DocumentListener() {

			void updateField(JTextField c, DocumentEvent e) {
				try {
					selectedDisplay.font=new Font(selectedDisplay.font.getFontName(),Font.PLAIN,Integer.parseInt(e.getDocument().getText(0, e.getDocument().getLength())));
					selectedDisplay.fontSize=Integer.parseInt(e.getDocument().getText(0, e.getDocument().getLength()));
					selectedDisplay.updateFont();
					MyRobot.p.repaint();
					c.setBackground(Color.WHITE);
				} catch (NullPointerException | NumberFormatException | BadLocationException e1) {
					c.setBackground(Color.RED);
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateField(fontSizeInput,e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateField(fontSizeInput,e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
			
		});
		//addComponent(1,5,3,1,new JLabel("Font Color"));
		g.anchor=GridBagConstraints.EAST;
		addComponent(2,7,1,1,new JLabel("Width"));
		g.anchor=GridBagConstraints.WEST;
		widthInput = (JTextField)addComponent(3,7,1,1,new JTextField() {
			@Override 
			public Dimension getPreferredSize() {
				return new Dimension(50,20);
			}	
		});
		if (MyRobot.p.configData.containsKey("LAST_WIDTH")) {
			widthInput.setText(MyRobot.p.configData.get("LAST_WIDTH"));
		}
		widthInput.getDocument().addDocumentListener(new DocumentListener() {

			void updateField(JTextField c, DocumentEvent e) {
				try {
					selectedDisplay.width=Integer.parseInt(e.getDocument().getText(0, e.getDocument().getLength()));
					selectedDisplay.updateFont();
					MyRobot.p.repaint();
					c.setBackground(Color.WHITE);
				} catch (NullPointerException | NumberFormatException | BadLocationException e1) {
					c.setBackground(Color.RED);
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateField(widthInput,e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateField(widthInput,e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
			
		});
		g.anchor=GridBagConstraints.EAST;
		addComponent(4,7,2,1,new JLabel("Height"));
		g.anchor=GridBagConstraints.WEST;
		heightInput = (JTextField)addComponent(6,7,2,1,new JTextField() {
			@Override 
			public Dimension getPreferredSize() {
				return new Dimension(50,20);
			}	
		});
		if (MyRobot.p.configData.containsKey("LAST_HEIGHT")) {
			heightInput.setText(MyRobot.p.configData.get("LAST_HEIGHT"));
		}
		heightInput.getDocument().addDocumentListener(new DocumentListener() {

			void updateField(JTextField c, DocumentEvent e) {
				try {
					selectedDisplay.height=Integer.parseInt(e.getDocument().getText(0, e.getDocument().getLength()));
					MyRobot.p.repaint();
					c.setBackground(Color.WHITE);
				} catch (NullPointerException | NumberFormatException | BadLocationException e1) {
					c.setBackground(Color.RED);
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateField(heightInput,e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateField(heightInput,e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
			
		});
		g.anchor=GridBagConstraints.WEST;
		addComponent(1,10,6,1,new JLabel(" "));
		g.anchor=GridBagConstraints.WEST;
		delayInput = (JTextField)addComponent(5,10,1,1,new JTextField() {
			@Override 
			public Dimension getPreferredSize() {
				return new Dimension(50,20);
			}	
		});
		delayInput.getDocument().addDocumentListener(new DocumentListener() {

			void updateField(JTextField c, DocumentEvent e) {
				try {
					selectedDisplay.delay=Integer.parseInt(e.getDocument().getText(0, e.getDocument().getLength()));
					MyRobot.p.repaint();
					c.setBackground(Color.WHITE);
				} catch (NullPointerException | NumberFormatException | BadLocationException e1) {
					c.setBackground(Color.RED);
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateField(delayInput,e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateField(delayInput,e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
			
		});
		if (MyRobot.p.configData.containsKey("LAST_DELAY")) {
			delayInput.setText(MyRobot.p.configData.get("LAST_DELAY"));
		}
		g.anchor=GridBagConstraints.WEST;
		addComponent(6,10,1,1,new JLabel("ms"));
		
		addComponent(1,2,12,1,new JLabel(" "));
		addComponent(1,4,12,1,new JLabel(" "));
		addComponent(1,6,12,1,new JLabel(" "));
		addComponent(1,8,12,1,new JLabel(" "));
		addComponent(1,9,20,1,new JLabel("Label Delay (Time between each Display)"));
		addComponent(1,11,12,1,new JLabel(" "));
		
		
		labels.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane pane = new JScrollPane(labels);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		labels.setDragEnabled(true);
		labels.setDropMode(DropMode.INSERT);
		
		pane.setPreferredSize(new Dimension(200,100));
		labels.addListSelectionListener(this);
		labels.setTransferHandler(new ListTransferHandler());
		labels.setBackground(new Color(160,160,160));
		for (int i=0;i<AVAILABLELABELS.length;i++) {
			model.addElement(AVAILABLELABELS[i]);
		}
		displayedLabels.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane pane2 = new JScrollPane(displayedLabels);
		pane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		displayedLabels.setDragEnabled(true);
		displayedLabels.setDropMode(DropMode.INSERT);
		
		pane2.setPreferredSize(new Dimension(200,100));
		displayedLabels.addListSelectionListener(this);
		displayedLabels.setTransferHandler(new ListTransferHandler());

		g.anchor=GridBagConstraints.WEST;
		addComponent(1,13,5,1,pane);
		addComponent(7,13,5,1,pane2);
		g.anchor=GridBagConstraints.WEST;
		addComponent(1,12,2,1,new JLabel("Available Labels:"));
		addComponent(7,12,2,1,new JLabel("Active Labels:"));
		
		widthInput.setText("200");
		heightInput.setText("48");
		fontSizeInput.setText("32");
		delayInput.setText("10000");
		f.add(this);
		f.pack();
		f.setResizable(false);
		f.setVisible(false);
	}
	private Component addComponent(int x, int y, int w, int h,Component component) {
		g.gridx=x;
		g.gridy=y;
		g.gridwidth=w;
		g.gridheight=h;
		this.add(component,g);
		return component;
	}
	public static void setupSettings(Display d) {
		selectedDisplay=d;
		colorButton.setBackground(d.backgroundCol);
		MyRobot.p.configData.put("LAST_BACKGROUND",Integer.toString(d.backgroundCol.getRGB()));
		colorButton2.setBackground(d.textCol);
		MyRobot.p.configData.put("LAST_TEXT",Integer.toString(d.textCol.getRGB()));
		for (int i=0;i<fonts.getItemCount();i++) {
			if (d.font.getFontName().equals(((Font)(fonts.getItemAt(i))).getFontName())) {
				fonts.setSelectedItem(fonts.getItemAt(i));
				break;
			}
		}
		MyRobot.p.configData.put("LAST_FONT",d.font.getFontName());
		widthInput.setText(Integer.toString(d.width));
		MyRobot.p.configData.put("LAST_WIDTH",Integer.toString(d.width));
		heightInput.setText(Integer.toString(d.height));
		MyRobot.p.configData.put("LAST_HEIGHT",Integer.toString(d.height));
		delayInput.setText(Integer.toString(d.delay));
		MyRobot.p.configData.put("LAST_DELAY",Integer.toString(d.delay));
		fontSizeInput.setText(Integer.toString(d.fontSize));
		MyRobot.p.configData.put("LAST_FONTSIZE",Integer.toString(d.fontSize));
		model.clear();
		model2.clear();
		for (int i=0;i<d.labels.length;i++) {
			if (!d.labels[i].equalsIgnoreCase("Add a label!")) {
				model2.add(i, d.labels[i]);
			}
		}
		first:
		for (int j=0;j<AVAILABLELABELS.length;j++) {
			String labelCheck = AVAILABLELABELS[j];
			for (int i=0;i<d.labels.length;i++) {
				if (labelCheck.equalsIgnoreCase(d.labels[i])) {
					continue first;
				}
			}
			model.add(model.getSize(),labelCheck);
		}
		f.setVisible(true);
	}
	
	class FontRenderer extends JLabel
    implements ListCellRenderer {
		
		@Override 
		public Dimension getPreferredSize() {
			return new Dimension(300,40);
		}	

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			Font selectedFont = ((Font)value);

	        if (isSelected) {
	            setBackground(list.getSelectionBackground());
	            setForeground(list.getSelectionForeground());
	        } else {
	            setBackground(list.getBackground());
	            setForeground(list.getForeground());
	        }
	        setText(selectedFont.getFontName());
            setFont(selectedFont.deriveFont(32f));
			return this;
		}
	
	}
	
	class ColorButton extends JButton{
		protected Color col = Color.BLUE;
		String type = "background";
		public ColorButton(String string,final String type) {
			super(string);
			this.setColor(col);
			this.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					Color c = MyRobot.CP.getColor(col);
					if (c!=null) {
						((ColorButton)(e.getSource())).setColor(c);
						switch (type) {
							case "background":{
								selectedDisplay.backgroundCol=c;
								MyRobot.p.repaint();
							}break;
							case "foreground":{
								selectedDisplay.textCol=c;
								MyRobot.p.repaint();
							}break;
						}
					}
				}
			});
		}
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(120,24);
		}
		public void setColor(Color col) {
			this.setBackground(col);
			if (col.getRed()+col.getGreen()+col.getBlue()<=255) {
				this.setForeground(Color.WHITE);
			} else {
				this.setForeground(Color.BLACK);
			}
		}
		public Color getColor() {
			return col;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
	}
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (selectedDisplay!=null) {
			selectedDisplay.font=new Font(((Font)fonts.getSelectedItem()).getFontName(),Font.PLAIN,selectedDisplay.fontSize);
			selectedDisplay.updateFont();
			MyRobot.p.repaint();
		}
	}
}
