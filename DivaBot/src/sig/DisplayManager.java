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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DisplayManager extends JPanel implements MouseListener,ListSelectionListener{
	JFrame f = new JFrame();
	GridBagConstraints g = new GridBagConstraints();
	Font[] fontList = null;
	ColorButton colorButton;
	ColorButton colorButton2;
	JTextField fontSizeInput;
	JTextField widthInput;
	JTextField heightInput;
	JTextField delayInput;
	DefaultListModel model = new DefaultListModel();
	DefaultListModel model2 = new DefaultListModel();
	JList labels = new JList(model);
	JList displayedLabels = new JList(model2);
	String[] AVAILABLELABELS = new String[] {
			"Best Play",
			"Overall Rating",
			"Song Difficulty",
			"Song Title (Japanese)",
			"Song Title (Romanized)",
			"Song Title (English)",
			"Play",
			"Play/Pass Count (+%)",
			"Play/Pass Count (+%)",
			"FC Count",
			"FC/Play Count",
			"FC/Play Count (+%)"
	};
	
	DisplayManager() throws IOException {

        f.setIconImage(ImageIO.read(new File("cross.png")));
        f.setTitle("Create Display");
        
		List<Font> tempFontList = new ArrayList(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()));
		
		for (int i=0;i<tempFontList.size();i++) {
			if (!tempFontList.get(i).canDisplay(12479) || tempFontList.get(i).getFontName().equals("Dialog.plain")) {
				tempFontList.remove(i);
			}
		}
		
		fontList = tempFontList.toArray(new Font[tempFontList.size()]);
		
		this.setLayout(new GridBagLayout());
		g.anchor=GridBagConstraints.EAST;
		addComponent(1,1,3,1,new JLabel("Background Color"));
		g.anchor=GridBagConstraints.WEST;
		colorButton = (ColorButton)addComponent(4,1,4,1,new ColorButton("Color"));
		JComboBox fonts = new JComboBox(fontList) {
			@Override 
			public Dimension getPreferredSize() {
				return new Dimension(300,40);
			}	
		};
		addComponent(9,1,2,1,new JLabel(" "));
		fonts.setRenderer(new FontRenderer());
		fonts.setMaximumRowCount(5);
		g.anchor=GridBagConstraints.EAST;
		addComponent(1,3,3,1,new JLabel("Font"));
		g.anchor=GridBagConstraints.WEST;
		addComponent(4,3,4,1,fonts);
		g.anchor=GridBagConstraints.EAST;
		addComponent(1,5,3,1,new JLabel("Font Color"));
		g.anchor=GridBagConstraints.WEST;
		colorButton = (ColorButton)addComponent(4,5,4,1,new ColorButton("Color"));
		g.anchor=GridBagConstraints.EAST;
		addComponent(1,7,1,1,new JLabel("Width"));
		g.anchor=GridBagConstraints.WEST;
		widthInput = (JTextField)addComponent(3,7,1,1,new JTextField() {
			@Override 
			public Dimension getPreferredSize() {
				return new Dimension(50,20);
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
		g.anchor=GridBagConstraints.WEST;
		addComponent(1,10,6,1,new JLabel(" "));
		g.anchor=GridBagConstraints.WEST;
		delayInput = (JTextField)addComponent(5,10,1,1,new JTextField() {
			@Override 
			public Dimension getPreferredSize() {
				return new Dimension(50,20);
			}	
		});
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
		heightInput.setText("200");
		delayInput.setText("10000");
		f.add(this);
		f.pack();
		f.setResizable(false);
		//f.setVisible(true);
	}
	private Component addComponent(int x, int y, int w, int h,Component component) {
		g.gridx=x;
		g.gridy=y;
		g.gridwidth=w;
		g.gridheight=h;
		this.add(component,g);
		return component;
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
		public ColorButton(String string) {
			super(string);
			this.setColor(col);
			this.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					Color c = MyRobot.CP.getColor(col);
					if (c!=null) {
						((ColorButton)(e.getSource())).setColor(c);
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
		// TODO Auto-generated method stub
		
	}
}
