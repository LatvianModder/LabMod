package latmod.labmod.net;
import java.net.*;

import latmod.core.util.*;

public class NetServerClient extends NetClient
{
	public NetServer parent;
	
	public NetServerClient(NetServer se, Socket s, int id)
	{
		super(se.worldObj, s, id);
		parent = se;
		
		thread = new Thread(this, "NetClientDummy_" + getIP());
		
		try
		{
			data = new DataIOStream(socket.getInputStream(), socket.getOutputStream());
			data.writeShort(id);
			data.flush();
		}
		catch(Exception e)
		{ e.printStackTrace(); thread = null; }
		
		if(thread != null) thread.start();
	}
	
	public void run()
	{
		super.run();
	}
}