package latmod.labmod.net.packets;
import latmod.core.util.*;
import latmod.labmod.net.NetClient;
import latmod.labmod.net.Packet;

public class PacketProcessor
{
	public FastList<Packet> packetsLeft = new FastList<Packet>();
	public FastList<Packet> packetsToAdd = new FastList<Packet>();
	
	public NetClient client = null;
	
	public PacketProcessor(NetClient c)
	{ client = c; }
	
	public void update()
	{
		try
		{
			if(!packetsToAdd.isEmpty())
			{
				packetsLeft.addAll(packetsToAdd);
				packetsToAdd.clear();
			}
			
			for(int i = 0; i < packetsLeft.size(); i++)
			{
				packetsLeft.get(i).onPacket(client.worldObj, client);
				client.worldObj.packetsReceived++;
			}
			
			packetsLeft.clear();
			
			Thread.sleep(1);
		}
		catch(Exception e)
		{ e.printStackTrace(); }
	}
	
	public void addPacketToProcess(Packet p)
	{ packetsToAdd.add(p); }
}