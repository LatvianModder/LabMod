package latmod.labmod.cmd;
import latmod.core.util.MathHelper;
import latmod.labmod.*;
import latmod.labmod.entity.*;

public class CmdHP extends Command
{
	public String onCommand(World w, EntityPlayer ep, String[] args, String argsUnsplit)
	{
		if(args != null && args.length == 1)
		{
			int hp = MathHelper.toInt(args[0]);
			
			if(hp >= 0 && hp <= ep.maxHealth)
			{
				ep.health = hp;
			}
		}
		
		return null;
	}
}