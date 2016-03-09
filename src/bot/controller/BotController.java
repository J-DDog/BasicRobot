package bot.controller;

import bot.model.EV3Bot;

/**
 * @author jker3169
 * @version 0.x Dec 16, 2015
 */
public class BotController 
{
	
	private EV3Bot sillyBot;
	
	public BotController()
	{
		sillyBot = new EV3Bot();
		
	}
	
	public void start()
	{
		//sillyBot.driveRoom();
		sillyBot.driveRoomRand();
		//sillyBot.danceTime();
	}
	
}
