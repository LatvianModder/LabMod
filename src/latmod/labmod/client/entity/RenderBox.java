package latmod.labmod.client.entity;
import latmod.core.rendering.*;
import latmod.labmod.entity.*;

public class RenderBox extends EntityRenderer
{
	public Texture box, box_mask;
	
	public void loadTextures()
	{
		box = Renderer.getTexture("entities/box.png");
		box_mask = Renderer.getTexture("entities/box_mask.png");
	}
	
	public void renderEntity(Entity e)
	{
		Color.clear();
		Renderer.push();
		Renderer.translate(e, 1F);
		Renderer.rotate(e.rotYaw, e.rotPitch);
		Renderer.scale(e.sizeH, e.sizeV, e.sizeH);
		Renderer.enableTexture();
		Renderer3D.enable3DAlpha();
		Renderer.setTexture(box);
		Renderer3D.renderBox(0F, 0F, 0F, 1F, 1F, 1F);
		Renderer.pop();
	}
}