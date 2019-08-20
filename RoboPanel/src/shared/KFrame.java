package shared;

import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class KFrame extends Frame{
	public void Frame(){
		addMouseListener(new Dragger(this));
	}
	
	
}
