package latmod.labmod.net;
import java.io.*;

public class ServerOutputStream extends OutputStream
{
	public NetServer server;
	
	public ServerOutputStream(NetServer s) { server = s; }

	public void write(int b) throws IOException
	{ for(NetServerClient c : server.clients) c.getData().writeByte(b); }
	
	public void flush() throws IOException
	{ for(NetServerClient c : server.clients) c.getData().flush(); }
	
	public void close() throws IOException
	{ for(NetServerClient c : server.clients) c.getData().close(); }
}