package latmod.labmod.client.gui.ingame;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.client.particles.PartLayer;
import latmod.labmod.entity.*;
import latmod.labmod.world.*;

public class DPWorld extends DebugPage
{
	public DPWorld()
	{ super("World Info"); }
	
	public void addInfo(World w, EntityPlayerSP player, FastList<String> al)
	{
		al.add("Name: " + w.serverName);
		al.add("Side: " + w.side);
		al.add("AABB count: " + w.AABBList.size());
		
		if(w.side.isClient)
		{
			al.add("Rendered Entities: " + MainClient.inst.worldSP.renderedEntities + " / " + w.entities.size());
			int partMax = 0;
			partMax += MainClient.inst.worldSP.particles[PartLayer.NO_TEX.INDEX].size();
			partMax += MainClient.inst.worldSP.particles[PartLayer.TEX.INDEX].size();
			partMax += MainClient.inst.worldSP.particles[PartLayer.CUSTOM.INDEX].size();
			al.add("Rendered Particles: " + MainClient.inst.worldSP.renderedParticles + " / " + partMax);
		}
		else
		{
			al.add("Entities Spawned: " + w.entities.size());
		}
		
		al.add("Packets received: " + w.packetsReceived);
		al.add("Packets sent: " + w.packetsSent);
		
		if(w.side.isClient && MainServer.inst.isRunning())
		{
			al.add("");
			al.add("Client Count: " + MainServer.inst.worldMP.server.clients.size());
			al.add("Server Packets received: " + MainServer.inst.worldMP.packetsReceived);
			al.add("Server Packets sent: " + MainServer.inst.worldMP.packetsSent);
		}
	}
}