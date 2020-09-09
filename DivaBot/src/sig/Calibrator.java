package sig;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Calibrator{
	Calibrator(JPanel p) throws IOException, InterruptedException {
		boolean failed=false;
		int x = Math.min(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x);
		int y = Math.min(MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.y);
		int width = (Math.max(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x)-Math.min(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x));
		int height = (Math.max(MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.y)-Math.min(MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.y));
		
		if (MyRobot.STARTDRAG.x>MyRobot.ENDDRAG.x) {
			int xTemp=MyRobot.STARTDRAG.x;
			MyRobot.STARTDRAG.x=MyRobot.ENDDRAG.x;
			MyRobot.ENDDRAG.x=xTemp;
		}
		if (MyRobot.STARTDRAG.y>MyRobot.ENDDRAG.y) {
			int yTemp=MyRobot.STARTDRAG.y;
			MyRobot.STARTDRAG.y=MyRobot.ENDDRAG.y;
			MyRobot.ENDDRAG.y=yTemp;
		}
		
		/*Rectangle currentPointer = new Rectangle(
				x,y,
				(int)Math.floor(x+width*0.03984375d),
				(int)Math.floor(y+height*0.30833333333333333333333333333333d));
		calibrationline=currentPointer;*/
		failed=CalibrationStage1(p);
		if (failed) {return;}
		failed=CalibrationStage2(p);
		if (failed) {return;}
		p.setVisible(true);
		MyRobot.CALIBRATIONSTATUS="First calibration set done: X"+(x-MyRobot.STARTDRAG.x)+" Y"+(y-MyRobot.STARTDRAG.y);
//		failed=CalibrationStage3(p);
//		if (failed) {return;}
//		failed=CalibrationStage4(p);
//		if (failed) {return;}
		//MyRobot.CALIBRATIONSTATUS="First calibration set done: X"+(x-MyRobot.STARTDRAG.x)+" Y"+(y-MyRobot.STARTDRAG.y);
	}

	private boolean CalibrationStage1(JPanel p) throws IOException, InterruptedException {
		boolean calibrated=false;
		int MAXTRIES=10000;
		MyRobot.CALIBRATIONSTATUS="Calibration Stage 1...";
		p.repaint();
		//ImageIO.write(MYROBOT.getSizedCapture(new Rectangle(x,y,width,height)),"png",new File("capture.png"));
		BufferedImage currentScreen = MyRobot.MYROBOT.getSizedCapture(new Rectangle(0,0,MyRobot.screenSize.width,MyRobot.screenSize.height));
		ImageIO.write(currentScreen.getSubimage(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y),"png",new File("capture.png"));
		while (!calibrated&&MAXTRIES>0) {
			//Try moving left until the difference is too high or the colors are not right anymore.
			MyRobot.STARTDRAG.x-=1;
			//BufferedImage pixel = MYROBOT.getSizedCapture(new Rectangle(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,1,1));
			currentScreen = currentScreen.getSubimage(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,1,1);
			ImageIO.write(currentScreen,"png",new File("capture_"+System.nanoTime()+".png"));
			Color col = new Color(currentScreen.getRGB(0, 0));
			System.out.println("Checking "+col);
			if (!(col.getRed()>=5&&col.getRed()<=40&&
					col.getGreen()>=170&&col.getGreen()<=210&&
					col.getBlue()>=205&&col.getBlue()<=250)) {
				//This is the max X. Calibration on this side good.
				MyRobot.STARTDRAG.x++;
				System.out.println("End at "+MyRobot.STARTDRAG.x);
				return false;
			}
			MAXTRIES--;
		}
		MyRobot.CALIBRATIONSTATUS="Calibration failed! Try making the capture region larger and defining it more accurately.";
		p.repaint();
		return true;
	}

	private boolean CalibrationStage2(JPanel p) throws IOException {
		boolean calibrated=false;
		int MAXTRIES=10000;
		MyRobot.CALIBRATIONSTATUS="Calibration Stage 2...";
		p.repaint();
		//ImageIO.write(MYROBOT.getSizedCapture(new Rectangle(x,y,width,height)),"png",new File("capture.png"));
		//BufferedImage currentScreen = MYROBOT.getSizedCapture(new Rectangle(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,MyRobot.ENDDRAG,height));
		BufferedImage currentScreen = MyRobot.MYROBOT.getSizedCapture(new Rectangle(0,0,MyRobot.screenSize.width,MyRobot.screenSize.height));
		ImageIO.write(currentScreen.getSubimage(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.x-MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.y-MyRobot.STARTDRAG.y),"png",new File("capture_2.png"));
		while (!calibrated&&MAXTRIES>0) {
			//Try moving left until the difference is too high or the colors are not right anymore.
			MyRobot.STARTDRAG.y-=1;
			p.repaint();
			//currentScreen.setRGB(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y, new Color(0,0,0).getRGB());
			currentScreen = currentScreen.getSubimage(MyRobot.STARTDRAG.x,MyRobot.STARTDRAG.y,1,1);
			ImageIO.write(currentScreen,"png",new File("capture_2_"+System.nanoTime()+".png"));
			Color col = new Color(currentScreen.getRGB(0, 0));
			System.out.println("Checking "+col);
			if (!(col.getRed()>=5&&col.getRed()<=40&&
					col.getGreen()>=170&&col.getGreen()<=210&&
					col.getBlue()>=205&&col.getBlue()<=250)) {
				//This is the max Y. Calibration on this side good.
				MyRobot.STARTDRAG.y++;
				System.out.println("End at "+MyRobot.STARTDRAG.y);
				return false;
			}
			MAXTRIES--;
		}
		MyRobot.CALIBRATIONSTATUS="Calibration failed! Try making the capture region larger and defining it more accurately.";
		p.repaint();
		return true;
	}
}
