package latmod.labmod.client.entity;
import latmod.core.util.*;
import latmod.labmod.entity.*;

public abstract class EntityRenderer
{
	public abstract void renderEntity(Entity e);
	public abstract void loadTextures();
	
	// ---------- //
	
	public static final FastMap<Class<? extends Entity>, EntityRenderer> renderMap = new FastMap<Class<? extends Entity>, EntityRenderer>();
	
	public static void register(Class<? extends Entity> c, EntityRenderer e)
	{ renderMap.put(c, e); }
	
	public static void loadEntityRenderers()
	{
		register(EntityBox.class, new RenderBox());
	}
	
	@SuppressWarnings("all")
	public static EntityRenderer getRenderer(Class<? extends Entity> c)
	{
		if(c == null || Entity.class.equals(c)) return null;
		
		EntityRenderer r = renderMap.get(c);
		return (r != null) ? r : getRenderer((Class<? extends Entity>)c.getSuperclass());
	}
}