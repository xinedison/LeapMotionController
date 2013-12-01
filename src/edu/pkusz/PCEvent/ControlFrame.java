package edu.pkusz.PCEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

public class ControlFrame  {
	private Point pointScreen;	// 鼠标在屏幕的位置
	private MouseLocThread mouseLocThread;
	private static int modeId = 0;		//模式号id
	/*模式
	 * 0:选择鼠标
	 * 1：选择放大镜
	 * 2：选择画笔
	 */
	private static int drawId = 0;		//模式号id
	/*画板
	 * 0:画笔
	 * 1：直线
	 * 2：矩形
	 * 3：圆形
	 */
	private int frameWidth = 150;
	private int frameHeight = 240;
	private int frameDrawWidth = 400;
	private int frameDrawHeight = 80;
			
	private JFrame frame;
	private JFrame frameDraw;
	private int buttonModeNum =3;
	private int buttonDrawNum = 4;
	private JButton[] buttonModeArray = new JButton[buttonModeNum];
	private ImageIcon []iconModeSmall = new ImageIcon[buttonModeNum];
	private ImageIcon []iconModeBig = new ImageIcon[buttonModeNum];
	private JButton[] buttonDrawArray = new JButton[buttonDrawNum];
	private ImageIcon []iconDrawSmall = new ImageIcon[buttonDrawNum];
	private ImageIcon []iconDrawBig = new ImageIcon[buttonDrawNum];
	
	private int buttonIndex = 0;
	private int drawIndex = 0;

	/*方法：设置模式*/
	public   void setMode(int id){		
		modeId = id;
	}
	/*方法getMode*/
	public int getMode(){
		return modeId;
	}
	
