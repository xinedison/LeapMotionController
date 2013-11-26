package edu.pkusz.PCEvent;

import java.awt.event.KeyEvent;

public class PCControler {
	private KeyBoardEvent keyBoardEvent = new KeyBoardEvent();
	private MouseEvent mouseEvent = new MouseEvent();
	private MagnifierEvent magnifierEvent = new MagnifierEvent();
	private DrawFigureEvent drawFigureEvent = new DrawFigureEvent();
	private int delaytime = 10;
	private int mode = 0;	//mode = 0 鼠标模式   mode = 1 放大镜模式   mode = 2 画图模式
	
	public static void main(String[] args){
		PCControler pcControler = new PCControler();
		try {
			
			//test Control PPT
			Thread.sleep(5000);
			pcControler.startShow();
			Thread.sleep(1000);
			pcControler.pageDown();
			Thread.sleep(1000);
			pcControler.pageDown();
			Thread.sleep(1000);
			pcControler.pageUp();
			Thread.sleep(1000);
			pcControler.pageUp();
			Thread.sleep(1000);
			pcControler.endShow();
			
			//test Mouse
			Thread.sleep(1000);
			pcControler.rightClick();
			Thread.sleep(1000);
			for(int i =0;i<15;i++){
				Thread.sleep(100);
				pcControler.mouseMove(-5, 5);
			}
			pcControler.leftClick();
			Thread.sleep(1000);
			pcControler.wheel(1);
			Thread.sleep(1000);
			pcControler.wheel(1);
			Thread.sleep(1000);
			pcControler.wheel(-1);
			Thread.sleep(1000);
			pcControler.wheel(-1);
			
			
			
			
			
			//test Magnifier
			Thread.sleep(5000);
			pcControler.startShow();
			Thread.sleep(1000);
			pcControler.startMagnifier();
			for(int i =0;i<15;i++){
				Thread.sleep(100);
				pcControler.moveMagnifier(5, -5);
			}
			Thread.sleep(1000);
			pcControler.resizeMagnifier(50);			
			Thread.sleep(1000);
			pcControler.resizeMagnifier(50);			
			for(int i =0;i<15;i++){
				Thread.sleep(100);
				pcControler.moveMagnifier(-5, 5);
			}
			Thread.sleep(1000);
			pcControler.resizeMagnifier(-50);			
			Thread.sleep(1000);
			pcControler.resizeMagnifier(-50);			
			Thread.sleep(1000);
			pcControler.zoomMagnifier(1);			
			Thread.sleep(1000);
			pcControler.zoomMagnifier(1);			
			for(int i =0;i<15;i++){
				Thread.sleep(100);
				pcControler.moveMagnifier(5, -5);
			}
			Thread.sleep(1000);
			pcControler.zoomMagnifier(-1);			
			Thread.sleep(1000);
			pcControler.zoomMagnifier(-1);
			Thread.sleep(2000);
			pcControler.endMagnifier();
			Thread.sleep(1000);
			pcControler.endShow();
			
			
			//test DrawFigure
			Thread.sleep(5000);
			pcControler.startShow();
			Thread.sleep(1000);
			pcControler.startFigure();
			for(int i =0;i<1500;i++){
				Thread.sleep(10);
				pcControler.drawPoint(java.awt.MouseInfo.getPointerInfo().getLocation().x, 
						 			  java.awt.MouseInfo.getPointerInfo().getLocation().y,1);
			}
			pcControler.endFigure();
			Thread.sleep(2000);
			pcControler.endShow();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/************************************* 
	* 			Control PPT				 * 
	*************************************/
	public boolean keyPress(int key){	//按键按下，任何模式有效
		keyBoardEvent.keyDown(key);
		keyBoardEvent.delay(delaytime);
		keyBoardEvent.keyUp(key);
		return true;
	}
	public boolean startShow(){			//开始放映，任何模式有效
		keyBoardEvent.keyDown(KeyEvent.VK_SHIFT);
		keyBoardEvent.delay(delaytime);
		keyBoardEvent.keyDown(KeyEvent.VK_F5);
		keyBoardEvent.delay(delaytime);
		keyBoardEvent.keyUp(KeyEvent.VK_F5);
		keyBoardEvent.delay(delaytime);
		keyBoardEvent.keyUp(KeyEvent.VK_SHIFT);
		return true;
	}
	public boolean endShow(){			//结束放映，任何模式有效
		keyBoardEvent.keyDown(KeyEvent.VK_ESCAPE);
		keyBoardEvent.delay(delaytime);
		keyBoardEvent.keyUp(KeyEvent.VK_ESCAPE);		
		return true;
	}
	public boolean pageDown(){			//翻下一页，任何模式有效
		keyBoardEvent.keyDown(KeyEvent.VK_PAGE_DOWN);
		keyBoardEvent.delay(delaytime);
		keyBoardEvent.keyUp(KeyEvent.VK_PAGE_DOWN);		
		return true;
	}	
	public boolean pageUp(){			//翻上一页，任何模式有效
		keyBoardEvent.keyDown(KeyEvent.VK_PAGE_UP);
		keyBoardEvent.delay(delaytime);
		keyBoardEvent.keyUp(KeyEvent.VK_PAGE_UP);		
		return true;
	}
	
	/************************************* 
	* 			Control Mouse			 * 
	*************************************/
	public boolean startMouse(){		//开始鼠标控制模式，mode=0 由于默认为鼠标控制模式，因此没有endMouse
		if(mode == 0) return false;
		mode = 0;
		return true;
	}
	public boolean mouseMove(int vx,int vy){	//按矢量进行鼠标移动
		if(mode != 0) return false;
		mouseEvent.move(vx, vy);
		return true;
	}
	public boolean mouseMoveTo(int x,int y){	//按坐标进行鼠标移动
		if(mode != 0) return false;
		mouseEvent.moveTo(x, y);
		return true;
	}
	public boolean rightClick(){		//右键
		if(mode != 0) return false;
		mouseEvent.rightDown();
		mouseEvent.delay(delaytime);
		mouseEvent.rightUp();
		return true;
	}
	public boolean rightDown(){
		if(mode != 0) return false;
		mouseEvent.rightDown();
		return true;
	}
	public boolean rightUp(){
		if(mode != 0) return false;
		mouseEvent.rightUp();
		return true;
	}
	public boolean leftClick(){			//左键
		if(mode != 0) return false;
		mouseEvent.leftDown();
		mouseEvent.delay(delaytime);
		mouseEvent.leftUp();
		return true;
	}
	public boolean leftDown(){
		if(mode != 0) return false;
		mouseEvent.leftDown();
		return true;
	}	
	public boolean leftUp(){
		if(mode != 0) return false;
		mouseEvent.leftUp();
		return true;
	}	
	public boolean wheel(int w){		//滚轮，正数为向下，负数为向上
		if(mode != 0) return false;
		mouseEvent.wheel(w);
		return true;
	}
	
	/************************************* 
	* 			Enlarge Screen			 * 
	*************************************/
	public boolean startMagnifier(){	//开始放大镜模式，mode=1 
		if(mode == 1) return false;
		mode = 1;
		magnifierEvent.startMagnifier();
		return true;
	}
	public boolean endMagnifier(){		//结束放大镜模式，开始鼠标控制模式，mode=0 
		if(mode != 1) return false;
		mode = 0;
		magnifierEvent.endMagnifier();
		return true;
	}
	public boolean moveMagnifier(int vx,int vy){	//按矢量进行放大镜移动
		if(mode != 1) return false;
		magnifierEvent.moveMagnifier(vx, vy);
		return true;
	}
	public boolean resizeMagnifier(int size){		//改变放大镜窗口大小，正数为变大，负数为变小
		if(mode != 1) return false;
		magnifierEvent.resize(size);
		return true;
	}
	public boolean zoomMagnifier(double zoom){			//改变放大镜缩放大小，正数为放大，负数为缩小
		if(mode != 1) return false;
		magnifierEvent.zoom(zoom);
		return true;
	}

	/************************************* 
	* 			Draw Figure				 * 
	*************************************/	
	public boolean startFigure(){		//开始画图模式，mode=2 
		if(mode == 2) return false;
		mode = 2;
		drawFigureEvent.startFigure();
		return true;
	}
	public boolean endFigure(){			//结束画图模式，开始鼠标控制模式，mode=0 
		if(mode != 2) return false;
		mode = 0;
		drawFigureEvent.endFigure();
		return true;
	}
	public boolean drawPoint(int x,int y,int drawState){	//在x,y坐标处画点 
		if(mode != 2) return false;
		drawFigureEvent.mouseMove(x, y);
		if(drawState==1)
			drawFigureEvent.drawPoint();
		return true;
	}
	
	
}
