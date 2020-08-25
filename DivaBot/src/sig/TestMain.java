package sig;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class TestMain {
	public static void main(String[] args) throws IOException, AWTException, InterruptedException {
		
		TypeFace2 font = new TypeFace2(
				ImageIO.read(new File("typeface.png"))
				);
		CustomRobot r = new CustomRobot();
		/*Thread.sleep(3000);
		r.refreshScoreScreen();
		ImageIO.write(r.scoreCurrentScreen,"png",new File("testimage.png"));*/
		System.out.println(Arrays.toString(font.getAllData(ImageIO.read(new File("testimage.png")))));
		System.out.println(Arrays.toString(font.getAllData(ImageIO.read(new File("testimage2.png")))));
		System.out.println(Arrays.toString(font.getAllData(ImageIO.read(new File("testimage3.png")))));
		System.out.println(Arrays.toString(font.getAllData(ImageIO.read(new File("testimage4.png")))));
	}
}
