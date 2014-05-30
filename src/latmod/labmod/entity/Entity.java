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
	public static final int SNEAKING = 5;
	
	public World worldObj;
	public long worldID;
	public boolean[] flags = new boolean[8];
	public double sizeH, sizeV;
	public AABB collisionBox = null;
	@Expose public double rotYaw = 0D, rotPitch = 0D;
	@Expose public double motX = 0D, motY = 0D, motZ = 0D;
	@Expose public String displayName = getClass().getSimpleName();
	
	public Entity(World w)
	{
		worldObj = w;
		resetCollisionBox();
	}
	
	public int hashCode()
	{ return (int)worldID; }
	
	public String toString()
	{ return "[ E" + EntityID.getEID(this).entityID + ", W" + worldID + " ]: " + displayName; }
	
	public boolean equals(Object o)
	{ return (o == this) || ((o instanceof Entity) ? ((o.hashCode() == hashCode()) ? true : false) : false); }
	
	public void setSize(double h, double v)
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
	
	public void setMotion(double x, double y, double z)
	{ motX = x;  posY = y; posZ = z; }
	
	public void addMotion(double x, double y, double z)
	{ motX += x;  posY += y; posZ += z; }
	
	public void moveTowards(double x, double z, double s)
	{
		if((x == 0D && z == 0D) || s == 0D) return;
		double f = MathHelper.sqrt2sq(x, z);
		motX += x / f * s;
		motZ += z / f * s;
	}
	
	public void moveEntity()
	{
		flags[ON_GROUND] = false;
		
		resetCollisionBox();
		
		if(!flags[NO_CLIP])
		{
			// Y
			
			AABB b = worldObj.getAABBInBox(collisionBox, 0F, motY, 0F);
			if(b != null)
			{
				if(motY <= 0F)
				{
					posY = b.max.posY;
					flags[ON_GROUND] = true;
				}
				
				motY = 0F;
			}
			else motY -= worldObj.gravity;
			
			// XZ
			
			AABB bx = worldObj.getAABBInBox(collisionBox, motX * 2F, 0.01F, 0F);
			if(bx != null)
			{
				//if(motY != 0F || bx.posY2 - collisionBox.posY1 > 0.5F)
				motX = 0F;
				//else posY = bx.posY2;
			}
			
			AABB bz = worldObj.getAABBInBox(collisionBox, 0F, 0.01F, motZ * 2F);
			if(bz != null)
			{
				//if(motY != 0F || bz.posY2 - collisionBox.posY1 > 0.5F)
				motZ = 0F;
				//else posY = bz.posY2;
			}
		}
		
		posX += motX;
		posY += motY;
		posZ += motZ;
		
		resetCollisionBox();
	}
	
	public final void resetCollisionBox()
	{
		if(collisionBox != null) collisionBox.set(posX, posY, posZ, sizeH, sizeV, sizeH);
		else collisionBox = new AABB.BottomCentred(posX, posY, posZ, sizeH, sizeV, sizeH);
		collisionBox.owner = this;
		collisionBox.solid = false;
	}
	
	public void readFromNBT(NBTMap map)
	{
		flags = Converter.toBool(map.getByte("Flags"));
		
		Iterator<Double> doubles = map.getList("PosRot").getIterator();
		posX = doubles.next();
		posY = doubles.next();
		posZ = doubles.next();
		rotYaw = doubles.next();
		rotPitch = doubles.next();
		motX = doubles.next();
		motY = doubles.next();
		motZ = doubles.next();
	}
	
	public void writeToNBT(NBTMap map)
	{
		map.setByte("Flags", Converter.toInt(flags));
		
		NBTList doubles = new NBTList("PosRot");
		doubles.addObj(posX);
		doubles.addObj(posY);
		doubles.addObj(posZ);
		doubles.addObj(rotYaw);
		doubles.addObj(rotPitch);
		doubles.addObj(motX);
		doubles.addObj(motY);
		doubles.addObj(motZ);
		map.setList(doubles);
	}
	
	/** Return true if entity got killed */
	public boolean onAttacked(Entity entity, float f)
	{ return true; }
	
	public void onRightClick(EntityPlayer ep) { }

	public boolean isGreenDot(EntityPlayerSP ep)
	{ return true; }
}