package latmod.labmod.cmd;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.client.gui.GuiIngame;
import latmod.labmod.net.packets.PacketChat;
import latmod.labmod.world.*;

public class CmdSay extends Command
{
	public String onCommand(World w, CommandSender sender, String[] args, String argsUnsplit)
	{
		if(argsUnsplit != null && argsUnsplit.length() > 0)
		{
			String s = TextColor.AQUA + "[" + sender.name + "]: " + TextColor.WHITE + argsUnsplit;
			
			GuiIngame.printChat(s);
			w.sendPacket(new PacketChat(s));
			return null;
		}
		
		return "Too few arguments!";
	}
	
	public TextColor getArgCol(int i, String s)
	{
		return FINE;
	}
	
	public Side getCommandSide()
	{ return Side.SERVER; }
}