package latmod.labmod.cmd;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.world.*;

public class CmdTP extends Command
{
	public String onCommand(World w, CommandSender sender, String[] args, String argsUnsplit)
	{
		if(args.length == 3)
		{
			float x = Float.parseFloat(args[0]);
			float y = Float.parseFloat(args[1]);
			float z = Float.parseFloat(args[2]);
			
			sender.player.setPos(x, y, z);
			sender.player.isDirty = true;
		}
		else if(args.length == 1)
		{
			return null;
		}
		
		return "Invalid arguments!";
	}

	public TextColor getArgCol(int i, String s)
	{ return FINE; }

	public Side getCommandSide()
	{ return Side.CLIENT; }
}