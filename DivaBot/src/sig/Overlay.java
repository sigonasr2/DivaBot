package sig;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import sig.utils.FileUtils;
import sig.utils.MouseUtils;

public class Overlay extends JPanel implements MouseMotionListener,MouseListener{
	
	public static Overlay OVERLAY;
	public static boolean started=false;
	BufferedImage setupWindowButton;
	BufferedImage finishButton;
	BufferedImage changeMonitorButton;
	Font drawFont = new Font("Verdana",Font.PLAIN,32);
	
	Overlay() throws IOException {
		setupWindowButton = ImageIO.read(new File("setupwindow.png"));
		finishButton = ImageIO.read(new File("finish.png"));
		changeMonitorButton = ImageIO.read(new File("changemonitor.png"));
		Thread t = new Thread() {
			public void run() {
				while (true) {
					repaint();
					try {
						if (started) {
							MyRobot.FRAME.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
						} else {
							MyRobot.FRAME.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						}
						Thread.sleep(1000/60);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
		OVERLAY=this;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (started) {
			g.setColor(new Color(0,0,0,1));
			g.fillRect(0, 0, MyRobot.screenSize.width, MyRobot.screenSize.height);
			g.setFont(drawFont);
			g.setColor(new Color(0,255,255,96));
			if (MyRobot.STARTDRAG!=null&&MyRobot.ENDDRAG!=null) {
				g.fillRect(0,0,Math.min(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x),MyRobot.screenSize.height);
				g.fillRect(Math.min(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x),0,MyRobot.screenSize.width,Math.min(MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.y));
				g.fillRect(Math.min(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x), Math.max(MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.y), Math.max(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x)-Math.min(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x), MyRobot.screenSize.height-Math.max(MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.y));
				g.fillRect(Math.max(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x), Math.min(MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.y), MyRobot.screenSize.width-Math.max(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x), MyRobot.screenSize.height-Math.min(MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.y));
			}
			g.setColor(Color.WHITE);
			g.drawString("Make sure your game is surrounded by a completely black background (Turn off overlays temporarily).",4,26);
			g.setColor(Color.BLACK);
			g.drawString("Make sure your game is surrounded by a completely black background (Turn off overlays temporarily).",5,28);
			g.setColor(Color.WHITE);
			g.drawString("Drag your cursor over the ENTIRE game region, include a bit of black space around it.",4,58);
			g.setColor(Color.BLACK);
			g.drawString("Drag your cursor over the ENTIRE game region, include a bit of black space around it.",5,60);
		} else 
		if (MyRobot.CALIBRATIONSTATUS.length()>0) {
			g.setColor(new Color(0,0,0,1));
			g.fillRect(0, 0, MyRobot.screenSize.width, MyRobot.screenSize.height);
			g.setFont(drawFont);
			g.setColor(new Color(0,140,170,255));
			if (MyRobot.STARTDRAG!=null&&MyRobot.ENDDRAG!=null) {
				g.fillRect(0,0,Math.min(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x),MyRobot.screenSize.height);
				g.fillRect(Math.min(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x),0,MyRobot.screenSize.width,Math.min(MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.y));
				g.fillRect(Math.min(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x), Math.max(MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.y), Math.max(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x)-Math.min(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x), MyRobot.screenSize.height-Math.max(MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.y));
				g.fillRect(Math.max(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x), Math.min(MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.y), MyRobot.screenSize.width-Math.max(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x), MyRobot.screenSize.height-Math.min(MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.y));
			}
			g.setColor(Color.WHITE);
			g.drawString(MyRobot.CALIBRATIONSTATUS,4,26);
			g.setColor(Color.BLACK);
			g.drawString(MyRobot.CALIBRATIONSTATUS,5,28);
			g.setColor(Color.WHITE);
			g.drawString("If the game changes location later on, you will have to redo this. Hit the 'Finish' button to exit!",4,58);
			g.setColor(Color.BLACK);
			g.drawString("If the game changes location later on, you will have to redo this. Hit the 'Finish' button to exit!",5,60);
			g.drawImage(finishButton,MyRobot.screenSize.width-finishButton.getWidth()+1,0,this);
		} else
		{
			g.drawImage(setupWindowButton,MyRobot.screenSize.width-setupWindowButton.getWidth()+1,0,this);
			g.drawImage(changeMonitorButton,MyRobot.screenSize.width-changeMonitorButton.getWidth()+1,setupWindowButton.getHeight(),this);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Point cursor = MouseUtils.GetCursorPosition(MyRobot.FRAME, e);
		if (started) {
			MyRobot.STARTDRAG=e.getPoint();
		} else
		if (MyRobot.CALIBRATIONSTATUS.length()>0) {
			if (cursor.x>=MyRobot.screenSize.width-finishButton.getWidth()+1&&
					cursor.x<=MyRobot.screenSize.width&&
					cursor.y>=0&&
					cursor.y<=finishButton.getHeight()) {
				System.exit(0);
			}
		} else
		{
			if (cursor.x>=MyRobot.screenSize.width-setupWindowButton.getWidth()+1&&
					cursor.x<=MyRobot.screenSize.width&&
					cursor.y>=0&&
					cursor.y<=setupWindowButton.getHeight()) {
				started=true;
			} else
			if (cursor.x>=MyRobot.screenSize.width-setupWindowButton.getWidth()+1&&
					cursor.x<=MyRobot.screenSize.width&&
					cursor.y>setupWindowButton.getHeight()&&
					cursor.y<=setupWindowButton.getHeight()*2) {
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			    GraphicsDevice[] gs = ge.getScreenDevices();
			    MyRobot.screen=(MyRobot.screen+1)%gs.length;
			    FileUtils.writetoFile(new String[] {Integer.toString(MyRobot.screen)}, "screenConfig.txt", false);
			    try {
					Runtime.getRuntime().exec("java -ea -Dawt.useSystemAAFontSettings=on -Dswing.aatext=true -jar DivaBot.jar calibrate");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			    System.exit(0);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (started) {
			if (MyRobot.STARTDRAG!=null) {
			MyRobot.ENDDRAG=e.getPoint();
				if (MyRobot.STARTDRAG.x>MyRobot.ENDDRAG.x) { 
					int xTemp = MyRobot.STARTDRAG.x;
					MyRobot.STARTDRAG.x=MyRobot.ENDDRAG.x;
					MyRobot.ENDDRAG.x=xTemp;
				}
				if (MyRobot.STARTDRAG.y>MyRobot.ENDDRAG.y) { 
					int xTemp = MyRobot.STARTDRAG.y;
					MyRobot.STARTDRAG.y=MyRobot.ENDDRAG.y;
					MyRobot.ENDDRAG.y=xTemp;
				}
				MyRobot.calibrating=true;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (started) {
			MyRobot.ENDDRAG=e.getPoint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
