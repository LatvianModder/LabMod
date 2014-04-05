package latmod.labmod.cmd;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.entity.*;
import latmod.labmod.world.*;

public class CmdSpawn extends Command
{
	public String onCommand(World w, CommandSender sender, String[] args, String argsUnsplit)
	{
		if(args == null || args.length <= 0) return "Missing EntityID";
		
		int id = EntityID.getEID(args[0]);
		
		if(id != 0)
		{
			Vertex pos = sender.player.camera;
			
			try
			{
				Entity e = EntityID.createEntity(w, id);
				e.setPos(pos.posX, pos.posY, pos.posZ);
				w.spawnEntity(e);
			}
			catch(Exception e)
			{ e.printStackTrace(); return "Failed to contruct entity!"; }
			
			print("Spawned '" + args[0] + "' @ " + LatCore.stripFloat(pos.posX, pos.posY, pos.posZ));
			return null;
		}
		
		return "Invalid EntityID '" + args[0] + "'!";
	}

	public TextColor getArgCol(int i, String s)
	{
		if(i == 0) return EntityID.entityNameMap.values.contains(s) ? NAME : ERR;
		else return ERR;
	}
	
	public Side getCommandSide()
	{ return Side.CLIENT; }
}