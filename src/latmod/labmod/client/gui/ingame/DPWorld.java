package latmod.labmod.client.gui.ingame;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.client.particles.PartLayer;
import latmod.labmod.entity.*;

public class DPWorld extends DebugPage
{
	public DPWorld()
	{ super("World Info"); }
	
	public void addInfo(World w, EntityPlayerSP player, FastList<String> al)
	{
		al.add("Name: " + w.serverName);
		al.add("AABB count: " + w.AABBList.size());
		
		al.add("Rendered Entities: " + Main.inst.worldObj.worldRenderer.renderedEntities + " / " + w.entities.size());
		
		{
			int partMax = 0;
			
			for(int i = 0; i < PartLayer.VALUES.length; i++)
			partMax += Main.inst.worldObj.worldRenderer.particles[i].size();
			
			al.add("Rendered Particles: " + Main.inst.worldObj.worldRenderer.renderedParticles + " / " + partMax);
		}
		
		al.add("Entities Spawned: " + w.entities.size());
	}
}