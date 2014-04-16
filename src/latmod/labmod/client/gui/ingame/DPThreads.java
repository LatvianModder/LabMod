package latmod.labmod.client.gui.ingame;
import java.util.*;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.entity.*;

public class DPThreads extends DebugPage
{
	public DPThreads()
	{ super("Threads"); }
	
	public void addInfo(World w, EntityPlayerSP ep, FastList<String> al)
	{
		try
		{
			al.add("Thread Count: " + Thread.activeCount());
			al.add("");
			String[] threadNames = new String[Thread.activeCount()];
			
			int i = 0;
			Iterator<Thread> threads = Thread.getAllStackTraces().keySet().iterator();
			
			while(threads.hasNext())
			{
				Thread t = threads.next();
				if(t != null && t.isAlive() && !t.isInterrupted() && !t.isDaemon())
				{
					threadNames[i] = t.getName();
					i++;
				}
			}
			
			Arrays.sort(threadNames);
			al.addAll(threadNames);
		}
		catch(Exception e) { }
	}
}