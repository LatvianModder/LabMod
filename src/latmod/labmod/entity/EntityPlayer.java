package latmod.labmod.entity;
import latmod.core.nbt.*;
import latmod.labmod.*;

public abstract class EntityPlayer extends Entity
{
	public Cursor cursor;
	public int maxHealth = 100;
	public int health = maxHealth;
	public double hurtTimer = 0F;
	public double distanceMovedH = 0F;
	public double distanceMovedT = 0F;
	public boolean isRunning = false;
	public double eyeHeight;
	
	public EntityPlayer(World w)
	{
		super(w);
		sizeH = 0.75D;
		sizeV = 1.75D;
		eyeHeight = 1.65D;
		displayName = "Player";
		cursor = new Cursor(this);
	}
	
	public void onRender()
	{
	}
	
	public boolean isVisible()
	{ return true; }
	
	public final void readFromNBT(NBTMap map)
	{
		super.readFromNBT(map);
		
		health = map.getShort("HP");
		maxHealth = map.getShort("MaxHP");
	}
	
	public final void writeToNBT(NBTMap map)
	{
		super.writeToNBT(map);
		
		map.setShort("HP", health);
		map.setShort("MaxHP", maxHealth);
	}
}