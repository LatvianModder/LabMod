package latmod.labmod.net.packets;
import latmod.core.util.*;
import latmod.labmod.entity.EntityPlayerMP;
import latmod.labmod.net.NetClient;
import latmod.labmod.net.Packet;
import latmod.labmod.world.*;

public class PacketPlayerLogout extends Packet
{
	public int playerID = 0;
	
	public PacketPlayerLogout()
	{ super(ID_PLAYER_LOGOUT); }
	
	public PacketPlayerLogout(EntityPlayerMP ep)
	{
		this();
		playerID = ep.playerID;
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
		if(ep != null) w.playerMap.remove(playerID);
	}
}