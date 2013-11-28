package edu.pkusz.gestureAnalysis;



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
	private double fingerPosX = 0f;			//descripe the single finger position
	private double fingerPosY = 0f;
	private double fingerPosZ = 0f;
	
	private double handsDistance = 0f;
	
	private double zoomRate = 0f;
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
		update(controller.frame(1));
		//分析识别到的手势信息,如果有gesture，分析gesture
		GestureList gestures = frame.gestures();
		if(!gestures.isEmpty()&&frame.hands().count()==1)//分析由一只手产生的挥动或者点击动作
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
				if(leftHand.palmVelocity().getX()>judgeV&&rightHand.palmVelocity().getX()<-judgeV)//两手横向合并
					this.modeIndex[Mode.EndMagnifier]++;
				else if(leftHand.palmVelocity().getY()>judgeV&&rightHand.palmVelocity().getY()<-judgeV)//两手纵向合并
					this.modeIndex[Mode.EndMagnifier]++;
				else if(leftHand.palmVelocity().getX()<-judgeV&&rightHand.palmVelocity().getX()>judgeV)//两手横向分开
					this.modeIndex[Mode.StartMagnifier]++;
				else if(leftHand.palmVelocity().getY()<-judgeV&&rightHand.palmVelocity().getY()>judgeV)//两手纵向分开
					this.modeIndex[Mode.StartMagnifier]++;
			}
			else if(leftHand.fingers().count()>=3&&rightHand.fingers().count()>=3){//当两支手都检测到多个手指，认为是调整放大镜大小
				if(leftHand.palmVelocity().getX()>judgeV&&rightHand.palmVelocity().getX()<-judgeV){//两手合并
					this.magState = MagnifierState.Shrink;
					this.handsDistance = rightHand.palmPosition().getX()-leftHand.palmPosition().getX();
				}
				if(leftHand.palmVelocity().getX()<-judgeV&&rightHand.palmVelocity().getX()>judgeV){
					this.magState = MagnifierState.Enlarge;
					this.handsDistance = rightHand.palmPosition().getX()-leftHand.palmPosition().getX();
				}
				this.mode = Mode.MagnifierResize;
			}
		}
	}
	/**
	 * 分析手指的动作及移动方向信息
	 * @param fingers
	 */
	private void analyseFingers(FingerList fingers){
		if(fingers.count()==1){		//1根手指，则设置为移动状态
			Finger finger = fingers.get(0);
			Vector fingerDir = finger.tipVelocity();
//			System.out.println("shang x"+fingerDir.getX()+"\tshang y"+fingerDir.getY());
			fingerPosX = fingerDir.getX();
			fingerPosY = fingerDir.getY();
			//如果x,y方向速度较小，并且z方向速度较大，视为点击，屏幕坐标不动
			if(Math.abs(fingerDir.getZ())>50&&(Math.abs(fingerPosX)+Math.abs(fingerPosY))<200){
				fingerPosX = 0;
				fingerPosY = 0;
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
			this.mode = Mode.MouseMove;
		}
	}
	private enum Direction{
		notJudged,left,right,up,down,in,out;
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
				return Direction.left;
			else
				return Direction.right;
		}
		if(y>x&&y>z){
			if(direction.getY()>0)
				return Direction.up;
			else
				return Direction.down;
		}
		if(z>x&&z>y){
			if(direction.getZ()>0)
				return Direction.out;
			else
				return Direction.in;
		}
		return Direction.notJudged;
	}
	/*
	 * analyse the gestures leap motion percepted
	 */
	private void analyseGesture(GestureList gestures,Frame frame){
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
				case right:
					this.modeIndex[Mode.PageUp]++;
					this.mode = Mode.Nothing;
					break;
				case left:
					this.modeIndex[Mode.PageDown]++;
					this.mode = Mode.Nothing;
					break;
				case up:
					this.modeIndex[Mode.StartFigure]++;
					this.mode = Mode.Nothing;
					break;
				case down:
					this.modeIndex[Mode.EndFigure]++;
					this.mode = Mode.Nothing;
					break;
				case in:
					this.modeIndex[Mode.EndShow]++;
					this.mode = Mode.Nothing;
					break;
				case out:
					this.modeIndex[Mode.StartShow]++;
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
	public double getParaX(){
		return fingerPosX;
	}
	public double getParaY(){
		return fingerPosY;
	}
	public double getParaZ(){
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
	public double getHandsDistance(){
		return handsDistance;
	}
	public double getZoomRate(){
		return zoomRate;
	}
}
