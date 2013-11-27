package edu.pkusz.PCEvent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.*;


public class MagnifierEvent extends JFrame{
	private Container container = getContentPane();
	private static int magnifierSize = 200;
	private static int maxSize = 600;
	private static int minSize = 100;
	private static double magnifierZoom = 2;
	private static double maxZoom = 6;
	private static double minZoom = 1;
	private MagnifierPanel magnifierPanel = new MagnifierPanel(magnifierSize);
	private boolean startMag = false;
	private MagnifierThread magThread;
	
	public static void main(String[] args){
    	MagnifierEvent magnifier = new MagnifierEvent(); 	
    	magnifier.delay(1000);
    	magnifier.startMagnifier();
		for(int i =0;i<500;i++){
			magnifier.moveMagnifier(0, 0);
			magnifier.delay(30);
		}
		magnifier.endMagnifier();
	}
	public MagnifierEvent(){
	    setUndecorated(true); // �����Ե
	    setResizable(false);
	    container.add(magnifierPanel);	    
	    setSize(magnifierSize);
	    moveMagnifier(0,0);
	    setZoom(magnifierZoom);
	    this.setVisible(false);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setAlwaysOnTop(true);//���ô����������
		this.setFocusableWindowState(false);
	}
	public boolean startMagnifier(){
		if(startMag)
			return false;
		startMag = true;
		update();
		this.setZoom(magnifierZoom);
		this.setSize(magnifierSize);
		this.setVisible(true);
		this.moveMagnifier(0, 0);
		if(magThread == null){
			magThread = new MagnifierThread();
		}
		else
			magThread.setFlag(true);
        new Thread(magThread).start();
		return true;		
	}
	public boolean endMagnifier(){
		if(!startMag)
			return false;
		startMag = false;
		if(magThread == null){
            return false;
        }
		magThread.setFlag(false);
		this.setVisible(false);
		this.delay(500);
		this.setVisible(false);
		return true;		
	}
	public void update(){
		if(magnifierPanel.judgeDiff()){
			this.setVisible(false);
			magnifierPanel.delay(10);
			magnifierPanel.recaptrue();
			this.setVisible(true);
			validate();
		}
	}
	public boolean moveMagnifier(int Vx,int Vy){
		if(!startMag)
			return false;
		
		magnifierPanel.myMove(Vx,Vy);
		this.setLocation(java.awt.MouseInfo.getPointerInfo().getLocation().x, 
						 java.awt.MouseInfo.getPointerInfo().getLocation().y);
		magnifierPanel.setMagnifierLocation(java.awt.MouseInfo.getPointerInfo().getLocation().x, 
				 							java.awt.MouseInfo.getPointerInfo().getLocation().y);
		validate();		// ���������ӿؼ�
		return true;
	}
	public boolean setSize(int magnifierSize){
		if(!startMag)
			return false;
		magnifierPanel.setMagnifierSize(magnifierSize + 100);
		this.setSize(magnifierPanel.getWidth(), magnifierPanel.getHeight());
		validate();    // ���������ӿؼ�
		return true;
	}
	public boolean setZoom(double scale){
		if(!startMag)
			return false;
		magnifierPanel.setMagnifierScale(scale);
		validate();
		return true;
	}
	public boolean resize(int changedSize){
		if(!startMag)
			return false;
		magnifierSize+=changedSize;
		if(magnifierSize>maxSize || magnifierSize<minSize)
			magnifierSize-=changedSize;
		magnifierPanel.setMagnifierSize(magnifierSize + 100);
		this.setSize(magnifierPanel.getWidth(), magnifierPanel.getHeight());
		validate();    // ���������ӿؼ�
		return true;
	}
	public boolean zoom(double changedScale){
		if(!startMag)
			return false;
		magnifierZoom+=changedScale;
		if(magnifierZoom>maxZoom || magnifierZoom<minZoom)
			magnifierZoom-=changedScale;
		magnifierPanel.setMagnifierScale(magnifierZoom);
		validate();
		return true;
	}
	public void delay(int time){
		magnifierPanel.delay(time);
	}
	class MagnifierThread implements Runnable{
        public void run(){
            while(this.flag){
                update();
                try{
                    Thread.sleep(1000);
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
class MagnifierPanel extends JPanel
{
    private Image screenImage = null;
    private Image tempscreenImage = null;
    private int magnifierSize;
    private int locationX=java.awt.MouseInfo.getPointerInfo().getLocation().x;
    private int locationY=java.awt.MouseInfo.getPointerInfo().getLocation().y;
    private double width = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
    private double height = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
    private double magnifierScale=2;
    private boolean locked = false;
    private Robot robot;

    public MagnifierPanel(int magnifierSize){
    	
	    try{
	        robot = new Robot();
	    }
	    catch (AWTException e){}
//	    // ����Ļ
//	    screenImage = robot.createScreenCapture(new Rectangle(0, 0, 
//	    		Toolkit.getDefaultToolkit().getScreenSize().width, 
//	    		Toolkit.getDefaultToolkit().getScreenSize().height));
	    this.magnifierSize = magnifierSize;
	    width = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
	    height = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
	    height = magnifierSize*(height/width);
	    width = magnifierSize;
    }

    public void setMagnifierLocation(int locationX, int locationY){
    	while(locked){
    		delay(50);
    	}
	    this.locationX = locationX;
    	this.locationY = locationY;
    	repaint();        // ע���ػ��ؼ�
    }
    public int getWidth(){return (int)width;}
    public int getHeight(){return (int)height;}
    public void setMagnifierScale(double scale){
    	while(locked){
    		delay(50);
    	}
	    this.magnifierScale = scale;
    	repaint();        // ע���ػ��ؼ�
    }
    public void setMagnifierSize(int magnifierSize){
    	while(locked){
    		delay(50);
    	}
    	this.magnifierSize = magnifierSize;
	    width = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
	    height = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
	    height = magnifierSize*(height/width);
	    width = magnifierSize;
	    System.out.println(width);
	    System.out.println(height);
    }
    public void paintComponent(Graphics g){
	    super.paintComponent((Graphics2D) g);
	    // �ؼ��������
	    double scaleSizeX = width/magnifierScale;
	    double scaleSizeY = height/magnifierScale;
	    int startX = (int)((width-scaleSizeX)/2);
	    int startY = (int)((height-scaleSizeY)/2);
	    int endX = (int)(startX+scaleSizeX);
	    int endY = (int)(startY+scaleSizeY);	    
	    g.drawImage(screenImage,                // Ҫ����ͼƬ
	    			0,							// Ŀ����εĵ�һ���ǵ�x����     
	    			0,							// Ŀ����εĵ�һ���ǵ�y����
	    			(int)width,              // Ŀ����εĵڶ����ǵ�x����
			        (int)height,              // Ŀ����εĵڶ����ǵ�y����
			        locationX + startX,   		// Դ���εĵ�һ���ǵ�x����
			        locationY + startY,    		// Դ���εĵ�һ���ǵ�y����
			        locationX + endX,			// Դ���εĵڶ����ǵ�x����
			        locationY + endY,			// Դ���εĵڶ����ǵ�y����
			        this);
	    g.drawRect(0, 0, (int)width-1, (int)height-1);
    }
	public boolean myMove(int Vx,int Vy){	//���ٶ���ĳ�����ƶ�
    	while(locked){
    		delay(50);
    	}
		robot.mouseMove(java.awt.MouseInfo.getPointerInfo().getLocation().x+Vx, 
						java.awt.MouseInfo.getPointerInfo().getLocation().y+Vy);
		return true;
	}
	public boolean recaptrue(){
//		locked = true;
		screenImage = robot.createScreenCapture(new Rectangle(0, 0, 
	    		Toolkit.getDefaultToolkit().getScreenSize().width, 
	    		Toolkit.getDefaultToolkit().getScreenSize().height));
// 	    locked = false;
 	    return true;
	}
	public void delay(int time){
		robot.delay(time);
	}
	public boolean judgeDiff(){
//		locked = true;
		if(screenImage == null) return true;
		tempscreenImage = robot.createScreenCapture(new Rectangle(0, 0, 
	    		Toolkit.getDefaultToolkit().getScreenSize().width, 
	    		Toolkit.getDefaultToolkit().getScreenSize().height));
		BufferedImage img1 = (BufferedImage)screenImage;
		BufferedImage img2 = (BufferedImage)tempscreenImage;
		for(int i=0;i<Toolkit.getDefaultToolkit().getScreenSize().width;i+=50)
			for(int j=0;j<Toolkit.getDefaultToolkit().getScreenSize().height-50;j+=50){
				if(i>locationX-5 && i<locationX+width+5 && j>locationY-5 && j<locationY+height+5)
					continue;
				if(img1.getRGB(i, j)!=img2.getRGB(i, j))
					return true;
			}		
//		locked = false;
		return false;
	}
}
