package latmod.labmod.client;
import latmod.core.model.*;
import latmod.core.rendering.*;
import latmod.core.res.Resource;
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
	public Color skyColor = Color.get(0xFF0F0F0F);
	
	public static OBJModel skybox;
	public static final Resource skyboxTex = Resource.getTexture("world/skybox.png");
	public static final Resource grassTex = Resource.getTexture("world/grass.png");
	public static final Resource particlesTex = Resource.getTexture("world/particles.png");
	
	public static void loadTextures(TextureManager t)
	{
		skybox = ClientUtils.inst.loadModel(Resource.getModel("world/skybox.obj"));
		
		Main.inst.textureManager.loadTexturesBlured = true;
		t.getTexture(skyboxTex);
		Main.inst.textureManager.loadTexturesBlured = false;
		
		t.getTexture(grassTex);
		t.getTexture(particlesTex);
	}
	
	public WorldRenderer(World w)
	{
		worldObj = w;
		
		for(int i = 0; i < particles.length; i++)
		particles[i] = new FastList<Particle>();
	}
	
	public void preRender()
	{
		Renderer3D.getRenderDistance = 300F;
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
			Renderer3D.disableFog();
			
			//Renderer.colorize(skyColor);
			
			Renderer3D.disableDepth();
			Renderer.push();
			Renderer.translate(Renderer3D.camera, 1D);
			Renderer.scale(30D);
			
			Renderer3D.disableCulling();
			Main.inst.textureManager.setTexture(skyboxTex);
			skybox.renderAll();
			
			Renderer.pop();
			Renderer3D.enableDepth();
		}
		
		//Renderer3D.enableFog();
		//Renderer3D.drawFog(10F, 30F, Color.WHITE);
		
		{
			Color.reset();
			Main.inst.textureManager.setTexture(grassTex);
			Renderer3D.plane(0D, 0D, 0D, worldObj.width, worldObj.depth, 0D, 0D, worldObj.width, worldObj.depth);
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
		
		Main.inst.textureManager.setTexture(particlesTex);
		
		for(Particle p : particles[PartLayer.TEX.INDEX]) if(p != null && p.isVisible())
		{ p.onRender(); renderedParticles++; }
		
		for(Particle p : particles[PartLayer.CUSTOM.INDEX]) if(p != null && p.isVisible())
		{ p.onRender(); renderedParticles++; }
		
		worldObj.player.onRender();
		
		Renderer3D.disableFog();
	}
}