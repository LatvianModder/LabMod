package latmod.labmod.cmd;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.world.*;

public class CmdTPS extends Command
{
	public String onCommand(World w, CommandSender sender, String[] args, String argsUnsplit)
	{
		if(args.length > 0)
		{
			if(!MathHelper.canParseInt(args[0])) return "Invalid number!";
			
			int i = MathHelper.toInt(args[0]);
			
			if(i > 0 && i <= 1000)
			{
				Main.inst.updateTimer.onDestroyed();
				Main.inst.updateTimer = null;
				Main.inst.updateTimer = new Timer(Main.inst, i);
				Main.inst.updateTimer.start();
				return null;
			}
		}
		
		return "TPS argument is missing!";
	}
	
	public TextColor getArgCol(int i, String s)
	{ return FINE; }
	
	public Side getCommandSide()
	{ return Side.UNIVERSAL; }
}