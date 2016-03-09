package bot.model;

import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.utility.Delay;

public class EV3Bot {
	
	public enum State
	{
		DRIVE, AVOID, LOADING, STOP
	}
	
	private String botMessage;
	private int xPosition;
	private int yPosition;
	private long waitTime;
	private float[] ultrasonicSamples;
	private State state;
	
	private MovePilot botPilot;
	private EV3UltrasonicSensor distanceSensor;
	private EV3 thisBot;
	private EV3TouchSensor backTouch;
	
	public EV3Bot()
	{
		this.botMessage = "Jared code JohnBot";
		this.xPosition = 1;
		this.yPosition = 2;
		this.waitTime = 4000;
		
		distanceSensor = new EV3UltrasonicSensor(LocalEV3.get().getPort("S1"));
		distanceSensor.getDistanceMode();
		backTouch = new EV3TouchSensor(LocalEV3.get().getPort("S2"));
		thisBot = LocalEV3.get();
		state = State.DRIVE;
		
		setupPilot();
		displayMessage();
	}
	


	private void setupPilot() 
	{
		Wheel leftWheel = WheeledChassis.modelWheel(Motor.A, 43.3).offset(-72);
		Wheel rightWheel = WheeledChassis.modelWheel(Motor.B, 43.3).offset(72);
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
		
		if(false) //From back of Room
		{
			botPilot.rotate(-10);
			botPilot.travel(3810);
			botPilot.rotate(-50);
			botPilot.travel(5000);
			botPilot.rotate(60);
			botPilot.travel(3048);
			botPilot.rotate(-60);
			botPilot.travel(600);
		}
		else //From Front of Room
		{
			botPilot.travel(400);
			botPilot.rotate(60);
			botPilot.travel(3048);
			botPilot.rotate(-60);
			botPilot.travel(5100);
			botPilot.rotate(60);
			botPilot.travel(3810);
			
		}
		
	}
	
	public void driveRoomRand()
	{
		ultrasonicSamples = new float [distanceSensor.sampleSize()];
		distanceSensor.fetchSample(ultrasonicSamples, 0);
		
		while(state != State.STOP)
		{
			
			switch(this.state)
			{
				case DRIVE:
					displayMessage("Drive");
					//If there is something in the way then change states to avoid
					if(ultrasonicSamples[0] < .2) 
					{
						//If the robot is moving stop
						if(botPilot.isMoving())
						{
							botPilot.stop();
						}
						state = State.AVOID;
						
					}
					else//If it's clear drive forward
					{
						if(!botPilot.isMoving())
						{
							botPilot.forward();
						}
					}
					break;
					
				case AVOID:
					displayMessage("Avoid");
					//Turn to the right and check if there is something in the way
					botPilot.rotate(60);
					distanceSensor.fetchSample(ultrasonicSamples, 0);
					
					//if there is something is in the way turn around.
					if(ultrasonicSamples[0] < .2)
					{
						botPilot.rotate(-120);
						distanceSensor.fetchSample(ultrasonicSamples, 0);
						if(ultrasonicSamples[0] < .2)
						{
							//Turn around and give up
							botPilot.rotate(-60);
							state = State.DRIVE;
						}
						else
						{
							botPilot.travel(200);
							botPilot.rotate(60);
							distanceSensor.fetchSample(ultrasonicSamples, 0);
							if(ultrasonicSamples[0] < .2)
							{
								//Turn around and give up
								botPilot.rotate(120);
								state = State.DRIVE;
							}
							else
							{
								//Continue on its way
								state = State.DRIVE;
							}
						}
					}
					else
					{
						//Drive forward to get past the object
						botPilot.travel(200);
						//Turn to pas it and check if its still there
						botPilot.rotate(-60);
						distanceSensor.fetchSample(ultrasonicSamples, 0);
						if(ultrasonicSamples[0] < .2)
						{
							//turn around and give up
							botPilot.rotate(120);
							state = State.DRIVE;
						}
						else
						{
							//Start going
							state = State.DRIVE;
						}
						
					}
					break;
			}
			distanceSensor.fetchSample(ultrasonicSamples, 0);
			thisBot.getKeys();
			if(thisBot.getKeys().waitForAnyPress() == Keys.ID_ESCAPE)
			{
				state = State.STOP;
			}
		}
		
	}

	
	
	public void danceTime()
	{
		displayMessage("DANCE TIME™");
		
		for(int repeats = 3; repeats > 0; repeats--)
		{
			
			if(repeats%2 == 1)
			{
				botPilot.rotate(10);
				botPilot.rotate(-10);
				botPilot.rotate(360);
			}
			else
			{
				botPilot.rotate(-10);
				botPilot.rotate(10);
				botPilot.rotate(-360);
			}
			
		}
		
	}
	
	
	private void displayMessage() 
	{
		LCD.clearDisplay();
		LCD.drawString(botMessage, xPosition, yPosition);
		Delay.msDelay(waitTime);
		//Delay
	}
	
	private void displayMessage(String message) 
	{
		LCD.clearDisplay();
		LCD.drawString(message, xPosition, yPosition);
		Delay.msDelay(waitTime);
	}
	
}
