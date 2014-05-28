package latmod.labmod.cmd;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.entity.EntityPlayer;

public class CmdTP extends Command
{
	public String onCommand(World w, EntityPlayer ep, String[] args, String argsUnsplit) throws Exception
	{
		if(args.length == 3)
		{
			float x = Float.parseFloat(args[0]);
			float y = Float.parseFloat(args[1]);
			float z = Float.parseFloat(args[2]);
			
			ep.setPos(x, y, z);
			print("Teleported to " + LatCore.stripDouble(x, y, z));
			
			return null;
		}
		
		return "Invalid arguments!";
	}
}