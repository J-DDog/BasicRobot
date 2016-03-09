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
		
		while(state != State.STOP)
		{
			
			switch(state)
			{
				case DRIVE:
					displayMessage("Drive");
					
					//If there is something in the way then change states to avoid
					if(checkSensor(.5)) 
					{
						//If the robot is moving stop
						displayMessage("If 1");
						if(botPilot.isMoving())
						{
							displayMessage("If 2");
							botPilot.stop();
						}
						state = State.AVOID;
						
					}
					else//If it's clear drive forward
					{
						displayMessage("Else 1");
						if(!botPilot.isMoving())
						{
							displayMessage("If 3");
							botPilot.forward();
						}
					}
					displayMessage("Break");
					break;
					
				case AVOID:
					displayMessage("Avoid");
					//Turn to the right and check if there is something in the way
					botPilot.rotate(60);
					
					//if there is something is in the way turn around.
					if(checkSensor(.5))
					{
						botPilot.rotate(-120);
						if(checkSensor(.5))
						{
							//Turn around and give up
							botPilot.rotate(-60);
							state = State.DRIVE;
						}
						else
						{
							botPilot.travel(200);
							botPilot.rotate(60);
							if(checkSensor(.5))
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
						if(checkSensor(.5))
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
			displayMessage("Wait");
			thisBot.getKeys();
			if(thisBot.getKeys().waitForAnyPress() == Keys.ID_ESCAPE)
			{
				displayMessage("Wait 1");
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
	
	private boolean checkSensor(double distance)
	{
		boolean isClear = true;
		displayMessage("Distance Check");
		distanceSensor.fetchSample(ultrasonicSamples, 0);
		if(ultrasonicSamples[0] < distance)
		{
			isClear = false;
		}
		return isClear;
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
