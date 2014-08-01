package latmod.labmod.client.entity;
import latmod.core.rendering.*;
import latmod.core.res.Resource;
import latmod.labmod.entity.*;

public class RenderBox extends EntityRenderer
{
	public static final Resource boxTex = Resource.getTexture("entities/box.png");
	public static final Resource maskTex = Resource.getTexture("entities/box_mask.png");
	
	public void loadTextures()
	{
		texManager.getTexture(boxTex);
		texManager.getTexture(maskTex);
	}
	
	public void renderEntity(Entity e)
	{
		Color.reset();
		Renderer.push();
		Renderer.translate(e, 1F);
		Renderer.rotate(e.rotYaw, e.rotPitch);
		Renderer.scale(e.sizeH, e.sizeV, e.sizeH);
		Renderer.enableTexture();
		Renderer3D.enable3DAlpha();
		texManager.setTexture(boxTex);
		Renderer3D.box(0F, 0F, 0F, 1F, 1F, 1F);
		Renderer.pop();
	}
}