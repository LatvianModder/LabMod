package latmod.labmod;
import com.google.gson.annotations.Expose;

public class Props
{
	@Expose public Integer logCount;
	@Expose public String username;
	@Expose public Float soundVolume;
	@Expose public String lastIP;
	@Expose public Float rotSens;
	
	public void setDefaults()
	{
		if(logCount == null) logCount = 3;
		if(username == null) username = "Player";
		if(soundVolume == null) soundVolume = 1F;
		if(lastIP == null) lastIP = "localhost";
		if(rotSens == null) rotSens = 1F;
		
		username = Main.getArg("-username", username);
	}
}