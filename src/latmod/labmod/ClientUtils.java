package latmod.labmod;
import latmod.core.model.*;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.client.entity.*;
import latmod.labmod.client.gui.*;
import latmod.labmod.client.gui.ingame.DebugPage;

public class ClientUtils
{
	public static ClientUtils inst;
	public boolean takingScreenshot = false;
	public boolean processingScreenshot = false;
	public boolean destroyClient = false;
	public boolean reloadTextures = false;
	
	public ClientUtils()
	{
		Event.DEFAULT.addListener(this);
	}
	
	public void loadTextures()
	{
		//TODO: Load all textures
		
		for(EntityRenderer r : EntityRenderer.renderMap) r.loadTextures();
		for(DebugPage p : DebugPage.debugPages) p.loadTextures();
	}
	
	public OBJModel loadModel(String s)
	{
		try
		{
			OBJModel m = OBJModel.load(ClientUtils.class.getResourceAsStream(s));
			LatCore.println("Loaded model '" + s + "' with " + m.groups.size() + " groups & " + m.totalFaces.size() + " faces", "ClientUtils");
			//for(int i = 0; i < m.groupNames.length; i++)
			//LatCore.println("[" + i + "]\t" + m.groupNames[i], "ClientUtils");
			return m;
		}
		catch(Exception e)
		{ LatCore.printlnErr("Failed to load model '" + s + "'!", "ClientUtils"); }
		
		return null;
	}
	
	@EventHandler
	public void onScreenshot(EventScreenshot e)
	{ processingScreenshot = false; }
	
	@EventHandler
	public void resizedEvent(EventResized e)
	{ GuiBasic.resetStars(e.prevWidth / (float)Main.inst.width, e.prevHeight / (float)Main.inst.height); }

	public void onUpdate()
	{
		if(takingScreenshot)
		{
			Renderer.takeScreenshot();
			processingScreenshot = true;
			takingScreenshot = false;
		}
		
		if(processingScreenshot)
		{
			Renderer.disableTexture();
			Renderer.enableSmooth();
			Renderer.colorize(255, 200);
			float d = 32F;
			float d1 = d / 2F + 4F;
			float r = Main.inst.renderTick * 0.61F;
			Renderer.drawArc(Main.inst.width - d1, d1, d, 90F, r);
			Renderer.drawArc(Main.inst.width - d1, d1, d, 90F, r + 180F);
			Renderer.enableTexture();
			Renderer.disableSmooth();
		}
		
		if(destroyClient)
		{
			MainClient.inst.stop(true);
			destroyClient = false;
		}
		
		//TODO: Fix texture reloader
		reloadTextures = false;
		if(reloadTextures)
		{
			FastList<String> tex = new FastList<String>();
			
			for(int i = 0; i < Renderer.textureMap.values.size(); i++)
			{
				Texture t = Renderer.textureMap.values.get(i);
				
				if(t != null && !(t instanceof TextureCustom))
				{
					tex.add(t.name);
					t.destroy();
				}
			}
			
			int trl = 0;
			
			for(String s : tex)
			{
				Renderer.getTexture(s);
				trl++;
			}
			
			reloadTextures = false;
			LatCore.println("Reloaded " + trl + " textures", "ClientUtils");
		}
	}
}