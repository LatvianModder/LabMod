package latmod.labmod.cmd;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.world.*;

public class CmdHurt extends Command
{
	public String onCommand(World w, CommandSender sender, String[] args, String argsUnsplit)
	{
		if(args != null && args.length == 0)
		{
		}
		
		return null;
	}
	
	public TextColor getArgCol(int i, String s)
	{
		return FINE;
	}
	
	public Side getCommandSide()
	{ return Side.CLIENT; }
}