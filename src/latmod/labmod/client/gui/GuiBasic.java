package latmod.labmod.client.gui;
import java.util.Random;

import latmod.core.gui.*;
import latmod.core.rendering.*;
import latmod.labmod.*;

public abstract class GuiBasic extends Gui
{
	public int width, height;
	public Class<? extends GuiBasic> prevGui = null;
	
	public GuiBasic(Main m)
	{
		super(m);
		
		if(stars == null)
		{
			stars = new Star[300];
			for(int i = 0; i < stars.length; i++)
			stars[i] = new Star().randomX();
		}
	}
	
	public void addButton(int id, float x, float y, float w, float h, String s)
	{
		Button b = new Button(this, x, y, w, h, s);
		if(Main.inst.worldObj != null) b.setColor(Color.get(0xFFC1C1C1));
		addWidget(id, b);
	}
	
	public void onReplacedBy(GuiBasic g)
	{
	}
	
	public boolean allowPlayerInput()
	{ return false; }
	
	public void onRender()
	{ renderBackground(); super.onRender(); }
	
	public void renderBackground()
	{
		if(Main.inst.worldObj == null)
		{
			if(doRenderStars())
			{
				renderStars();
				Renderer.disableTexture();
				Color.BLACK.set(200);
				Renderer.rect(0, 0, width, height);
				Renderer.enableTexture();
			}
		}
		else
		{
			Renderer.disableTexture();
			Color.BLACK.set(50);
			Renderer.rect(0, 0, width, height);
			Renderer.enableTexture();
		}
	}
	
	public boolean doRenderStars()
	{ return true; }
	
	public void onEscPressed()
	{ Main.inst.openGui(null); }
	
	// - Background - //
	
	public static Star[] stars = null;
	
	public static void resetStars(float deltaW, float deltaH)
	{
		for(int i = 0; i < stars.length; i++)
		if(stars[i] != null)
		{
			stars[i].posX /= deltaW;
			stars[i].posY /= deltaH;
		}
	}
	
	public static void renderStars()
	{
		Renderer.enableTexture();
		Renderer.loadTexturesSmooth = true;
		Renderer.setTexture("gui/star.png");
		Renderer.loadTexturesSmooth = false;
		Color.WHITE.set(75);
		
		for(int i = 0; i < stars.length; i++)
		if(stars[i] != null) stars[i].render();
		
		Color.clear();
	}
	
	public static final class Star
	{
		public static Random starRand = new Random(30920394L);
		
		public float posX, posY;
		public float size;
		public float rotation;
		public float rotSpeed;
		
		public Star()
		{
			size = starRand.nextInt(900) / 100F + 2F;
			posX = -size;
			posY = starRand.nextInt(Main.inst.height * 3) / 3F;
			rotation = starRand.nextInt(90);
			rotSpeed = starRand.nextFloat() * 1.3F;
		}
		
		public Star randomX()
		{
			posX = starRand.nextInt(Main.inst.width * 3) / 3F;
			return this;
		}
		
		public void render()
		{
			Renderer.push();
			Renderer.translate(posX, posY);
			Renderer.rotateZ(rotation);
			
			Renderer.rect(-size / 2F, -size / 2F, size, size);
			
			Renderer.pop();
			
			posX += size / 100F;
			rotation += rotSpeed;
			if(posX - size > Main.inst.width) posX = -size;
		}
	}
}