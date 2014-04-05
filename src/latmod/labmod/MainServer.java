package latmod.labmod;
import latmod.core.util.MathHelper;
import latmod.labmod.world.*;

public class MainServer
{
	public static MainServer inst = null;
	public WorldServer worldMP;
	
	public boolean isCreated()
	{ return worldMP != null; }
	
	public boolean isRunning()
	{ return isCreated() && worldMP.server != null && worldMP.server.thread != null; }
	
	public void create()
	{
		worldMP = new WorldServer();
	}
	
	public void start(String customIP, int maxPlayers)
	{
		if(isRunning()) return;
		if(!isCreated()) create();
		worldMP.start(customIP, maxPlayers);
	}
	
	public void stop()
	{
		if(!isCreated()) return;
		worldMP.onStopped();
		worldMP = null;
	}
	
	public static final boolean isPort(String s)
	{
		if(!MathHelper.canParseInt(s)) return false;
		int i = MathHelper.toInt(s);
		if(i <= 0 || i >= 65563) return false;
		return true;
	}
}