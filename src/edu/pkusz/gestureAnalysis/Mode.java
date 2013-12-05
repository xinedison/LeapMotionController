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
 * 15:up hand   (start figure or change music volume)
 * 16:down hand(end figure or change music volume)
 * 17:drawPoint	
*/
public class Mode {
	public static final int Nothing = 0;
	public static final int StretchHand = 1;
	public static final int CloseHand = 2;
	public static final int HandLeft = 3;
	public static final int HandRight =4;
	public static final int MouseModeStart = 5;
	public static final int FingerMove = 6;
	public static final int RightClk = 7;
	public static final int LeftClk = 8;
//	public static final int Wheel = 9;
	public static final int TwoFingerAway = 10;
	public static final int TwoFingerApproach = 11;
//	public static final int MagnifierMove = 12;
	public static final int TwoHandAway = 13;
	public static final int MagnifierZoom = 14;
	public static final int UpHand = 15;
	public static final int DownHand = 16;
//	public static final int DrawPoint = 17; 
	public static final int TwoFingerPoke = 18;
	public static int getModeByIndex(int index){
		switch(index){
		case 0:return  Nothing;
		case 1: return StretchHand;
		case 2:return CloseHand;
		case 3:return HandLeft;
		case 4:return HandRight;
		case 5:return MouseModeStart;
		case 6:return FingerMove;
		case 7:return RightClk;
		case 8:return LeftClk;
//		case 9:return Wheel;
		case 10:return TwoFingerAway;
		case 11:return TwoFingerApproach;
//		case 12:return MagnifierMove;
		case 13:return TwoHandAway;
		case 14:return MagnifierZoom;
		case 15:return UpHand;
		case 16:return DownHand;
//		case 17:return DrawPoint;	
		case 18:return TwoFingerPoke;
		default :return Nothing;
		}
	}
}
