package latmod.labmod.client.gui.ingame;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.client.gui.*;
import latmod.labmod.client.gui.GuiIngame.Message;
import latmod.labmod.entity.*;

public class DPIngame extends DebugPage
{
	public DPIngame()
	{ super(""); }
	
	public void loadTextures()
	{
	}
	
	public void onCustom2DRender(World w, EntityPlayerSP ep)
	{
		if(ep == null || !ep.renderGui) return;
		
		boolean chatOpened = Main.inst.getGui() instanceof GuiChat;
		
		if(!chatOpened)
		{
			for(int i = 0; i < GuiIngame.visibleChat.size(); i++)
			{
				Message m = GuiIngame.visibleChat.get(i);
				Font.inst.alpha = Math.min(m.tick, 255);
				Font.inst.drawShadedText(4, Main.inst.height - 4 - 20 * (i + 2), m.text);
			}
		}
		
		Font.inst.alpha = 255;
		Renderer.disableTexture();
		
		{
			int bw = 50;
			int bh = 90;
			
			Color.GREEN.set(75);
			Renderer.rect(Main.inst.width - bw - 8, Main.inst.height - bh - 8, bw, bh);
		}
		
		if(ep.hurtTimer > 0) Renderer.drawCircle(Main.inst.width / 2F, Main.inst.height / 2F,
		Main.inst.width * 1.5F, Color.NONE, Color.RED.clone((int)(ep.hurtTimer * 25F)));
		
		Renderer.enableTexture();
	}
	
	public void addInfo(World w, EntityPlayerSP ep, FastList<String> al)
	{
	}
}