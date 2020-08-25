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
				ImageIO.read(new File("typeface.png")),
				ImageIO.read(new File("typeface2.png"))
				);
		/*CustomRobot r = new CustomRobot();
		Thread.sleep(3000);
		r.refreshScoreScreen();
		ImageIO.write(r.scoreCurrentScreen,"png",new File("liveimage.png"));*/
		//System.out.println(font.getAllData(ImageIO.read(new File("liveimage.png")),true).display());
		/*System.out.println(font.getAllData(ImageIO.read(new File("testimage.png"))).display());
		System.out.println(font.getAllData(ImageIO.read(new File("testimage2.png"))).display());
		System.out.println(font.getAllData(ImageIO.read(new File("testimage3.png"))).display());
		System.out.println(font.getAllData(ImageIO.read(new File("testimage4.png"))).display());*/
	}
}
