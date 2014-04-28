package latmod.labmod;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import org.lwjgl.input.*;
import org.lwjgl.opengl.Display;

import latmod.core.input.*;
import latmod.core.rendering.*;
import latmod.core.sound.*;
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
	public Main() { super(defaultWidth, defaultHeight, 60); }
	public String getTitle() { return "LabMod"; }
	public static FastMap<String, String> mainArgs = null;
	private static int defaultWidth = 800, defaultHeight = 600;
	public static FastList<Class<?>> allClasses = new FastList<Class<?>>();
	private GuiBasic openedGui;
	public World worldObj = null;
	
	public static final Logger gameLogger = Logger.getLogger("Game");
	static { gameLogger.setParent(LatCore.logger); }
	
	public static void main(String[] args)
	{
		LatCore.initLogger();
		new GameLogger();
		
		mainArgs = LatCore.createArgs(args);
		defaultWidth = MathHelper.toInt(getArg("-width", "800"));
		defaultHeight = MathHelper.toInt(getArg("-height", "600"));
		
		inst = new Main();
	}
	
	public static String getArg(String s, String def)
	{ String s1 = mainArgs.get(s); return (s1 == null) ? def : s1; }
	
	public void onLoaded()
	{
		LatCore.setProjectName("LabMod");
		
		super.onLoaded();
		
		Renderer.loadTexturesSmooth = true;
		Font.inst = new Font(Renderer.getTexture("gui/fontLabMod.png"));
		Font.inst.shadowEnabled = false;
		Renderer.loadTexturesSmooth = false;
		
		if(!mainArgs.keys.contains("-noicons"))
		{
			String iconPaths[] =
			{
				"gui/logo_16.png",
				"gui/logo_32.png",
				"gui/logo_128.png",
			};
			
			ByteBuffer[] list = new ByteBuffer[iconPaths.length];
			for(int i = 0; i < iconPaths.length; i++)
			list[i] = Renderer.getTexture(iconPaths[i]).getAsByteBuffer();
			Display.setIcon(list);
		}
		
		//if(mainArgs.keys.contains("-fs") || mainArgs.keys.contains("-fullscreen"))
		//setFullscreen(true);
		
		Event.DEFAULT.addListener(this);
		
		ClientUtils.inst = new ClientUtils();
		
		if(mainArgs.keys.contains("-noload"))
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
	
	public static void loadServer()
	{
		long l = Time.millis();
		gameLogger.info("Loading server...");
		gameLogger.info("Loading all classes from: " + LatCore.getSourceDirectory(Main.class).getPath());
		LatCore.getAllClassesInDir(Main.class, allClasses);
		gameLogger.info("Totally found " + allClasses.size() + " classes for loaders");
		
		GameOptions.loadOptions();
		EntityID.loadEntities();
		Command.loadCommands();
		
		gameLogger.info("Done loading server in " + Time.since(l) + " seconds");
	}
	
	public static void loadClient()
	{
		long l = Time.millis();
		gameLogger.info("Loading client...");
		
		if(mainArgs.keys.contains("-mute")) SoundManager.setMuted(true);
		
		DebugPage.loadPages();
		
		EntityRenderer.loadEntityRenderers();
		
		ClientUtils.inst.loadTextures();
		
		gameLogger.info("Done loading client in " + Time.since(l) + " seconds");
	}
	
	public boolean doStartThread()
	{ return startThread; }
	
	public void onFrameUpdate(Timer t)
	{
		if(worldObj != null)
		{
			worldObj.onUpdate(t);
			
			String s = LatCore.readConsole();
			if(s != null && worldObj.player != null)
			worldObj.player.executeCommand(s);
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
				Color.clear();
				worldObj.worldRenderer.onRender();
				worldObj.worldRenderer.postRender();
			}
		}
		catch(Exception e)
		{  }
		
		// Render 2D Stuff //
		Renderer.enter2D();
		Color.clear();
		
		if(worldObj != null && worldObj.player != null) worldObj.player.renderGui();
		openedGui.onRender();
		
		ClientUtils.inst.onUpdate();
	}
	
	public Cancel onKeyPressed(int key, char keyChar)
	{
		if(key == Keyboard.KEY_ESCAPE)
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			destroy(); else openedGui.onEscPressed();
			return Cancel.TRUE;
		}
		else if(key == GameOptions.KEY_SCREENSHOT.key)
		{
			ClientUtils.inst.takingScreenshot = true;
			return Cancel.TRUE;
		}
		else if(key == GameOptions.KEY_FULLSCREEN.key)
		{
			//setFullscreen(!Display.isFullscreen());
			return Cancel.TRUE;
		}
		else if(key == GameOptions.KEY_TEST.key)
		{
			if(worldObj != null && worldObj.player != null)
			worldObj.player.executeCommand("spawn box");
		}
		
		return Cancel.FALSE;
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
			openedGui.width = width;
			openedGui.height = height;
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