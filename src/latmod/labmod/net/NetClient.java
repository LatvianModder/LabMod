package latmod.labmod.net;
import java.net.*;

import latmod.core.util.*;
import latmod.labmod.net.packets.PacketProcessor;
import latmod.labmod.net.packets.PacketSender;
import latmod.labmod.world.*;

public class NetClient implements Runnable, INet
{
	public World worldObj;
	public int clientID = 0;
	public Thread thread = null;
	public DataIOStream data = null;
	public Socket socket;
	private String socketIP = null;
	private int socketPort = 0;
	public boolean isFailed = false;
	public boolean isConnecting = true;
	public PacketSender packetSender = null;
	public PacketProcessor packetProcessor = null;
	
	protected NetClient(World w, Socket s, int id)
	{
		worldObj = w;
		socket = s;
		clientID = id;
		packetSender = new PacketSender(this);
		packetProcessor = new PacketProcessor(this);
	}
	
	public NetClient(WorldClient w, String ip, int p)
	{
		this(w, (Socket)null, 0);
		socketIP = ip;
		socketPort = p;
		
		thread = new Thread(this, "NetClient_" + ip + ':' + p);
		thread.start();
	}
	
	public void run()
	{
		long l = Time.millis();
		
		try
		{
			isFailed = false;
			isConnecting = false;
			
			if(socket == null)
			{
				socket = new Socket(socketIP, socketPort);
				data = new DataIOStream(socket.getInputStream(), socket.getOutputStream());
				clientID = data.readShort();
				isConnecting = false;
			}
			else isConnecting = false;
		}
		catch(Exception e)
		{ isFailed = true; thread = null; e.printStackTrace(); }
		
		if(worldObj instanceof WorldClient) ((WorldClient)worldObj).onConnectionCreated(this, Time.since(l));
		
		try
		{
			while(thread != null)
			{
				if(data.available() > 0)
				worldObj.onClientData(this);
				
				packetSender.update();
				packetProcessor.update();
				
				Thread.sleep(1);
			}
		}
		catch(Exception e)
		{ isFailed = true; stop(); e.printStackTrace(); }
		
		stopNow();
	}
	
	public void stop()
	{ thread = null;  }
	
	public void stopNow()
	{
		thread = null;
		
		if(data != null)
		{
			try { data.close(); }
			catch(Exception e)
			{ e.printStackTrace(); }
			data = null;
		}
		
		if(socket != null)
		{
			try { socket.close(); }
			catch(Exception e)
			{ e.printStackTrace(); }
			socket = null;
		}
		
		packetSender = null;
		packetProcessor = null;
	}
	
	public DataIOStream getData()
	{ return data; }
	
	public String getIP()
	{ return (socket == null) ? "null" : (socket.getInetAddress().getHostName() + ":" + socket.getPort()); }
}