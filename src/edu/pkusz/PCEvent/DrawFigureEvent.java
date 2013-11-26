package edu.pkusz.PCEvent;
import java.awt.*;

import javax.swing.*;

import edu.pkusz.PCEvent.MagnifierEvent.MagnifierThread;

import java.util.Vector;

public class DrawFigureEvent extends JFrame{
	private FigurePanel figurePanel = new FigurePanel();
	private Container container = getContentPane();	
	private Color color = new Color(255,0,0);
	private float stroke = 3f;
	private int windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
	private int windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
//	private int windowWidth = 500;
//	private int windowHeight = 300;
	private boolean startFig = false;
	private DrawThread drawThread;
	private Robot robot;
	
	public static void main(String[] args){
		DrawFigureEvent drawFigure = new DrawFigureEvent();
		drawFigure.delay(500);
		drawFigure.startFigure();
//		for(int i=0;i<1500;i++){
//			drawFigure.drawPoint(java.awt.MouseInfo.getPointerInfo().getLocation().x, 
//					 java.awt.MouseInfo.getPointerInfo().getLocation().y);
//			drawFigure.delay(1);
//		}
		//drawFigure.endFigure();
	}
	public DrawFigureEvent(){
		container.add(figurePanel);	
		this.setUndecorated(true);	// 设置窗体样式
	    this.setResizable(false);
	    this.setVisible(false);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setSize(windowWidth,windowHeight);
		this.setAlwaysOnTop(true);//设置窗体总在最顶层
		Color background = new Color(0,0,0,0);	//透明背景
		this.setBackground(background);
		this.setFocusableWindowState(false);
		try{
			robot = new Robot(); 
		}catch (AWTException e) {
			e.printStackTrace();
		}
	}
	public boolean startFigure(){
		if(startFig)
			return false;
		startFig = true;
		figurePanel.clear();
		delay(50);
		this.drawPoint(0,0);
		this.setVisible(true);
//		if(drawThread == null)
//			drawThread = new DrawThread();
//		else
//			drawThread.setFlag(true);
//		 new Thread(drawThread).start();
		return true;		
	}
	public boolean endFigure(){
		if(!startFig)
			return false;
		startFig = false;
		figurePanel.clear();
		delay(30);
		this.setVisible(false);
//		drawThread.setFlag(false);
		return true;		
	}
	public boolean mouseMove(int Vx,int Vy){	//按速度往某方向移动
		robot.mouseMove(java.awt.MouseInfo.getPointerInfo().getLocation().x+Vx, 
						java.awt.MouseInfo.getPointerInfo().getLocation().y+Vy);
		return true;
	}
	public boolean drawPoint(){
		if(!startFig)
			return false;
//		figurePanel.drawRec(x, y, x+(int)stroke, y+(int)stroke,color,stroke);
		figurePanel.drawRec(java.awt.MouseInfo.getPointerInfo().getLocation().x,
											java.awt.MouseInfo.getPointerInfo().getLocation().y,
											(int)stroke, (int)stroke,color,stroke);
		validate();    // 更新所有子控件
		return true;
	}
	public boolean drawPoint(int x,int y){
		if(!startFig)
			return false;
//		figurePanel.drawRec(x, y, x+(int)stroke, y+(int)stroke,color,stroke);
		figurePanel.drawRec(x, y, (int)stroke, (int)stroke,color,stroke);
		validate();    // 更新所有子控件
		return true;
	}
	public boolean drawLine(int x1,int y1, int x2,int y2){
		if(!startFig)
			return false;
		figurePanel.drawLine(x1, y1, x2, y2,color,stroke);
		validate();    // 更新所有子控件
		return true;
	}
	public boolean drawRec(int x1,int y1, int x2,int y2){
		if(!startFig)
			return false;
		figurePanel.drawRec(x1, y1, x2, y2,color,stroke);
		validate();    // 更新所有子控件
		return true;
	}
	public boolean drawOval(int x,int y, int width,int height){
		if(!startFig)
			return false;
		figurePanel.drawOval(x, y, width, height,color,stroke);
		validate();    // 更新所有子控件
		return true;
	}
	public void delay(int time){
		figurePanel.delay(time);
	}

