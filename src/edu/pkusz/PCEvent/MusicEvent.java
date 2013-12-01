package edu.pkusz.PCEvent;
import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.File; 
import java.util.Vector;

import javax.media.Manager; 
import javax.media.MediaLocator; 
import javax.media.Player; 
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MusicEvent extends JFrame{
	private boolean startMusic = false;
	private Player player = null; 
	private File musicfile;
	private int state = 0;	//0未开始播放 1开始播放 2暂停播放
	private int windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
	private int windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
//	private int windowWidth = 500;
//	private int windowHeight = 300;
	private BackgroundPanel backgroundPanel = new BackgroundPanel();
	private Container container = getContentPane();	
	private float volume = 0.2f;
	
	public static void main(String[] args){
		try {
			MusicEvent musicEvent = new MusicEvent();
			musicEvent.startMusic();
			musicEvent.startBackground();
			Thread.sleep(1000);
			musicEvent.endBackground();
			Thread.sleep(5000);
			musicEvent.endMusic();
			Thread.sleep(5000);
//			musicEvent.startMusic();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public MusicEvent(){
		container.add(backgroundPanel);	
		this.setUndecorated(true);	// 设置窗体样式
	    this.setResizable(false);
	    this.setVisible(false);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setSize(windowWidth,windowHeight);
		this.setAlwaysOnTop(true);//设置窗体总在最顶层
	}
	public boolean startMusic(){
		if(startMusic)
			return false;
		startMusic = true;
		playMusic();
		return true;		
	}
	public boolean endMusic(){
		if(!startMusic)
			return false;
		endBackground();
		stop();
		startMusic = false;
		return true;		
	}
	public boolean startBackground(){
		if(!startMusic)
			return false;
		this.setVisible(true);
		return true;
	}
	public boolean endBackground(){
		if(!startMusic)
			return false;
		this.setVisible(false);
		return true;
	}
	public boolean playMusic(){
		musicfile = new File("MusicResource\\01.wav"); 
		try { 
			if(state == 2){
				player.start();
				state = 1;
				return true;
			}
			if (player == null) { 
				if (musicfile.exists()) { 
					MediaLocator locator = new MediaLocator("file:"+ musicfile.getAbsolutePath()); 
					player = Manager.createRealizedPlayer(locator); 
					player.prefetch();
				}
				else
					System.out.println("music file error");
			}
			player.start();// 开始播放 
			setVolumeUp();
			state = 1;
		}catch (Exception e) { 
			e.getStackTrace(); 
		} 
		return true;
	}
	public boolean stop(){
		if(state == 1){
			player.stop();
			state = 2;
			return true;
		}
		return false;
	}
	public boolean setVolume(double volume){
		if(state==0)
			return false;		
		if(volume<0.2f)
			volume = 0.2f;
		if(volume>0.6f)
			volume = 0.6f;
		this.volume = (float)volume;
		player.getGainControl().setLevel((float)volume);
		return true;
	}
	public boolean setVolumeUp(){
		if(state==0)
			return false;
		volume+=0.01f;
		if(volume<0.2f)
			volume = 0.2f;
		if(volume>0.6f)
			volume = 0.6f;
		System.out.println(volume);
		player.getGainControl().setLevel(volume);
		return true;
	}
	public boolean setVolumeDown(){
		if(state==0)
			return false;
		volume-=0.01f;
		if(volume<0.2f)
			volume = 0.2f;
		if(volume>0.6f)
			volume = 0.6f;
		player.getGainControl().setLevel(volume);
		return true;
	}
	public boolean nextBackground(){
		backgroundPanel.nextBackground();
		return true;
	}
}

class BackgroundPanel extends JPanel
{
    private Robot robot;
    private Image screenImage = null;
    private String []filename = new String[99];
    private File imgfile;
    private int imageIndex = 0;
    private int width = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
    private int height = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
    
    public BackgroundPanel(){
	    try{
	        robot = new Robot();
	        imgfile = new File("MusicResource\\01.jpg"); 
	        String filename = imgfile.getAbsolutePath();
			if (imgfile.exists())
				screenImage = Toolkit.getDefaultToolkit().createImage(filename);
	    }catch (AWTException e){}
    }
    public void paintComponent(Graphics g){
    	Graphics2D graphics = (Graphics2D) g;
	    super.paintComponent((Graphics2D)graphics);
	    graphics.drawImage(screenImage,0,0, width,height,this);
    }
    public void nextBackground(){
    	imageIndex++;
    	imgfile = new File("MusicResource\\01.jpg"); 
        String filename = imgfile.getAbsolutePath();
		if (imgfile.exists())
			screenImage = Toolkit.getDefaultToolkit().createImage(filename);
		else
			imageIndex = 0;
		repaint();
    }
}
