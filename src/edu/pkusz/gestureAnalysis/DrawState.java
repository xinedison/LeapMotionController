package edu.pkusz.gestureAnalysis;
/**
 * the draw state
 * 0:nothing
 * 1:draw point
 * 2:draw rec
 */
public enum DrawState {
	Nothing(0),DrawPoint(1),DrawRec(2);
	private int drawState;
	private DrawState(int state){
		this.drawState = state;
	}
	public int getDrawState(){
		return this.drawState;
	}
}
