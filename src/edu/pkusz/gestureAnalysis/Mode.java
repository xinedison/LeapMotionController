package edu.pkusz.gestureAnalysis;
/**
 * 标记各种不同的用例mode
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
public enum Mode {
	Nothing(0),StartShow(1), EndShow(2), PageDown(3), PageUp(4),
	MouseModeStart(5), MouseMove(6), RightClk(7),LeftClk(8),Wheel(9),
	StartMagnifier(10),EndMagnifier(11),MagnifierMove(12),
	MagnifierResize(13),MagnifierZoom(14),
	StartFigure(15),EndFigure(16),DrawPoint(17); 
	private int mode;
	private Mode(int mode){
		this.mode = mode;
	}
	public int getMode(){
		return mode;
	}
}
