package latmod.labmod;
import java.util.*;

import latmod.core.util.*;
import latmod.core.util.Timer;
import latmod.labmod.client.WorldRenderer;
import latmod.labmod.entity.*;

public class World
{
	public String serverName = "Loading...";
	public FastList<AABB> AABBList = new FastList<AABB>();
	public double width, depth;
	public long nextEWID = 0L;
	public long tick = 0L;
	public double gravity = 0.007F;
	
	public FastMap<Long, Entity> entities = new FastMap<Long, Entity>();
	public FastMap<Long, Entity> entitiesToBeAdded = new FastMap<Long, Entity>();
	public EntityPlayerSP player;
	public WorldRenderer worldRenderer;
	public AABB collBoxes[] = new AABB[0];
	public Random rand = new Random();
	public Vertex spawnPoint = new Vertex(0F, 0F, 0F);
	
	public World()
	{
		width = 128F;
		depth = 128F;
		spawnPoint.setPos(width / 2F + 0.5F, 0.5F, depth / 2F + 0.5F);
	}
	
	public void createWorld()
	{
		player = new EntityPlayerSP(this);
		worldRenderer = new WorldRenderer(this);
		
		float height = Float.POSITIVE_INFINITY;
		
		collBoxes = new AABB[]
		{
			new AABB.Corner(0D, -1D, 0D, width, 1D, depth), // Floor
			//new AABB.Corner(0F, height, 0F, width, 1F, depth), // Ceiling
			new AABB.Corner(0D, 0D, -1D, width, height, 1D), // Wall Z-
			new AABB.Corner(0D, 0D, depth, width, height, 1D), // Wall Z+
			new AABB.Corner(-1D, 0D, 0D, 1D, height, depth), // Wall X-
			new AABB.Corner(width, 0F, 0D, 1D, height, depth), // Wall X+
		};
	}
	
	public void spawnEntity(Entity e)
	{
		e.worldID = ++nextEWID;
		entitiesToBeAdded.put(e.worldID, e);
	}
	
	public void onUpdate(Timer t)
	{
		resetCollisionBoxes();
		
		if(!entitiesToBeAdded.isEmpty())
		{
			entities.putAll(entitiesToBeAdded);
			entitiesToBeAdded.clear();
		}
		
		for(Entity e : entities) e.onUpdate(t);
		
		for(Entity e : entities)
		{
			if(e.flags[Entity.IS_DEAD])
			{
				e.onDeath();
				entities.remove(e.worldID);
			}
		}
		
		player.onUpdate(t);
		
		tick++;
	}
	
	public void onStopped()
	{
	}
	
	public final void resetCollisionBoxes()
	{
		AABBList.clear();
		AABBList.addAll(collBoxes);
		for(Entity e : entities)
		AABBList.add(e.collisionBox);
	}
	
	public final AABB getAABBAtPoint(double x, double y, double z)
	{ return AABB.getAABBAtPoint(AABBList, x, y, z); }
	
	public final AABB getAABBInBox(AABB pos, double x, double y, double z)
	{ return AABB.getAABBInBox(AABBList, pos, x, y, z); }
	
	public final FastList<AABB> getAllAABBsInBox(AABB pos, double x, double y, double z)
	{ return AABB.getAllAABBsInBox(AABBList, pos, x, y, z); }
	
	public final FastList<AABB> getAllAABBsAtPoint(double x, double y, double z)
	{ return AABB.getAllAABBsAtPoint(AABBList, x, y, z); }
	
	public final Entity getFromWID(long wid)
	{ return entities.get(wid); }
	
	public AABB rayTrace(AABB ignore, Vertex pos, Vertex look)
	{
		return null;
	}
}