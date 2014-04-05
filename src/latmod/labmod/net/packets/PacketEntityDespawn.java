package latmod.labmod.net.packets;
import latmod.core.util.*;
import latmod.labmod.net.NetClient;
import latmod.labmod.net.Packet;
import latmod.labmod.world.*;

public class PacketEntityDespawn extends Packet
{
	public int entityWID = 0;
	
	public PacketEntityDespawn()
	{ super(ID_ENTITY_DESPAWN); }
	
	public PacketEntityDespawn(int wid)
	{
		this();
		entityWID = wid;
	}
	
	public void writePacket(World w, DataIOStream dios) throws Exception
	{
		dios.writeInt(entityWID);
	}

	public void readPacket(World w, DataIOStream dios) throws Exception
	{
		entityWID = dios.readInt();
	}

	public void onPacket(World w, NetClient from)
	{
		w.entities.remove(entityWID);
		w.entitiesToBeAdded.remove(entityWID);
	}
}