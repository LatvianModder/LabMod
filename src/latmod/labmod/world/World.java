package latmod.labmod.world;
import java.util.*;

import latmod.core.model.*;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.core.util.Timer;
import latmod.labmod.ClientUtils;
import latmod.labmod.client.particles.*;
import latmod.labmod.entity.*;

public class World
{
	public String serverName = "Loading...";
	public FastList<AABB> AABBList = new FastList<AABB>();
	
	public FastMap<Integer, Entity> entities = new FastMap<Integer, Entity>();
	public FastMap<Integer, Entity> entitiesToBeAdded = new FastMap<Integer, Entity>();
	
	public EntityPlayerSP playerSP;
	
	@SuppressWarnings("unchecked")
	public FastList<Particle>[] particles = new FastList[PartLayer.VALUES.length];
	
	public int renderedEntities = 0;
	public int renderedParticles = 0;
	public int skyColor = 0xFF0F0F0F;
	
	public int nextEWID = 0;
	
	public OBJModel skybox;
	public Texture skyboxTex;
	
	public Random rand = new Random();
	public long tick = 0L;
	
	public long packetsReceived = 0L;
	public long packetsSent = 0L;
	
	public World()
	{
		for(int i = 0; i < particles.length; i++)
		particles[i] = new FastList<Particle>();
		
		playerSP = new EntityPlayerSP(this);
	}
	
	public void spawnEntity(Entity e)
	{
		e.worldID = ++nextEWID;
		e.isDirty = true;
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
			entities.remove(i);
			else if(e.isDirty)
			{
				e.isDirty = false;
				//
			}
		}
		
		tick++;
	}
	
	public void onStopped()
	{
	}
	
	public void preRender()
	{
		Renderer3D.getRenderDistance = 1000F;
		
		if(skybox == null)
		{
			skybox = ClientUtils.inst.loadModel("/models/skybox.obj");
			skyboxTex = Renderer.getTexture("world/skybox.jpg");
		}
	}
	
	public void postRender()
	{
	}
	
	public void onRender()
	{
		Renderer.background(skyColor);
		
		Renderer3D.disable3DAlpha();
		Renderer.enableTexture();
		
		if(playerSP != null)
		{
			Renderer3D.disableDepth();
			Renderer.push();
			Renderer.translate(Renderer3D.camPos, 1F);
			Renderer.scale(30F);
			
			Renderer3D.disableCulling();
			Renderer.setTexture(skyboxTex);
			skybox.renderAll();
			
			Renderer.pop();
			Renderer3D.enableDepth();
		}
		
		Renderer3D.enableCulling();
		
		renderedEntities = 0;
		for(Entity e : entities)
		if(e.isVisible())
		{
			e.onRender();
			renderedEntities++;
		}
		
		Renderer3D.disableCulling();
		
		renderedParticles = 0;
		
		Renderer.disableTexture();
		
		for(Particle p : particles[PartLayer.NO_TEX.INDEX]) if(p != null && p.isVisible())
		{ p.onRender(); renderedParticles++; }
		
		Renderer.enableTexture();
		
		Renderer.setTexture("gui/particles.png");
		
		for(Particle p : particles[PartLayer.TEX.INDEX]) if(p != null && p.isVisible())
		{ p.onRender(); renderedParticles++; }
		
		for(Particle p : particles[PartLayer.CUSTOM.INDEX]) if(p != null && p.isVisible())
		{ p.onRender(); renderedParticles++; }
		
		if(playerSP != null)
		playerSP.onRender();
	}
	
	public final void resetCollisionBoxes()
	{
		AABBList.clear();
		for(Entity e : entities)
		e.addCollisionBoxes(AABBList);
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