package latmod.labmod.client.gui;
import latmod.core.gui.*;
import latmod.core.util.*;
import latmod.labmod.*;

public class GuiServer extends GuiBasic
{
	public GuiServer()
	{ super(Main.inst); }

	public void loadWidgets()
	{
		Button.setCentred(true, true);
		
		addButton(0, width / 2, height / 12F * 11F, 300F, 48F, "Back");
		addButton(1, width / 2, height / 12F * 10F, 300F, 48F, "Create");
		
		TextBox.setCentred(true, true);
		
		addWidget(2, new TextBox(this, width / 2F, height / 12F * 3F, 300F, 48F).setLabel("Server Name / IP:Port").setText("Default"));
		addWidget(3, new TextBox(this, width / 2F, height / 12F * 4F, 300F, 48F).setText("localhost:" + GameOptions.DEF_PORT));
	}
	
	public void onWidgetEvent(int i, Widget w, String event, Object... args)
	{
		if(event == Button.PRESSED)
		{
			if(i == 0) Main.inst.openGui(null);
			else if(i == 1)
			{
				String serverName = widgets.get(2).txt;
				String serverIP = widgets.get(3).txt;
				
				MainServer.inst.create();
				MainServer.inst.worldMP.serverName = serverName;
				MainServer.inst.start(serverIP, 16);
				
				Main.inst.openGui(null);
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
}