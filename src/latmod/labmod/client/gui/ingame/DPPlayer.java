package latmod.labmod.client.gui.ingame;
import latmod.core.util.*;
import latmod.labmod.entity.*;
import latmod.labmod.world.*;

public class DPPlayer extends DebugPage
{
	public DPPlayer()
	{ super("Player Info"); }
	
	public void addInfo(World w, EntityPlayerSP ep, FastList<String> al)
	{
		al.add("Position: " + LatCore.stripFloat(ep.posX, ep.posY, ep.posZ));
		al.add("Rotation: " + LatCore.stripFloat(ep.rotYaw, ep.rotPitch));
		al.add("Running: " + (ep.isRunning ? 1 : 0));
		
		/*if(ep.cursor.lookEntity != null)
		{
			al.add("Cursor: " + ep.cursor.lookEntity);
			al.add("Cursor hit: " + LatCore.stripFloat(ep.cursor.hitDelta.posX, ep.cursor.hitDelta.posY, ep.cursor.hitDelta.posZ));
		}*/
	}
}