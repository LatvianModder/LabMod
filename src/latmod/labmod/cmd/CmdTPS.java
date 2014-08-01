package latmod.labmod.cmd;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.entity.EntityPlayer;

public class CmdTPS extends Command
{
	public String onCommand(World w, EntityPlayer ep, String[] args, String argsUnsplit)
	{
		if(args.length > 0)
		{
			if(!MathHelper.canParseInt(args[0])) return "Invalid number!";
			
			int i = MathHelper.toInt(args[0]);
			
			if(i > 0 && i <= 1000)
			{
				Main.inst.updateTimer.restart(i);
				return null;
			}
		}
		
		return "TPS argument is missing!";
	}
}