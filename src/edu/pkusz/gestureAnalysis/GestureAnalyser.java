package edu.pkusz.gestureAnalysis;


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

public class GestureAnalyser {
	private int mode;
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
		if(endSwipeDirection.getX()>0)
			return true;
		return false;
	}
	/*
	 * analyse the gestures leap motion percepted
	 */
	private void analyseGesture(GestureList gestures){
		 for (int i = 0; i < gestures.count(); i++) {
	            Gesture gesture = gestures.get(i);
	            switch (gesture.type()) {
	            //画圈的gesture
	                case TYPE_CIRCLE:
	                    CircleGesture circle = new CircleGesture(gesture);
	                    // Calculate clock direction using the angle between circle normal and pointable
	                    String clockwiseness;
	                    if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/4) {
	                        // Clockwise if angle is less than 90 degrees
	                        clockwiseness = "clockwise";
	                    } else {
	                        clockwiseness = "counterclockwise";
	                    }
	                    // Calculate angle swept since last frame
	                    double sweptAngle = 0;
	                    if (circle.state() != State.STATE_START) {
	                        CircleGesture previousUpdate = new CircleGesture(controller.frame(1).gesture(circle.id()));
	                        sweptAngle = (circle.progress() - previousUpdate.progress()) * 2 * Math.PI;
	                    }
//	                    System.out.println("Circle id: " + circle.id()  + ", " + circle.state() + ", progress: " + circle.progress()  + ", radius: " + circle.radius() + ", angle: " + Math.toDegrees(sweptAngle) + ", " + clockwiseness);
	                    if(circle.state()==State.STATE_STOP){
	                    	System.out.println("draw circle!");
	                    }
	                    break;
	                // 滑动的gesture
	                case TYPE_SWIPE:
	                    SwipeGesture swipe = new SwipeGesture(gesture);
	                    if(swipe.state()==State.STATE_START){
	                    	if(swipeDirectionDown(swipe.direction())){
	                    		this.mode = 3;
//	                    		pcController.pageDown();
	                    	}else{
	                    		this.mode = 4;
//	                    		pcController.pageUp();
	                    	}
	                    }
	                    break;
	                case TYPE_SCREEN_TAP:
	                    ScreenTapGesture screenTap = new ScreenTapGesture(gesture);
	                    System.out.println("Screen Tap id: " + screenTap.id() + ", " + screenTap.state() + ", position: " + screenTap.position() + ", direction: " + screenTap.direction());
	                    break;
	                case TYPE_KEY_TAP:
	                    KeyTapGesture keyTap = new KeyTapGesture(gesture);
	                    System.out.println("Key Tap id: " + keyTap.id() + ", " + keyTap.state()  + ", position: " + keyTap.position() + ", direction: " + keyTap.direction());
	                    break;
	                default:
	                    System.out.println("Unknown gesture type.");
	                    break;
	            }
	        }
	}
}
