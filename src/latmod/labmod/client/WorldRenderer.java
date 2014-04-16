package latmod.labmod.client;
import latmod.core.model.*;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.client.particles.*;
import latmod.labmod.entity.*;

public class WorldRenderer
{
	public final World worldObj;
	
	@SuppressWarnings("unchecked")
	public FastList<Particle>[] particles = new FastList[PartLayer.VALUES.length];
	
	public int renderedEntities = 0;
	public int renderedParticles = 0;
	public int skyColor = 0xFF0F0F0F;
	
	public OBJModel skybox;
	public Texture skyboxTex;
	
	public WorldRenderer(World w)
	{
		worldObj = w;
		
		for(int i = 0; i < particles.length; i++)
		particles[i] = new FastList<Particle>();
	}
	
	public void preRender()
	{
		Renderer3D.getRenderDistance = 1000F;
		
		if(skybox == null)
		{
			skybox = ClientUtils.inst.loadModel("/models/skybox.obj");
			Renderer.loadTexturesSmooth = true;
			skyboxTex = Renderer.getTexture("world/skybox.jpg");
			Renderer.loadTexturesSmooth = false;
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
		
		if(worldObj.player != null)
		{
			//Renderer.colorize(skyColor);
			
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
		
		{
			Renderer.recolor();
			Renderer.setTexture("world/grass.png");
			Renderer.plane(0F, -1F, 0F, worldObj.width, worldObj.depth, 0F, 0F, worldObj.width, worldObj.depth);
		}
		
		Renderer3D.enableCulling();
		
		renderedEntities = 0;
		for(Entity e : worldObj.entities)
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
		
		worldObj.player.onRender();
	}
}