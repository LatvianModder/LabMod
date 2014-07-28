package latmod.labmod.cmd;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.client.gui.*;
import latmod.labmod.entity.EntityPlayer;

public abstract class Command
{
	public static FastMap<String, Command> commands = new FastMap<String, Command>();
	
	public static final TextColor FINE = TextColor.WHITE;
	public static final TextColor ERR = TextColor.RED;
	public static final TextColor NUM = TextColor.YELLOW;
	public static final TextColor NAME = TextColor.L_BLUE;
	
	public static final String CLIENT = "Client";
	public static final String SERVER = "Server";
	public static final String UNIVERSAL = "Uni";
	
	public static void loadCommands()
	{
		Main.gameLogger.info("Loading Commands...");
		
		register("tps", new CmdTPS());
		register("hurt", new CmdHP());
		register("spawn", new CmdSpawn());
		register("tp", new CmdTP());
		register("gravity", new CmdGravity());
		
		Main.gameLogger.info("Loaded " + commands.size() + " commands");
	}
	
	public static void register(String s, Command c)
	{
		if(commands.keys.contains(s))
		Main.gameLogger.info("Overriding " + LMCommon.classpath(commands.get(s).getClass()) + " with " + LMCommon.classpath(c.getClass()));
		commands.put(s, c);
	}
	
	public abstract String onCommand(World w, EntityPlayer ep, String[] args, String argsUnsplit) throws Exception;
	
	public void print(Object o)
	{ GuiIngame.printChat(String.valueOf(o)); }
}