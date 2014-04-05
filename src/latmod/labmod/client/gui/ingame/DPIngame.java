package latmod.labmod.client.gui.ingame;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.client.gui.*;
import latmod.labmod.client.gui.GuiIngame.Message;
import latmod.labmod.entity.*;
import latmod.labmod.world.*;

public class DPIngame extends DebugPage
{
	public static Texture bars[] = new Texture[4];
	public static Texture barLines[] = new Texture[4];
	
	public DPIngame()
	{ super(""); }
	
	public void loadTextures()
	{
		bars[0] = Renderer.getTexture("gui/bars/health.png");
		bars[1] = Renderer.getTexture("gui/bars/armor.png");
		bars[2] = Renderer.getTexture("gui/bars/ammo.png");
		bars[3] = Renderer.getTexture("gui/bars/reload.png");
		
		barLines[0] = Renderer.getTexture("gui/bars/healthO.png");
		barLines[1] = Renderer.getTexture("gui/bars/armorO.png");
		barLines[2] = Renderer.getTexture("gui/bars/ammoO.png");
		barLines[3] = Renderer.getTexture("gui/bars/reloadO.png");
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
			if(ep.camera.entityHit != null && ep.camera.entityHit != null && ep.camera.entityHit.isGreenDot(ep))
			Renderer.colorize(50, 220, 50); else
			Renderer.colorize(220, 50, 50);
			
			Renderer.lineWidth(4F);
			Renderer.enableSmooth();
			Renderer.point(Main.inst.width / 2F, Main.inst.height / 2F);
			Renderer.disableSmooth();
			Renderer.lineWidth(1F);
		}
		
		{
			float hp = 1F;
			float arm = 1F;
			float amm = 1F;
			float rel = 1F;
			
			float s = 128F;
			float y = Main.inst.height - s;
			float xw = Main.inst.width - s;
			
			//Renderer.loadTexturesSmooth = true;
			Renderer.enableTexture();
			Renderer.colorize(255, 120);
			
			Renderer.setTexture(bars[0]); drawHQuad(0, y, s, hp);
			if(arm > 0F) { Renderer.setTexture(bars[1]); drawHQuad(0, y, s, arm); }
			if(amm > 0F) { Renderer.setTexture(bars[2]); drawHQuad(xw, y, s, amm);}
			if(rel > 0F) { Renderer.setTexture(bars[3]); drawHQuad(xw, y, s, rel); }
			
			Renderer.colorize(255, 160);
			Renderer.setTexture(barLines[0]); Renderer.rect(0, y, s, s);
			if(arm > 0F) { Renderer.setTexture(barLines[1]); Renderer.rect(0, y, s, s); }
			if(amm > 0F) { Renderer.setTexture(barLines[2]); Renderer.rect(xw, y, s, s); }
			if(rel > 0F) { Renderer.setTexture(barLines[3]); Renderer.rect(xw, y, s, s); }
			
			//Renderer.loadTexturesSmooth = false;
		}
		
		if(ep.hurtTimer > 0) Renderer.drawCircle(Main.inst.width / 2F, Main.inst.height / 2F,
		Main.inst.width * 1.5F, 0x00FFFFFF, Renderer.getColor(0xFF0000, (int)(ep.hurtTimer * 25F)));
		
		Renderer.enableTexture();
	}
	
	private static void drawHQuad(float x, float y, float s, float h)
	{ Renderer.rect(x, y, s, s, 0F, 0F, 1F, 1F); }

	public void addInfo(World w, EntityPlayerSP ep, FastList<String> al)
	{
	}
}