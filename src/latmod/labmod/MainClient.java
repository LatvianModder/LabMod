package latmod.labmod;
import latmod.labmod.client.particles.Particle;
import latmod.labmod.world.*;

public class MainClient
{
	public static MainClient inst;
	public WorldClient worldSP;
	
	public boolean isCreated()
	{ return worldSP != null; }
	
	public boolean isRunning()
	{ return isCreated() && worldSP.client != null && !worldSP.client.isFailed && !worldSP.client.isConnecting; }
	
	public void create()
	{
		worldSP = new WorldClient();
	}
	
	public void start(String ip)
	{
		if(isRunning()) return;
		if(!isCreated()) create();
		int port = GameOptions.DEF_PORT;
		worldSP.start(ip, port);
	}
	
	public void stop(boolean instant)
	{
		if(instant)
		{
			if(!isCreated()) return;
			worldSP.onStopped();
			worldSP = null;
		}
		else ClientUtils.inst.destroyClient = true;
	}

	public boolean hasPlayer()
	{ return isRunning() && worldSP.playerSP != null; }

	public void spawnParticle(Particle p)
	{ if(worldSP != null) worldSP.particles[p.getLayer().INDEX].add(p); }
}