package latmod.labmod.client.gui;
import latmod.core.gui.*;
import latmod.core.rendering.*;
import latmod.labmod.*;

public class GuiStart extends GuiBasic
{
	public GuiStart()
	{ super(Main.inst); }
	
	public void loadWidgets()
	{
		Button.setCentred(true, false);
		
		int h0 = parent.height / 2; int h1 = 48;
		addWidget(1, new TextButton(this, "Play",		h0 + h1 * -3).setLeftAlign(false));
		addWidget(2, new TextButton(this, "Options",	h0 + h1 * -2).setLeftAlign(false));
		addWidget(3, new TextButton(this, "Help",		h0 + h1 *  -1).setLeftAlign(false));
		addWidget(0, new TextButton(this, "Exit Game",	h0 + h1 *  1).setLeftAlign(false));
	}
	
	public void onRender()
	{
		renderStars();
		
		super.onRender();
		
		Renderer.enableTexture();
		String txt = "LabMod";
		//double s = renderSin(0.03D, 0D, 3D, 3.5D);
		double s = 3D;
		
		parent.font.alpha = 200;
		parent.font.drawText((parent.width - parent.font.textWidth(txt, s)) / 2F, 50F, txt, s);
		parent.font.alpha = 255;
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
			else if(i == 1) Main.inst.openGui(new GuiSingleplayer());
			else if(i == 2) Main.inst.openGui(new GuiOptions());
			else if(i == 3) Main.inst.openGui(new GuiHelp());
		}
	}
	
	public void onEscPressed() { }
}
