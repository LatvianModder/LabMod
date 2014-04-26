package latmod.labmod.client.gui;
import org.lwjgl.input.*;

import latmod.core.gui.*;
import latmod.core.input.*;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.*;

public class GuiChat extends GuiBasic implements IKeyListener.Pressed
{
	public String text = "";
	
	public GuiChat() { super(null); }
	public void loadWidgets() { Keyboard.enableRepeatEvents(true); }
	public void onDestroyed() { Keyboard.enableRepeatEvents(false); }
	
	public void onRender()
	{
		Renderer.disableTexture();
		Color.BLACK.set(100);
		Renderer.rect(0, height - 24, width, 24);
		Renderer.enableTexture();
		Color.clear();
		
		String txt = text;
		if(Time.millis() % 1000 > 500) txt += '_';
		Font.inst.drawShadedText(4, height - 20, txt);
		
		int s = GuiIngame.allChat.size(); for(int i = 0; i < s; i++)
		Font.inst.drawShadedText(4, height - 4 - s * 20 + 20 * (i - 1), GuiIngame.allChat.get(i));
	}
	
	public Cancel onKeyPressed(int key, char keyChar)
	{
		if(key == Keyboard.KEY_BACK)
		{
			if(text.length() > 0) text = text.substring(0, text.length() - 1);
		}
		else if(key == Keyboard.KEY_RETURN)
		{
			Main.inst.worldObj.player.executeCommand(text);
			Main.inst.openGui(null);
		}
		else if(LatCore.isASCIIChar(keyChar))
		{
			text += keyChar;
		}
		
		return Cancel.TRUE;
	}
	
	public void onWidgetEvent(int i, Widget w, String event, Object... args)
	{
	}
}