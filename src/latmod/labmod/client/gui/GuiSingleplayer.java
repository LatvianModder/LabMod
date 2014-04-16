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
		addButton(0, width / 2, height - 56, 200, 48, "Back");
		
		Main.inst.openWorld();
		Main.inst.openGui(null);
	}
	
	public void onWidgetEvent(int i, Widget w, String event, Object... args)
	{
		if(event == Button.PRESSED)
		{
			if(i == 0) Main.inst.openGui(null);
		}
	}
}