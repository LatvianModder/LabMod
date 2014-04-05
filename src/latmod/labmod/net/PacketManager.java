package latmod.labmod.net;
import latmod.core.util.*;
import latmod.labmod.world.*;

public class PacketManager
{
	public static final Packet receivePacket(World w, DataIOStream dios) throws Exception
	{
		int id = dios.readByte();
		
		//ODO: Printing Incoming packet
		//LatCore.println("Data received: " + id, w.side.toString());
		
		Packet p = Packet.newPacket(id);
		if(p != null)
		{
			p.readPacket(w, dios);
			w.processPacket(p);
			return p;
		}
		else LatCore.printlnErr("Unknown packetID " + id + "!", "Packet", w.side.toString());
		
		return null;
	}
	
	public static final void sendPacket(World w, DataIOStream dios, Packet p) throws Exception
	{
		if(p != null)
		{
			//ODO: Printing Incoming packet
			//LatCore.println("Data sent: " + p.hashCode(), w.side.toString());
			
			dios.writeByte(p.hashCode());
			p.writePacket(w, dios);
			dios.flush();
		}
	}
}