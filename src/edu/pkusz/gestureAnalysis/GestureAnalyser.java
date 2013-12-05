package edu.pkusz.gestureAnalysis;



import java.util.Iterator;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.SwipeGesture;
import com.leapmotion.leap.Vector;
/**
 * 分析每帧数据中的手势信息，分析后通过设置Mode参数供上层调用
 * 手势中有实时手势信息以及系列手势信息
 * 实时手势信息通过返回的mode来说明
 * 系列手势信息通过多帧frame分析，利用modeIndex来判别其最可能的手势意图
 */

public class GestureAnalyser {
	private int mode;
	private int modeNum = 19;
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
	 * 18:music
	*/
	private double fingerSpeedX = 0f;			//descripe the single finger position
	private double fingerSpeedY = 0f;
	
	private double fingerPosZ = 0f;
	private double handsDistance = 0f;
	
	private double magStateCache = 0;
	
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
	private MagnifierState magState = MagnifierState.Nothing;
	/*
	 * 0:nothing
	 * 1:enlarge magnifier
	 * -1:shrink magnifier
	 */
	private Controller controller = null;
	private Frame preFrame = null;
	
	public GestureAnalyser(Controller controller){
		this.controller = controller;
	}
	/**
	 * 清楚计数器
	 */
	public void clearModeIndex(){	//调用即意味着一次gesture的开始
		for(int i=0;i<modeNum;i++)
			modeIndex[i] = 0;		
	}
	public void clearMagCache(){
		this.magStateCache = 0;
	}
	/**
	 * 对上一次的一列动作做统计，找出其中最有可能的动作意图
	 * @return 动作Mode对应的index
	 */
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
	/**
	 * 分析一帧数据，得到这帧的动作意图
	 * @param frame 当前leap motion 获取的数据帧
	 * @return 实时的交互mode
	 */
	public int analyseFrame(Frame frame){
		update(controller.frame(1));
		//分析识别到的手势信息,如果有gesture，分析gesture
		GestureList gestures = frame.gestures();
		if(!gestures.isEmpty()&&frame.hands().count()==1)//分析由一只手产生的挥动或者点击动作
			analyseGesture(gestures,frame.hands());
		if (!frame.hands().isEmpty()) {//分析手的信息
			analyseHands(frame.hands());
		}
		if(!frame.fingers().isEmpty()){  //分析手指信息
			analyseFingers(frame.fingers());
		}
        return mode;
	}
	private void analyseHands(HandList hands){
		if(hands.count()==2){//分析两只手完成的动作
			Hand leftHand = hands.leftmost();
			Hand rightHand = hands.rightmost();
			int judgeV = 200;
			if(leftHand.fingers().count()==1&&leftHand.fingers().count()==rightHand.fingers().count()){//由两只手分别各出一个手指的动作
				Finger leftFinger = leftHand.fingers().get(0);
				Finger rightFinger = rightHand.fingers().get(0);
				if(leftFinger.tipVelocity().getX()>judgeV&&rightFinger.tipVelocity().getX()<-judgeV){//两手横向合并，认为是关闭放大镜
					this.modeIndex[Mode.TwoFingerApproach]+=2;
				}
				else if(leftFinger.tipVelocity().getX()<-judgeV&&rightFinger.tipVelocity().getX()>judgeV){//两手横向分开，认为是打开放大镜
					this.modeIndex[Mode.TwoFingerAway]++;
				}
				if(leftFinger.tipPosition().getZ()<0&&rightFinger.tipPosition().getZ()<0){//两指一戳，打开音乐
					this.modeIndex[Mode.TwoFingerPoke] ++;
				}
			}
			else if(leftHand.fingers().count()>=3&&rightHand.fingers().count()>=3){//由两只张开手完成的动作，认为是调整放大镜大小
				if(leftHand.palmVelocity().getX()>judgeV&&rightHand.palmVelocity().getX()<-judgeV){//两手合并，减小放大镜大小
					this.magState = MagnifierState.Shrink;
					this.handsDistance = rightHand.palmPosition().distanceTo(leftHand.palmPosition());
				}
				else if(leftHand.palmVelocity().getX()<-judgeV&&rightHand.palmVelocity().getX()>judgeV){//两手分开，增大放大镜大小
					this.magState = MagnifierState.Enlarge;
					this.handsDistance = rightHand.palmPosition().distanceTo(leftHand.palmPosition());
				}
				this.mode = Mode.TwoHandAway;
			}
		}
		else if(hands.count()==1){//由一只手完成动作
			Hand hand = hands.get(0);
			Hand preHand = preFrame.hand(hand.id());
			if(hand.fingers().count()==2){//两指相对移动，调整放大镜缩放大小
				FingerList fingers = hand.fingers();
				Finger frontFinger = fingers.frontmost();//最靠近屏幕的手指
				Finger farToScreenFinger = null;
				for(Iterator<Finger> it=fingers.iterator();it.hasNext();){//取出后面的手指
					farToScreenFinger = it.next();
					if(farToScreenFinger.id()!=frontFinger.id())
						break;
				}
			}
			else if(hands.get(0).fingers().count()>=4){//单手握拳或分开
				int preFingerCount = preHand.fingers().count();
				if(preFingerCount>=4){//上一帧也有大于四指
					double handMoveDis = preHand.palmPosition().distanceTo(hand.palmPosition());
					Vector frontFingerV = preHand.fingers().rightmost().tipVelocity();
					int judgeV = 100;
					if(handMoveDis<5&&frontFingerV.getZ()<-judgeV&&frontFingerV.getY()<-judgeV){//五指合并结束放映
						this.modeIndex[Mode.CloseHand]++;
					}
					else if(handMoveDis<5&&frontFingerV.getZ()>judgeV&&frontFingerV.getY()>judgeV){//五指张开开始放映
						this.modeIndex[Mode.StretchHand]++;
					}
				}
			}
			//单手张开挥动
			if(hand.fingers().count()>=4&&preHand.fingers().count()==hand.fingers().count()){
				if(preHand.palmVelocity().getZ()>100&&hand.palmVelocity().getZ()>100){
					if(hand.palmPosition().getY()-preHand.palmPosition().getY()>5){
						this.mode = Mode.UpHand;
					}
				}
				else if(preHand.palmVelocity().getZ()<-100&&hand.palmVelocity().getZ()<-100){
					if(hand.palmPosition().getY()-preHand.palmPosition().getY()<-5){
						this.mode = Mode.DownHand;
					}
				}
			}
		}
		else 
			this.mode = Mode.Nothing;
	}
	/**
	 * 分析手指的动作及移动方向信息
	 * @param fingers
	 */
	private void analyseFingers(FingerList fingers){
		if(fingers.count()==1){		//1根手指，则设置为移动状态
			Finger finger = fingers.get(0);
			Vector fingerDir = finger.tipVelocity();
			fingerSpeedX = fingerDir.getX();
			fingerSpeedY = fingerDir.getY();
			//如果x,y方向速度较小，并且z方向速度较大，视为点击，屏幕坐标不动
			if(Math.abs(fingerDir.getZ())>50&&(Math.abs(fingerSpeedX)+Math.abs(fingerSpeedY))<200){
				fingerSpeedX = 0;
				fingerSpeedY = 0;
			}
			fingerPosZ = (finger.tipPosition().getZ()+100)/2;
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
			this.mode = Mode.FingerMove;
			if(this.mode == Mode.FingerMove&&finger.tipPosition().getZ()>100)
				this.mode = Mode.Nothing;
		}
	}
	private enum Direction{
		NJUDGED,LEFT,RIGHT,UP,DOWN,IN,OUT,LEFT_OUT,RIGHT_IN;
	}
	/**
	 * 分析手滑动的方向
	 * @param direction 手的移动方向
	 * @return right(x):向右滑动 left(-x):左滑 
	 * 							up(y):上滑	 down(-y):下滑 
	 * 							in(-z):前滑	 out(z):后滑 
	 * 
	 */
	private Direction swipeDirection(Vector direction){	//1:x 2:-x 3:y 4:-y 5:z 6:-z
		double x = Math.abs(direction.getX());
		double y = Math.abs(direction.getY());
		double z = Math.abs(direction.getZ());
		if(x>y&&x>z){
			if(direction.getX()>0)
				return Direction.RIGHT;
			else
				return Direction.LEFT;
		}
		else if(y>x&&y>z){
			if(direction.getY()>0)
				return Direction.UP;
			else
				return Direction.DOWN;
		}
//		if(direction.getX()<0&&direction.getZ()>0)
//			return Direction.leftOut;
		else if(direction.getX()>0&&direction.getZ()<0)
			return Direction.RIGHT_IN;
		return Direction.NJUDGED;
	}
	/**
	 * 分析由单手完成的手势
	 */
	private void analyseGesture(GestureList gestures,HandList hands){
		for (int i = 0; i < gestures.count(); i++) {
			Gesture gesture = gestures.get(i);
			switch (gesture.type()) {
			case TYPE_SCREEN_TAP:
				this.modeIndex[7]++;
				break;
			case TYPE_SWIPE:
				SwipeGesture swipe = new SwipeGesture(gesture);
				Direction direction = swipeDirection(swipe.direction());
				switch(direction){//根据挥动的方向判别手的
				case RIGHT:
					this.modeIndex[Mode.HandRight]++;
					this.mode = Mode.Nothing;
					break;
				case LEFT:
					this.modeIndex[Mode.HandLeft]++;
					this.mode = Mode.Nothing;
					break;
				case UP:
					this.modeIndex[Mode.UpHand]++;
					this.mode = Mode.UpHand;
					break;
				case DOWN:
					this.modeIndex[Mode.DownHand]++;
					this.mode = Mode.DownHand;
					break;
//				case leftOut:
//					this.modeIndex[Mode.PageDown]++;
//					this.mode = Mode.Nothing;
//					break;
				case RIGHT_IN:
					this.modeIndex[Mode.HandRight]++;
					this.mode = Mode.Nothing;
					break;
				default :break;
				}
			default :break;
			}
		}
	}
	private void update(Frame preFrame){
			this.preFrame = preFrame;
	}
	public double getSpeedX(){
		return fingerSpeedX;
	}
	public double getSpeedY(){
		return fingerSpeedY;
	}
	public double getPosZ(){
		return fingerPosZ;
	}
	public MouseState getMouseState(){
		return mouseState;
	}
	public DrawState getDrawState(){
		return drawState;
	}
	public MagnifierState getMagState(){
		return magState;
	}
	public MagnifierState getMagZoomState(){
		double zoomStateJudge = this.magStateCache/this.modeIndex[Mode.MagnifierZoom];
		if(zoomStateJudge>0)
			return MagnifierState.Enlarge;
		else if(zoomStateJudge<0)
			return MagnifierState.Shrink;
		return MagnifierState.Nothing;
	}
	public double getHandsDistance(){
		return handsDistance;
	}
}
