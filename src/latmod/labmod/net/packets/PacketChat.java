package latmod.labmod.net.packets;
import latmod.core.util.*;
import latmod.labmod.client.gui.GuiIngame;
import latmod.labmod.net.NetClient;
import latmod.labmod.net.Packet;
import latmod.labmod.world.*;

public class PacketChat extends Packet
{
	public String text;
	
	public PacketChat()
	{ super(ID_CHAT); }
	
	public PacketChat(String s)
	{
		this();
		text = s;
	}

	public void writePacket(World w, DataIOStream dios) throws Exception
	{
		dios.writeString(text);
	}
	
	public void readPacket(World w, DataIOStream dios) throws Exception
	{
		text = dios.readString();
	}
	
	public void onPacket(World w, NetClient from)
	{
		if(w.side == Side.SERVER)
		w.sendPacket(new PacketChat(text));
		else GuiIngame.printChat(text);
	}
}