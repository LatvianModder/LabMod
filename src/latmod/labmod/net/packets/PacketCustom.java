package latmod.labmod.net.packets;
import java.util.*;

import latmod.core.util.*;
import latmod.labmod.net.ICustomPacketHandler;
import latmod.labmod.net.NetClient;
import latmod.labmod.net.Packet;
import latmod.labmod.world.*;

public class PacketCustom extends Packet
{
	public String channel = null;
	public byte[] data = null;
	
	public PacketCustom()
	{ super(ID_CUSTOM); }
	
	public PacketCustom(String s, byte[] b)
	{
		this();
		channel = s;
		data = b;
	}
	
	public void writePacket(World w, DataIOStream dios) throws Exception
	{
		dios.writeString(channel);
		dios.writeBytes(data);
	}
	
	public void readPacket(World w, DataIOStream dios) throws Exception
	{
		channel = dios.readString();
		data = dios.readBytes();
	}
	
	public void onPacket(World w, NetClient from)
	{ handleCustomPacket(w, this); }
	
	// -------- //
	
	public static final HashMap<String, ICustomPacketHandler> customHandlers = new HashMap<String, ICustomPacketHandler>();
	
	public static final void addCustomHandler(String channel, ICustomPacketHandler h)
	{ if(channel != null && h != null && channel.length() > 0) customHandlers.put(channel, h); }
	
	public static final void handleCustomPacket(World w, PacketCustom p)
	{ if(p == null) return; ICustomPacketHandler h = customHandlers.get(p.channel);
	if(h != null) h.onCustomPacket(w, p); }
}