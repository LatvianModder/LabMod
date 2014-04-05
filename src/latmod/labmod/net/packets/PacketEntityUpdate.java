package latmod.labmod.net.packets;
import latmod.core.nbt.*;
import latmod.core.util.*;
import latmod.labmod.entity.*;
import latmod.labmod.net.NetClient;
import latmod.labmod.net.Packet;
import latmod.labmod.world.*;

public class PacketEntityUpdate extends Packet
{
	public int entityID = 0;
	public int entityWID = 0;
	public NBTMap entityData = null;
	
	public PacketEntityUpdate()
	{ super(ID_ENTITY_UPDATE); }
	
	public PacketEntityUpdate(Entity e)
	{
		this();
		entityID = EntityID.getEID(e);
		entityWID = e.worldID;
		
		entityData = new NBTMap("Entity");
		e.writeToNBT(entityData);
	}
	
	public void writePacket(World w, DataIOStream dios) throws Exception
	{
		dios.writeShort(entityID);
		dios.writeInt(entityWID);
		entityData.write(dios);
	}

	public void readPacket(World w, DataIOStream dios) throws Exception
	{
		entityID = dios.readShort();
		entityWID = dios.readInt();
		entityData = new NBTMap("Entity");
		entityData.read(dios);
	}
	
	public void onPacket(World w, NetClient from)
	{
		Entity e = w.getFromWID(entityWID); boolean spawned = false;
		
		try { if(e == null) e = EntityID.createEntity(w, entityID); e.worldID = entityWID; spawned = true; }
		catch(Exception ex) { }
		
		if(e != null) e.readFromNBT(entityData);
		if(spawned) w.entitiesToBeAdded.put(entityWID, e);
	}
}