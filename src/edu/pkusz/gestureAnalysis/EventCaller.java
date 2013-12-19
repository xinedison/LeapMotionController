package edu.pkusz.gestureAnalysis;


import edu.pkusz.PCEvent.PCControler;
/**
 * 负责调用PC的类
 * 根据分析好传回的手势Mode信息，调用PC做响应
 */
public class EventCaller {
	private int mode;
	private MouseState state;
	private MagnifierState magState;
	private DrawState drawState = DrawState.Nothing;
	private int drawMode = 0;	//0画点 1画line 2画rec 3画oval 
	private boolean musicStart = false;
	private int x;
	private int y;
	private int z;
	private int magResize;
	
	private PCControler pcControler = new PCControler();
	public EventCaller(){}
	public boolean callMouseState(MouseState state){
		this.state = state;
		switch(state){
		case Nothing:
			break;
		case LeftDown:
			pcControler.leftDown();
			break;
		case LeftUp:
			pcControler.leftUp();
			break;
		default :break;
		}
		return true;
	}
	public void setMode(int mode){
		this.mode = mode;
	}
	public int getMode(){
		return mode;
	}
	/**
	 * 根据mode的不同来完成pc调用
	 * @param mode 指示手势信息
	 * @return 
	 */
	public boolean callEvent(int mode){
//		this.mode = mode;
		boolean bSucceed = false;
		switch(mode){
		case Mode.StretchHand  :
			bSucceed = pcControler.startShow();
			System.out.println("start show");
			break;
		case Mode.CloseHand:
			bSucceed = pcControler.endShow();
			System.out.println("end show");
			break;
		case Mode.HandLeft:
			bSucceed = pcControler.pageDown();
			System.out.println("pagedown");
			break;
		case Mode.HandRight:
			bSucceed = pcControler.pageUp();
			System.out.println("pageup");
			break;
		case Mode.FingerMove:
			bSucceed = pcControler.mouseMove(x/20,- y/20);
			pcControler.drawPoint(x/20,- y/20,drawState.getDrawState(),drawMode);
			pcControler.moveMagnifier(x/20, -y/20);
			pcControler.setRound(z);
			break;
		case Mode.RightClk:
			bSucceed = pcControler.rightClick();
			break;
		case Mode.LeftClk:
			bSucceed = pcControler.leftClick();
			break;
		case Mode.UpHand:
			bSucceed = pcControler.musicVolumeUp();
			bSucceed = pcControler.startFigure();
//			bSucceed = pcControler.zoomMagnifier(0.1);
			break;
		case Mode.DownHand:
			bSucceed = pcControler.endFigure();
			bSucceed = pcControler.musicVolumeDown();
//			bSucceed = pcControler.zoomMagnifier(-0.1);
			break;
		case Mode.TwoFingerAway:
			bSucceed = pcControler.startMagnifier();
			pcControler.startBackground();
			System.out.println("start magnifier");
			break;
		case Mode.TwoFingerApproach:
			bSucceed = pcControler.endMagnifier();
			pcControler.endBackground();
			System.out.println("end magnifier");
			break;
		case Mode.TwoHandMove:
			bSucceed = pcControler.resizeMagnifier(magState.getState()*magResize/50);
			break;
		case Mode.MagnifierZoom:
			bSucceed = pcControler.zoomMagnifier(magState.getState());
			System.out.println("magnifier zoom changed "+magState.getState());
			break;
		case Mode.MouseModeStart:
			bSucceed = pcControler.startMouse();
			System.out.println("mouse mode start");
			break;
		case Mode.TwoFingerPoke:
			if(musicStart){
				setMode(Mode.Nothing);
				musicStart = false;
				bSucceed = pcControler.endMusic();
				System.out.println("music end");
			}
			else{
				setMode(Mode.TwoFingerPoke);
				musicStart = true;
				bSucceed = pcControler.startMusic();
				System.out.println("music start");
			}
			break;
		default :break;
		}
		return bSucceed;
	}
	public void setParam(int x,int y,int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public void setMagParam(double resize){
		this.magResize = (int)resize;
	}
	public void setDrawState(DrawState drawState){
		this.drawState = drawState;
	}
	public void setMagState(MagnifierState magState){
		this.magState = magState;
	}
	public void setDrawMode(int mode){
		drawMode = mode;
		System.out.println(drawMode);
	}
}
