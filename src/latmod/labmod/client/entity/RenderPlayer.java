package latmod.labmod.client.entity;
import latmod.labmod.entity.*;

public class RenderPlayer extends EntityRenderer
{
	public RenderPlayer()
	{
	}
	
	public void loadTextures()
	{
	}
	
	public void renderEntity(Entity e)
	{ renderPlayer((EntityPlayer)e); }
	
	public void renderPlayer(EntityPlayer ep)
	{
		/*Renderer.enableTexture();
		//Renderer.disableTexture();
		Renderer.recolor();
		
		Renderer3D.disable3DAlpha();
		Renderer3D.enableCulling();
		Renderer.recolor();
		
		Renderer.push();
		Renderer.translate(ep.posX, ep.posY, ep.posZ);
		Renderer.rotateY(ep.rotYaw);
		Renderer.rotateX(-ep.rotPitch);
		
		Renderer.setTexture(texBody);
		spaceship.render("Body");
		
		Renderer.push();
		Renderer.rotateX(MathHelper.renderSin(0.04F, -10F, 10F));
		
		Renderer.setTexture(texWings);
		spaceship.render("Wings");
		Renderer.pop();
		
		Iterator<Upgrade> uItr = ep.upgrades.keys.iterator();
		Iterator<Integer> iItr = ep.upgrades.values.iterator();
		
		while(uItr.hasNext() && iItr.hasNext())
		{
			Upgrade u = uItr.next();
			Integer i = iItr.next();
			u.onRender(ep, new UpgradeContainer(u, ep, i));
		}
		
		Renderer.pop();
		
		Renderer3D.enable3DAlpha();
		*/
	}
}