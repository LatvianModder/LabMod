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
		al.add("AABB count: " + w.AABBList.size());
		
		al.add("Rendered Entities: " + Main.inst.worldSP.renderedEntities + " / " + w.entities.size());
		int partMax = 0;
		partMax += Main.inst.worldSP.particles[PartLayer.NO_TEX.INDEX].size();
		partMax += Main.inst.worldSP.particles[PartLayer.TEX.INDEX].size();
		partMax += Main.inst.worldSP.particles[PartLayer.CUSTOM.INDEX].size();
		al.add("Rendered Particles: " + Main.inst.worldSP.renderedParticles + " / " + partMax);
		
		al.add("Entities Spawned: " + w.entities.size());
		
		al.add("Packets received: " + w.packetsReceived);
		al.add("Packets sent: " + w.packetsSent);
	}
}