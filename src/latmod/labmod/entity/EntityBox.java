package latmod.labmod.entity;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.client.gui.GuiIngame;

public class EntityBox extends Entity
{
	public EntityBox(World w)
	{
		super(w);
		setSize(0.9F, 0.9F);
		displayName = "Box";
	}
	
	public void onUpdate(Timer t)
	{
		moveEntity();
		
		//posY = 0.01F;
		//motY = 0F;
		
		if(posY > 150) setDead();
		
		GuiIngame.printChat("Mot: " + LatCore.strip(motX, motY, motZ));
	}
}