package latmod.labmod.client.gui.ingame;
import latmod.core.rendering.TextureManager;
import latmod.core.util.*;
import latmod.labmod.Main;
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
		
		Main.gameLogger.info("Loaded " + debugPages.size() + " debug pages!");
	}
	
	public static void add(DebugPage p)
	{ debugPages.add(p); }
	
	public final String name;
	public int pageID = 0;
	
	public TextureManager texManager;
	
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