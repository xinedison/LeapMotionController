package edu.pkusz.gestureAnalysis;


import java.io.IOException;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
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
	private double paraZ = 0f;
	private MouseState  mouseState = MouseState.Nothing;	//鼠标状态
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
	private DrawState drawState = DrawState.Nothing;	//画图状态
	/*
	 * 0:nothing
	 * 1:draw point
	 * 2:draw rec
	 */
//	private Controller controller = null;
	private PCControler pcController = null;
	
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
	public int getBestMode(){	//调用以计算一次gesture结束时的最终结果
		int maxMode = 0;
		mode = Mode.Nothing;
		for(int i=0;i<modeNum;i++)
			if(maxMode<modeIndex[i] ){
				maxMode=modeIndex[i];
				mode = Mode.getModeByIndex(i);
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
		if(!gestures.isEmpty())//分析系统提供的手势信息调用
			analyseGesture(gestures,frame);
		if (!frame.hands().isEmpty()) {//分析手的信息
			analyseHands(frame.hands());
		}
		if(!frame.fingers().isEmpty()){  //分析手指信息
			analyseFingers(frame.fingers());
		}
        return mode;
	}
	private void analyseHands(HandList hands){
		if(hands.count()==2){//如果有两支手
			Hand leftHand = hands.leftmost();
			Hand rightHand = hands.rightmost();
			int judgeV = 50;
			if(leftHand.fingers().count()==1&&leftHand.fingers().count()==rightHand.fingers().count()){//每只手一个指头
				if(leftHand.palmVelocity().getX()>judgeV&&rightHand.palmVelocity().getX()<-judgeV)//两手合并
					this.modeIndex[Mode.EndMagnifier]++;
				if(leftHand.palmVelocity().getX()<-judgeV&&rightHand.palmVelocity().getX()>judgeV)
					this.modeIndex[Mode.StartMagnifier]++;
			}
		}
	}
	private void analyseFingers(FingerList fingers){
		if(fingers.count()==1){		//1根手指
			Finger finger = fingers.get(0);
			Vector fingerDir = finger.tipVelocity();
//			System.out.println("shang x"+fingerDir.getX()+"\tshang y"+fingerDir.getY());
			paraX = fingerDir.getX();
			paraY = fingerDir.getY();
			//如果x,y方向速度较小，并且z方向速度较大，视为点击，屏幕坐标不动
			if(Math.abs(fingerDir.getZ())>50&&(Math.abs(paraX)+Math.abs(paraY))<200){
				paraX = 0;
				paraY = 0;
			}
			paraZ = (finger.tipPosition().getZ()+100)/2;
			if(finger.tipPosition().getZ()<-100){
				drawState = DrawState.DrawPoint;
				if(mouseState==MouseState.Nothing)
					mouseState	= MouseState.LeftDown;	//left down
				else if(mouseState==MouseState.LeftDown)
					mouseState = MouseState.LeftUp;
				else if(mouseState ==MouseState.LeftUp)
					mouseState = MouseState.ClkOver;
			}
			else{
				drawState = DrawState.Nothing;
				if(mouseState==MouseState.LeftDown||mouseState==MouseState.ClkOver)
					mouseState = MouseState.LeftUp;	//left up
				else
					mouseState=MouseState.Nothing;	//nothing
			}
			this.mode = Mode.MouseMove;
		}
		else{
			this.mode = Mode.Nothing;
		}
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
	public MouseState getMouseState(){
		return mouseState;
	}
	public DrawState getDrawState(){
		return drawState;
	}
	/*
	 * analyse the gestures leap motion percepted
	 */
	private int analyseGesture(GestureList gestures,Frame frame){
		for (int i = 0; i < gestures.count(); i++) {
			Gesture gesture = gestures.get(i);
			switch (gesture.type()) {
			case TYPE_SCREEN_TAP:
				this.modeIndex[7]++;
				break;
			case TYPE_SWIPE:
				SwipeGesture swipe = new SwipeGesture(gesture);
				int direction = swipeDirection(swipe.direction());
				if (direction == 1) {
					this.modeIndex[4]++;
					this.mode = Mode.Nothing;
				} else if (direction == 2) {
					this.modeIndex[3]++;
					this.mode = Mode.Nothing;
				} else if (direction == 5) {
					this.modeIndex[1]++;
					this.mode = Mode.Nothing;
				} else if (direction == 6) {
					this.modeIndex[2]++;
					this.mode = Mode.Nothing;
				} else if (direction == 3) {
					this.modeIndex[15]++;
					this.mode = Mode.Nothing;
				} else if (direction == 4) {
					this.modeIndex[16]++;
					this.mode = Mode.Nothing;
				}
			default :return mode;
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
	public double getParaZ(){
		return paraZ;
	}
}
