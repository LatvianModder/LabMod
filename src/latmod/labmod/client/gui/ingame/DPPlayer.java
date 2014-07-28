package latmod.labmod.client.gui.ingame;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.entity.*;

public class DPPlayer extends DebugPage
{
	public DPPlayer()
	{ super("Player Info"); }
	
	public void addInfo(World w, EntityPlayerSP ep, FastList<String> al)
	{
		al.add("Position: " + LMCommon.stripDouble(ep.posX, ep.posY, ep.posZ));
		al.add("Rotation: " + LMCommon.stripDouble(ep.rotYaw, ep.rotPitch));
		al.add("Running: " + (ep.isRunning ? 1 : 0));
		al.add("Cursor Position: " + LMCommon.stripDouble(ep.cursor.posX, ep.cursor.posY, ep.cursor.posZ));
		
		if(ep.cursor.aabbHit != null)
		{
			al.add("Cursor Hit: " + LMCommon.stripDouble(ep.cursor.relPos.posX, ep.cursor.relPos.posY, ep.cursor.relPos.posZ));
			al.add("Cursor Entity: " + ep.cursor.lookEntity);
			al.add("Cursor Face: " + ep.cursor.side);
		}
	}
}