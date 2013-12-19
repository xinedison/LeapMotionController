package edu.pkusz.gestureAnalysis;
/**
 * the draw state记录画图的状态信息
 * 0:nothing
 * 1:draw point
 * 2:draw rec
 */
public enum DrawState {
	Nothing(0),DrawPoint(1),DrawSet(2),DrawFinish(3);
	private int drawState;
	private DrawState(int state){
		this.drawState = state;
	}
	public int getDrawState(){
		return this.drawState;
	}
}
