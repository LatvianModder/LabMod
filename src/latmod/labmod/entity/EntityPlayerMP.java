package latmod.labmod.entity;
import latmod.core.util.*;
import latmod.labmod.world.*;

public class EntityPlayerMP extends EntityPlayer
{
	public long tick = 0L;
	public long lastUpdate = 0L;
	
	public EntityPlayerMP(World w, int pid)
	{
		super(w, pid);
	}
	
	public void onUpdate(Timer t)
	{
		if(tick - lastUpdate >= t.seconds(5))
		{
			worldObj.removePlayer(this);
		}
		
		tick++;
	}
}