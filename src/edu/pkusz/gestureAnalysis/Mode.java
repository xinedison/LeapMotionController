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
public class Mode {
	public static final int Nothing = 0;
	public static final int StartShow = 1;
	public static final int EndShow = 2;
	public static final int PageDown = 3;
	public static final int PageUp =4;
	public static final int MouseModeStart = 5;
	public static final int MouseMove = 6;
	public static final int RightClk = 7;
	public static final int LeftClk = 8;
	public static final int Wheel = 9;
	public static final int StartMagnifier = 10;
	public static final int EndMagnifier = 11;
	public static final int MagnifierMove = 12;
	public static final int MagnifierResize = 13;
	public static final int MagnifierZoom = 14;
	public static final int StartFigure = 15;
	public static final int EndFigure = 16;
	public static final int DrawPoint = 17; 
	public static int getModeByIndex(int index){
		switch(index){
		case 0:return  Nothing;
		case 1: return StartShow;
		case 2:return EndShow;
		case 3:return PageDown;
		case 4:return PageUp;
		case 5:return MouseModeStart;
		case 6:return MouseMove;
		case 7:return RightClk;
		case 8:return LeftClk;
		case 9:return Wheel;
		case 10:return StartMagnifier;
		case 11:return EndMagnifier;
		case 12:return MagnifierMove;
		case 13:return MagnifierResize;
		case 14:return MagnifierZoom;
		case 15:return StartFigure;
		case 16:return EndFigure;
		case 17:return DrawPoint;	
		default :return Nothing;
		}
	}
}
