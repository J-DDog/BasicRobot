package bot.model;

import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.utility.Delay;

public class EV3Bot {
	
	private String botMessage;
	private int xPosition;
	private int yPosition;
	private long waitTime;
	
	private MovePilot botPilot;
	private EV3UltrasonicSensor distanceSensor;
	private EV3TouchSensor ackTouch;
	
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
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.A, 43.3).offset(-72);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.A, 43.3).offset(-72);
		WheeledChassis chassis = new WheeledChassis(new Wheel[]{leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
		botPilot = new MovePilot(chassis);
	}

	public void driveRoom()
	{
		displayMessage("driveRoom");
	}

	
	private void displayMessage() 
	{
		LCD.drawString(botMessage, xPosition, yPosition);
		Delay.msDelay(waitTime);
		//Delay
	}
	
	private void displayMessage(String message) 
	{
		LCD.drawString(message, xPosition, yPosition);
		Delay.msDelay(waitTime);
	}
	
}
