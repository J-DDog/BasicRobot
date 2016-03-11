package bot.model;

import bot.model.DataExchange.EV3State;
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

public class EV3Bot extends Thread
{
	
	private String botMessage;
	private int xPosition;
	private int yPosition;
	private long waitTime;
	private float[] ultrasonicSamples;
	
	private DataExchange DE;
	private MovePilot botPilot;
	private EV3UltrasonicSensor distanceSensor;
	
	public EV3Bot(DataExchange DE)
	{
		this.DE = DE;
		this.botMessage = "Jared code JohnBot";
		this.xPosition = 1;
		this.yPosition = 2;
		this.waitTime = 4000;
		
		distanceSensor = new EV3UltrasonicSensor(LocalEV3.get().getPort("S1"));
		distanceSensor.getDistanceMode();
		
		setupPilot();
		displayMessage();
	}
	
	public void run()
	{
		driveRoomRand();
		danceTime();
		DE.finish();
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
	
	private void driveRoomRand()
	{
		ultrasonicSamples = new float [distanceSensor.sampleSize()];
		
		while(DE.getEV3State() != EV3State.STOP)
		{
			
			switch(DE.getEV3State())
			{
				case DRIVE:
					displayMessage("Drive");
					
					//If there is something in the way then change states to avoid
					if(checkSensor(.5)) 
					{
						//If the robot is moving stop
						if(botPilot.isMoving())
						{
							botPilot.stop();
						}
						if(DE.getEV3State() != EV3State.STOP)
						{
							DE.setEV3State(EV3State.AVOID);
						}
						
						
					}
					else//If it's clear drive forward
					{
						
						botPilot.forward();
						
					}
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
							if(DE.getEV3State() != EV3State.STOP)
							{
								DE.setEV3State(EV3State.DRIVE);
							}
						}
						else
						{
							botPilot.travel(200);
							botPilot.rotate(60);
							if(checkSensor(.5))
							{
								//Turn around and give up
								botPilot.rotate(120);
								if(DE.getEV3State() != EV3State.STOP)
								{
									DE.setEV3State(EV3State.DRIVE);
								}
							}
							else
							{
								//Continue on its way
								if(DE.getEV3State() != EV3State.STOP)
								{
									DE.setEV3State(EV3State.DRIVE);
								}
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
							
						}
						if(DE.getEV3State() != EV3State.STOP)
						{
							DE.setEV3State(EV3State.DRIVE);
						}
						
					}
					break;
				default:
					break;
					
			}
			
		}
		
	}
	
	private void danceTime()
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
		if(ultrasonicSamples[0] > distance)
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
