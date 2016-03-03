package bot.model;

import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.hardware.ev3.LocalEV3;
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
	private float[] ultrasonicSamples;
	
	private MovePilot botPilot;
	private EV3UltrasonicSensor distanceSensor;
	private EV3TouchSensor backTouch;
	
	public EV3Bot()
	{
		this.botMessage = "Jared codes JohnBot";
		this.xPosition = 50;
		this.yPosition = -50;
		this.waitTime = 4000;
		
		distanceSensor = new EV3UltrasonicSensor(LocalEV3.get().getPort("S1"));
		distanceSensor.getDistanceMode();
		backTouch = new EV3TouchSensor(LocalEV3.get().getPort("S2"));
		
		setupPilot();
		displayMessage();
	}
	


	private void setupPilot() 
	{
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.A, 43.3).offset(72);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.B, 43.3).offset(-72);
		WheeledChassis chassis = new WheeledChassis(new Wheel[]{leftWheel, rightWheel}, WheeledChassis.TYPE_DIFFERENTIAL);
		botPilot = new MovePilot(chassis);
	}

	@SuppressWarnings("unused")
	public void driveRoom()
	{
		displayMessage("driveRoom");
		
		ultrasonicSamples = new float [distanceSensor.sampleSize()];
		distanceSensor.fetchSample(ultrasonicSamples, 0);
		
//		if(ultrasonicSamples[0] < 2.3) 
//		{
//			botPilot.travel(20.00);
//			
//		}
//		else
//		{
//			botPilot.travel(254.00);
//		}
		
		if(true) //From back of Room
		{
			botPilot.rotate(10);
			botPilot.travel(3810);
			botPilot.rotate(50);
			botPilot.travel(5000);
			botPilot.rotate(-60);
			botPilot.travel(3048);
			botPilot.rotate(60);
			botPilot.travel(600);
		}
		else //From Front of Room
		{
			botPilot.travel(500);
			botPilot.rotate(-60);
			botPilot.travel(3048);
			botPilot.rotate(60);
			botPilot.travel(5100);
			botPilot.rotate(-60);
			botPilot.travel(3810);
			
		}
		danceTime();
	}

	private void danceTime()
	{
		for(int repeats = 3; repeats > 0; repeats--)
		{
			botPilot.rotate(10);
			botPilot.rotate(-10);
			botPilot.rotate(360);
			
		}
		
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
