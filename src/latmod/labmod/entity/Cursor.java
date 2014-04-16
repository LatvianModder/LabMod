package latmod.labmod.entity;
import java.util.Random;

import latmod.core.util.*;

public class Cursor extends Vertex
{
	public EntityPlayer player = null;
	public float dist = 0F;
	public float maxDist = 3F;
	public AABB aabbHit = null;
	public Entity lookEntity = null;
	public Vertex relPos = new Vertex(0F, 0F, 0F);
	public RenderSide side = RenderSide.NONE;
	
	public Cursor(EntityPlayer e)
	{ player = e; }
	
	public void update()
	{
		float step = 1F / 150F;
		aabbHit = null;
		side = RenderSide.NONE;
		lookEntity = null;
		
		Vertex dir = MathHelper.getLook(player.rotYaw, player.rotPitch);
		
		for(float d = 0F; d < 1F; d += step)
		{
			dist = d * maxDist;
			relPos.posX = dir.posX * dist;
			relPos.posY = dir.posY * dist;
			relPos.posZ = dir.posZ * dist;
			
			posX = player.posX + relPos.posX;
			posY = player.posY + relPos.posY;
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
	
	public Vertex getShakenRel(Random r, float degShake)
	{ return getShakenRel(r, degShake, dist); }
	
	public Vertex getShakenRel(Random r, float degShake, float dist)
	{
		float dYaw = r.nextFloat() * degShake - degShake / 2F;
		float dPitch= r.nextFloat() * degShake - degShake / 2F;
		Vertex dir = MathHelper.getLook(player.rotYaw + dYaw, player.rotPitch + dPitch);
		return new Vertex(dir.posX * dist, dir.posY * dist, dir.posZ * dist);
	}
}