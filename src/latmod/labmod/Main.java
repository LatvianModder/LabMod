package latmod.labmod;
import java.util.logging.Logger;
import org.lwjgl.input.*;
import latmod.core.input.*;
import latmod.core.rendering.*;
import latmod.core.res.Resource;
import latmod.core.util.*;
import latmod.labmod.client.ClientUtils;
import latmod.labmod.client.entity.*;
import latmod.labmod.client.gui.*;
import latmod.labmod.client.gui.ingame.*;
import latmod.labmod.cmd.*;
import latmod.labmod.entity.*;

public class Main extends LMFrame implements IKeyListener.Pressed
{
	public static boolean startThread = true;
	public static Main inst = null;
	public Main(String[] s) { super(s, 800, 600, 60); }
	public static FastList<Class<?>> allClasses = new FastList<Class<?>>();
	private GuiBasic openedGui;
	public World worldObj = null;
	
	public static final Logger gameLogger = Logger.getLogger("Game");
	static { gameLogger.setParent(LatCore.logger); }
	
	public static void main(String[] args)
	{
		LatCore.initLogger();
		new GameLogger()
		{
			public int getLogCount()
			{ return GameOptions.props.logCount; }
		};
		
		inst = new Main(args);
	}
	
	public void onLoaded()
	{
		LatCore.setProjectName("LabMod");
		
		super.onLoaded();
		
		setTitle("LabMod");
		
		textureManager.loadTexturesBlured = true;
		font = new Font(Resource.getTexture("gui/font.png"));
		font.shadowEnabled = false;
		textureManager.loadTexturesBlured = false;
		
		EventGroup.DEFAULT.addListener(this);
		
		ClientUtils.inst = new ClientUtils();
		
		if(mainArgs.getB("noload", false))
		{
			loadServer();
			loadClient();
			openGui(new GuiStart());
		}
		else
		{
			openGui(new GuiLoadingScreen());
		}
	}
	
	public void loadServer()
	{
		long l = Time.millis();
		gameLogger.info("Loading server...");
		gameLogger.info("Loading all classes from: " + LMCommon.getSourceDirectory(Main.class).getPath());
		LMCommon.getAllClassesInDir(Main.class, allClasses);
		gameLogger.info("Totally found " + allClasses.size() + " classes for loaders");
		
		GameOptions.loadOptions();
		EntityID.loadEntities();
		Command.loadCommands();
		
		gameLogger.info("Done loading server in " + Time.since(l) + " seconds");
	}
	
	public void loadClient()
	{
		long l = Time.millis();
		gameLogger.info("Loading client...");
		
		soundManager.muted = mainArgs.getB("mute", false);
		
		DebugPage.loadPages();
		
		EntityRenderer.loadEntityRenderers();
		
		ClientUtils.inst.loadTextures(textureManager);
		
		gameLogger.info("Done loading client in " + Time.since(l) + " seconds");
	}
	
	public boolean doStartThread()
	{ return startThread; }
	
	public void onUpdate()
	{
		if(worldObj != null)
		{
			worldObj.onUpdate(updateTimer);
		}
	}
	
	public void onRender()
	{
		if(updateTimer == null) return;
		
		Renderer.background(Color.DARK_GRAY);
		
		try
		{
			if(worldObj != null && worldObj.worldRenderer != null)
			{
				worldObj.worldRenderer.preRender();
				Renderer.enter3D();
				Color.reset();
				worldObj.worldRenderer.onRender();
				worldObj.worldRenderer.postRender();
			}
		}
		catch(Exception e)
		{  }
		
		// Render 2D Stuff //
		Renderer.enter2D();
		Color.reset();
		
		if(worldObj != null && worldObj.player != null) worldObj.player.renderGui();
		openedGui.onRender();
		
		ClientUtils.inst.onUpdate();
	}
	
	public void onKeyPressed(latmod.core.input.EventKey.Pressed e)
	{
		if(e.key == Keyboard.KEY_ESCAPE)
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			destroy(); else openedGui.onEscPressed();
			e.cancel();
		}
		else if(e.key == GameOptions.KEY_SCREENSHOT.key)
		{
			ClientUtils.inst.takingScreenshot = true;
			e.cancel();
		}
		else if(e.key == GameOptions.KEY_FULLSCREEN.key)
		{
			//setFullscreen(!Display.isFullscreen());
			e.cancel();
		}
		else if(e.key == GameOptions.KEY_TEST.key)
		{
			if(worldObj != null && worldObj.player != null)
			worldObj.player.executeCommand("spawn box");
			e.cancel();
		}
	}
	
	public void openGui(GuiBasic g)
	{
		if(g == null) g = (worldObj != null ? new GuiIngame() : new GuiStart());
		if(openedGui != null)
		{
			g.prevGui = openedGui.getClass();
			openedGui.onReplacedBy(g);
			openedGui.onUnloaded();
			openedGui.onDestroyed();
			openedGui = null;
		}
		
		openedGui = g;
		onResized();
		
		Mouse.setGrabbed(g.allowPlayerInput());
	}
	
	public void onResized()
	{
		super.onResized();
		if(openedGui != null)
		{
			openedGui.onUnloaded();
			openedGui.onLoaded();
		}
	}
	
	public GuiBasic getGui()
	{ return openedGui; }
	
	public void onDestroyed()
	{
		closeWorld();
		super.onDestroyed();
	}
	
	public void openPrevGui()
	{
		if(openedGui.prevGui == null) openGui(null);
		try { openGui(openedGui.prevGui.newInstance()); }
		catch(Exception e) { e.printStackTrace(); openGui(null); }
	}
	
	public boolean hasPlayer()
	{ return worldObj != null && worldObj.player != null; }
	
	public void openWorld()
	{
		worldObj = new World();
		worldObj.createWorld();
		openGui(null);
	}
	
	public void closeWorld()
	{
		if(worldObj != null)
		worldObj.onStopped();
		worldObj = null;
		openGui(null);
	}
}