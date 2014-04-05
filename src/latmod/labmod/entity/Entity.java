package latmod.labmod.entity;
import java.util.Iterator;

import latmod.core.nbt.*;
import latmod.core.util.*;
import latmod.labmod.client.entity.*;
import latmod.labmod.world.*;

public abstract class Entity extends Vertex
{
	public static final int IS_DEAD = 0;
	public static final int IN_WALL = 1;
	public static final int NO_CLIP = 2;
	public static final int BOOST = 3;
	
	public World worldObj;
	public boolean[] flags = new boolean[8];
	public float rotYaw, rotPitch;
	public boolean isDirty = false;
	public float sizeH, sizeV;
	public AABB collisionBox = null;
	public float motX, motY, motZ;
	public int worldID;
	protected String displayName = getClass().getSimpleName();
	
	public Entity(World w)
	{
		worldObj = w;
	}
	
	public int hashCode()
	{ return worldID; }
	
	public String toString()
	{ return displayName + ": " + worldID; }
	
	public boolean equals(Object o)
	{ return (o == this) || ((o instanceof Entity) ? ((o.hashCode() == hashCode()) ? true : false) : false); }
	
	public void setSize(float h, float v)
	{ sizeH = h; sizeV = v; }
	
	public void onUpdate(Timer t)
	{
	}
	
	private EntityRenderer entityRenderer;
	public void onRender()
	{
		if(entityRenderer == null)
		entityRenderer = EntityRenderer.getRenderer(getClass());
		if(entityRenderer != null) entityRenderer.renderEntity(this);
	}
	
	public boolean isVisible()
	{ return true; }
	
	public void setDead()
	{ flags[IS_DEAD] = true; }
	
	public void setMotion(float x, float y, float z)
	{ motX = x;  posY = y; posZ = z; }
	
	public void addMotion(float x, float y, float z)
	{ motX += x;  posY += y; posZ += z; }
	
	public void moveTowards(float x, float z, float s)
	{
		if((x == 0F && z == 0F) || s == 0F) return;
		float f = MathHelper.sqrt2(x, z);
		addMotion(x / f * s, 0F, z / f * s);
	}
	
	public void moveEntity()
	{
		if(motX == 0F && motY == 0F && motZ == 0F) return;
		
		if(!flags[NO_CLIP])
		{
			AABB bx = worldObj.getAABBInBox(collisionBox, motX * 2F, 0F, 0F);
			if(bx != null)
			{
				motX = 0F;
			}
			
			AABB bz = worldObj.getAABBInBox(collisionBox, 0F, 0F, motZ * 2F);
			if(bz != null)
			{
				motZ = 0F;
			}
			
			AABB by = worldObj.getAABBInBox(collisionBox, 0F, motY * 2F, 0F);
			if(by != null)
			{
				motY = 0F;
			}
		}
		
		if(motX != 0F || motY != 0F || motZ != 0F)
		{
			posX += motX;
			posY += motY;
			posZ += motZ;
			isDirty = true;
		}
		
		collisionBox.set(posX, posY, posZ, sizeH, sizeV, sizeH);
	}
	
	public void addCollisionBoxes(FastList<AABB> b)
	{
		if(collisionBox != null) collisionBox.set(posX, posY, posZ, sizeH, sizeV, sizeH);
		else collisionBox = new AABB.BottomCentred(posX, posY, posZ, sizeH, sizeV, sizeH);
		collisionBox.owner = this;
		collisionBox.solid = false;
		b.add(collisionBox);
	}
	
	public void readFromNBT(NBTMap map)
	{
		flags = MathHelper.toBool(map.getByte("Flags"));
		
		Iterator<Float> floats = map.getList("PosRot").getIterator();
		posX = floats.next();
		posY = floats.next();
		posZ = floats.next();
		rotYaw = floats.next();
		rotPitch = floats.next();
		motX = floats.next();
		motY = floats.next();
		motZ = floats.next();
	}
	
	public void writeToNBT(NBTMap map)
	{
		map.setByte("Flags", MathHelper.toInt(flags));
		
		NBTList floats = new NBTList("PosRot");
		floats.addObj(posX);
		floats.addObj(posY);
		floats.addObj(posZ);
		floats.addObj(rotYaw);
		floats.addObj(rotPitch);
		floats.addObj(motX);
		floats.addObj(motY);
		floats.addObj(motZ);
		map.setList(floats);
	}
	
	/** Return true if entity got killed */
	public boolean onAttacked(Entity entity, float f)
	{ return false; }

	public boolean isGreenDot(EntityPlayerSP ep)
	{ return true; }
}