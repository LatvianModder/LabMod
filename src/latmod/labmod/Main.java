package latmod.labmod;
import org.lwjgl.input.*;
import latmod.core.input.*;
import latmod.core.rendering.*;
import latmod.core.sound.*;
import latmod.core.util.*;
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
	public static final FastMap<String, String> mainArgs = new FastMap<String, String>();
	private static int defaultWidth = 800, defaultHeight = 600;
	public static FastList<Class<?>> allClasses = new FastList<Class<?>>();
	private GuiBasic openedGui;
	
	public static void main(String[] args)
	{
		new GameLogger();
		
		mainArgs.clear();
		if(args != null && args.length > 0)
		for(int i = 0; i < args.length; i++)
		{
			String[] s = args[i].split("=");
			if(s == null || s.length != 2)
			{
				mainArgs.put(args[i], null);
				LatCore.println(args[i]);
			}
			else
			{
				mainArgs.put(s[0], s[1]);
				LatCore.println(s[1], s[0]);
			}
		}
		
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
		//Font.inst.preScale = 1.35F;
		Font.inst.shadowEnabled = false;
		Renderer.loadTexturesSmooth = false;
		
		//if(mainArgs.keys.contains("-fs") || mainArgs.keys.contains("-fullscreen"))
		//setFullscreen(true);
		
		Event.DEFAULT.addListener(this);
		
		ClientUtils.inst = new ClientUtils();
		MainServer.inst = new MainServer();
		MainClient.inst = new MainClient();
		
		if(mainArgs.keys.contains("-noLoading"))
		{
			loadServer();
			loadClient();
			openGui(new GuiStart());
			checkForInitJoin();
		}
		else
		{
			openGui(new GuiLoadingScreen());
		}
	}
	
	public void checkForInitJoin()
	{
		String openSP = getArg("-joinServer", null);
		if(openSP != null)
		{
			MainClient.inst.create();
			MainClient.inst.start(openSP);
			openGui(new GuiPause());
		}
		
		String openMP = getArg("-startServer", null);
		if(openMP != null)
		{
			MainServer.inst.start(openMP, 16);
			openGui(new GuiPause());
		}
	}
	
	public static void loadServer()
	{
		long l = Time.millis();
		LatCore.println("Loading server...", "Main", "Server");
		LatCore.println("Loading all classes from: " + LatCore.getSourceDirectory(Main.class).getPath(), "Main", "Server");
		LatCore.getAllClassesInDir(Main.class, allClasses);
		LatCore.println("Totally found " + allClasses.size() + " classes for loaders", "Main", "Server");
		
		GameOptions.loadOptions();
		EntityID.loadEntities();
		Command.loadCommands();
		
		LatCore.println("Done loading server in " + Time.since(l) + " seconds", "Main", "Server");
	}
	
	public static void loadClient()
	{
		long l = Time.millis();
		LatCore.println("Loading client...", "Main", "Client");
		
		if(mainArgs.keys.contains("-mute")) SoundManager.setMuted(true);
		
		DebugPage.loadPages();
		
		EntityRenderer.loadEntityRenderers();
		
		ClientUtils.inst.loadTextures();
		
		LatCore.println("Done loading client in " + Time.since(l) + " seconds", "Main", "Client");
	}
	
	public boolean doStartThread()
	{ return startThread; }
	
	public void onFrameUpdate(Timer t)
	{
		if(MainServer.inst.isRunning())
		MainServer.inst.worldMP.onUpdate(t);
		
		if(MainClient.inst.isRunning())
		MainClient.inst.worldSP.onUpdate(t);
	}
	
	public void onRender()
	{
		Renderer.background(20);
		
		if(MainClient.inst.isRunning())
		{
			MainClient.inst.worldSP.preRender();
			Renderer.enter3D();
			Renderer.recolor();
			MainClient.inst.worldSP.onRender();
			MainClient.inst.worldSP.postRender();
		}
		
		// Render 2D Stuff //
		Renderer.enter2D();
		Renderer.recolor();
		
		if(MainClient.inst.hasPlayer()) MainClient.inst.worldSP.playerSP.renderGui();
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
		else if(key == GameOptions.KEY_TEST.key) //TODO: Test key
		{
			LatCore.println("-------------------");
			GameOptions.loadOptions();
		}
		
		return Cancel.FALSE;
	}
	
	public void openGui(GuiBasic g)
	{
		if(g == null) g = (MainClient.inst.isCreated() ? new GuiIngame() : new GuiStart());
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
		MainServer.inst.stop();
		MainClient.inst.stop(true);
		super.onDestroyed();
	}
	
	public void openPrevGui()
	{
		if(openedGui.prevGui == null) openGui(null);
		try { openGui(openedGui.prevGui.newInstance()); }
		catch(Exception e) { e.printStackTrace(); openGui(null); }
	}
}