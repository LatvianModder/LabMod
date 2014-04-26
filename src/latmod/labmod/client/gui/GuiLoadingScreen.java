package latmod.labmod.client.gui;
import latmod.core.gui.Widget;
import latmod.core.rendering.*;
import latmod.labmod.Main;

public class GuiLoadingScreen extends GuiBasic
{
	public int timer = 0;
	public boolean doneLoading = false;
	
	public GuiLoadingScreen()
	{ super(Main.inst); Main.inst.renderTick = 0; }
	
	public void onRender()
	{
		if(Main.inst.renderTick % 3 == 0) timer++;
		
		renderStars();
		
		Renderer.enableTexture();
		Renderer.loadTexturesSmooth = true;
		Renderer.setTexture("gui/logo_1024.png");
		Renderer.loadTexturesSmooth = false;
		Color.clear();
		float s = height / 2F;
		Renderer.rect((width - s) / 2F, (height - s) / 2F, s, s);
		
		if(timer < 255)
		{
			Renderer.disableTexture();
			Color.BLACK.set(255 - timer);
			Renderer.rect(0, 0, width, height);
		}
		
		if(timer == 255)
		{
			String txt = "Loading";
			Font.inst.drawShadedText(Font.inst.getCenterX(txt, 3F) + 8F, 64F, txt, 3F);
		}
		
		if(timer > 255)
		{
			if(!doneLoading)
			{
				Main.loadServer();
				Main.loadClient();
				doneLoading = true;
			}
			
			if(timer > 300)
			{
				Renderer.disableTexture();
				Color.BLACK.set(timer - 300);
				Renderer.rect(0, 0, width, height);
				
				if(timer == 555)
				{
					Main.inst.openGui(null);
				}
			}
		}
	}
	
	public void onEscPressed()
	{
		//Main.inst.openGui(null);
	}
	
	public void loadWidgets()
	{
	}

	public void onWidgetEvent(int i, Widget w, String event, Object... args)
	{
	}
}