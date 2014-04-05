package latmod.labmod.net;
import latmod.core.util.*;
import latmod.labmod.net.packets.PacketChat;
import latmod.labmod.net.packets.PacketCustom;
import latmod.labmod.net.packets.PacketEntityDespawn;
import latmod.labmod.net.packets.PacketEntitySpawn;
import latmod.labmod.net.packets.PacketEntityUpdate;
import latmod.labmod.net.packets.PacketPlayerLogin;
import latmod.labmod.net.packets.PacketPlayerLogout;
import latmod.labmod.net.packets.PacketPlayerUpdate;
import latmod.labmod.world.*;

public abstract class Packet // PacketManager
{
	public static final int ID_CHAT = 1;
	public static final int ID_CUSTOM = 2;
	
	public static final int ID_PLAYER_LOGIN = 3;
	public static final int ID_PLAYER_UPDATE = 4;
	public static final int ID_PLAYER_LOGOUT = 5;
	
	public static final int ID_ENTITY_SPAWN = 6;
	public static final int ID_ENTITY_UPDATE = 7;
	public static final int ID_ENTITY_DESPAWN = 8;
	
	public static final Packet newPacket(int id)
	{
		if(id <= 0 || id > 255) return null;
		
		switch(id)
		{
			case ID_CHAT: return new PacketChat();
			case ID_CUSTOM: return new PacketCustom();
			
			case ID_PLAYER_LOGIN: return new PacketPlayerLogin();
			case ID_PLAYER_UPDATE: return new PacketPlayerUpdate();
			case ID_PLAYER_LOGOUT: return new PacketPlayerLogout();
			
			case ID_ENTITY_SPAWN: return new PacketEntitySpawn();
			case ID_ENTITY_UPDATE: return new PacketEntityUpdate();
			case ID_ENTITY_DESPAWN: return new PacketEntityDespawn();
		}
		
		return null;
	}
	
	// Non-Static Methods //
	
	public final int packetID;
	
	public Packet(int id)
	{ packetID = id; }
	
	public abstract void readPacket(World w, DataIOStream dios) throws Exception;
	public abstract void writePacket(World w, DataIOStream dios) throws Exception;
	public abstract void onPacket(World w, NetClient from);
	
	public final int hashCode()
	{ return packetID; }
}