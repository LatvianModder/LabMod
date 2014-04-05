package latmod.labmod.client.particles;
import latmod.core.rendering.*;
import latmod.labmod.entity.*;
import latmod.labmod.world.*;

public class Particle extends Entity
{
	public int color = 0xFFFF0000;
	public int tick = 0, maxTick = 0;
	
	public Particle(World w, float x, float y, float z, double mx, double my, double mz)
	{
		super(w); setPos(x, y, z);
		maxTick = 90 + w.rand.nextInt(30);
		motX = (float)mx; motY = (float)my; motZ = (float)mz;
	}
	
	public Particle(World w, float x, float y, float z)
	{ this(w, x, y, z, w.rand.nextGaussian() * 0.05F, w.rand.nextFloat() * 0.06F, w.rand.nextGaussian() * 0.05F); }
	
	public void onUpdate()
	{
		posX += motX;
		posY += motY;
		posZ += motZ;
		
		tick++;
		if(tick >= maxTick)
		flags[IS_DEAD] = true;
	}
	
	public void onRender()
	{
		Renderer.colorize(color);
		Renderer.push();
		Renderer.translate(this, 1F);
		Renderer.rotate(Renderer3D.camYaw, Renderer3D.camPitch);
		Renderer.rect(-sizeH / 2F, -sizeV / 2F, sizeH, sizeV);
		Renderer.pop();
	}
	
	public PartLayer getLayer()
	{ return PartLayer.TEX; }
}