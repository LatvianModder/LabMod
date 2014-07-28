package latmod.labmod.client.gui.ingame;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.client.gui.*;
import latmod.labmod.entity.*;
import org.lwjgl.input.Keyboard;

public class PlayerInputHandler
{
	public static final PlayerInputHandler inst = new PlayerInputHandler();
	
	public void keyPressed(EntityPlayerSP ep, latmod.core.input.EventKey.Pressed e)
	{
		KeyBinding kb = GameOptions.keyBindingFromKey(e.key);
		
		if(kb != null)
		{
			if(kb.isRegistredDouble())
			{
				long m = Time.millis();
				long lm = ep.lastKeyPressedMillis.get(kb);
				ep.lastKeyPressedMillis.put(kb, m);
				
				if(m - lm <= 300)
				{
					doubleKeyPressed(ep, kb);
					e.cancel();
					return;
				}
			}
			
			if(e.key == GameOptions.KEY_DEBUG.key)
			{
				if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
				{
					int id = ep.debugPage.pageID - 1;
					if(id < 0) id = DebugPage.debugPages.size() - 1;
					ep.debugPage = DebugPage.debugPages.get(id);
				}
				else
				{
					int id = ep.debugPage.pageID + 1;
					id %= DebugPage.debugPages.size();
					ep.debugPage = DebugPage.debugPages.get(id);
				}
			}
			else if(e.key == GameOptions.KEY_CHAT.key)
			{
				//if(Main.mainArgs.keys.contains("-console"))
				Main.inst.openGui(new GuiChat());
			}
			else if(e.key == GameOptions.KEY_SHOP.key)
				;
			else if(e.key == GameOptions.KEY_HIDE_GUI.key)
				ep.renderGui = !ep.renderGui;
			
			e.cancel();
			return;
		}
	}
	
	public void keyReleased(EntityPlayerSP ep, latmod.core.input.EventKey.Released e)
	{
		KeyBinding kb = GameOptions.keyBindingFromKey(e.key);
		
		if(kb != null)
		{
			//ep.isDirty = true;
		}
	}
	
	public void doubleKeyPressed(EntityPlayerSP ep, KeyBinding kb)
	{
		if(kb.key == GameOptions.KEY_MOVE_FORWARD.key)
			ep.isRunning = true;
	}
	
	public void mousePressed(EntityPlayerSP ep, latmod.core.input.EventMouse.Pressed e)
	{
		if(!Main.inst.getGui().allowPlayerInput()) return;
		
		if(ep.cursor.lookEntity != null)
		{
			if(e.button == 0)
			{
				boolean b = ep.cursor.lookEntity.onAttacked(ep, 1F);
				if(b) ep.cursor.lookEntity.setDead();
			}
			else if(e.button == 1) ep.cursor.lookEntity.onRightClick(ep);
		}
	}
	
	public void mouseReleased(EntityPlayerSP ep, latmod.core.input.EventMouse.Released e)
	{
	}
	
	public void mouseMoved(EntityPlayerSP ep, latmod.core.input.EventMouse.Moved e)
	{
		ep.rotYaw -= e.mouse.DX * GameOptions.props.rotSens * 0.05F;
		ep.rotPitch -= e.mouse.DY * GameOptions.props.rotSens * 0.05F;
		
		ep.rotYaw %= 360F;
		ep.rotPitch = MathHelper.limit(ep.rotPitch, -89.999F, 89.999F);
	}
	
	public void mouseScrolled(EntityPlayerSP ep, latmod.core.input.EventMouse.Scrolled e)
	{
		//ep.camera.zoom -= m.scroll * 0.5F;
		//ep.camera.zoom = MathHelper.limit(ep.camera.zoom, 2F, 10F);
	}
}