	/*方法：设置画板模式*/
	public   void setDraw(int id){		
		drawId = id;
	}
	/*方法getDraw*/
	public int getDraw(){
		return drawId;
	}
	/*初始化图像*/
	public void initMenu(){
		//MenuMode面板
		frame = new JFrame("Hello");
		JPanel panelMenu = new JPanel();
		panelMenu.setLayout(new GridLayout(3,1,0,0));
		frame.setLocation(1,java.awt.Toolkit.getDefaultToolkit().getScreenSize().height/2-frameHeight/2);
		frame.setSize(frameWidth, frameHeight);
		frame.setUndecorated(true);
		frame.setAlwaysOnTop(true);
		frame.setOpacity(0.8f);
		//初始化Mode图片
		 iconModeSmall[0] = new ImageIcon("PicResource\\mouseSmall.png");
		 iconModeBig[0] = new ImageIcon("PicResource\\mouseBig.png");
		 iconModeSmall[1] = new ImageIcon("PicResource\\magniferSmall.png");
		 iconModeBig[1] = new ImageIcon("PicResource\\magniferBig.png");
		 iconModeSmall[2] = new ImageIcon("PicResource\\penSmall.png");
		 iconModeBig[2] = new ImageIcon("PicResource\\penBig.png");
		 //modeButton设置
		 for(int i = 0;i < buttonModeNum;i++){
			 buttonModeArray[i] = new JButton(iconModeSmall[i]);
			 buttonModeArray[i].setBackground(new Color(0, 0, 0));
			 panelMenu.add(buttonModeArray[i]);
		 }
		Container contMenu = frame.getContentPane();
		contMenu.add(panelMenu);
		frame.setVisible(true);	
		
		//Draw面板
		frameDraw = new JFrame();
		JPanel panelDraw = new JPanel();
		panelDraw.setLayout(new GridLayout(1,4,0,0));
		frameDraw.setLocation(860,1);
		frameDraw.setSize(frameDrawWidth, frameDrawHeight);
		frameDraw.setUndecorated(true);
		frameDraw.setAlwaysOnTop(true);
		frameDraw.setOpacity(0.8f);
		//初始化Draw图片
		 iconDrawSmall[0] = new ImageIcon("PicResource\\pencilSmall.png");
		 iconDrawBig[0] = new ImageIcon("PicResource\\pencilBig.png");
		 iconDrawSmall[1] = new ImageIcon("PicResource\\lineSmall.png");
		 iconDrawBig[1] = new ImageIcon("PicResource\\lineBig.png");
		 iconDrawSmall[2] = new ImageIcon("PicResource\\rectSmall.png");
		 iconDrawBig[2] = new ImageIcon("PicResource\\rectBig.png");
		 iconDrawSmall[3] = new ImageIcon("PicResource\\circleSmall.png");
		 iconDrawBig[3] = new ImageIcon("PicResource\\circleBig.png");
		 //画板Button设置
		 for(int i = 0;i < buttonDrawNum;i++){
			 buttonDrawArray[i] = new JButton(iconDrawSmall[i]);
			 buttonDrawArray[i].setBackground(new Color(0, 0, 0));
			 panelDraw.add(buttonDrawArray[i]);
		 }
		Container contDraw = frameDraw.getContentPane();
		contDraw.add(panelDraw);
		frameDraw.setVisible(true);	
	}
	public void addButtonListener(){
		//循环为buttonMode添加响应，进入和离开
		for (buttonIndex =0;buttonIndex<buttonModeNum;buttonIndex++){ 
			buttonModeArray[buttonIndex].addMouseListener(new MouseAdapter() {
				private int index = buttonIndex;
				@Override//鼠标离开button,设置无背景
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
	//				buttonModeArray[index].setIcon(iconModeSmall[index]);
			//		System.out.println("leave Modebutton"+index);
				}
				@Override//鼠标进入button，图标变大，设置modeId
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					for(int i = 0;i < buttonModeNum;i++){
						if(i == index)
							buttonModeArray[i].setIcon(iconModeBig[i]);
						else
							buttonModeArray[i].setIcon(iconModeSmall[i]);

					}
					setMode(index);
		//			System.out.println("enter Modebutton" + index);
				}
			});
		}
		//为buttonDraw添加响应
		for (drawIndex =0;drawIndex < buttonDrawNum;drawIndex++){ 
			buttonDrawArray[drawIndex].addMouseListener(new MouseAdapter() {
				private int index = drawIndex;
				@Override//鼠标离开button,设置无背景
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
			//		System.out.println("leave Drawbutton"+index);
				}
				@Override//鼠标进入button，图标变大，设置modeId
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					for(int i = 0;i < buttonDrawNum;i++){
						if(i == index)
							buttonDrawArray[i].setIcon(iconDrawBig[i]);
						else
							buttonDrawArray[i].setIcon(iconDrawSmall[i]);

					}
					setDraw(index);
			//		System.out.println("enter Drawbutton" + index);
				}
			});
		}
	}
	
	//刷新操作，若鼠标靠近屏幕左侧，菜单出现；反之隐藏
		public void updateFrame(){
			pointScreen = MouseInfo.getPointerInfo().getLocation();
			//Menu隐藏
			if(pointScreen.x < frameWidth){
				frame.setLocation(1, java.awt.Toolkit.getDefaultToolkit().getScreenSize().height/2-frameHeight/2);
				//return;
			}
			else{
				try{															//frame延时消失
					System.out.println("going to sleep");
	//				Thread.sleep(1000);
	               }catch(Exception e){
	                   e.printStackTrace();
	                }
				frame.setLocation(-frameWidth+5, java.awt.Toolkit.getDefaultToolkit().getScreenSize().height/2-frameHeight/2);
			}
			//Draw面板隐藏
			if(modeId == 2 && pointScreen.y < frameDrawHeight)
				frameDraw.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-frameDrawWidth/2,1);
			else{
				try{															//frame延时消失
					System.out.println("going to sleep");
		//			Thread.sleep(1000);
		               }catch(Exception e){
		                   e.printStackTrace();
		                }
					frameDraw.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-frameDrawWidth/2, -frameDrawHeight+5);
			}
			System.out.println("Hello,java"+"modeId="+modeId);
			System.out.println("Hello,java"+"DrawId="+drawId);
			
		}	
	//内部类 mouseThread
		class  MouseLocThread implements Runnable{
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//选择执行
				while(true){
					updateFrame();
					try{
		                   Thread.sleep(1000);
		               }catch(Exception e){
		                   e.printStackTrace();
		                }
					}	 
				}
		}	
	public  ControlFrame(){
		initMenu();
		addButtonListener();
		//开线程mouseLocThread
		mouseLocThread = new MouseLocThread();
		new Thread(mouseLocThread).start();
	}
	public static void main(String args[]){
		ControlFrame controlFrame = new ControlFrame();
	}
}