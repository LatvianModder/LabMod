package latmod.labmod.cmd;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.entity.*;

public class CmdSpawn extends Command
{
	public String onCommand(World w, EntityPlayer ep, String[] args, String argsUnsplit) throws Exception
	{
		if(args == null || args.length <= 0) return "Missing EntityID";
		
		int id = EntityID.getEID(args[0]);
		
		if(id != 0)
		{
			Vertex pos = ep.cursor;
			
			Entity e = EntityID.createEntity(w, id);
			e.setPos(pos.posX, pos.posY, pos.posZ);
			w.spawnEntity(e);
			
			print("Spawned '" + args[0] + "' @ " + LatCore.stripFloat(pos.posX, pos.posY, pos.posZ));
			return null;
		}
		
		return "Invalid EntityID '" + args[0] + "'!";
	}
}