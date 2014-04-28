package latmod.labmod.client.gui;
import latmod.core.gui.*;
import latmod.core.input.*;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.client.gui.ingame.PlayerInputHandler;
import latmod.labmod.entity.EntityPlayerSP;

public class GuiIngame extends GuiBasic implements IKeyListener.Pressed, IKeyListener.Released, IMouseListener.Pressed, IMouseListener.Released, IMouseListener.Scrolled, IMouseListener.Moved
{
	public static FastList<Message> visibleChat = new FastList<Message>();
	public static FastList<String> allChat = new FastList<String>();
	
	public static class Message
	{ public String text = null;
	public int tick = 0; }
	
	public static void printChat(String s)
	{
		allChat.add(s);
		
		FastList<Message> al = new FastList<Message>();
		Message m = new Message();
		m.text = s;
		m.tick = 300;
		al.add(m);
		al.addAll(visibleChat);
		visibleChat = al;
		
		Main.gameLogger.info("[Chat]: " + s);
	}
	
	public GuiIngame()
	{ super(null); }
	
	public void onRender()
	{
	}
	
	public void loadWidgets()
	{
	}
	
	public void onWidgetEvent(int i, Widget w, String event, Object... args)
	{
	}
	
	public static void updateChat()
	{
		for(int i = 0; i < visibleChat.size(); i++)
		{ Message m = visibleChat.get(i);
		if(m.tick <= 0) visibleChat.remove(i);
		else m.tick--; }
	}
	
	public boolean allowPlayerInput()
	{ return true; }
	
	public EntityPlayerSP player()
	{ return Main.inst.worldObj.player; }
	
	public void onEscPressed()
	{ Main.inst.openGui(new GuiPause()); }
	
	public Cancel onKeyPressed(int key, char keyChar)
	{ if(Main.inst.hasPlayer()) return PlayerInputHandler.inst.keyPressed(player(), key, keyChar); return Cancel.FALSE; }
	
	public void onKeyReleased(int key)
	{ if(Main.inst.hasPlayer()) PlayerInputHandler.inst.keyReleased(player(), key, 1L); }
	
	public void onMouseScrolled(LMMouse m)
	{ if(Main.inst.hasPlayer()) PlayerInputHandler.inst.mouseScrolled(player(), m); }

	public void onMouseReleased(LMMouse m)
	{ if(Main.inst.hasPlayer()) PlayerInputHandler.inst.mouseReleased(player(), m, 1L); }

	public Cancel onMousePressed(LMMouse m)
	{ if(Main.inst.hasPlayer()) return PlayerInputHandler.inst.mousePressed(player(), m); return Cancel.FALSE; }
	
	public void onMouseMoved(LMMouse m)
	{ if(Main.inst.hasPlayer()) PlayerInputHandler.inst.mouseMoved(player(), m); }
}