package latmod.labmod.client.entity;
import latmod.core.rendering.*;
import latmod.labmod.entity.*;

public class RenderRock extends EntityRenderer
{
	public void loadTextures()
	{
	}
	
	public void renderEntity(Entity e)
	{
		Renderer.recolor();
		Renderer.push();
		Renderer.translate(e, 1F);
		Renderer.rotate(e.rotYaw, e.rotPitch);
		Renderer.enableTexture();
		Renderer.pop();
	}
}