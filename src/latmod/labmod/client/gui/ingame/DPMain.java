package latmod.labmod.client.gui.ingame;
import latmod.core.util.*;
import latmod.labmod.Main;
import latmod.labmod.entity.EntityPlayerSP;
import latmod.labmod.world.World;

public class DPMain extends DebugPage
{
	public DPMain()
	{ super("Game Info"); }
	
	public void addInfo(World w, EntityPlayerSP player, FastList<String> al)
	{
		al.add(Main.inst.getTitle());
		al.add("FPS: " + Main.inst.FPS + ", TPS: " + Main.inst.TPS);
	}
}