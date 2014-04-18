package latmod.labmod.entity;
import latmod.core.util.*;
import latmod.labmod.*;

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
		
		posY = 0.01F;
		motY = 0F;
	}
}