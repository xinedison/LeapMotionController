package edu.pkusz;

import java.io.IOException;

import com.leapmotion.leap.Controller;

import edu.pkusz.leapMotion.LMListener;

public class MainController implements Runnable{
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
		MainController mainController = new MainController();
        Thread thread = new Thread(mainController);
        thread.start();
        System.out.println("Press Enter to quit...");
        // the main control is in the LMListerner.onFrame()
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Remove the sample listener when done
        mainController.removeLister();
	}

	@Override
	public void run() {
		
	}
}
