package latmod.labmod.client;
import latmod.core.model.*;
import latmod.core.rendering.*;
import latmod.core.res.Resource;
import latmod.core.util.*;
import latmod.labmod.Main;
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
		EventGroup.DEFAULT.addListener(this);
	}
	
	public void loadTextures(TextureManager t)
	{
		//TODO: Load all textures
		
		for(EntityRenderer r : EntityRenderer.renderMap)
		{
			r.texManager = t;
			r.loadTextures();
		}
		
		for(DebugPage p : DebugPage.debugPages)
		{
			p.texManager = t;
			p.loadTextures();
		}
		
		WorldRenderer.loadTextures(t);
	}
	
	public OBJModel loadModel(Resource r)
	{
		try
		{
			OBJModel m = OBJModel.load(Main.inst.resManager.getInputStream(r));
			Renderer.logger.info("Loaded model '" + r.path + "' with " + m.groups.size() + " groups & " + m.totalFaces.size() + " faces");
			//for(int i = 0; i < m.groupNames.length; i++)
			//LatCore.println("[" + i + "]\t" + m.groupNames[i], "ClientUtils");
			return m;
		}
		catch(Exception e)
		{ Renderer.logger.warning("Failed to load model '" + r.path + "'!"); }
		
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
			Color.WHITE.set(200);
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
			Main.inst.destroy();
			destroyClient = false;
		}
		
		//TODO: Fix texture reloader
		reloadTextures = false;
		if(reloadTextures)
		{
			int trl = 0;
			reloadTextures = false;
			Renderer.logger.info("Reloaded " + trl + " textures");
		}
	}
}