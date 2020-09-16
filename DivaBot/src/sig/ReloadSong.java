package sig;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.json.JSONException;
import org.json.JSONObject;

import sig.DisplayManager.FontRenderer;
import sig.utils.FileUtils;

public class ReloadSong extends JPanel implements ItemListener,MouseListener{
	public static JFrame f = new JFrame();
	//public static HashMap<String,JSONObject> SONGARRAY = new HashMap<>();
	public static JComboBox<SongInfo> songs;
	static GridBagConstraints g = new GridBagConstraints();
	ReloadSong() throws IOException{
		//JSONObject obj = FileUtils.readJsonFromUrl("http://projectdivar.com/songs");
		
		f = new JFrame();
        f.setIconImage(ImageIO.read(new File("cross.png")));
        f.setTitle("DivaBot - Reload Song");
		this.setLayout(new GridBagLayout());
		g.anchor=GridBagConstraints.EAST;
		addComponent(1,1,2,1,new JLabel("Select correct song:"));
		
		SongInfo[] songList = new SongInfo[MyRobot.SONGNAMES.length];
		int count = 0;
		for (SongInfo s : MyRobot.SONGNAMES) {
			songList[count++]=s;
		}
		
		for (int i = 0; i < count; i++) 
        {
            for (int j = i + 1; j < count; j++) { 
                if (songList[i].english_name.toLowerCase().compareTo(songList[j].english_name.toLowerCase())>0) 
                {
                    SongInfo temp = songList[i];
                    songList[i] = songList[j];
                    songList[j] = temp;
                }
            }
        }
		
		songs = new JComboBox(songList) {
			@Override 
			public Dimension getPreferredSize() {
				return new Dimension(600,40);
			}	
		};
		songs.setRenderer(new SongRenderer());
		songs.setMaximumRowCount(12);
		songs.addItemListener(this);
		
		JButton submit = new JButton("Recalibrate Song") {
			@Override 
			public Dimension getPreferredSize() {
				return new Dimension(600,40);
			}	
		};
		
		addComponent(3,1,10,1,songs);
		addComponent(1,4,12,1,submit);
		
		submit.addMouseListener(this);

		addComponent(1,2,12,1,new JLabel(" "));
		addComponent(1,3,12,1,new JLabel(" "));
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
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}
	class SongRenderer extends JLabel
    implements ListCellRenderer {
		
		@Override 
		public Dimension getPreferredSize() {
			return new Dimension(300,40);
		}	

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			SongInfo selectedSong = ((SongInfo)value);

	        if (isSelected) {
	            setBackground(list.getSelectionBackground());
	            setForeground(list.getSelectionForeground());
	        } else {
	            setBackground(list.getBackground());
	            setForeground(list.getForeground());
	        }
	        setText(selectedSong.english_name
	        		+" - "
	        		+((selectedSong.romanized_name.length()>0)?
	        				(!selectedSong.romanized_name.equalsIgnoreCase(selectedSong.name))?
	        				selectedSong.romanized_name+"("+selectedSong.name+")":"":selectedSong.name));
			return this;
		}
	
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		BufferedImage img = null;
    	try {
			ImageIO.write(img=MyRobot.MYROBOT.createScreenCapture(new Rectangle(630,80,580,380)),"png",new File("test.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		long totalr=0;
		long totalg=0;
		long totalb=0;
		for (int i=0;i<580;i++) {
			for (int j=0;j<380;j++) {
				totalr+=Math.pow(new Color(img.getRGB(i,j),true).getRed(),2);
				totalg+=Math.pow(new Color(img.getRGB(i,j),true).getGreen(),2);
				totalb+=Math.pow(new Color(img.getRGB(i,j),true).getBlue(),2);
			}
		}
		try {
			SongData.saveSongToFile(((SongInfo)songs.getSelectedItem()).name,totalr,totalg,totalb);
		} catch (JSONException | IOException e2) {
			e2.printStackTrace();
		}
	    try {
			SongData.loadSongsFromFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    f.setVisible(false);
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
}

