package edu.pkusz.gestureAnalysis;
/**
 * 指示鼠标所属的状态
 * 0:noting
 * 1:left down
 * 2:left up
 * 3:right down
 * 4:right up
 * 5:wheel down
 * 6:wheel up
 * 7:click over
 */
public enum MouseState {
	Nothing(0),LeftDown(1),LeftUp(2),RightDown(3),RightUp(4),
	WheelDown(5),WheelUp(6),ClkOver(7);
	private int mouseState;
	private MouseState(int state){
		this.mouseState = state;
	}
	public int getMouseState(){
		return mouseState;
	}
}
