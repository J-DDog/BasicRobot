package bot.controller;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

import bot.model.EV3Bot;

/**
 * @author jker3169
 * @version 0.x Dec 16, 2015
 */
public class BotController 
{

	private String message;
	private int xPosition, yPosition;
	private long waitTime;
	
	private EV3Bot sillyBot;
	
	public BotController()
	{
		
		message = "Something about a robot";
		xPosition = 50;
		yPosition = 100;
		waitTime = 4000;
		
		sillyBot = new EV3Bot();
		
	}
	
	public void start()
	{
		LCD.drawString(message, xPosition, yPosition);
		Delay.msDelay(waitTime);
		sillyBot.driveRoom();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getxPosition() {
		return xPosition;
	}

	public void setxPosition(int xPosition) {
		this.xPosition = xPosition;
	}

	public int getyPosition() {
		return yPosition;
	}

	public void setyPosition(int yPosition) {
		this.yPosition = yPosition;
	}

	public long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}
	
	
	
}
