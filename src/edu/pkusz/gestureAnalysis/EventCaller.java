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
	/**
	 * 根据mode的不同来完成pc调用
	 * @param mode 指示手势信息
	 * @return 
	 */
	public boolean callEvent(int mode){
		this.mode = mode;
		boolean bSucceed = false;
		switch(mode){
		case Mode.StartShow  :
			bSucceed = pcControler.startShow();
			System.out.println("start show");
			break;
		case Mode.EndShow:
			bSucceed = pcControler.endShow();
			System.out.println("end show");
			break;
		case Mode.PageDown:
			bSucceed = pcControler.pageDown();
			System.out.println("pagedown");
			break;
		case Mode.PageUp:
			bSucceed = pcControler.pageUp();
			System.out.println("pageup");
			break;
		case Mode.MouseMove:
			bSucceed = pcControler.mouseMove(x/20,- y/20);
			pcControler.drawPoint(x/20,- y/20,drawState.getDrawState());
			pcControler.moveMagnifier(x/20, -y/20);
			pcControler.setRound(z);
			break;
		case Mode.RightClk:
			bSucceed = pcControler.rightClick();
			break;
		case Mode.LeftClk:
			bSucceed = pcControler.leftClick();
			break;
		case Mode.StartFigure:
			bSucceed = pcControler.startFigure();
			System.out.println("start figure");
			break;
		case Mode.EndFigure:
			bSucceed = pcControler.endFigure();
			System.out.println("end figure");
			break;
		case Mode.StartMagnifier:
			bSucceed = pcControler.startMagnifier();
			System.out.println("start magnifier");
			break;
		case Mode.EndMagnifier:
			bSucceed = pcControler.endMagnifier();
			System.out.println("end magnifier");
			break;
		case Mode.MagnifierResize:
			bSucceed = pcControler.resizeMagnifier(magState.getState()*magResize/50);
			break;
		case Mode.MagnifierZoom:
			bSucceed = pcControler.zoomMagnifier(magState.getState());
			System.out.println("magnifier zoom changed "+magState.getState());
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
}
