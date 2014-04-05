package latmod.labmod.client.particles;

public enum PartLayer
{
	NO_TEX,
	TEX,
	CUSTOM;
	
	public static final PartLayer[] VALUES = values();
	
	public int INDEX = ordinal();
}