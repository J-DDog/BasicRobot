package bot.model;

import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import bot.model.DataExchange.EV3State;

public class EV3ButtonListener extends Thread
{
	private DataExchange DE;
	private EV3 thisBot;
	
	public EV3ButtonListener(DataExchange DE)
	{
		this.DE = DE;
		thisBot = LocalEV3.get();
	}
	
	public void run()
	{
		thisBot.getKeys();
		if(thisBot.getKeys().waitForAnyPress() == Keys.ID_ESCAPE)
		{
			DE.setEV3State(EV3State.STOP);
		}
	}
	
}
