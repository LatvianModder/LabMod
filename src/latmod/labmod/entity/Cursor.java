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
	
	private Vertex look = new Vertex();
	private Vertex eyePos = new Vertex();
	
	public Cursor(EntityPlayer e)
	{ player = e; }
	
	public void update()
	{
		MathHelper.getLook(look, player.rotYaw, player.rotPitch, maxDist);
		eyePos.setPos(player.posX, player.posY + player.eyeHeight, player.posZ);
		
		aabbHit = null;
		lookEntity = null;
		relPos.setPos(0, 0, 0);
		side = RenderSide.NONE;
		dist = maxDist;
		posX = eyePos.posX + look.posX;
		posY = eyePos.posY + look.posY;
		posZ = eyePos.posZ + look.posZ;
		
		//Mouse3D mouse0 = null;
		Mouse3D ver0 = null;
		
		for(AABB aabb : player.worldObj.AABBList)
		{
			/*Mouse3D m = aabb.rayTrace(eyePos, look);
			
			if(m != null && (mouse0 == null || m.distSq < mouse0.distSq))
			{
				mouse0 = m;
				mouse0.box = aabb;
			}*/
			
			Mouse3D v = aabb.rayTrace(eyePos, look, maxDist);
			//Mouse3D v = aabb.rayTrace(eyePos, look);
			
			if(v != null && v.dist <= maxDist)
			{
				if(ver0 == null || v.dist < dist)
				{
					ver0 = v;
					aabbHit = aabb;
					lookEntity = (aabbHit.owner != null && aabbHit.owner instanceof Entity) ? (Entity)aabbHit.owner : null;
					side = v.side;
					dist = v.dist;
					relPos.setPos(ver0.hit);
					setPos(ver0.pos);
				}
			}
		}
		
		/*
		if(mouse0 != null)
		{
			aabbHit = mouse0.box;
			lookEntity = (aabbHit.owner != null && aabbHit.owner instanceof Entity) ? (Entity)aabbHit.owner : null;
			relPos = mouse0.hit;
			side = mouse0.face;
			dist = MathHelper.sqrt(mouse0.distSq);
		}
		*/
		
		/*
		double step = 1D / 150D;
		
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
		*/
	}
	
	public Vertex getShakenRel(Random r, double degShake)
	{ return getShakenRel(r, degShake, dist); }
	
	public Vertex getShakenRel(Random r, double degShake, double dist)
	{
		double dYaw = r.nextFloat() * degShake - degShake / 2D;
		double dPitch= r.nextFloat() * degShake - degShake / 2D;
		return MathHelper.getLook(null, player.rotYaw + dYaw, player.rotPitch + dPitch, dist);
	}
}