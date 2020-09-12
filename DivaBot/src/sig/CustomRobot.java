package sig;

import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import sig.utils.FileUtils;
import sig.utils.ImageUtils;

public class CustomRobot extends Robot{
	
	File calibration_file = new File("calibration_data.txt");
	BufferedImage currentScreen;
	BufferedImage scoreCurrentScreen;
	int[] calibration_data = new int[]{0,0,1,1};
	long lastCalibrationTime = 0;
	
	public CustomRobot() throws AWTException {
		super();
	}

	public CustomRobot(GraphicsDevice screen) throws AWTException {
		super(screen);
	}
	
	public synchronized BufferedImage getSizedCapture(Rectangle r) {
		return super.createScreenCapture(r);
	}
	
	public void refreshScreen() throws IOException {
		//currentScreen = super.createScreenCapture(new Rectangle(418+18,204+83,912-18,586-83));
		if (CalibrationDataChanged()) {
			ReloadCalibrationData();
		}
		if (calibration_data.length>0) {
			currentScreen = super.createScreenCapture(new Rectangle(calibration_data[0],calibration_data[1],calibration_data[2]-calibration_data[0],calibration_data[3]-calibration_data[1]));
		} else {
			currentScreen = super.createScreenCapture(new Rectangle(418+18,204+83,912-18,586-83));
		}
	}
	private void ReloadCalibrationData() throws IOException {
		lastCalibrationTime=calibration_file.lastModified();
		String[] data = FileUtils.readFromFile("calibration_data.txt");
		calibration_data[0]=Integer.parseInt(data[0]);
		calibration_data[1]=Integer.parseInt(data[1]);
		calibration_data[2]=Integer.parseInt(data[2]);
		calibration_data[3]=Integer.parseInt(data[3]);
	}

	private boolean CalibrationDataChanged() {
		return calibration_file.exists()&&lastCalibrationTime!=calibration_file.lastModified();
	}

	public void refreshScoreScreen() throws IOException {
		if (CalibrationDataChanged()) {
			ReloadCalibrationData();
		}
		if (calibration_data.length>0) {
			scoreCurrentScreen = super.createScreenCapture(new Rectangle(calibration_data[0],calibration_data[1],calibration_data[2]-calibration_data[0],calibration_data[3]-calibration_data[1]));
		} else {
			scoreCurrentScreen = super.createScreenCapture(new Rectangle(418+23,204+85,912-18,586-83));
		}
	}
	
	public synchronized BufferedImage createScreenCapture(Rectangle r) {
		return ImageUtils.toBufferedImage(currentScreen.getScaledInstance(1227, 690, BufferedImage.SCALE_AREA_AVERAGING)).getSubimage(r.x, r.y, r.width, r.height);
		//return img2.getSubimage(r.x-418, r.y-204, r.width, r.height);
	}
	public synchronized BufferedImage createScreenCapture() {
		return ImageUtils.toBufferedImage(currentScreen.getScaledInstance(1227, 690, BufferedImage.SCALE_AREA_AVERAGING));
		//return img2.getSubimage(r.x-418, r.y-204, r.width, r.height);
	}
	public synchronized BufferedImage createScoreScreenCapture() {
		return scoreCurrentScreen;
	}
}
