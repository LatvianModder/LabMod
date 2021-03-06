package latmod.labmod.client.gui;
import latmod.core.gui.*;
import latmod.labmod.*;

public class GuiSingleplayer extends GuiBasic
{
	public GuiSingleplayer()
	{ super(Main.inst); }
	
	public void loadWidgets()
	{
		Button.setCentred(true, false);
		addButton(0, parent.width / 2, parent.height - 56, 200, 48, "Back");
		addButton(1, parent.width / 2, 40, 200, 48, "New world");
	}
	
	public void onWidgetEvent(int i, Widget w, String event, Object... args)
	{
		if(event == Button.PRESSED)
		{
			if(i == 0) Main.inst.openGui(null);
			else if(i == 1) Main.inst.openWorld();
		}
	}
}