package latmod.labmod.client.gui;
import latmod.core.gui.Widget;
import latmod.core.rendering.*;
import latmod.core.res.Resource;
import latmod.labmod.Main;

public class GuiLoadingScreen extends GuiBasic
{
	public static final Resource logoTex = Resource.getTexture("gui/logo_1024.png");
	
	public int timer = 0;
	public boolean doneLoading = false;
	
	public GuiLoadingScreen()
	{ super(Main.inst); Main.inst.renderTick = 0; }
	
	public void onRender()
	{
		timer += 5;
		
		renderStars();
		
		Renderer.enableTexture();
		texManager.loadTexturesBlured = true;
		texManager.setTexture(logoTex);
		texManager.loadTexturesBlured = false;
		Color.reset();
		float s = parent.height / 2F;
		Renderer.rect((parent.width - s) / 2F, (parent.height - s) / 2F, s, s);
		
		if(timer < 255)
		{
			Renderer.disableTexture();
			Color.set(Color.BLACK, 255 - timer);
			Renderer.rect(0, 0, parent.width, parent.height);
		}
		
		if(timer == 255)
		{
			String txt = "Loading";
			parent.font.drawShadedText(parent.font.getCenterX(txt, 3F) + 8F, 64F, txt, 3F);
		}
		
		if(timer > 255)
		{
			if(!doneLoading)
			{
				Main.inst.loadServer();
				Main.inst.loadClient();
				doneLoading = true;
			}
			
			if(timer > 300)
			{
				Renderer.disableTexture();
				Color.set(Color.BLACK, timer - 300);
				Renderer.rect(0, 0, parent.width, parent.height);
				
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