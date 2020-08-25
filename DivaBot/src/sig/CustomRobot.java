package sig;

import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import sig.utils.ImageUtils;

public class CustomRobot extends Robot{
	
	BufferedImage currentScreen;
	BufferedImage scoreCurrentScreen;
	
	public CustomRobot() throws AWTException {
		super();
	}

	public CustomRobot(GraphicsDevice screen) throws AWTException {
		super(screen);
	}
	
	public void refreshScreen() {
		currentScreen = super.createScreenCapture(new Rectangle(418+18,204+83,912-18,586-83));
	}
	public void refreshScoreScreen() {
		scoreCurrentScreen = super.createScreenCapture(new Rectangle(418+23,204+85,912-18,586-83));
	}
	
	public synchronized BufferedImage createScreenCapture(Rectangle r) {
		BufferedImage img2 = ImageUtils.toBufferedImage(currentScreen.getScaledInstance(1227, 690, BufferedImage.SCALE_AREA_AVERAGING));
		return img2.getSubimage(r.x-418, r.y-204, r.width, r.height);
	}
	public synchronized BufferedImage createNormalScreenCapture(Rectangle r) {
		return currentScreen.getSubimage((int)((r.x-418)*(894d/1227)), (int)((r.y-204)*(503d/690)), (int)Math.ceil(r.width*(894d/1227)), (int)Math.ceil(r.height*(503d/690)));
		//return  super.createScreenCapture(new Rectangle(r.x-418,r.y-204,912-18,586-83));
	}
	public synchronized BufferedImage createScoreScreenCapture() {
		return scoreCurrentScreen;
	}
}
