package latmod.labmod.entity;
import latmod.core.util.*;
import latmod.labmod.*;

public class EntityBox extends Entity
{
	public EntityBox(World w)
	{
		super(w);
		setSize(1F, 1F);
		displayName = "Box";
	}
	
	public void onUpdate(Timer t)
	{
		moveEntity();
		if(posY > 150) setDead();
	}
}