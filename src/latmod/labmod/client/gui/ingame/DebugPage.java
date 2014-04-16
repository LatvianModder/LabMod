package latmod.labmod.client.gui.ingame;
import latmod.core.util.*;
import latmod.labmod.World;
import latmod.labmod.entity.*;

public abstract class DebugPage
{
	public static final FastList<DebugPage> debugPages = new FastList<DebugPage>();
	
	public static void loadPages()
	{
		debugPages.clear();
		
		add(new DPIngame());
		add(new DPMain());
		add(new DPPlayer());
		add(new DPWorld());
		add(new DPThreads());
		
		LatCore.println("Loaded " + debugPages.size() + " debug pages!", "DebugPages");
	}
	
	public static void add(DebugPage p)
	{ debugPages.add(p); }
	
	public final String name;
	public int pageID = 0;
	
	public DebugPage(String s)
	{
		name = s;
		pageID = debugPages.size();
	}
	
	public abstract void addInfo(World w, EntityPlayerSP ep, FastList<String> al);
	public void onPageOpened() { }
	public void onPageClosed() { }
	public void onCustom2DRender(World w, EntityPlayerSP ep) { }
	public void loadTextures() { }
}