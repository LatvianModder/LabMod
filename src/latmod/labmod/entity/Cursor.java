package latmod.labmod.entity;
import java.util.Random;

import latmod.core.util.*;

public class Cursor extends Vertex
{
	public EntityPlayer player = null;
	public double dist = 0F;
	public double maxDist = 3F;
	public AABB aabbHit = null;
	public Entity lookEntity = null;
	public Vertex relPos = new Vertex();
	public RenderSide side = RenderSide.NONE;
	
	public Cursor(EntityPlayer e)
	{ player = e; }
	
	public void update()
	{
		double step = 1D / 150D;
		aabbHit = null;
		side = RenderSide.NONE;
		lookEntity = null;
		
		Vertex dir = MathHelper.getLook(player.rotYaw, player.rotPitch);
		
		for(double d = 0F; d < 1D; d += step)
		{
			dist = d * maxDist;
			relPos.posX = dir.posX * dist;
			relPos.posY = dir.posY * dist;
			relPos.posZ = dir.posZ * dist;
			
			posX = player.posX + relPos.posX;
			posY = player.posY + player.eyeHeight + relPos.posY;
			posZ = player.posZ + relPos.posZ;
			
			AABB box = player.worldObj.getAABBAtPoint(posX, posY, posZ);
			if(box != null)
			{
				aabbHit = box;
				lookEntity = (aabbHit.owner != null && aabbHit.owner instanceof Entity) ? (Entity)aabbHit.owner : null;
				return;
			}
		}
	}
	
	public Vertex getShakenRel(Random r, double degShake)
	{ return getShakenRel(r, degShake, dist); }
	
	public Vertex getShakenRel(Random r, double degShake, double dist)
	{
		double dYaw = r.nextFloat() * degShake - degShake / 2D;
		double dPitch= r.nextFloat() * degShake - degShake / 2D;
		Vertex dir = MathHelper.getLook(player.rotYaw + dYaw, player.rotPitch + dPitch);
		return new Vertex(dir.posX * dist, dir.posY * dist, dir.posZ * dist);
	}
}