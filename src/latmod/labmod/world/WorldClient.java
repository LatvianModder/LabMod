package latmod.labmod.world;
import latmod.core.model.*;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.ClientUtils;
import latmod.labmod.MainClient;
import latmod.labmod.client.particles.*;
import latmod.labmod.entity.*;
import latmod.labmod.net.*;
import latmod.labmod.net.packets.*;

public class WorldClient extends World
{
	public NetClient client;
	public EntityPlayerSP playerSP;
	
	@SuppressWarnings("unchecked")
	public FastList<Particle>[] particles = new FastList[PartLayer.VALUES.length];
	
	public int renderedEntities = 0;
	public int renderedParticles = 0;
	public int skyColor = 0xFF0F0F0F;
	
	public OBJModel skybox;
	public Texture skyboxTex;
	
	public WorldClient()
	{
		super(Side.CLIENT);
		for(int i = 0; i < particles.length; i++)
		particles[i] = new FastList<Particle>();
	}
	
	public INet getNet()
	{ return client; }
	
	public void start(String ip, int port)
	{
		LatCore.println("Connecting to '" + ip + ":" + port + "' ...", "World");
		client = new NetClient(this, ip, port);
	}
	
	public void onUpdate(Timer t)
	{
		resetCollisionBoxes();
		
		for(int j = 0; j < particles.length; j++)
		{
			for(int i = 0; i < particles[j].size(); i++)
			particles[j].get(i).onUpdate();
			
			for(int i = 0; i < particles[j].size(); i++)
			{
				if(particles[j].get(i).flags[Entity.IS_DEAD])
				particles[j].remove(i);
			}
		}
		
		if(playerSP != null)
		{
			playerSP.onUpdate(t);
			
			if(playerSP.isDirty)
			{
				sendPacket(new PacketPlayerUpdate(playerSP));
				playerSP.isDirty = false;
			}
		}
	}
	
	public void onConnectionCreated(NetClient c, float secs)
	{
		if(!c.isFailed)
		{
			LatCore.println("Connected to server after " + secs + " seconds with clientID " + c.clientID, "World", side.toString());
			playerSP = new EntityPlayerSP(this);
			//
		}
		else
		{
			LatCore.println("Failed Connecting after " + secs + " seconds!", "World", side.toString());
			MainClient.inst.stop(false);
		}
	}
	
	public void onClientData(NetClient c) throws Exception
	{ PacketManager.receivePacket(this, c.data); }
	
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
		{
			playerSP.onRender();
			
			for(EntityPlayerMP p : playerMap)
			if(p.playerID != playerSP.playerID) p.onRender();
		}
	}
	
	public void onStopped()
	{
		client.stop();
		super.onStopped();
	}

	public void spawnEntity(Entity e)
	{
		sendPacket(new PacketEntitySpawn(e));
	}

	public void removeEntity(int wid)
	{
		entities.remove(wid);
	}
	
	public void sendPacket(Packet p)
	{ client.packetSender.sendPacket(p); }
	
	public void processPacket(Packet p)
	{ client.packetProcessor.addPacketToProcess(p); }
}