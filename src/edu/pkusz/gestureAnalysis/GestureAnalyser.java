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
/**
 * ����ÿ֡�����е�������Ϣ��������ͨ������Mode�������ϲ����
 * ��������ʵʱ������Ϣ�Լ�ϵ��������Ϣ
 * ʵʱ������Ϣͨ�����ص�mode��˵��
 * ϵ��������Ϣͨ����֡frame����������modeIndex���б�������ܵ�������ͼ
 */

public class GestureAnalyser {
	private int mode;
	private int modeNum = 19;
	private int[] modeIndex = new int[modeNum];
	//�ֵ�ָ������-z��ĽǶ�
	private double yaw = 0;

	/*
	 * 0:ʲôҲ����
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
	
	private MouseState  mouseState = MouseState.Nothing;	//���״̬
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
	private DrawState drawState = DrawState.Nothing;	//��ͼ״̬
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
	 * ���������
	 */
	public void clearModeIndex(){	//���ü���ζ��һ��gesture�Ŀ�ʼ
		for(int i=0;i<modeNum;i++)
			modeIndex[i] = 0;		
	}
	public void clearMagCache(){
		this.magStateCache = 0;
	}
	/**
	 * ����һ�ε�һ�ж�����ͳ�ƣ��ҳ��������п��ܵĶ�����ͼ
	 * @return ����Mode��Ӧ��index
	 */
	public int getBestMode(){	//�����Լ���һ��gesture����ʱ�����ս��
		int maxMode = 0;
		mode = Mode.Nothing;
		for(int i=0;i<modeNum;i++)
			if(maxMode<modeIndex[i] *Mode.getModeOffset(i)
				&& modeIndex[i] *Mode.getModeOffset(i)>Mode.getModeByIndex(i)){
				maxMode=(int)(modeIndex[i] *Mode.getModeOffset(i));
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
	 * ����һ֡���ݣ��õ���֡�Ķ�����ͼ
	 * @param frame ��ǰleap motion ��ȡ������֡
	 * @return ʵʱ�Ľ���mode
	 */
	public int analyseFrame(Frame frame){
		update(controller.frame(1),frame);
		//����ʶ�𵽵�������Ϣ,�����gesture������gesture
		GestureList gestures = frame.gestures();
		if(!gestures.isEmpty()&&frame.hands().count()==1)//������һֻ�ֲ����ĻӶ����ߵ������
			analyseGesture(gestures,frame.hands());
		if (!frame.hands().isEmpty()) {//�����ֵ���Ϣ
			analyseHands(frame.hands());
		}
		if(!frame.fingers().isEmpty()){  //������ָ��Ϣ
			analyseFingers(frame.fingers());
		}
        return mode;
	}
	private void analyseHands(HandList hands){
		if(hands.count()==2){//������ֻ����ɵĶ���
			Hand leftHand = hands.leftmost();
			Hand rightHand = hands.rightmost();
			int judgeV = 200;
			if(leftHand.fingers().count()==1&&leftHand.fingers().count()==rightHand.fingers().count()){//����ֻ�ֱַ����һ����ָ�Ķ���
				Finger leftFinger = leftHand.fingers().get(0);
				Finger rightFinger = rightHand.fingers().get(0);
				Vector leftFTV = rotateVectorByYAxis(leftFinger.tipVelocity());
				Vector rightFTV = rotateVectorByYAxis(rightFinger.tipVelocity());
				Vector leftFPos = rotateVectorByYAxis(leftFinger.tipPosition());
				Vector rightFPos = rotateVectorByYAxis(rightFinger.tipPosition());
				if(leftFTV.getX()>judgeV&&rightFTV.getX()<-judgeV){//���ֺ���ϲ�����Ϊ�ǹرշŴ�
					this.modeIndex[Mode.TwoFingerApproach]++;
				}
				else if(leftFTV.getX()<-judgeV&&rightFTV.getX()>judgeV){//���ֺ���ֿ�����Ϊ�Ǵ򿪷Ŵ�
					this.modeIndex[Mode.TwoFingerAway]++;
				}
				if(leftFPos.getZ()<0&&rightFPos.getZ()<0){//��ָһ����������
					this.modeIndex[Mode.TwoFingerPoke] ++;
				}
			}
			else if(leftHand.fingers().count()>=3&&rightHand.fingers().count()>=3){//����ֻ�ſ�����ɵĶ�������Ϊ�ǵ����Ŵ󾵴�С
				Vector leftHV = rotateVectorByYAxis(leftHand.palmVelocity());
				Vector rightHV = rotateVectorByYAxis(rightHand.palmVelocity());
				if(leftHV.getX()>judgeV&&rightHV.getX()<-judgeV){//���ֺϲ�����С�Ŵ󾵴�С
					this.magState = MagnifierState.Shrink;
					this.handsDistance = rightHand.palmPosition().distanceTo(leftHand.palmPosition());
				}
				else if(leftHV.getX()<-judgeV&&rightHV.getX()>judgeV){//���ַֿ�������Ŵ󾵴�С
					this.magState = MagnifierState.Enlarge;
					this.handsDistance = rightHand.palmPosition().distanceTo(leftHand.palmPosition());
				}
				this.mode = Mode.TwoHandMove;
			}
		}
		else if(hands.count()==1){//��һֻ����ɶ���
			Hand hand = hands.get(0);
			Hand preHand = preFrame.hand(hand.id());
//			if(hand.fingers().count()==2){//��ָ����ƶ��������Ŵ����Ŵ�С
//				FingerList fingers = hand.fingers();
//				Finger frontFinger = fingers.frontmost();//�����Ļ����ָ
//				Finger farToScreenFinger = null;
//				for(Iterator<Finger> it=fingers.iterator();it.hasNext();){//ȡ���������ָ
//					farToScreenFinger = it.next();
//					if(farToScreenFinger.id()!=frontFinger.id())
//						break;
//				}
//			}
			if(hands.get(0).fingers().count()>=4){//������ȭ��ֿ�
				int preFingerCount = preHand.fingers().count();
				if(preFingerCount>=4){//��һ֡Ҳ�д�����ָ
					double handMoveDis = preHand.palmPosition().distanceTo(hand.palmPosition());
					Vector frontFingerV = rotateVectorByYAxis(preHand.fingers().rightmost().tipVelocity());
					int judgeV = 100;
					if(handMoveDis<5&&frontFingerV.getZ()<-judgeV&&frontFingerV.getY()<-judgeV){//��ָ�ϲ�������ӳ
						this.modeIndex[Mode.CloseHand]++;
					}
					else if(handMoveDis<5&&frontFingerV.getZ()>judgeV&&frontFingerV.getY()>judgeV){//��ָ�ſ���ʼ��ӳ
						this.modeIndex[Mode.StretchHand]++;
					}
				}
			}
			//�����ſ��Ӷ�
			if(hand.fingers().count()>=4&&preHand.fingers().count()==hand.fingers().count()){
				Vector hPos = rotateVectorByYAxis(hand.palmPosition());
				Vector hV = rotateVectorByYAxis(hand.palmVelocity());
				Vector preHPos = rotateVectorByYAxis(preHand.palmVelocity());
				Vector preHV = rotateVectorByYAxis(preHand.palmVelocity());
				if(preHV.getZ()>100&&hV.getZ()>100){
					if(hPos.getY()-preHPos.getY()>5){
						this.mode = Mode.UpHand;
					}
				}
				else if(preHV.getZ()<-100&&hV.getZ()<-100){
					if(hPos.getY()-preHPos.getY()<-5){
						this.mode = Mode.DownHand;
					}
				}
			}
		}
		else 
			this.mode = Mode.Nothing;
	}
	/**
	 * ������ָ�Ķ������ƶ�������Ϣ
	 * @param fingers
	 */
	private void analyseFingers(FingerList fingers){
		if(fingers.count()==1){		//1����ָ��������Ϊ�ƶ�״̬
			Finger finger = fingers.get(0);
			Vector fingerDir = rotateVectorByYAxis(finger.tipVelocity());
			fingerSpeedX = fingerDir.getX();
			fingerSpeedY = fingerDir.getY();
			//���x,y�����ٶȽ�С������z�����ٶȽϴ���Ϊ�������Ļ���겻��
			if(Math.abs(fingerDir.getZ())>50&&(Math.abs(fingerSpeedX)+Math.abs(fingerSpeedY))<200){
				fingerSpeedX = 0;
				fingerSpeedY = 0;
			}
			Vector fingerTPos = rotateVectorByYAxis(finger.tipPosition());
			fingerPosZ = (fingerTPos.getZ()+100)/2;
			if(fingerTPos.getZ()<-50){
				if(drawState == DrawState.DrawSet)
					drawState = DrawState.DrawFinish;
				else if(drawState == DrawState.Nothing)
					drawState = DrawState.DrawPoint;
				if(mouseState==MouseState.Nothing)
					mouseState	= MouseState.LeftDown;	//left down
				else if(mouseState==MouseState.LeftDown)
					mouseState = MouseState.LeftUp;
				else if(mouseState ==MouseState.LeftUp)
					mouseState = MouseState.ClkOver;
			}
			else{
				if(drawState == DrawState.DrawPoint)
					drawState = DrawState.DrawSet;
				else if(drawState == DrawState.DrawFinish)
					drawState = DrawState.Nothing;
				if(mouseState==MouseState.LeftDown||mouseState==MouseState.ClkOver)
					mouseState = MouseState.LeftUp;	//left up
				else
					mouseState=MouseState.Nothing;	//nothing
			}
			this.mode = Mode.FingerMove;
			if(this.mode == Mode.FingerMove&&fingerTPos.getZ()>20)
				this.mode = Mode.Nothing;
		}
		else if(this.mode == Mode.FingerMove)
				this.mode = Mode.Nothing;
	}
	private enum Direction{
		NJUDGED,LEFT,RIGHT,UP,DOWN,IN,OUT,LEFT_OUT,RIGHT_IN;
	}
	/**
	 * �����ֻ����ķ���
	 * @param direction �ֵ��ƶ�����
	 * @return right(x):���һ��� left(-x):�� 
	 * 							up(y):�ϻ�	 down(-y):�»� 
	 * 							in(-z):ǰ��	 out(z):�� 
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
	 * �����ɵ�����ɵ�����
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
				Vector swDirV = rotateVectorByYAxis(swipe.direction());
				Direction direction = swipeDirection(swDirV);
				switch(direction){//���ݻӶ��ķ����б��ֵ�
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
//				case RIGHT_IN:
//					this.modeIndex[Mode.HandRight]++;
//					this.mode = Mode.Nothing;
//					break;
				default :break;
				}
			default :break;
			}
		}
	}
	/**
	 * ��y����ת�ƶ�����
	 * ��ת�ĽǶ����ֵĳ����-z����ļн�yaw����
	 * @param curVec : ��ǰ���ƶ�����  
	 * @return ��y����ת-yaw�ǶȺ������
	 */
	private Vector rotateVectorByYAxis(Vector curVec){
		//ʹ����ת����T=[cos(yaw) sin(yaw) 0;-sin(yaw) cos(yaw) 0;0 0 1]���Ը�x-zƽ���ͶӰ��������ת
		//��yawΪ��ʱ����ʱ����ת����yawΪ��ʱ��˳ʱ����ת
		double cosYaw = Math.cos(yaw);
		double sinYaw = Math.sin(yaw);
		if(yaw>=-Math.PI/2&&yaw<=Math.PI/2){//���ƶ���Χ������������ҷ���-90��90֮ǰʱ��������ת����
			float x = curVec.getX();
			float z = curVec.getZ();
			float nz = (float) (z*cosYaw-x*sinYaw);
			float nx = (float) (z*sinYaw+x*cosYaw);
			return new Vector(nx,curVec.getY(),nz);
		}
		else//��������Χ����Ϊ���ݲ�׼ȷ��������ת
			return curVec;
	}
	/**
	 * �����ֵ�ָ��Ƕ�
	 * @param preFrame
	 */
	private void update(Frame preFrame,Frame curFrame){
			if(curFrame.isValid()){//���õ�ǰ����x-zƽ�����ת�Ƕ�,��leap motionû�з���ʱ�����Ը��ݸýǶȵ�������ƽ��
				if(!curFrame.hands().isEmpty()){
					Vector direction = curFrame.hands().get(0).direction();
					if(curFrame.hands().count()==2)
						direction = direction.plus(curFrame.hands().get(1).direction());
					direction = direction.divide(curFrame.hands().count());
					this.yaw = direction.yaw();
				}
			}
			this.preFrame = preFrame;
	}
	public double getSpeedX(){
		return fingerSpeedX;
	}
	public double getPosZ(){
		return fingerPosZ;
	}
	public double getSpeedY(){
		return fingerSpeedY;
	}
	public MouseState getMouseState(){
		return mouseState;
	}
	public DrawState getDrawState(){
		return drawState;
	}
	public void clearDrawState(){
		drawState = DrawState.Nothing;
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
