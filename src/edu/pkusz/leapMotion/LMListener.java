package edu.pkusz.leapMotion;


import java.io.IOException;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Listener;

import edu.pkusz.gestureAnalysis.DrawState;
import edu.pkusz.gestureAnalysis.EventCaller;
import edu.pkusz.gestureAnalysis.GestureAnalyser;
import edu.pkusz.gestureAnalysis.MagnifierState;
import edu.pkusz.gestureAnalysis.Mode;
import edu.pkusz.gestureAnalysis.MouseState;

public class LMListener extends Listener {
	private int mode = Mode.Nothing;
	private MouseState mouseState = MouseState.Nothing;	//鼠标状态
	public DrawState drawState = DrawState.Nothing;	//画图状态
	private MagnifierState magState = MagnifierState.Nothing;
	private double preParaX = 0f;
	private double preParaY = 0f;
	private GestureAnalyser gesAnalyser;
	private EventCaller caller = null;
	
	public LMListener(){
		if(caller == null)
			caller = new EventCaller();
	}
	//init the controller
    public void onInit(Controller controller) {
    	//设置leap motion可在后台运行
    	controller.setPolicyFlags(Controller.PolicyFlag.POLICY_BACKGROUND_FRAMES);
        System.out.println("Initialized");
    }
    //connect the controller and set the parameter for the controller
    public void onConnect(Controller controller) {
        System.out.println("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
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
        if(frame.fingers().count()==0){//当前如果没有检测到手指的话，将之前检测到的一系列动作分析为一个连贯动作，找出意图实施
        	this.mode = gesAnalyser.getBestMode();
        	gesAnalyser.clearModeIndex();
	//        caller.setParam((int)mv.getX(), (int)mv.getY());
        	caller.callEvent(this.mode);
    	}else{	//有手指
	        int tempmode = gesAnalyser.analyseFrame(frame);	//分析mode
	        if(tempmode == Mode.MouseMove){//如果是鼠标移动的话
	        	this.mode = tempmode;
	        	double x = gesAnalyser.getSpeedX();
	        	double y = gesAnalyser.getSpeedY();
	    		if((int)(preParaX*10) ==(int)(x*10))
	    			x = 0;
	    		else
	    			preParaX = x;
	    		if((int)(preParaY*10) ==(int)(y*10))
	    			y = 0;
	    		else
	    			preParaY = y;
	    		caller.setParam((int)x,(int) y,(int)gesAnalyser.getPosZ());
	        	caller.callEvent(this.mode);
	        	mouseState = gesAnalyser.getMouseState();//移动手指
	        	drawState = gesAnalyser.getDrawState();
	        	caller.setDrawState(drawState);
	        	if(mouseState!=MouseState.Nothing){
	        		caller.callMouseState(mouseState);
	        	}
	        }
	        else if(tempmode == Mode.MagnifierResize){
	        	this.mode = tempmode;
	        	this.magState = gesAnalyser.getMagState();
	        	double handsDis = gesAnalyser.getHandsDistance();
	        	caller.setMagParam(handsDis,0);//设置
	        	caller.setMagState(magState);
	        	if(magState!=MagnifierState.Nothing)//需要调整缩放比例
	        		caller.callEvent(this.mode);
	        }
	        else if(tempmode==Mode.MagnifierZoom){//调整放大镜缩放比例
	        	this.magState = gesAnalyser.getMagState();
	        	double zoomRate = gesAnalyser.getZoomRate();
	        	caller.setMagState(magState);
	        	caller.setMagParam(0,zoomRate);//设置
	        }
        }
    }
    
	public static void main(String[] args){
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
}
