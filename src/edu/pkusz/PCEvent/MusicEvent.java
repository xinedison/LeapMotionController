package edu.pkusz.PCEvent;

import edu.pkusz.PCEvent.MagnifierEvent.MagnifierThread;

public class MusicEvent {
	private boolean startMusic = false;
	public static void main(String[] args){
		
	}
	public MusicEvent(){
		
	}
	public boolean startMagnifier(){
		if(startMusic)
			return false;
		startMusic = true;
		return true;		
	}
	public boolean endMagnifier(){
		if(!startMusic)
			return false;
		startMusic = false;
		return true;		
	}
}
