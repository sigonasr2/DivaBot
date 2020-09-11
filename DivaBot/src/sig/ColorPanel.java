package sig;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JColorChooser;
import javax.swing.JPanel;

public class ColorPanel extends JPanel{
		public ColorPanel() {
		}
		
		public Color getBackgroundColor() {
			if (DrawCanvas.configData.containsKey("BACKGROUND")) {
				try {
					return JColorChooser.showDialog(this, "Color Picker", 
							new Color(
									Integer.parseInt(DrawCanvas.configData.get("BACKGROUND"))));
				} catch (NumberFormatException e) {
					return JColorChooser.showDialog(this, "Color Picker", Color.MAGENTA);
				}
			} else {
				System.out.println("Running");
				return JColorChooser.showDialog(this, "Color Picker", Color.MAGENTA);
			}
		}
		
		public Color getColor(Color color) {
			return JColorChooser.showDialog(this, "Color Picker", color);
		}

	    public Dimension getPreferredSize() {
	        return new Dimension(640,480);
	    }
}
