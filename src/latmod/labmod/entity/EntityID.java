package latmod.labmod.entity;
import java.lang.reflect.*;

import latmod.core.util.*;
import latmod.labmod.*;

public class EntityID implements Comparable<EntityID>
{
	private static final World fakeWorld = new World();
	
	public static final FastList<EntityID> entityList = new FastList<EntityID>();
	
	public static void loadEntities()
	{
		Main.gameLogger.info("Loading Entities...");
		
		register(1, "box", EntityBox.class);
		
		Main.gameLogger.info("Loaded " + entityList.size() + " entities");
	}
	
	public static void register(int id, String s, Class<? extends Entity> c)
	{
		String s1 = register0(id, s, c);
		if(s1 != null) Main.gameLogger.warning("Failed to register: " + s1);
	}
	
	private static String register0(int id, String s, Class<? extends Entity> c)
	{
		if(c == null && s != null) return "Class for '" + s + "' can't be null!";
		
		String sc = LMCommon.classpath(c);
		
		if(id < 1 || id > 32767) return "Entity ID " + id + " for '" + sc + "' is < 1 or > 32767!";
		if(entityList.contains(id))
		return "Entity ID " + id + " for '" + LMCommon.classpath(c) + "' already registred by '" + entityList.get(id).classPath + "'!";
		
		if(s != null && entityList.contains(s))
		return "Entity Name '" + s + "' for '" + LMCommon.classpath(c) + "' already registred by '" + entityList.get(id).classPath + "'!";
		
		try
		{
			EntityID eid = new EntityID(id, s, c);
			eid.createNewEntity(fakeWorld);
			entityList.add(eid);
			return null;
		}
		catch(Exception e)
		{
			return "Constructor '" + c.getSimpleName() + "(World w) { super(w); } ' for '" + sc + "' not found!";
		}
	}
	
	public static EntityID getEID(Object o)
	{ return entityList.getObj(o); }
	
	// ------------------ //
	
	public final int entityID;
	public final String entityName;
	public final Class<? extends Entity> entityClass;
	public final String classPath;
	
	public EntityID(int i, String s, Class<? extends Entity> c)
	{
		entityID = i;
		entityName = s;
		entityClass = c;
		classPath = LMCommon.classpath(c);
	}
	
	public Entity createNewEntity(World w) throws Exception
	{ Constructor<?> cons = entityClass.getConstructor(World.class);
	return (Entity)cons.newInstance(w); }
	
	public boolean equals(Object o)
	{
		if(o == null) return false;
		if(o == this) return true;
		
		if(o instanceof Integer)
		return o.equals(entityID);
		
		if(o instanceof Entity)
		return equals(o.getClass());
		
		if(o instanceof String)
		return o.equals(entityName);
		
		if(o instanceof Class)
		return o.equals(entityClass);
		
		return false;
	}
	
	public int hashCode()
	{ return entityID; }

	public int compareTo(EntityID o)
	{ return Integer.compare(entityID, o.entityID); }
	
	public String toString()
	{ return LMCommon.strip(entityID, entityName, classPath); }
}