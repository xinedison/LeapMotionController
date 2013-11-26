package edu.pkusz.PCEvent;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class MouseEvent {
	public static void main(String[] args){
		System.out.println("key");
		MouseEvent mouse = new MouseEvent();
		for(int i =0;i<15;i++){
			mouse.delay(100);
			mouse.move(-10, 1);
//			mouse.leftDown();
//			mouse.leftUp();
			mouse.wheel(-1);
		}
	}
	public MouseEvent(){
		try{
		robot = new Robot(); 
		}catch (AWTException e) {
			e.printStackTrace();
		}		
	}
	public boolean moveTo(int x,int y){	//�ƶ���ĳ����
		robot.mouseMove(x, y);
		return true;
	}
	public boolean move(int Vx,int Vy){	//���ٶ���ĳ�����ƶ�
		robot.mouseMove(java.awt.MouseInfo.getPointerInfo().getLocation().x+Vx, 
						java.awt.MouseInfo.getPointerInfo().getLocation().y+Vy);
		return true;
	}
	public boolean rightDown(){
		robot.mousePress(KeyEvent.BUTTON3_MASK);
		return true;
	}
	public boolean rightUp(){
		robot.mouseRelease(KeyEvent.BUTTON3_MASK);
		return true;
	}
	public boolean leftDown(){
		robot.mousePress(KeyEvent.BUTTON1_MASK);
		return true;
	}
	public boolean leftUp(){
		robot.mouseRelease(KeyEvent.BUTTON1_MASK);
		return true;
	}
	public boolean wheel(int wheelNum){
		robot.mouseWheel(wheelNum);
		return true;
	}
	public void delay(int time){
		robot.delay(time);
	}
	private Robot robot;
}
