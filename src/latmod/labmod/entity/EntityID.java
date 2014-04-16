package latmod.labmod.entity;
import java.lang.reflect.*;
import latmod.core.util.*;
import latmod.labmod.*;

public class EntityID
{
	private static final World fakeWorld = new World();
	
	public static FastMap<Integer, Class<? extends Entity>> entityMap;
	public static FastMap<Integer, String> entityNameMap;
	public static FastMap<Integer, Entity> entityStaticMap;
	
	private static final String LOGGER = "EntityID";
	
	public static void loadEntities()
	{
		LatCore.println("Loading Entities...", LOGGER);
		entityMap = new FastMap<Integer, Class<? extends Entity>>();
		entityNameMap = new FastMap<Integer, String>();
		entityStaticMap = new FastMap<Integer, Entity>();
		
		register(1, "rock", EntityRock.class);
		
		LatCore.println("Loaded " + entityMap.size() + " entit" + (LatCore.numEnding(entityMap.size()).length() == 0 ? "y" : "ies"), LOGGER);
	}
	
	public static void register(int id, String s, Class<? extends Entity> c)
	{
		String s1 = register0(id, s, c);
		if(s1 != null) LatCore.printlnErr("Failed to register: " + s1, LOGGER);
	}
	
	private static String register0(int id, String s, Class<? extends Entity> c)
	{
		if(c == null && s != null) return "Class for '" + s + "' can't be null!";
		
		String sc = LatCore.classpath(c);
		
		if(id < 1 || id > 32767) return "Entity ID " + id + " for '" + sc + "' is < 0 or > 32767!";
		if(entityMap.keys.contains(id))
		return "Entity ID " + id + " for '" + LatCore.classpath(c) + "' already registred by '" + LatCore.classpath(entityMap.get(id)) + "'!";
		
		if(s != null && entityNameMap.values.contains(s))
		return "Entity Name '" + s + "' for '" + LatCore.classpath(c) + "' already registred by '" + LatCore.classpath(entityMap.get(id)) + "'!";
		
		try
		{
			Constructor<?> cons = c.getConstructor(World.class);
			Entity e = (Entity)cons.newInstance(fakeWorld);
			e.toString();
			entityStaticMap.put(id, e);
		}
		catch(Exception e)
		{
			return "Constructor '" + c.getSimpleName() + "(World w) { super(w); } ' for '" + sc + "' not found!";
		}
		
		entityMap.put(id, c);
		if(s != null) entityNameMap.put(id, s);
		
		return null;
	}

	public static Entity createEntity(World w, int eid) throws Exception
	{
		Class<? extends Entity> c = entityMap.get(eid);
		Constructor<?> cons = c.getConstructor(World.class);
		return (Entity)cons.newInstance(w);
	}
	
	public static int getEID(Class<? extends Entity> c)
	{
		if(c == null) return 0;
		Integer i = entityMap.getKey(c);
		return (i == null) ? 0 : i.intValue();
	}

	public static int getEID(Entity entity)
	{ return (entity == null) ? 0 : getEID(entity.getClass()); }
	
	public static int getEID(String s)
	{
		if(s == null) return 0;
		Integer i = entityNameMap.getKey(s);
		return (i == null) ? 0 : i.intValue();
	}
}