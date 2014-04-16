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
	public float width, depth;
	public int nextEWID = 0;
	public long tick = 0L;
	public float gravity = 0.007F;
	
	public FastMap<Integer, Entity> entities = new FastMap<Integer, Entity>();
	public FastMap<Integer, Entity> entitiesToBeAdded = new FastMap<Integer, Entity>();
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
			new AABB.Corner(0F, -1F, 0F, width, 1F, depth), // Floor
			//new AABB.Corner(0F, height, 0F, width, 1F, depth), // Ceiling
			new AABB.Corner(0F, 0F, -1F, width, height, 1F), // Wall Z-
			new AABB.Corner(0F, 0F, depth, width, height, 1F), // Wall Z+
			new AABB.Corner(-1F, 0F, 0F, 1F, height, depth), // Wall X-
			new AABB.Corner(width, 0F, 0F, 1F, height, depth), // Wall X+
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
		
		entities.putAll(entitiesToBeAdded);
		entitiesToBeAdded.clear();
		
		for(int i = 0; i < entities.size(); i++)
		entities.get(i).onUpdate(t);
		
		for(int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			
			if(e.flags[Entity.IS_DEAD])
			{
				e.onDeath();
				entities.remove(i);
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
		
		for(int i = 0; i < entities.size(); i++)
		entities.get(i).addCollisionBoxes(AABBList);
		
		player.addCollisionBoxes(null);
	}
	
	public final AABB getAABBAtPoint(float x, float y, float z)
	{ return AABB.getAABBAtPoint(AABBList, x, y, z); }
	
	public final AABB getAABBInBox(AABB pos, float x, float y, float z)
	{ return AABB.getAABBInBox(AABBList, pos, x, y, z); }
	
	public final FastList<AABB> getAllAABBsInBox(AABB pos, float x, float y, float z)
	{ return AABB.getAllAABBsInBox(AABBList, pos, x, y, z); }
	
	public final FastList<AABB> getAllAABBsAtPoint(float x, float y, float z)
	{ return AABB.getAllAABBsAtPoint(AABBList, x, y, z); }
	
	public final Entity getFromWID(int wid)
	{
		for(int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			if(e.worldID == wid) return e;
		}
		
		return null;
	}
}