package edu.pkusz.gestureAnalysis;

public enum MagnifierState {
	Nothing(0),Enlarge(1),Shrink(-1);
	private int state;
	private MagnifierState(int state){
		this.state = state;
	}
	public int getState(){
		return state;
	}
}
