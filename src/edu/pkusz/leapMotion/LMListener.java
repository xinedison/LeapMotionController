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
	private MouseState mouseState = MouseState.Nothing;	//���״̬
	public DrawState drawState = DrawState.Nothing;	//��ͼ״̬
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
    	//����leap motion���ں�̨����
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
     * ����ÿһ֡���ݣ���ȡ������Ϣ
     */
    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
        if(frame.fingers().count()==0){//��ǰ���û�м�⵽��ָ�Ļ�����֮ǰ��⵽��һϵ�ж�������Ϊһ�����ᶯ�����ҳ���ͼʵʩ
        	this.mode = gesAnalyser.getBestMode();
        	gesAnalyser.clearModeIndex();
	//        caller.setParam((int)mv.getX(), (int)mv.getY());
        	caller.callEvent(this.mode);
    	}else{	//����ָ
	        int tempmode = gesAnalyser.analyseFrame(frame);	//����mode
	        if(tempmode == Mode.MouseMove){//���������ƶ��Ļ�
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
	        	mouseState = gesAnalyser.getMouseState();//�ƶ���ָ
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
	        	caller.setMagParam(handsDis,0);//����
	        	caller.setMagState(magState);
	        	if(magState!=MagnifierState.Nothing)//��Ҫ�������ű���
	        		caller.callEvent(this.mode);
	        }
	        else if(tempmode==Mode.MagnifierZoom){//�����Ŵ����ű���
	        	this.magState = gesAnalyser.getMagState();
	        	double zoomRate = gesAnalyser.getZoomRate();
	        	caller.setMagState(magState);
	        	caller.setMagParam(0,zoomRate);//����
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
