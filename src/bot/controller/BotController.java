package bot.controller;

import bot.model.DataExchange;
import bot.model.EV3Bot;
import bot.model.EV3ButtonListener;

/**
 * @author jker3169
 * @version 0.x Dec 16, 2015
 */
public class BotController 
{
	private DataExchange DE;
	private EV3ButtonListener EV3BL;
	private EV3Bot sillyBot;
	
	public BotController()
	{
		DE = new DataExchange();
		sillyBot = new EV3Bot(DE);
		EV3BL = new EV3ButtonListener(DE);
		EV3BL.start();
	}
	
	public void start()
	{
		//sillyBot.driveRoom();
		sillyBot.driveRoomRand();
		//sillyBot.danceTime();
	}
	
}
