package latmod.labmod.net;
import latmod.labmod.net.packets.PacketCustom;
import latmod.labmod.world.*;

public interface ICustomPacketHandler
{
	public void onCustomPacket(World w, PacketCustom p);
}