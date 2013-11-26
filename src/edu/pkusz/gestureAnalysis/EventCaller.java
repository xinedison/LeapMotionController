package edu.pkusz.gestureAnalysis;

import java.awt.event.KeyEvent;

import edu.pkusz.PCEvent.PCControler;

public class EventCaller {
	private int mode;
	private int x;
	private int y;
	private int num;
	private double dnum;
	private PCControler pcControler = new PCControler();
	
	public boolean callEvent(int mode){
		this.mode = mode;
		switch(mode){
		case 1:
			pcControler.keyPress(KeyEvent.VK_C);break;
		case 3:
			pcControler.keyPress(KeyEvent.VK_3);break;
		case 4:
			pcControler.keyPress(KeyEvent.VK_4);break;
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
