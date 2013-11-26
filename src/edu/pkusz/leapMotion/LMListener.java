package edu.pkusz.leapMotion;


import java.io.IOException;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Vector;

import edu.pkusz.gestureAnalysis.EventCaller;
import edu.pkusz.gestureAnalysis.GestureAnalyser;

public class LMListener extends Listener {
	private int mode;
	private int mouseState;	//鼠标状态
	public int drawState;	//画图状态
	private double preParaX = 0f;
	private double preParaY = 0f;
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
    /**
     * 分析每一帧数据，获取手势信息
     */
    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
        if(frame.fingers().count()==0){//当前如果没有手指的话，将上次缓存动作实施
        	this.mode = gesAnalyser.getBestModeIndex();
        	gesAnalyser.clearModeIndex();
	        Vector mv = gesAnalyser.getMotionParam();		//设置参数
	//        caller.setParam((int)mv.getX(), (int)mv.getY());
        	caller.callEvent(this.mode);
    	}else{	//有手指
	        int tempmode = gesAnalyser.analyseFrame(frame);	//分析mode
	        if(tempmode == 6){
	        	this.mode = tempmode;
	        	double x = gesAnalyser.getParaX();
	        	double y = gesAnalyser.getParaY();
	    		if((int)(preParaX*10) ==(int)(x*10))
	    			x = 0;
	    		else
	    			preParaX = x;
	    		if((int)(preParaY*10) ==(int)(y*10))
	    			y = 0;
	    		else
	    			preParaY = y;
	    		caller.setParam((int)x,(int) y);
	        	caller.callEvent(this.mode);
	        }
	        mouseState = gesAnalyser.mouseState;
	        drawState = gesAnalyser.drawState;
	        caller.drawState = drawState;
	        if(mouseState!=0){
	        	caller.callMouseState(mouseState);
	        }
        }
    }
    
	public static void main(String[] args){
//		MainController mainController = new MainController();
		LMListener listener = new LMListener();
		Controller controller = new Controller();
		controller.addListener(listener);
		//while(true);
		 try {
	            System.in.read();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
        controller.removeListener(listener);
	}
}
