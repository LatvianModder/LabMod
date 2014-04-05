package latmod.labmod.net.packets;
import latmod.core.nbt.*;
import latmod.core.util.*;
import latmod.labmod.entity.*;
import latmod.labmod.net.NetClient;
import latmod.labmod.net.Packet;
import latmod.labmod.world.*;

public class PacketEntitySpawn extends Packet
{
	public int entityID;
	public NBTMap entityData;
	
	public PacketEntitySpawn()
	{ super(ID_ENTITY_SPAWN); }
	
	public PacketEntitySpawn(Entity e)
	{
		this();
		entityID = EntityID.getEID(e);
		entityData = new NBTMap("Entity");
		e.writeToNBT(entityData);
	}
	
	public void readPacket(World w, DataIOStream dios) throws Exception
	{
		entityID = dios.readShort();
		entityData = new NBTMap("Entity");
		entityData.read(dios);
	}

	public void writePacket(World w, DataIOStream dios) throws Exception
	{
		dios.writeShort(entityID);
		entityData.write(dios);
	}

	public void onPacket(World w, NetClient from)
	{
		try
		{
			Entity e = EntityID.createEntity(w, entityID);
			
			if(e != null)
			{
				e.readFromNBT(entityData);
				w.spawnEntity(e);
			}
		}
		catch(Exception ex)
		{ ex.printStackTrace(); }
	}
}