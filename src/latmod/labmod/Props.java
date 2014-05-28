package latmod.labmod;
import com.google.gson.annotations.Expose;

public class Props
{
	@Expose public Integer logCount;
	@Expose public Double soundVolume;
	@Expose public Double rotSens;
	
	public void setDefaults()
	{
		if(logCount == null) logCount = 3;
		if(soundVolume == null) soundVolume = 1D;
		if(rotSens == null) rotSens = 1D;
	}
}