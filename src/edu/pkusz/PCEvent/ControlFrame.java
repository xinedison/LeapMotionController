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
	private Point pointScreen;	// �������Ļ��λ��
	private MouseLocThread mouseLocThread;
	private static int modeId = 0;		//ģʽ��id
	/*ģʽ
	 * 0:ѡ�����
	 * 1��ѡ��Ŵ�
	 * 2��ѡ�񻭱�
	 */
	private static int drawId = 0;		//ģʽ��id
	/*����
	 * 0:����
	 * 1��ֱ��
	 * 2������
	 * 3��Բ��
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

	/*����������ģʽ*/
	public   void setMode(int id){		
		modeId = id;
	}
	/*����getMode*/
	public int getMode(){
		return modeId;
	}
	
	/*���������û���ģʽ*/
	public   void setDraw(int id){		
		drawId = id;
	}
	/*����getDraw*/
	public int getDraw(){
		return drawId;
	}
	/*��ʼ��ͼ��*/
	public void initMenu(){
		//MenuMode���
		frame = new JFrame("Hello");
		JPanel panelMenu = new JPanel();
		panelMenu.setLayout(new GridLayout(3,1,0,0));
		frame.setLocation(1,java.awt.Toolkit.getDefaultToolkit().getScreenSize().height/2-frameHeight/2);
		frame.setSize(frameWidth, frameHeight);
		frame.setUndecorated(true);
		frame.setAlwaysOnTop(true);
		frame.setOpacity(0.8f);
		//��ʼ��ModeͼƬ
		 iconModeSmall[0] = new ImageIcon("PicResource\\mouseSmall.png");
		 iconModeBig[0] = new ImageIcon("PicResource\\mouseBig.png");
		 iconModeSmall[1] = new ImageIcon("PicResource\\magniferSmall.png");
		 iconModeBig[1] = new ImageIcon("PicResource\\magniferBig.png");
		 iconModeSmall[2] = new ImageIcon("PicResource\\penSmall.png");
		 iconModeBig[2] = new ImageIcon("PicResource\\penBig.png");
		 //modeButton����
		 for(int i = 0;i < buttonModeNum;i++){
			 buttonModeArray[i] = new JButton(iconModeSmall[i]);
			 buttonModeArray[i].setBackground(new Color(0, 0, 0));
			 panelMenu.add(buttonModeArray[i]);
		 }
		Container contMenu = frame.getContentPane();
		contMenu.add(panelMenu);
		frame.setVisible(true);	
		
		//Draw���
		frameDraw = new JFrame();
		JPanel panelDraw = new JPanel();
		panelDraw.setLayout(new GridLayout(1,4,0,0));
		frameDraw.setLocation(860,1);
		frameDraw.setSize(frameDrawWidth, frameDrawHeight);
		frameDraw.setUndecorated(true);
		frameDraw.setAlwaysOnTop(true);
		frameDraw.setOpacity(0.8f);
		//��ʼ��DrawͼƬ
		 iconDrawSmall[0] = new ImageIcon("PicResource\\pencilSmall.png");
		 iconDrawBig[0] = new ImageIcon("PicResource\\pencilBig.png");
		 iconDrawSmall[1] = new ImageIcon("PicResource\\lineSmall.png");
		 iconDrawBig[1] = new ImageIcon("PicResource\\lineBig.png");
		 iconDrawSmall[2] = new ImageIcon("PicResource\\rectSmall.png");
		 iconDrawBig[2] = new ImageIcon("PicResource\\rectBig.png");
		 iconDrawSmall[3] = new ImageIcon("PicResource\\circleSmall.png");
		 iconDrawBig[3] = new ImageIcon("PicResource\\circleBig.png");
		 //����Button����
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
		//ѭ��ΪbuttonMode�����Ӧ��������뿪
		for (buttonIndex =0;buttonIndex<buttonModeNum;buttonIndex++){ 
			buttonModeArray[buttonIndex].addMouseListener(new MouseAdapter() {
				private int index = buttonIndex;
				@Override//����뿪button,�����ޱ���
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
	//				buttonModeArray[index].setIcon(iconModeSmall[index]);
			//		System.out.println("leave Modebutton"+index);
				}
				@Override//������button��ͼ��������modeId
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
		//ΪbuttonDraw�����Ӧ
		for (drawIndex =0;drawIndex < buttonDrawNum;drawIndex++){ 
			buttonDrawArray[drawIndex].addMouseListener(new MouseAdapter() {
				private int index = drawIndex;
				@Override//����뿪button,�����ޱ���
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
			//		System.out.println("leave Drawbutton"+index);
				}
				@Override//������button��ͼ��������modeId
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
	
	//ˢ�²���������꿿����Ļ��࣬�˵����֣���֮����
		public void updateFrame(){
			pointScreen = MouseInfo.getPointerInfo().getLocation();
			//Menu����
			if(pointScreen.x < frameWidth){
				frame.setLocation(1, java.awt.Toolkit.getDefaultToolkit().getScreenSize().height/2-frameHeight/2);
				//return;
			}
			else{
				try{															//frame��ʱ��ʧ
					System.out.println("going to sleep");
	//				Thread.sleep(1000);
	               }catch(Exception e){
	                   e.printStackTrace();
	                }
				frame.setLocation(-frameWidth+5, java.awt.Toolkit.getDefaultToolkit().getScreenSize().height/2-frameHeight/2);
			}
			//Draw�������
			if(modeId == 2 && pointScreen.y < frameDrawHeight)
				frameDraw.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-frameDrawWidth/2,1);
			else{
				try{															//frame��ʱ��ʧ
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
	//�ڲ��� mouseThread
		class  MouseLocThread implements Runnable{
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//ѡ��ִ��
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
		//���߳�mouseLocThread
		mouseLocThread = new MouseLocThread();
		new Thread(mouseLocThread).start();
	}
	public static void main(String args[]){
		ControlFrame controlFrame = new ControlFrame();
	}
}