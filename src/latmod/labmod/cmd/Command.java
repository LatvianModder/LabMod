package latmod.labmod.cmd;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.client.gui.*;
import latmod.labmod.world.*;

public abstract class Command
{
	public static FastMap<String, Command> commands = new FastMap<String, Command>();
	private static final String LOGGER = "CommandLoader";
	
	public static final TextColor FINE = TextColor.WHITE;
	public static final TextColor ERR = TextColor.RED;
	public static final TextColor NUM = TextColor.YELLOW;
	public static final TextColor NAME = TextColor.L_BLUE;
	
	public static final String CLIENT = "Client";
	public static final String SERVER = "Server";
	public static final String UNIVERSAL = "Uni";
	
	public static void loadCommands()
	{
		LatCore.println("Loading Commands...", LOGGER);
		
		register("say", new CmdSay());
		register("tps", new CmdTPS());
		register("hurt", new CmdHurt());
		register("spawn", new CmdSpawn());
		register("tp", new CmdTP());
		
		LatCore.println("Loaded " + commands.size() + " command" + LatCore.numEnding(commands.size()), LOGGER);
	}
	
	public static void register(String s, Command c)
	{
		if(commands.keys.contains(s))
		LatCore.printlnErr("Overriding " + LatCore.classpath(commands.get(s).getClass()) + " with " + LatCore.classpath(c.getClass()), s, LOGGER);
		commands.put(s, c);
	}
	
	public abstract String onCommand(World w, CommandSender sender, String[] args, String argsUnsplit);
	public abstract TextColor getArgCol(int i, String s);
	public abstract Side getCommandSide();
	
	public void print(Object o)
	{ GuiIngame.printChat(String.valueOf(o)); }
}