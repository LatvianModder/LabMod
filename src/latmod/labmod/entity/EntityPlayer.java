package latmod.labmod.entity;
import latmod.core.nbt.*;
import latmod.labmod.*;

public abstract class EntityPlayer extends Entity
{
	public String username = null;
	public Cursor cursor;
	public int health = 100;
	public int maxHealth = 100;
	public float hurtTimer = 0F;
	public float distanceMovedH = 0F;
	public float distanceMovedT = 0F;
	public boolean isRunning = false;
	public float eyeHeight;
	
	public EntityPlayer(World w)
	{
		super(w);
		sizeH = 0.75F;
		sizeV = 1.75F;
		eyeHeight = 1.65F;
		cursor = new Cursor(this);
		username = "[Unknown]";
	}
	
	public void onRender()
	{
		displayName = username;
		super.onRender();
	}
	
	public boolean isVisible()
	{ return true; }
	
	public final void readFromNBT(NBTMap map)
	{
		super.readFromNBT(map);
		
		health = map.getShort("HP");
		maxHealth = map.getShort("MaxHP");
		//username = map.getString("Name");
	}
	
	public final void writeToNBT(NBTMap map)
	{
		super.writeToNBT(map);
		
		map.setShort("HP", health);
		map.setShort("MaxHP", maxHealth);
		//map.setString("Name", username);
	}
}