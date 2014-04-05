package latmod.labmod.client.gui;
import latmod.core.gui.*;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.*;

public class GuiStart extends GuiBasic
{
	public GuiStart()
	{ super(null); }
	
	public void loadWidgets()
	{
		Button.setCentred(true, false);
		
		String serverTxt = MainServer.inst.isCreated() ? "Stop Server" : "Start Server";
		
		int h0 = height / 2; int h1 = 48;
		addWidget(1, new TextButton(this, serverTxt,	h0 + h1 * -3).setLeftAlign(false));
		addWidget(2, new TextButton(this, "Join Game",	h0 + h1 * -2).setLeftAlign(false));
		addWidget(3, new TextButton(this, "Options",	h0 + h1 * -1).setLeftAlign(false));
		addWidget(4, new TextButton(this, "Help",		h0 + h1 *  0).setLeftAlign(false));
		addWidget(0, new TextButton(this, "Exit Game",	h0 + h1 *  2).setLeftAlign(false));
	}
	
	public void onRender()
	{
		renderStars();
		
		super.onRender();
		
		Renderer.enableTexture();
		String txt = "Space Forge";
		float s = MathHelper.renderSin(0.03F, 3F, 3.5F);
		
		Font.inst.alpha = 200;
		Font.inst.drawText((width - Font.inst.textWidth(txt, s)) / 2F, 50F, txt, s);
		Font.inst.alpha = 255;
	}
	
	public boolean doRenderStars()
	{ return false; }
	
	public void onUpdate()
	{
	}
	
	public void onWidgetEvent(int i, Widget w, String event, Object... args)
	{
		if(event == Button.PRESSED)
		{
			if(i == 0) Main.inst.destroy();
			else if(i == 1)
			{
				if(MainServer.inst.isCreated())
				{
					MainServer.inst.stop();
					Main.inst.openGui(null);
				}
				else Main.inst.openGui(new GuiServer());
			}
			else if(i == 2) Main.inst.openGui(new GuiJoinServer());
			else if(i == 3) Main.inst.openGui(new GuiOptions());
			else if(i == 4) Main.inst.openGui(new GuiHelp());
		}
	}
	
	public void onEscPressed() { }
}
