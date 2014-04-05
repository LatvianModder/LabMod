package latmod.labmod.client.gui;
import latmod.core.gui.*;
import latmod.labmod.*;

public class GuiPause extends GuiBasic
{
	public GuiPause()
	{ super(Main.inst); }

	public void loadWidgets()
	{
		int w = 340;
		int x = width / 2;
		int h = height / 12;
		
		Button.setCentred(true, true);
		
		addButton(1, x, h * 3, w, 40, "Resume");
		addButton(2, x, h * 5, w, 40, "Options");
		
		addButton(3, x - w / 4 - 2, h * 4, w / 2 - 4, 40, "Save");
		addButton(4, x + w / 4 + 2, h * 4, w / 2 - 4, 40, "Load");
		
		addButton(0, x, h * 8, w, 40, "Quit");
		
		Button.setCentred(false, false);
	}
	
	public void onWidgetEvent(int i, Widget w, String event, Object... args)
	{
		if(i == 0)
		{
			MainClient.inst.stop(false);
			while(MainClient.inst.isCreated());
			Main.inst.openGui(null);
		}
		else if(i == 1) Main.inst.openGui(null);
		else if(i == 2) Main.inst.openGui(new GuiOptions());
		else if(i == 3)
		{
		}
		else if(i == 4)
		{
		}
	}
}