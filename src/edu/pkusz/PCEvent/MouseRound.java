package edu.pkusz.PCEvent;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.pkusz.PCEvent.MagnifierEvent.MagnifierThread;

public class MouseRound extends JFrame{
	private MouseRoundPanel mouseRoundPanel = new MouseRoundPanel();
	private Container container = getContentPane();	
	private int windowWidth = 120;
	private int windowHeight = 120;
	private boolean startRnd = false;
	private MouseRoundThread rndThread;
	
	public static void main(String[] args){
		MouseRound mouseRound = new MouseRound();
		mouseRound.startRound();
	}
	public MouseRound(){
		container.add(mouseRoundPanel);	
		this.setUndecorated(true);	// 设置窗体样式
	    this.setResizable(false);
	    this.setVisible(false);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setSize(windowWidth,windowHeight);
	    this.setLocation(java.awt.MouseInfo.getPointerInfo().getLocation().x-60, 
				 		 java.awt.MouseInfo.getPointerInfo().getLocation().y-60);
		this.setAlwaysOnTop(true);//设置窗体总在最顶层
		Color background = new Color(0,0,0,0);	//透明背景
		this.setBackground(background);
		this.setFocusableWindowState(false);
	}
	public boolean startRound(){
		if(startRnd)
			return false;
		startRnd = true;
		this.setVisible(true);
		this.validate();
		if(rndThread == null)
			rndThread = new MouseRoundThread();
		else
			rndThread.setFlag(true);
        new Thread(rndThread).start();
        return true;		
	}
	public boolean endRound(){
		if(!startRnd)
			return false;
		startRnd = false;
		this.setVisible(false);
		if(rndThread == null){
            return false;
        }
		rndThread.setFlag(false);
		return true;		
	}
	public void setRadius(int radius){
		mouseRoundPanel.setRadius(radius);
	}
	public void update(){
		mouseRoundPanel.update();
		this.setLocation(java.awt.MouseInfo.getPointerInfo().getLocation().x-60, 
		 		 		 java.awt.MouseInfo.getPointerInfo().getLocation().y-60);
	}
	public void delay(int time){
		mouseRoundPanel.delay(time);
	}
	class MouseRoundThread implements Runnable{
        public void run(){
        	while(this.flag){
                update();
                try{
                    Thread.sleep(10);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        } 
        public void setFlag(boolean flag){
            this.flag = flag;
        } 
        private boolean flag = true;
    }
}

class MouseRoundPanel extends JPanel
{
    private Robot robot;
    private Color color = new Color(200,0,200);
	private float stroke = 5f;
	private int radius=50;
	private int minRadius=7;
	private int maxRadius=50;
	private int xoffset=35;
	private int yoffset=35;
	private int x=java.awt.MouseInfo.getPointerInfo().getLocation().x;
	private int y=java.awt.MouseInfo.getPointerInfo().getLocation().y;	
	
    public MouseRoundPanel(){
	    try{
	        robot = new Robot();
	    }catch (AWTException e){}
    }
    public void paintComponent(Graphics g){
    	Graphics2D graphics = (Graphics2D) g;
	    super.paintComponent((Graphics2D)graphics);
	    this.setOpaque(false);
	    graphics.setColor(color);	    
	    graphics.setStroke(new BasicStroke(stroke));
	    graphics.drawOval(60-radius/2, 60-radius/2, radius, radius);
    }
    public int setRadius(int r){
    	if(r<=maxRadius && r>=minRadius)
    		radius = r;  
    	repaint();
    	return radius;
    }
    public void update(){
    	x=java.awt.MouseInfo.getPointerInfo().getLocation().x;
    	y=java.awt.MouseInfo.getPointerInfo().getLocation().y;
    	repaint();
    }
    public void delay(int time){
		robot.delay(time);
	}
}