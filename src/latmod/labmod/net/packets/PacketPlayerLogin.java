package latmod.labmod.net.packets;
import latmod.core.util.*;
import latmod.labmod.entity.*;
import latmod.labmod.net.*;
import latmod.labmod.world.*;

public class PacketPlayerLogin extends Packet
{
	public int playerID = 0;
	
	public PacketPlayerLogin()
	{ super(ID_PLAYER_LOGIN); }
	
	public PacketPlayerLogin(int pid)
	{
		this();
		playerID = pid;
	}

	public void readPacket(World w, DataIOStream dios) throws Exception
	{
		playerID = dios.readShort();
	}
	
	public void writePacket(World w, DataIOStream dios) throws Exception
	{
		dios.writeShort(playerID);
	}
	
	public void onPacket(World w, NetClient from)
	{
		EntityPlayerMP ep = w.getPlayer(playerID);
		if(ep == null)
		{
			ep = new EntityPlayerMP(w, playerID);
			w.playerMap.put(playerID, ep);
		}
		else LatCore.printlnErr("Player '" + ep.username + "' already logged in!", "Packet", w.side.toString());
	}
}