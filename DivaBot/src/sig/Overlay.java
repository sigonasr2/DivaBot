package sig;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class Overlay extends JPanel implements MouseMotionListener,MouseListener{
	
	public static Overlay OVERLAY;
	
	Overlay() {
		Thread t = new Thread() {
			public void run() {
				while (true) {
					repaint();
					try {
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
		g.setColor(new Color(0,0,0,1));
		g.fillRect(0, 0, MyRobot.screenSize.width, MyRobot.screenSize.height);
		g.setColor(new Color(0,255,255,96));
		if (MyRobot.STARTDRAG!=null&&MyRobot.ENDDRAG!=null) {
			g.fillRect(0,0,Math.min(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x),MyRobot.screenSize.height);
			g.fillRect(Math.min(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x),0,MyRobot.screenSize.width,Math.min(MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.y));
			g.fillRect(Math.min(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x), Math.max(MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.y), Math.max(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x)-Math.min(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x), MyRobot.screenSize.height-Math.max(MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.y));
			g.fillRect(Math.max(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x), Math.min(MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.y), MyRobot.screenSize.width-Math.max(MyRobot.STARTDRAG.x,MyRobot.ENDDRAG.x), MyRobot.screenSize.height-Math.min(MyRobot.STARTDRAG.y,MyRobot.ENDDRAG.y));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		MyRobot.STARTDRAG=e.getLocationOnScreen();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		MyRobot.ENDDRAG=e.getLocationOnScreen();
		if (MyRobot.STARTDRAG.x>MyRobot.ENDDRAG.x) { 
			var xTemp = MyRobot.STARTDRAG.x;
			MyRobot.STARTDRAG.x=MyRobot.ENDDRAG.x;
			MyRobot.ENDDRAG.x=xTemp;
		}
		if (MyRobot.STARTDRAG.y>MyRobot.ENDDRAG.y) { 
			var xTemp = MyRobot.STARTDRAG.y;
			MyRobot.STARTDRAG.y=MyRobot.ENDDRAG.y;
			MyRobot.ENDDRAG.y=xTemp;
		}
		MyRobot.calibrating=true;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		MyRobot.ENDDRAG=e.getLocationOnScreen();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
