package edu.pkusz.gestureAnalysis;


import java.io.IOException;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.ScreenTapGesture;
import com.leapmotion.leap.SwipeGesture;
import com.leapmotion.leap.Vector;
import com.leapmotion.leap.Gesture.State;

import edu.pkusz.PCEvent.PCControler;
import edu.pkusz.leapMotion.LMListener;

public class GestureAnalyser {
	private int mode;
	private int modeNum = 17;
	private int[] modeIndex = new int[modeNum];
	/*
	 * 0:什么也不做
	 * 1:start show
	 * 2:end show
	 * 3:pagedown
	 * 4:pageup
	 * 5:mouseModeStart
	 * 6:mousemove
	 * 7:right click
	 * 8:left click
	 * 9:wheel
	 * 10:magnifierModeStart
	 * 11:magnifierModeEnd
	 * 12:magnifier move
	 * 13:magnifier resize
	 * 14:magnifier zoom
	 * 15:figureModeStart
	 * 16:figureModeEnd
	 * 17:drawPoint	
	*/
	private Controller controller = null;
	private PCControler pcController = null;
	private Vector motionVector = null;
	
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
	
	public GestureAnalyser(Controller controller){
		this.controller = controller;
		pcController = new PCControler();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		pcController.startShow();
	}
	public void clearModeIndex(){	//调用即意味着一次gesture的开始
		for(int i=0;i<modeNum;i++)
			modeIndex[i] = 0;		
	}
	public int getBestModeIndex(){	//调用以计算一次gesture结束时的最终结果
		int maxMode = 0;
		mode = 0;
		for(int i=0;i<modeNum;i++)
			if(maxMode<modeIndex[i] ){
				maxMode=modeIndex[i];
				mode = i;
			}
		return mode;
	}
	public void setMode(int mode){
		this.mode = mode;
	}
	public int getMode(){
		return mode;
	}
	public int analyseFrame(Frame frame){
		//如果有手的信息，分析手
		if (!frame.hands().isEmpty()) {
			analyseHands(frame.hands());
		}else if(!frame.fingers().isEmpty()){  //如果没有手有手指信息，分析手指
			analyseFingers(frame.fingers());
		}
		//分析识别到的手势信息
        GestureList gestures = frame.gestures();
        if(!gestures.isEmpty())
        	analyseGesture(gestures);
//        mode = 1;
        return mode;
	}
	private void analyseHands(HandList hands){
		
	}
	private void analyseFingers(FingerList fingers){
		
	}
	public Vector getMotionParam(){
		return this.motionVector;
	}
	/*
	 * analyse the direction of the swipe gesture
	 * 四个指头向左，pageup
	 * 四个指头向右，pagedown
	 */
	private boolean swipeDirectionDown(Vector endSwipeDirection){
		System.out.println(endSwipeDirection.getX());
		if(endSwipeDirection.getX()>0)
			return true;
		return false;
	}
	/*
	 * analyse the gestures leap motion percepted
	 */
	private void analyseGesture(GestureList gestures){
		if(gestures.count() ==0) mode = 0;
		 for (int i = 0; i < gestures.count(); i++) {
	            Gesture gesture = gestures.get(i);
	            switch (gesture.type()) {
	                case TYPE_SWIPE:
	                    SwipeGesture swipe = new SwipeGesture(gesture);
	                    	if(swipeDirectionDown(swipe.direction())){
	                    		this.modeIndex[3]++;
	                    	}else{
	                    		this.modeIndex[4]++;
	                    	}
	            }
	        }
	}
}
