package latmod.labmod.world;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.entity.*;
import latmod.labmod.net.*;
import latmod.labmod.net.packets.*;

public class WorldServer extends World
{
	public NetServer server;
	public int nextEWID = 0;
	
	public WorldServer()
	{ super(Side.SERVER); }
	
	public INet getNet()
	{ return server; }
	
	public void start(String customIP, int maxPlayers)
	{
		String[] s = LatCore.split(customIP, ":");
		int port = (s.length == 2) ? MathHelper.toInt(s[1]) : GameOptions.DEF_PORT;
		
		Event.NET.addListener(this);
		server = new NetServer(this, port, maxPlayers, s[0]);
		
		if(server.isFailed)
		{
			LatCore.printlnErr("Failed to start server!", "World", side.toString());
			MainServer.inst.stop();
		}
		else
		{
			LatCore.println("Server started!", "World", side.toString());
		}
	}
	
	public void onUpdate(Timer t)
	{
		resetCollisionBoxes();
		
		entities.putAll(entitiesToBeAdded);
		entitiesToBeAdded.clear();
		
		for(int i = 0; i < entities.size(); i++)
		entities.get(i).onUpdate(t);
		
		for(int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			
			if(e.flags[Entity.IS_DEAD])
			removeEntity(e.worldID);
			else if(e.isDirty)
			{
				e.isDirty = false;
				sendPacket(new PacketEntityUpdate(e));
			}
		}
		
		for(int i = 0; i < playerMap.size(); i++)
		playerMap.values.get(i).onUpdate(t);
		
		tick++;
	}
	
	public void onStopped()
	{
		server.stop();
		Event.NET.removeListener(this);
		super.onStopped();
	}
	
	public void onClientJoined(NetClient c) throws Exception
	{
		LatCore.println("Client joined with clientID " + c.clientID, c.getIP(), side.toString());
		sendPacket(new PacketPlayerLogin(c.clientID));
	}
	
	public void onClientData(NetClient c) throws Exception
	{ PacketManager.receivePacket(this, c.data); }
	
	public void spawnEntity(Entity e)
	{
		e.worldID = ++nextEWID;
		e.isDirty = true;
		entitiesToBeAdded.put(e.worldID, e);
		
		sendPacket(new PacketEntityUpdate(e));
	}

	public void removeEntity(int wid)
	{
		entities.remove(wid);
		sendPacket(new PacketEntityDespawn(wid));
	}
	
	public void sendPacket(Packet p)
	{ for(NetClient c : server.clients)
	c.packetSender.sendPacket(p); }
	
	public void processPacket(Packet p)
	{ for(NetClient c : server.clients)
	c.packetProcessor.addPacketToProcess(p); }
}