package edu.pkusz;

import java.io.IOException;

import com.leapmotion.leap.Controller;

import edu.pkusz.leapMotion.LMListener;

public class MainController{
	private LMListener listener = null;
	private Controller controller = null;
//	private PCControler pcController = null;
//	private int mode = 0;
	public MainController(){
		listener = new LMListener();
		controller = new Controller();
		controller.addListener(listener);
	}
	public void removeLister(){
		this.controller.removeListener(listener);
	}
	public static void main(String[] args){
//		MainController mainController = new MainController();
		LMListener listener = new LMListener();
		Controller controller = new Controller();
		controller.addListener(listener);
		 try {
	            System.in.read();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
        controller.removeListener(listener);
	}

//	public void run() {
//	try {
//      Thread.sleep(1000);
//  } catch (Exception e) {
//      e.printStackTrace();
//  }
//	}
}
