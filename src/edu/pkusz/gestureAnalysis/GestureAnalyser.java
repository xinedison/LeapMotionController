package edu.pkusz.gestureAnalysis;


import java.io.IOException;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
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
	private double paraX = 0f;
	private double paraY = 0f;
	public int mouseState;	//鼠标状态
	/*
	 * 0:noting
	 * 1:left down
	 * 2:left up
	 * 3:right down
	 * 4:right up
	 * 5:wheel down
	 * 6:wheel up
	 * 7:click over
	 */
	public int drawState;	//画图状态
	/*
	 * 0:nothing
	 * 1:draw point
	 * 2:draw rec
	 */
//	private Controller controller = null;
	private PCControler pcController = null;
	private Vector motionVector = null;
	
	public GestureAnalyser(Controller controller){
//		this.controller = controller;
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
		//分析识别到的手势信息,如果有gesture，分析gesture
		GestureList gestures = frame.gestures();
		if(!gestures.isEmpty())
			analyseGesture(gestures,frame);
		//如果有手的信息，分析手
//		else if (!frame.hands().isEmpty()) {
//			analyseHands(frame.hands());
//		}
		else if(!frame.fingers().isEmpty()){  //如果没有gesture，而有手指在
			analyseFingers(frame.fingers());
		}
//        mode = 1;
        return mode;
	}
	private void analyseHands(HandList hands){
		
	}
	private void analyseFingers(FingerList fingers){
		if(fingers.count()<=1){		//1根手指
			Finger finger = fingers.get(0);
			Vector fingerDir = finger.tipVelocity();
//			System.out.println("shang x"+fingerDir.getX()+"\tshang y"+fingerDir.getY());
			paraX = fingerDir.getX();
			paraY = fingerDir.getY();
			
			System.out.println("up "+finger.tipPosition().getZ());
			if(finger.tipPosition().getZ()<-100){
				drawState = 1;
				if(mouseState == 0)
					mouseState	=1;	//left down
				else if(mouseState ==1)
					mouseState =2;
				else if(mouseState ==2)
					mouseState = 7;
			}
			else{
				drawState = 0;
				if(mouseState==1||mouseState==7)
					mouseState = 2;	//left up
				else
					mouseState=0;	//nothing
			}
			this.mode = 6;
		}
		else{
			this.mode = 0;
		}
	}
	public Vector getMotionParam(){
		return this.motionVector;
	}
	/*
	 * analyse the direction of the swipe gesture
	 * 四个指头向左，pageup
	 * 四个指头向右，pagedown
	 */
	private int swipeDirection(Vector direction){	//1:x 2:-x 3:y 4:-y 5:z 6:-z
		double x = Math.abs(direction.getX());
		double y = Math.abs(direction.getY());
		double z = Math.abs(direction.getZ());
		if(x>y&&x>z){
			if(direction.getX()>0)
				return 1;
			else
				return 2;
		}
		if(y>x&&y>z){
			if(direction.getY()>0)
				return 3;
			else
				return 4;
		}
		if(z>x&&z>y){
			if(direction.getZ()>0)
				return 5;
			else
				return 6;
		}
		return 0;
	}
	/*
	 * analyse the gestures leap motion percepted
	 */
	private int analyseGesture(GestureList gestures,Frame frame){
		FingerList fingers = frame.fingers();
		if(fingers.count() ==0) 	//没有手指
			mode = 0;
		else if(fingers.count()<=1){		//1根手指
			Finger finger = fingers.get(0);
			Vector fingerDir = finger.tipVelocity();
			
			//System.out.println("x"+fingerDir.getX()+"\ty"+fingerDir.getY());
			paraX = fingerDir.getX();
			paraY = fingerDir.getY();
			System.out.println(finger.tipPosition().getZ());
			if(finger.tipPosition().getZ()<-100){
				drawState = 1;
				if(mouseState == 0)
					mouseState	=1;	//left down
				else if(mouseState ==1)
					mouseState =2;
				else if(mouseState ==2)
					mouseState = 7;
			}
			else{
				drawState = 0;
				if(mouseState==1||mouseState==7)
					mouseState = 2;	//left up
				else
					mouseState=0;	//nothing
			}
			this.mode = 6;
		}
		else if(fingers.count()>=2){		//2根手指		
			for (int i = 0; i < gestures.count(); i++) {
		            Gesture gesture = gestures.get(i);
		            switch (gesture.type()) {
		            	case	TYPE_SCREEN_TAP:
		            		this.modeIndex[7]++;
		            		break;
		                case TYPE_SWIPE:
		                    SwipeGesture swipe = new SwipeGesture(gesture);
		                    int direction = swipeDirection(swipe.direction());
		                    	if(direction==1){
		                    		this.modeIndex[3]++;
		                    		this.mode = 0;
		                    	}
		                    	else if(direction==2){
		                    		this.modeIndex[4]++;
		                    		this.mode = 0;
		                    	}
		                    	else if(direction==5){
		                    		this.modeIndex[1]++;
		                    		this.mode = 0;
		                    	}
		                    	else if(direction==6){
		                    		this.modeIndex[2]++;
		                    		this.mode = 0;
		                    	}
		                    	else if(direction==3){
		                    		this.modeIndex[15]++;
		                    		this.mode = 0;
		                    	}
		                    	else if(direction==4){
		                    		this.modeIndex[16]++;
		                    		this.mode = 0;
		                    	}
		            }
		    }
		}
		return mode;
	}
	public double getParaX(){
		return paraX;
	}
	public double getParaY(){
		return paraY;
	}
}
