package latmod.labmod.entity;
import java.util.Iterator;
import com.google.gson.annotations.Expose;
import latmod.core.nbt.*;
import latmod.core.util.*;
import latmod.labmod.World;
import latmod.labmod.client.entity.*;

public abstract class Entity extends Vertex
{
	public static final int IS_DEAD = 0;
	public static final int IN_WALL = 1;
	public static final int NO_CLIP = 2;
	public static final int ON_GROUND = 3;
	public static final int BOOST = 4;
	
	public World worldObj;
	public boolean[] flags = new boolean[8];
	@Expose public float rotYaw, rotPitch;
	public float sizeH, sizeV;
	public AABB collisionBox = null;
	@Expose public float motX = 0F, motY = 0F, motZ = 0F;
	public long worldID;
	@Expose public String displayName = getClass().getSimpleName();
	
	public Entity(World w)
	{ worldObj = w; }
	
	public int hashCode()
	{ return (int)worldID; }
	
	public String toString()
	{ return "[ E" + EntityID.getEID(this).entityID + ", W" + worldID + " ]: " + displayName; }
	
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
	
	public void onDeath() { }
	
	public void setMotion(float x, float y, float z)
	{ motX = x;  posY = y; posZ = z; }
	
	public void addMotion(float x, float y, float z)
	{ motX += x;  posY += y; posZ += z; }
	
	public void moveTowards(float x, float z, float s)
	{
		if(x == 0F && z == 0F) return;
		float f = MathHelper.sqrt2(x, z);
		motX += x / f * s;
		motZ += z / f * s;
	}
	
	public void moveEntity()
	{
		flags[ON_GROUND] = false;
		
		addCollisionBoxes(null);
		
		AABB b = worldObj.getAABBInBox(collisionBox, 0F, motY, 0F);
		if(b != null && !flags[NO_CLIP])
		{
			if(motY <= 0F)
			{
				posY = b.posY2;
				flags[ON_GROUND] = true;
			}
			
			motY = 0F;
		}
		else motY -= worldObj.gravity;
		
		if(!flags[NO_CLIP])
		{
			AABB bx = worldObj.getAABBInBox(collisionBox, motX * 2F, 0.01F, 0F);
			if(bx != null)
			{
				if(motY != 0F || bx.posY2 - collisionBox.posY1 > 0.5F)
				motX = 0F; else
				{
					posY = bx.posY2;
				}
			}
			
			AABB bz = worldObj.getAABBInBox(collisionBox, 0F, 0.01F, motZ * 2F);
			if(bz != null)
			{
				if(motY != 0F || bz.posY2 - collisionBox.posY1 > 0.5F)
				motZ = 0F; else
				{
					posY = bz.posY2;
				}
			}
		}
		
		posX += motX;
		posY += motY;
		posZ += motZ;
	}
	
	public void addCollisionBoxes(FastList<AABB> b)
	{
		if(collisionBox != null) collisionBox.set(posX, posY, posZ, sizeH, sizeV, sizeH);
		else collisionBox = new AABB.BottomCentred(posX, posY, posZ, sizeH, sizeV, sizeH);
		collisionBox.owner = this;
		collisionBox.solid = false;
		if(b != null) b.add(collisionBox);
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
	{ return true; }
	
	public void onRightClick(EntityPlayer ep) { }

	public boolean isGreenDot(EntityPlayerSP ep)
	{ return true; }
}