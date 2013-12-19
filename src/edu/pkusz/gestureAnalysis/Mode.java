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
	public static final int TwoHandMove = 13;
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
		case 13:return TwoHandMove;
		case 14:return MagnifierZoom;
		case 15:return UpHand;
		case 16:return DownHand;
//		case 17:return DrawPoint;	
		case 18:return TwoFingerPoke;
		default :return Nothing;
		}
	}
	public static final double NothingOffset = 1;
	public static final double StretchHandOffset = 3;
	public static final double CloseHandOffset = 1;
	public static final double HandLeftOffset = 0.5;
	public static final double HandRightOffset = 0.2;
	public static final double MouseModeStartOffset = 1;
	public static final double FingerMoveOffset = 1;
	public static final double RightClkOffset = 1;
	public static final double LeftClkOffset = 1;
//	public static final int Wheel = 9;
	public static final double TwoFingerAwayOffset = 1;
	public static final double TwoFingerApproachOffset = 1;
//	public static final int MagnifierMove = 12;
	public static final double TwoHandMoveOffset = 1;
	public static final double MagnifierZoomOffset = 1;
	public static final double UpHandOffset = 0.5;
	public static final double DownHandOffset = 0.5;
//	public static final int DrawPoint = 17; 
	public static final double TwoFingerPokeOffset = 1;
	public static double getModeOffset(int index){
		switch(index){
		case 0:return  NothingOffset;
		case 1: return StretchHandOffset;
		case 2:return CloseHandOffset;
		case 3:return HandLeftOffset;
		case 4:return HandRightOffset;
		case 5:return MouseModeStartOffset;
		case 6:return FingerMoveOffset;
		case 7:return RightClkOffset;
		case 8:return LeftClkOffset;
//		case 9:return Wheel;
		case 10:return TwoFingerAwayOffset;
		case 11:return TwoFingerApproachOffset;
//		case 12:return MagnifierMove;
		case 13:return TwoHandMoveOffset;
		case 14:return MagnifierZoomOffset;
		case 15:return UpHandOffset;
		case 16:return DownHandOffset;
//		case 17:return DrawPoint;	
		case 18:return TwoFingerPokeOffset;
		default :return NothingOffset;
		}
	}
	private static final  int defaultMin = 20;
	public static final int NothingMin = defaultMin;
	public static final int StretchHandMin = defaultMin;
	public static final int CloseHandMin = defaultMin;
	public static final int HandLeftMin = defaultMin;
	public static final int HandRightMin = defaultMin;
	public static final int MouseModeStartMin = defaultMin;
	public static final int FingerMoveMin = defaultMin;
	public static final int RightClkMin = defaultMin;
	public static final int LeftClkMin = defaultMin;
	public static final int TwoFingerAwayMin = defaultMin;
	public static final int TwoFingerApproachMin = defaultMin;
	public static final int TwoHandMoveMin = defaultMin;
	public static final int MagnifierZoomMin = defaultMin;
	public static final int UpHandMin = defaultMin;
	public static final int DownHandMin = defaultMin;
//	public static final int DrawPoint = defaultMin; 
	public static final int TwoFingerPokeMin = defaultMin;
	public static int getModeMin(int index){
		switch(index){
		case 0:return  NothingMin;
		case 1: return StretchHandMin;
		case 2:return CloseHandMin;
		case 3:return HandLeftMin;
		case 4:return HandRightMin;
		case 5:return MouseModeStartMin;
		case 6:return FingerMoveMin;
		case 7:return RightClkMin;
		case 8:return LeftClkMin;
		case 10:return TwoFingerAwayMin;
		case 11:return TwoFingerApproachMin;
		case 13:return TwoHandMoveMin;
		case 14:return MagnifierZoomMin;
		case 15:return UpHandMin;
		case 16:return DownHandMin;
		case 18:return TwoFingerPokeMin;
		default :return NothingMin;
		}
	}	
}
