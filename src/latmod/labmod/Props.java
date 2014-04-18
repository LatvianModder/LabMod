package latmod.labmod;
import com.google.gson.annotations.Expose;

public class Props
{
	@Expose public Integer logCount;
	@Expose public Float soundVolume;
	@Expose public Float rotSens;
	
	public void setDefaults()
	{
		if(logCount == null) logCount = 3;
		if(soundVolume == null) soundVolume = 1F;
		if(rotSens == null) rotSens = 1F;
	}
}