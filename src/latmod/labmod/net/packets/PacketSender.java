package latmod.labmod.net.packets;
import latmod.core.util.*;
import latmod.labmod.net.NetClient;
import latmod.labmod.net.Packet;
import latmod.labmod.net.PacketManager;

public class PacketSender
{
	public FastList<Packet> packetsLeft = new FastList<Packet>();
	public FastList<Packet> packetsToAdd = new FastList<Packet>();
	
	public NetClient client = null;
	
	public PacketSender(NetClient c)
	{ client = c; }
	
	public void update()
	{
		try
		{
			/*if(!packetsToAdd.isEmpty())
			{
				packetsLeft.addAll(packetsToAdd);
				packetsToAdd.clear();
			}*/
			
			for(int i = 0; i < packetsLeft.size(); i++)
			{
				Packet p = packetsLeft.get(i);
				
				if(p != null && client.data != null)
				{
					try
					{
						PacketManager.sendPacket(client.worldObj, client.data, p);
						client.worldObj.packetsSent++;
					}
					catch(Exception e)
					{ e.printStackTrace(); }
				}
			}
			
			packetsLeft.clear();
			
			Thread.sleep(1);
		}
		catch(Exception e)
		{ e.printStackTrace(); }
	}
	
	public void sendPacket(Packet p)
	{ packetsLeft.add(p); }
}