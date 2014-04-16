package latmod.labmod.client.particles;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.*;

public class PartBullet extends Particle
{
	//public Vertex target;
	
	public PartBullet(World w, float x, float y, float z, float tx, float ty, float tz)
	{
		super(w, x, y, z, 0F, 0F, 0F);
		//target = new Vertex(tx, ty, tz);
		maxTick = 2;
		color = 0xFFFFBB00;
		
		//setPos(MathHelper.getMidPoint(this, target, w.rand.nextFloat()));
		float dX = tx - x;
		float dY = ty - y;
		float dZ = tz - z;
		float d1 = 6F + w.rand.nextFloat() * 10F;
		float d = MathHelper.sqrt3(tx, ty, tz);
		
		motX = dX / d * d1;
		motY = dY / d * d1;
		motZ = dZ / d * d1;
		
		//target.posX = motX;
		//target.posY = motY;
		//target.posZ = motZ;
	}
	
	public void onRender()
	{
		Renderer.recolor();
		Renderer3D.disable3DAlpha();
		//Renderer.colorize(color, 255F * (1F - (tick / (float)maxTick)));
		Renderer.setTexture("gui/star.png");
		//Renderer.colorize(color);
		//Renderer.lineWidth(2F);
		//Renderer.line(posX, posY, posZ, posX + target.posX, posY + target.posY, posZ + target.posZ);
		//Renderer.lineWidth(1F);
		//Renderer.
	}
	
	public PartLayer getLayer()
	{ return PartLayer.CUSTOM; }
}