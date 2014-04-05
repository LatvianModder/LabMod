package latmod.labmod.world;
import java.util.*;

import latmod.core.util.*;
import latmod.core.util.Timer;
import latmod.labmod.entity.*;
import latmod.labmod.net.*;
import latmod.labmod.net.packets.PacketPlayerLogout;

public abstract class World // WorldServer // WorldClient
{
	public final Side side;
	public String serverName = "Loading...";
	public FastList<AABB> AABBList = new FastList<AABB>();
	
	public FastMap<Integer, Entity> entities = new FastMap<Integer, Entity>();
	public FastMap<Integer, Entity> entitiesToBeAdded = new FastMap<Integer, Entity>();
	
	public FastMap<Integer, EntityPlayerMP> playerMap = new FastMap<Integer, EntityPlayerMP>();
	
	public Random rand = new Random();
	public long tick = 0L;
	
	public long packetsReceived = 0L;
	public long packetsSent = 0L;
	
	public World(Side s)
	{
		side = s;
	}
	
	public abstract INet getNet();
	public abstract void sendPacket(Packet p);
	public abstract void spawnEntity(Entity e);
	public abstract void removeEntity(int wid);
	public abstract void onUpdate(Timer t);
	public abstract void onClientData(NetClient c) throws Exception;
	public abstract void processPacket(Packet p);
	
	public void onStopped()
	{
	}
	
	public final void resetCollisionBoxes()
	{
		AABBList.clear();
		for(Entity e : entities)
		e.addCollisionBoxes(AABBList);
	}
	
	public final AABB getAABBAtPoint(float x, float y, float z)
	{ return AABB.getAABBAtPoint(AABBList, x, y, z); }
	
	public final AABB getAABBInBox(AABB pos, float x, float y, float z)
	{ return AABB.getAABBInBox(AABBList, pos, x, y, z); }
	
	public final FastList<AABB> getAllAABBsInBox(AABB pos, float x, float y, float z)
	{ return AABB.getAllAABBsInBox(AABBList, pos, x, y, z); }
	
	public final FastList<AABB> getAllAABBsAtPoint(float x, float y, float z)
	{ return AABB.getAllAABBsAtPoint(AABBList, x, y, z); }
	
	public final EntityPlayerMP getPlayer(int i)
	{ return playerMap.get(i); }
	
	public final Entity getFromWID(int wid)
	{
		for(int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			if(e.worldID == wid) return e;
		}
		
		return null;
	}

	public void removePlayer(EntityPlayerMP ep)
	{
		playerMap.removeValue(ep);
		sendPacket(new PacketPlayerLogout(ep));
	}
}