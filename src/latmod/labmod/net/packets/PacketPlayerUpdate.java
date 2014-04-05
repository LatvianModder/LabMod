package latmod.labmod.net.packets;
import latmod.core.nbt.*;
import latmod.core.util.*;
import latmod.labmod.entity.*;
import latmod.labmod.net.NetClient;
import latmod.labmod.net.Packet;
import latmod.labmod.world.*;

public class PacketPlayerUpdate extends Packet
{
	public int playerID;
	public NBTMap playerData;
	
	public PacketPlayerUpdate()
	{ super(ID_PLAYER_UPDATE); }
	
	public PacketPlayerUpdate(EntityPlayer ep)
	{
		this();
		playerID = ep.playerID;
		playerData = new NBTMap("Data");
		ep.writeToNBT(playerData);
	}
	
	public void writePacket(World w, DataIOStream dios) throws Exception
	{
		dios.writeInt(playerID);
		playerData.write(dios);
	}

	public void readPacket(World w, DataIOStream dios) throws Exception
	{
		playerID = dios.readInt();
		playerData = new NBTMap("Data");
		playerData.read(dios);
	}

	public void onPacket(World w, NetClient from)
	{
		EntityPlayerMP ep = w.getPlayer(playerID);
		if(ep != null) 
		{
			ep.readFromNBT(playerData);
			if(w.side.isServer) w.sendPacket(new PacketPlayerUpdate(ep));
		}
		else
		{
			//LatCore.printlnErr("Player with ID " + playerID + " not found, creating one", "Packet");
			ep = new EntityPlayerMP(w, playerID);
			ep.readFromNBT(playerData);
			w.playerMap.put(playerID, ep);
		}
		//else LatCore.printlnErr("Player with ID " + playerID + " not found!", "Packet");
	}
}