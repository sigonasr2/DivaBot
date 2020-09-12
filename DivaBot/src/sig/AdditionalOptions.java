package sig;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AdditionalOptions extends JPanel{
	public static JFrame f = new JFrame();
	static GridBagConstraints g = new GridBagConstraints();
	AdditionalOptions() throws IOException{
		f = new JFrame();
        f.setIconImage(ImageIO.read(new File("cross.png")));
        f.setTitle("DivaBot - Additional Options");
		this.setLayout(new GridBagLayout());
		g.anchor=GridBagConstraints.EAST;
		addComponent(1,1,5,1,new JLabel("Show Twitch Overlay Auto-Hotkey"));
		addComponent(1,3,5,1,new JLabel("Hide Twitch Overlay Auto-Hotkey"));

		addComponent(1,2,12,1,new JLabel(" "));
		f.add(this);
		f.pack();
		f.setResizable(false);
		f.setVisible(true);
	}
	private Component addComponent(int x, int y, int w, int h,Component component) {
		g.gridx=x;
		g.gridy=y;
		g.gridwidth=w;
		g.gridheight=h;
		this.add(component,g);
		return component;
	}
}