	class DrawThread implements Runnable{
        public void run(){
            while(this.flag){
                try{
                	drawPoint(java.awt.MouseInfo.getPointerInfo().getLocation().x, 
   					 java.awt.MouseInfo.getPointerInfo().getLocation().y);
                	delay(1);
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

class FigurePanel extends JPanel
{
    private Robot robot;
    private Vector<FigureLineInfo> vLine=new Vector<FigureLineInfo>(); 	//直线
    private Vector<FigureRecInfo> vRec=new Vector<FigureRecInfo>(); 	//矩形
    private Vector<FigureOvalInfo> vOval=new Vector<FigureOvalInfo>(); 	//椭圆

    public FigurePanel(){
	    try{
	        robot = new Robot();
	    }catch (AWTException e){}
    }
    public void paintComponent(Graphics g){
    	Graphics2D graphics = (Graphics2D) g;
	    super.paintComponent((Graphics2D)graphics);
	    this.setOpaque(false);
    	FigureLineInfo figureLine;
    	FigureRecInfo figureRec;
    	FigureOvalInfo figureOval;
	    for(int i=0;i<vLine.size();i++){
	    	figureLine = vLine.get(i);
		    graphics.setColor(figureLine.color);	    
		    graphics.setStroke(new BasicStroke(figureLine.stroke));
		    graphics.drawLine(figureLine.x1, figureLine.y1, figureLine.x2, figureLine.y2);  	
	    }
	    for(int i=0;i<vRec.size();i++){
	    	figureRec = vRec.get(i);
		    graphics.setColor(figureRec.color);	    
		    graphics.setStroke(new BasicStroke(figureRec.stroke));
		    graphics.drawRect(figureRec.x, figureRec.y, figureRec.width, figureRec.height);  	
	    }	
	    for(int i=0;i<vOval.size();i++){
	    	figureOval = vOval.get(i);
		    graphics.setColor(figureOval.color);	    
		    graphics.setStroke(new BasicStroke(figureOval.stroke));
		    graphics.drawOval(figureOval.x, figureOval.y, figureOval.width, figureOval.height);  	
	    }
    }
    public void drawLine(int x1,int y1, int x2,int y2,Color color,float stroke){
    	vLine.add(new FigureLineInfo(x1,y1,x2,y2,color,stroke));
    	repaint();	//重画控件
    }
    public void drawRec(int x,int y, int width,int height,Color color,float stroke){
    	vRec.add(new FigureRecInfo(x,y,width,height,color,stroke));
    	repaint();	//重画控件
    }
    public void drawOval(int x,int y, int width,int height,Color color,float stroke){
    	vOval.add(new FigureOvalInfo(x,y,width,height,color,stroke));
    	repaint();	//重画控件
    }
    public void delay(int time){
		robot.delay(time);
	}
    public void clear(){
    	vRec.clear();
    	vLine.clear();
    	vOval.clear();
    	repaint();
    }
}

class FigureLineInfo{
	public FigureLineInfo(int x1,int y1, int x2,int y2,Color color,float stroke){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.color = color;
		this.stroke = stroke;
	}
	public int x1=0;
	public int y1=0;
	public int x2=0;
	public int y2=0;
	public Color color=new Color(255,0,0);
	public float stroke=2f;	//线条粗细
}

class FigureRecInfo{
	public FigureRecInfo(int x,int y, int width,int height,Color color,float stroke){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		this.stroke = stroke;
	}
	public int x=0;
	public int y=0;
	public int width=0;
	public int height=0;
	public Color color=new Color(255,0,0);
	public float stroke=2f;	//线条粗细
}

class FigureOvalInfo{
	public FigureOvalInfo(int x,int y, int width,int height,Color color,float stroke){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		this.stroke = stroke;
	}
	public int x=0;
	public int y=0;
	public int width=0;
	public int height=0;
	public Color color=new Color(255,0,0);
	public float stroke=2f;	//线条粗细
}

