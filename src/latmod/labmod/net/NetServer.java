package latmod.labmod.net;
import java.net.*;

import latmod.core.util.*;
import latmod.labmod.world.*;

public class NetServer implements Runnable, INet
{
	public FastMap<Integer, NetServerClient> clients = new FastMap<Integer, NetServerClient>();
	public WorldServer worldObj;
	public Thread thread = null;
	public ServerSocket serverSocket;
	public DataIOStream data = null;
	public boolean isFailed = false;
	private int nextClientID = 0;
	
	public NetServer(WorldServer w, int p, int m, String ip)
	{
		worldObj = w;
		
		try
		{
			serverSocket = new ServerSocket(p, m, ip.equals("localhost") ? null : InetAddress.getByName(ip));
			data = new DataIOStream(new ServerOutputStream(this));
		}
		catch(Exception e)
		{ e.printStackTrace(); isFailed = true; }
		
		thread = new Thread(this, "NetServer_" + ip + ":" + p);
		thread.start();
	}
	
	public void stop()
	{ thread = null;  }
	
	public void stopNow()
	{
		thread = null;
		
		if(clients != null)
		for(NetServerClient c : clients) c.stop();
		clients = null;
		
		try { if(serverSocket != null) serverSocket.close(); }
		catch(Exception e)
		{ e.printStackTrace(); }
		serverSocket = null;
	}
	
	public DataIOStream getData()
	{ return data; }
	
	public void run()
	{
		while(thread != null)
		{
			try
			{
				Socket s = serverSocket.accept();
				int id = ++nextClientID;
				
				NetServerClient c = new NetServerClient(this, s, id);
				clients.put(id, c);
				worldObj.onClientJoined(c);
			}
			catch(Exception e)
			{ if(!(e instanceof SocketException)) { e.printStackTrace(); stop(); }}
		}
	}
}