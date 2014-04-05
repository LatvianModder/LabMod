package latmod.labmod.client.gui;
import org.lwjgl.input.*;

import latmod.core.gui.*;
import latmod.core.input.*;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.cmd.Command;

public class GuiChat extends GuiBasic implements IKeyListener.Pressed
{
	public String text = "";
	public String visibleText = "";
	
	public GuiChat() { super(null); }
	public void loadWidgets() { Keyboard.enableRepeatEvents(true); }
	public void onDestroyed() { Keyboard.enableRepeatEvents(false); }
	
	public void onRender()
	{
		Renderer.disableTexture();
		Renderer.colorize(0, 100);
		Renderer.rect(0, height - 24, width, 24);
		Renderer.enableTexture();
		Renderer.recolor();
		
		String txt = visibleText;
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
			MainClient.inst.worldSP.playerSP.printChat(text);
			Main.inst.openGui(null);
		}
		else if(LatCore.isASCIIChar(keyChar))
		{
			text += keyChar;
		}
		
		visibleText = text;
		
		if(text.startsWith("/") && text.length() > 1)
		{
			String[] s = LatCore.split(text.substring(1), " ");
			
			Command c = Command.commands.get(s[0]);
			
			if(c == null) visibleText = Command.ERR + text;
			else
			{
				visibleText = '/' + s[0] + ' ';
				
				for(int i = 1; i < s.length; i++)
				{
					TextColor col = c.getArgCol(i - 1, s[i]);
					visibleText += col + s[i] + ' ';
				}
			}
		}
		
		LatCore.println(LatCore.strip(LatCore.split("A B  C D   E", " ")));
		
		return Cancel.TRUE;
	}
	
	public void onWidgetEvent(int i, Widget w, String event, Object... args)
	{
	}
}