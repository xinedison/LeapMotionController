package edu.pkusz.PCEvent;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class KeyBoardEvent {
	public static void main(String[] args){
		System.out.println("key");
		KeyBoardEvent key = new KeyBoardEvent();
		for(int i =0;i<15;i++){
			key.delay(1000);
			key.keyDown(KeyEvent.VK_C);
			key.keyUp(KeyEvent.VK_C);
		}
	}
	public KeyBoardEvent(){
		try{
		robot = new Robot(); 
		}catch (AWTException e) {
			e.printStackTrace();
		}		
	}
	public boolean keyDown(int key){
		robot.keyPress(key);
		return true;
	}
	public boolean keyUp(int key){
		robot.keyRelease(key);
		return true;
	}
	public void delay(int time){
		robot.delay(time);
	}
	private Robot robot;
}
