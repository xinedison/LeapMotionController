package edu.pkusz.leapMotion;


import java.io.IOException;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Listener;

import edu.pkusz.PCEvent.ControlFrame;
import edu.pkusz.gestureAnalysis.DrawState;
import edu.pkusz.gestureAnalysis.EventCaller;
import edu.pkusz.gestureAnalysis.GestureAnalyser;
import edu.pkusz.gestureAnalysis.MagnifierState;
import edu.pkusz.gestureAnalysis.Mode;
import edu.pkusz.gestureAnalysis.MouseState;
/**
 * 与leap motion的交互部分
 * 负责leap motion的初始化、连接以及数据传递
 * 同时负责传递手势分析后的核心参数，是整个代码的数据中枢
 *
 */
public class LMListener extends Listener {
	private int mode = Mode.Nothing;
	private MouseState mouseState = MouseState.Nothing;	//鼠标状态
	public DrawState drawState = DrawState.Nothing;	//画图状态
	private MagnifierState magState = MagnifierState.Nothing;
	private double preParaX = 0f;
	private double preParaY = 0f;
	private GestureAnalyser gesAnalyser;
	public EventCaller caller = null;
	private static ControlFrame controlFrame = null;
	
	public LMListener(){
		if(caller == null)
			caller = new EventCaller();
	}
	/**
	 * 初始化应用程序，设置其在后台运行，可以设置leap motion的感应参数
	 */
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
     * 手势信息分为两种方式响应：
     * 1 实时响应														如鼠标移动，调整放大镜大小等
     * 2 整个手势完成离开感应区域后响应     如挥动手来翻页，打开/关闭放大镜等
     * @param leap motion准备好的数据
     */
    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
        if(frame.fingers().count()==0){//当前如果没有检测到手指的话，将之前检测到的一系列动作分析为一个连贯动作，找出意图实施
        	this.mode = gesAnalyser.getBestMode();
        	if(this.mode==Mode.MagnifierZoom){//如果为特殊的Mode,则需要设置参数
        		this.magState = gesAnalyser.getMagZoomState();
        		caller.setMagState(magState);
        		gesAnalyser.clearMagCache();
        	}
        	gesAnalyser.clearModeIndex();
        	caller.callEvent(this.mode);
    	}else{	//如果有手指信息的话，分析当前帧，同时设置实时响应数据
	        int tempmode = gesAnalyser.analyseFrame(frame);	//分析mode
	        if(tempmode == Mode.MouseMove){//如果是鼠标移动的话，实时响应
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
	        else if(tempmode == Mode.MagnifierResize){//如果是调整放大镜缩放大小，实时响应
	        	this.mode = tempmode;
	        	this.magState = gesAnalyser.getMagState();
	        	double handsDis = gesAnalyser.getHandsDistance();
	        	caller.setMagParam(handsDis);//设置
	        	caller.setMagState(magState);
	        	if(magState!=MagnifierState.Nothing)//需要调整缩放比例
	        		caller.callEvent(this.mode);
	        }
        }
    }
    
	public static void main(String[] args){
		LMListener listener = new LMListener();
		Controller controller = new Controller();
		controlFrame = new ControlFrame(listener.caller);
		controlFrame.startFrame();
		controller.addListener(listener);
		
		 try {
	            System.in.read();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
        controller.removeListener(listener);
	}
}
