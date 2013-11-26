package edu.pkusz.leapMotion;


import com.leapmotion.leap.Config;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

import edu.pkusz.gestureAnalysis.EventCaller;
import edu.pkusz.gestureAnalysis.GestureAnalyser;

public class LMListener extends Listener {
	private int mode;
	private GestureAnalyser gesAnalyser;
	private EventCaller caller = new EventCaller();
	//init the controller
    public void onInit(Controller controller) {
        System.out.println("Initialized");
    }
    //connect the controller and set the parameter for the controller
    public void onConnect(Controller controller) {
        System.out.println("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
//        // config of the leap motion  parameter
//        Config config = controller.config();
//     // key tap parameters
//        config.setFloat("Gesture.KeyTap.MinDownVelocity", 30.0f);
//
//        System.out.println("Key Tap MinDownVelocity: " +
//                     config.getFloat("Gesture.KeyTap.MinDownVelocity"));
//        System.out.println("Key Tap HistorySeconds: " + 
//                     config.getFloat("Gesture.KeyTap.HistorySeconds"));
//        System.out.println("Key Tap MinDistance: " + 
//                     config.getFloat("Gesture.KeyTap.MinDistance"));
//        System.out.println();
//
//        // screen tap parameters
//        config.setFloat("Gesture.ScreenTap.MinForwardVelocity", 30.0f);
//        config.setFloat("Gesture.ScreenTap.MinDistance", 1.0f);
//        
//        //config the swipe gesture parameter
////        controller.config().setFloat("Gesture.Swipe.MinLength", 200.0f) ;
////        controller.config().setFloat("Gesture.Swipe.MinVelocity", 750);
//        
//        controller.config().save();
        this.gesAnalyser = new GestureAnalyser(controller);
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }
    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();

        if(frame.fingers().count()==0){
        	this.mode = gesAnalyser.getBestModeIndex();
        	gesAnalyser.clearModeIndex();
	        Vector mv = gesAnalyser.getMotionParam();			//设置参数
	//        caller.setParam((int)mv.getX(), (int)mv.getY());
        	caller.callEvent(this.mode);
    	}else{	//有手指
	        gesAnalyser.analyseFrame(frame);	//分析mode
        }
    }
}
