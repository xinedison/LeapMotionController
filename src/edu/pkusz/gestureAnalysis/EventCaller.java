package edu.pkusz.gestureAnalysis;

import java.awt.event.KeyEvent;

import edu.pkusz.PCEvent.PCControler;

public class EventCaller {
	private int mode;
	private int state;
	public int drawState;
	private int x;
	private int y;
	private int num;
	private double dnum;
	private PCControler pcControler = new PCControler();
	public boolean callMouseState(int state){
		this.state = state;
		switch(state){
		case 0:
			break;
		case 1:
			pcControler.leftDown();
			break;
		case 2:
			pcControler.leftUp();
			break;
		}
		return true;
	}
	public boolean callEvent(int mode){
		this.mode = mode;
		switch(mode){
		case 1:
			pcControler.startShow();
			System.out.println("start show");
			break;
		case 2:
			pcControler.endShow();
			System.out.println("end show");
			break;
		case 3:
			pcControler.pageDown();
			System.out.println("pagedown");
			break;
		case 4:
			pcControler.pageUp();
			System.out.println("pageup");
			break;
		case 6:
//			System.out.println(x+"\t"+y);
			pcControler.mouseMove(x/20,- y/20);
			pcControler.drawPoint(x/20,- y/20,drawState);
			break;
		case 7:
			pcControler.rightClick();
			break;
		case 8:
			pcControler.leftClick();
			break;
		case 15:
			pcControler.startFigure();
			System.out.println("start figure");
			break;
		case 16:
			pcControler.endFigure();
			System.out.println("end figure");
			break;
		default :break;
		}
		return true;
	}
	public void setParam(int x,int y){
		this.x = x;
		this.y = y;
	}
	public void setParam(int num){
		
		
	}
	public void setParam(double dnum){
		
		
	}
}
