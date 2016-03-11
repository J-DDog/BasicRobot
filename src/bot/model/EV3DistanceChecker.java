package bot.model;

import lejos.hardware.sensor.EV3UltrasonicSensor;

public class EV3DistanceChecker extends Thread
{
	
	private DataExchange DE;
	private EV3UltrasonicSensor distanceSensor;
	
	private float[] ultrasonicSamples;
	
	public EV3DistanceChecker(DataExchange DE)
	{
		this.DE = DE;
	}
	
}
