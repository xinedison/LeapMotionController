package edu.pkusz.gestureAnalysis;


import edu.pkusz.PCEvent.PCControler;

public class EventCaller {
	private int mode;
	private MouseState state;
	public DrawState drawState = DrawState.Nothing;
	private int x;
	private int y;
	private int z;
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
		}
		return true;
	}
	public boolean callEvent(int mode){
		this.mode = mode;
		switch(mode){
		case Mode.StartShow  :
			pcControler.startShow();
			System.out.println("start show");
			break;
		case Mode.EndShow:
			pcControler.endShow();
			System.out.println("end show");
			break;
		case Mode.PageDown:
			pcControler.pageDown();
			System.out.println("pagedown");
			break;
		case Mode.PageUp:
			pcControler.pageUp();
			System.out.println("pageup");
			break;
		case Mode.MouseMove:
//			System.out.println(x+"\t"+y);
			pcControler.mouseMove(x/20,- y/20);
			pcControler.drawPoint(x/20,- y/20,drawState.getDrawState());
			pcControler.moveMagnifier(x/20, -y/20);
			pcControler.setRound(z);
			break;
		case Mode.RightClk:
			pcControler.rightClick();
			break;
		case Mode.LeftClk:
			pcControler.leftClick();
			break;
		case Mode.StartFigure:
			pcControler.startFigure();
			System.out.println("start figure");
			break;
		case Mode.EndFigure:
			pcControler.endFigure();
			System.out.println("end figure");
			break;
		case Mode.StartMagnifier:
			pcControler.startMagnifier();
			System.out.println("start magnifier");
			break;
		case Mode.EndMagnifier:
			pcControler.endMagnifier();
			System.out.println("end magnifier");
			break;
		default :break;
		}
		return true;
	}
	public void setParam(int x,int y,int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
