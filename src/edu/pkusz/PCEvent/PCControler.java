package edu.pkusz.PCEvent;

import java.awt.event.KeyEvent;

public class PCControler {
	private KeyBoardEvent keyBoardEvent = new KeyBoardEvent();
	private MouseEvent mouseEvent = new MouseEvent();
	private MagnifierEvent magnifierEvent = new MagnifierEvent();
	private DrawFigureEvent drawFigureEvent = new DrawFigureEvent();
	private MouseRound mouseRound = new MouseRound();
	private MusicEvent musicEvent = new MusicEvent();
	private int delaytime = 10;
	private int mode = 0;	//mode = 0 ���ģʽ   mode = 1 �Ŵ�ģʽ   mode = 2 ��ͼģʽ mode = 3����ģʽ
	private boolean drawOver = true;
	private int  lastX=0;
	private int  lastY = 0;
	
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
						 			  java.awt.MouseInfo.getPointerInfo().getLocation().y,1,0);
			}
			pcControler.endFigure();
			Thread.sleep(2000);
			pcControler.endShow();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public PCControler(){
	}
	/************************************* 
	* 			Control PPT				 * 
	*************************************/
	public boolean keyPress(int key){	//�������£��κ�ģʽ��Ч
		keyBoardEvent.keyDown(key);
		keyBoardEvent.delay(delaytime);
		keyBoardEvent.keyUp(key);
		return true;
	}
	public boolean startShow(){			//��ʼ��ӳ���κ�ģʽ��Ч
		keyBoardEvent.keyDown(KeyEvent.VK_SHIFT);
		keyBoardEvent.delay(delaytime);
		keyBoardEvent.keyDown(KeyEvent.VK_F5);
		keyBoardEvent.delay(delaytime);
		keyBoardEvent.keyUp(KeyEvent.VK_F5);
		keyBoardEvent.delay(delaytime);
		keyBoardEvent.keyUp(KeyEvent.VK_SHIFT);
		return true;
	}
	public boolean endShow(){			//������ӳ���κ�ģʽ��Ч
		keyBoardEvent.keyDown(KeyEvent.VK_ESCAPE);
		keyBoardEvent.delay(delaytime);
		keyBoardEvent.keyUp(KeyEvent.VK_ESCAPE);		
		return true;
	}
	public boolean pageDown(){			//����һҳ���κ�ģʽ��Ч
		keyBoardEvent.keyDown(KeyEvent.VK_PAGE_DOWN);
		keyBoardEvent.delay(delaytime);
		keyBoardEvent.keyUp(KeyEvent.VK_PAGE_DOWN);		
		return true;
	}	
	public boolean pageUp(){			//����һҳ���κ�ģʽ��Ч
		keyBoardEvent.keyDown(KeyEvent.VK_PAGE_UP);
		keyBoardEvent.delay(delaytime);
		keyBoardEvent.keyUp(KeyEvent.VK_PAGE_UP);		
		return true;
	}
	
	/************************************* 
	* 			Control Mouse			 * 
	*************************************/
	public boolean startMouse(){		//��ʼ������ģʽ��mode=0 ����Ĭ��Ϊ������ģʽ�����û��endMouse
		if(mode == 0) return false;
		mode = 0;
		return true;
	}
	public boolean mouseMove(int vx,int vy){	//��ʸ����������ƶ�
		if(mode != 0) return false;
		mouseEvent.move(vx, vy);
		mouseRound.startRound();
		return true;
	}
	public boolean mouseMoveTo(int x,int y){	//�������������ƶ�
		if(mode != 0) return false;
		mouseEvent.moveTo(x, y);		
		return true;
	}
	public boolean rightClick(){		//�Ҽ�
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
	public boolean leftClick(){			//���
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
	public boolean wheel(int w){		//���֣�����Ϊ���£�����Ϊ����
		if(mode != 0) return false;
		mouseEvent.wheel(w);
		return true;
	}
	public boolean setRound(int z){
		mouseRound.setRadius(z);
		return true;
	}
	
	/************************************* 
	* 			Enlarge Screen			 * 
	*************************************/
	public boolean startMagnifier(){	//��ʼ�Ŵ�ģʽ��mode=1 
//		if(mode == 1 ) return false;
		if(mode == 0){
			mode = 1;
			magnifierEvent.startMagnifier();
			mouseRound.endRound();
			return true;
		}
		return false;
	}
	public boolean endMagnifier(){		//�����Ŵ�ģʽ����ʼ������ģʽ��mode=0 
		if(mode != 1) return false;
		mode = 0;
		magnifierEvent.endMagnifier();
		mouseRound.startRound();
		return true;
	}
	public boolean moveMagnifier(int vx,int vy){	//��ʸ�����зŴ��ƶ�
		if(mode != 1) return false;
		magnifierEvent.moveMagnifier(vx, vy);
		return true;
	}
	public boolean resizeMagnifier(int size){		//�ı�Ŵ󾵴��ڴ�С������Ϊ��󣬸���Ϊ��С
		if(mode != 1) return false;
		magnifierEvent.resize(size);
		return true;
	}
	public boolean zoomMagnifier(double zoom){			//�ı�Ŵ����Ŵ�С������Ϊ�Ŵ󣬸���Ϊ��С
		if(mode != 1) return false;
		magnifierEvent.zoom(zoom);
		return true;
	}

	/************************************* 
	* 			Draw Figure				 * 
	*************************************/	
	public boolean startFigure(){		//��ʼ��ͼģʽ��mode=2 
//		if(mode == 2) return false;
		if(mode == 0){
			mode = 2;
			drawFigureEvent.startFigure();
			return true;
		}
		return false;
	}
	public boolean endFigure(){			//������ͼģʽ����ʼ������ģʽ��mode=0 
		if(mode != 2) return false;
		mode = 0;
		drawFigureEvent.endFigure();
		return true;
	}
	public boolean drawPoint(int x,int y,int drawState,int drawMode){	//��x,y���괦���� 
		if(mode != 2) return false;
		
		if(drawOver){
			lastX = java.awt.MouseInfo.getPointerInfo().getLocation().x;
			lastY = java.awt.MouseInfo.getPointerInfo().getLocation().y;
		}
		drawFigureEvent.mouseMove(x, y);
		if(drawState==1){
			if(drawMode == 0)
				drawFigureEvent.drawPoint(lastX,lastY);
			else if(drawMode==1){
				if(drawOver){
					drawFigureEvent.drawLine(lastX,lastY);
					drawOver=false;
				}
				else
					drawFigureEvent.setLine(lastX,lastY);
			}
			else if(drawMode==2){
				if(drawOver){
					drawFigureEvent.drawRec(lastX,lastY);
					drawOver=false;
				}
				else
					drawFigureEvent.setRec(lastX,lastY);
			}
			else if(drawMode==3){
				if(drawOver){
					drawFigureEvent.drawOval(lastX,lastY);
					drawOver=false;
				}
				else
					drawFigureEvent.setOval(lastX,lastY);
			}
		}
		else{
			//if(!drawOver)
				drawOver = true;
		}
		return true;
	}
	
	/************************************* 
	* 				Music				 * 
	*************************************/	
	public boolean startMusic(){		//��ʼ����ģʽ��mode=3 
//		if(mode == 2) return false;
		if(mode == 0){
			mode = 3;
			musicEvent.startMusic();
			return true;
		}
		return false;
	}
	public boolean endMusic(){			//��������ģʽ����ʼ������ģʽ��mode=0 
		if(mode != 3) return false;
		mode = 0;
		musicEvent.endMusic();
		return true;
	}
	public boolean startBackground(){
		if(mode != 3) return false;
		musicEvent.startBackground();
		return true;
	}
	public boolean endBackground(){
		if(mode != 3) return false;
		musicEvent.endBackground();
		return true;
	}
	public boolean setMusicVolume(double volume){
		if(mode != 3) return false;
		musicEvent.setVolume(volume);
		return true;
	}
	public boolean musicVolumeUp(){
		if(mode != 3) return false;
		musicEvent.setVolumeUp();
		return true;
	}
	public boolean musicVolumeDown(){
		if(mode != 3) return false;
		musicEvent.setVolumeDown();
		return true;
	}
}
