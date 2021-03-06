package latmod.labmod.client.particles;
import latmod.core.rendering.*;
import latmod.labmod.World;
import latmod.labmod.entity.*;

public class Particle extends Entity
{
	public Color color = Color.WHITE;
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
		Color.set(color);
		Renderer.push();
		Renderer.translate(this, 1F);
		Renderer.rotate(Renderer3D.camera.yaw, Renderer3D.camera.pitch);
		Renderer.rect(-sizeH / 2F, -sizeV / 2F, sizeH, sizeV);
		Renderer.pop();
	}
	
	public PartLayer getLayer()
	{ return PartLayer.TEX; }
}