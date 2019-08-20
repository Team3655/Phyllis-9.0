package shared;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.Timer;
import java.util.TimerTask;

public class KPane extends Component{
	Rectangle X=new Rectangle();
	Rectangle topBar=new Rectangle(0,0,this.getWidth()-X.width,20);
	BufferStrategy bs;
	Image buffer;
	String title;
	Frame f;
	boolean decorated;
	
	public KPane(){
		X.setSize(20, 20);
		setFocusable(true);
		this.addMouseListener(new MAdapter());
		Timer t=new Timer();
		f=new Frame();
		f.setUndecorated(true);
		title="KPane";
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.add(this);
		decorated=false;
		t.schedule(new Task(), 0, 50);
		repaint();
	}
	
	public KPane(int width,int height){
		X.setSize(20, 20);
		setFocusable(true);
		this.addMouseListener(new MAdapter());
		Timer t=new Timer();
		f=new Frame();
		f.setUndecorated(true);
		title="KPane";
		f.setLocationRelativeTo(null);
		f.setSize(width, height);
		f.setVisible(true);
		f.add(this);
		decorated=true;
		repaint();
		t.schedule(new Task(), 0, 50);
	}
	
	public KPane(String title){
		X.setSize(20, 20);
		setFocusable(true);
		this.addMouseListener(new MAdapter());
		Timer t=new Timer();
		this.title=title;
		decorated=true;
		repaint();
		t.schedule(new Task(), 0, 50);
	}
	
	public KPane(int width,int height,String title){
		X.setSize(20, 20);
		setFocusable(true);
		this.addMouseListener(new MAdapter());
		Timer t=new Timer();
		
		this.title=title;
		
		decorated=true;
		setVisible(true);
		repaint();
		t.schedule(new Task(), 0, 50);
	}
	
	public Frame getFrame(){
		return f;
	}
	
	/**Override this to draw without decoration and auto double buffer.
	 * 
	 */
	public void paint(Graphics g){
		buffer=this.createImage(getWidth(), getHeight());
		Graphics bg=buffer.getGraphics();
		paintComponent(bg);
		buffer(bg);
		bg.dispose();
		g.drawImage(buffer,0,0,this);
	}
	
	/**Use this to paint stuff
	 * 
	 * @param g
	 */
	public void paintComponent(Graphics g){
		
	}
	
	private void buffer(Graphics g){
		X.setLocation(this.getWidth()-X.width, 0);
		g.setColor(Color.gray);
		g.fillRect(0, 0, getWidth(), X.height-1);
		g.drawRect(0, 0,this.getWidth()-1, X.height-1);
		g.setColor(Color.red);
		g.fillRect(X.x, X.y, X.width, X.height);
		g.setColor(Color.black);
		g.drawRect(0, 0,this.getWidth()-1, X.height-1);
		g.drawLine(X.x, X.y, X.x, X.height-1);
		g.drawLine(X.x, X.y, X.x+X.width-1, X.y+X.height-1);
		g.drawLine(X.x+X.width-1, X.y, X.x, X.y+X.height-1);
		g.drawRect(0, 0,this.getWidth()-1, this.getHeight()-1);
		g.drawString(title, 2, X.height-5);
	}
	
	private void updateTopBar(){
		topBar.width=this.getWidth()-X.width;
	}
	
	private class MAdapter extends MouseAdapter{
		
		public void mouseClicked(MouseEvent e){
			if (e.getButton()==MouseEvent.BUTTON1&&X.contains(e.getPoint())){
				System.exit(0);
			}
		}
		
	}
	
	private class Task extends TimerTask{

		@Override
		public void run() {
			updateTopBar();
			repaint();
		}
		
	}
	
	public void setObserver(Frame f){
		this.f=f;
	}
	
	
}
