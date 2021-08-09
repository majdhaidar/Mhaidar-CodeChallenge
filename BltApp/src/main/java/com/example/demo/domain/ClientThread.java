package com.example.demo.domain;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

import javax.microedition.io.StreamConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientThread extends Thread {

	private StreamConnection mStreamConnection = null;
	private static Robot robot; 
	
	Logger logger = LoggerFactory.getLogger(ClientThread.class);
	
	public ClientThread(StreamConnection sc) {
		mStreamConnection = sc;
		
		
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(mStreamConnection.openInputStream()));
			logger.info("Start reading data...");
			logger.info("tid: "+this.getId()+" "+this.getName());
			String line ="";
			while (!(line = bufferedReader.readLine()).equals("exit")) {/* !"exit".equals(bufferedReader.readLine())) { */
				/* String line = bufferedReader.readLine(); */
				performAction(line);
			}
			if("exit".equals(bufferedReader.readLine())) {
				mStreamConnection.close();
				bufferedReader.close();
			}		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("ERROR: ClientThread - run() method");
		}
	}

	private void performAction(String line) {
		try {

			if (line.contains(",")) {
				float movex = Float.parseFloat(line.split(",")[0]);// extract movement in x direction
				float movey = Float.parseFloat(line.split(",")[1]);// extract movement in y direction
				Point point = MouseInfo.getPointerInfo().getLocation(); // Get current mouse position
				float nowx = point.x;
				float nowy = point.y;
				robot.mouseMove((int) (nowx + movex), (int) (nowy + movey));// Move mouse pointer to new location
				logger.info("User Action: Mouse Pointer Move Location");
			} else if (line.contains("single_click")) {
				// first click
				robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				logger.info("User Action: Mouse Click Action");
			} else if(line.length()>0){
				try {
					Field field = KeyEvent.class.getField("VK_" + line.toUpperCase());
					robot.keyPress(field.getInt(null));
					logger.info("User Action: Press Key");
				} catch (Exception e) {
					logger.error("ERROR: ClientThread - performAction() method - Reading Keyboard Input");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("ERROR: ClientThread - performAction() method");
		}

	}

}
