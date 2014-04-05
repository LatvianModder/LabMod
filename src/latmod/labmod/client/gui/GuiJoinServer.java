package latmod.labmod.client.gui;
import latmod.core.gui.*;
import latmod.core.util.*;
import latmod.labmod.*;

public class GuiJoinServer extends GuiBasic
{
	public FastList<WidgetServer> serverList = new FastList<WidgetServer>();
	
	public GuiJoinServer()
	{ super(Main.inst); }
	
	public void loadWidgets()
	{
		Button.setCentred(false, false);
		TextBox.setCentred(false, false);
		
		addButton(0, width / 2 + width / 4 + 8, 8, width / 4 - 16, 48, "Back");
		addButton(1, width / 2 + 8, 8, width / 4 - 16, 48, "Add Server");
		addButton(2, width / 4 + 8, 8, width / 4 - 16, 48, "Connect");
		
		{
			TextBox tb = new TextBox(this, 8, 8, width / 4 - 16, 48);
			tb.setLeftAlign();
			tb.charLimit = 100;
			tb.txt = GameOptions.props.lastIP;
			addWidget(3, tb);
		}
		
		serverList.clear();
		
		//for(int i = 0; i < 15; i++)
		//addServer("Test" + Integer.toHexString(i * 9));
	}
	
	public boolean addServer(String s)
	{
		if(serverList.size() >= 9) return false;
		
		WidgetServer w = new WidgetServer(this, s);
		serverList.add(w);
		addWidget(100 + serverList.size(), w);
		return true;
	}

	public void onRender()
	{
		super.onRender();
		/*
		Renderer.disableTexture();
		Renderer.colorize(45);
		Renderer.rect(0, 64, width, 64);
		Renderer.rect(0, height - 64, width, 64);
		*/
	}
	
	public void onWidgetEvent(int i, Widget w, String event, Object... args)
	{
		if(event == Button.PRESSED)
		{
			if(i == 0) Main.inst.openPrevGui();
			else if(i == 1);
			else if(i == 2)
			{
				GameOptions.props.lastIP = widgets.get(3).txt;
				GameOptions.saveProps();
				
				MainClient.inst.start(GameOptions.props.lastIP);
				Main.inst.openGui(new GuiConnecting());
			}
		}
		else if(event == TextBox.CHANGED)
		{
			if(i == 3)
			{
				w.color = 60;
				
				String[] s = LatCore.split(w.txt, ":");
				if(s.length == 2)
				{
					if(!MainServer.isPort(s[1]))
					w.color = 0xFFFF0000;
				}
				else if(s.length != 1) w.color = 0xFFFF0000;
			}
		}
	}
	
	public static class WidgetServer extends Button
	{
		public GuiJoinServer guiMP = null;
		
		public WidgetServer(GuiJoinServer s, String s1)
		{
			super(s, 8F, 68F + s.serverList.size() * 56F, s.width - 16F, 48F, s1); guiMP = s;
			setLeftAlign(true);
		}
		
		public void onRender()
		{
			super.onRender();
		}
		
		public void onPostRender()
		{
			super.onPostRender();
		}
	}
}