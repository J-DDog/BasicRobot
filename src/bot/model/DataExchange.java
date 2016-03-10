package bot.model;

public class DataExchange
{
	
	public enum EV3State
	{
		DRIVE, AVOID, LOADING, STOP
	}
	
	private boolean exitClicked = false;
	private EV3State state = EV3State.DRIVE;
	
	public DataExchange(){}
	
	public void setExitClicked(boolean status)
	{
		this.exitClicked = status;
	}
	
	public boolean getExitClicked()
	{
		return exitClicked;
	}
	
	public void setEV3State(EV3State state)
	{
		this.state = state;
	}
	
	public EV3State getEV3State()
	{
		return state;
	}
	
}
