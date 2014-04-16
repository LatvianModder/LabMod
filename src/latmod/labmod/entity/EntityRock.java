package latmod.labmod.entity;
import latmod.core.util.*;
import latmod.labmod.*;

public class EntityRock extends Entity
{
	public EntityRock(World w)
	{
		super(w);
		setSize(1.3F, 1.3F);
		displayName = "Rock";
		
		motX = (w.rand.nextFloat() - 1F) / 2F * 0.01F;
		motY = (w.rand.nextFloat() - 1F) / 2F * 0.01F;
		motZ = (w.rand.nextFloat() - 1F) / 2F * 0.01F;
	}
	
	public void onUpdate(Timer t)
	{
		rotYaw += worldObj.rand.nextFloat() * 3F;
		rotPitch += worldObj.rand.nextFloat() * 3F;
		
		moveEntity();
	}
}