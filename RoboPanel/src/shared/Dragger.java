package shared;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Dragger extends MouseAdapter{
	private KFrame f;
	private Point mouseDownCompCoords = null;
	public Dragger(KFrame observer){
		f=observer;
	}
		
		
	public void mouseReleased(MouseEvent e) {
           mouseDownCompCoords = null;
       }

	public void mousePressed(MouseEvent e) {
		mouseDownCompCoords = e.getPoint();
    }
		
	public void mouseDragged(MouseEvent e){
		Point currCoords = e.getLocationOnScreen();
	    f.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
	}
		
}
