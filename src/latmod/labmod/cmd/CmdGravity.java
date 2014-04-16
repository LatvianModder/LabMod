package latmod.labmod.cmd;
import latmod.core.util.MathHelper;
import latmod.labmod.*;
import latmod.labmod.entity.*;

public class CmdGravity extends Command
{
	public String onCommand(World w, EntityPlayer ep, String[] args, String argsUnsplit)
	{
		if(args != null && args.length == 1)
		{
			float g = MathHelper.toFloat(args[0]);
			w.gravity = g;
			print("Gravity set to " + g);
			return null;
		}
		
		return "Missing arg!";
	}
}