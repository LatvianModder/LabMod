package latmod.labmod.client.gui;
import latmod.core.gui.*;
import latmod.core.rendering.Font;
import latmod.labmod.*;

public class GuiConnecting extends GuiBasic
{
	public boolean isFailed = false;
	
	public GuiConnecting()
	{ super(Main.inst); }
	
	public void loadWidgets()
	{
		Button.setCentred(true, false);
		addButton(0, width / 2, height - 64, 300, 48, "Back");
		addButton(1, width / 2, height - 128, 300, 48, "Refresh");
	}
	
	public void onRender()
	{
		super.onRender();
		
		if(!isFailed)
		{
			if(MainClient.inst.isCreated())
			{
				if(MainClient.inst.worldSP.client.isFailed)
				{
					MainClient.inst.stop(true);
					isFailed = true;
				}
				else if(!MainClient.inst.worldSP.client.isConnecting)
				{
					Main.inst.openGui(null);
				}
			}
			else MainClient.inst.start(GameOptions.props.lastIP);
		}
		
		String s = isFailed ? "Connection failed!" : "Connecting to server";
		Font.inst.drawShadedText(16F + Font.inst.getCenterX(s, 3F), height / 3, s, 3F);
	}
	
	public void onEscPressed() { }

	public void onWidgetEvent(int i, Widget w, String event, Object... args)
	{
		if(event == Button.PRESSED)
		{
			if(i == 0)
			{
				if(isFailed) Main.inst.openGui(null);
			}
			else if(i == 1)
			{
				isFailed = false;
			}
		}
	}
}