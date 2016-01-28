package bot.model;

import lejos.robotics.navigation.MovePilot;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;

public class EV3Bot {
	
	private String botMessage;
	private int xPosition;
	private int yPosition;
	private long waitTime;
	private MovePilot botPilot;
	
	public EV3Bot()
	{
		this.botMessage = "Jared codes jaredBot";
		this.xPosition = 50;
		this.yPosition = 50;
		this.waitTime = 4000;
		
		setupPilot();
		displayMessage();
	}
	


	private void setupPilot() {
		// TODO Auto-generated method stub
		
	}

	public void driveRoom()
	{
		displayMessage("driveRoom");
	}

	
	private void displayMessage() 
	{
		LCD.drawString(botMessage, xPosition, yPosition);
		//Delay
	}
	
	private void displayMessage(String string) 
	{
		
		
	}
	
}
