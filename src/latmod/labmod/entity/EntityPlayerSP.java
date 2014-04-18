package latmod.labmod.entity;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.client.gui.*;
import latmod.labmod.client.gui.ingame.*;
import latmod.labmod.cmd.*;

public class EntityPlayerSP extends EntityPlayer // Entity
{
	public float moveSpeed = 0.05F;
	public FastMap<KeyBinding, Long> lastKeyPressedMillis = new FastMap<KeyBinding, Long>();
	private float runFovDelta = 0F;
	private float zoomFovDelta = 0F;
	public boolean renderGui = true;
	public DebugPage debugPage = null;
	public boolean isZooming = false;
	
	public EntityPlayerSP(World w)
	{
		super(w);
		setPos(w.spawnPoint);
		username = "LatvianModder";
		collisionBox = new AABB.BottomCentred(posX, posY, posZ, sizeH, sizeV, sizeH);
		
		for(KeyBinding k : GameOptions.keyBindings)
		lastKeyPressedMillis.put(k, Time.millis());
		
		debugPage = DebugPage.debugPages.get(0);
	}
	
	public void renderGui()
	{
		Renderer.enableTexture();
		
		boolean chatOpened = Main.inst.getGui() instanceof GuiChat;
		Font.inst.alpha = chatOpened ? 40 : 200;
		
		if(debugPage != null)
		{
			FastList<String> al = new FastList<String>();
			al.add(TextColor.BOLD + debugPage.name + TextColor.BOLD);
			
			debugPage.addInfo(worldObj, this, al);
			
			for(int i = 0; i < al.size(); i++)
			Font.inst.drawShadedText(4, 4 + i * 20, al.get(i));
			
			if(debugPage != null)
			debugPage.onCustom2DRender(worldObj, this);
		}
		
		Font.inst.alpha = 255;
	}
	
	public void onRender()
	{
		super.onRender();
	}
	
	public void onUpdate(Timer t)
	{
		GuiIngame.updateChat();
		collisionBox.set(posX, posY, posZ, sizeH, sizeV, sizeH);
		
		boolean canMove = Main.inst.getGui().allowPlayerInput();
		
		float tarX = MathHelper.sinFromDeg(rotYaw);
		float tarZ = MathHelper.cosFromDeg(rotYaw);
		
		float speed = moveSpeed;
		if(!canMove || !GameOptions.KEY_MOVE_FORWARD.isPressed()) isRunning = false;
		if(isRunning) speed *= 2.1F;
		
		if(canMove)
		{
			if(GameOptions.KEY_MOVE_FORWARD.isPressed())
			{
				float x = tarX;
				float z = tarZ;
				moveTowards(x, z, speed * 1F);
			}
			
			if(GameOptions.KEY_MOVE_BACKWARD.isPressed())
			{
				float x = tarX;
				float z = tarZ;
				moveTowards(x, z, speed * -0.8F);
			}
			
			if(GameOptions.KEY_MOVE_LEFT.isPressed())
			{
				float x = MathHelper.sinFromDeg(rotYaw + 90F);
				float z = MathHelper.cosFromDeg(rotYaw + 90F);
				moveTowards(x, z, speed * 0.6F);
			}
			
			if(GameOptions.KEY_MOVE_RIGHT.isPressed())
			{
				float x = MathHelper.sinFromDeg(rotYaw - 90F);
				float z = MathHelper.cosFromDeg(rotYaw - 90F);
				moveTowards(x, z, speed * 0.6F);
			}
			
			if(GameOptions.KEY_MOVE_JUMP.isPressed() && flags[ON_GROUND]) motY += 0.15F;
		}
		
		moveEntity();
		
		if(flags[ON_GROUND])
		{
			motX *= 0.49F;
			motZ *= 0.49F;
		}
		else
		{
			motX *= 0.29F;
			motZ *= 0.29F;
		}
		
		if(Math.abs(motX) < 0.01F) motX = 0F;
		if(Math.abs(motZ) < 0.01F) motZ = 0F;
		
		if(isRunning) runFovDelta += 3.5F;
		else runFovDelta -= 4F;
		runFovDelta = MathHelper.limit(runFovDelta, 0F, 20F);
		
		if(isZooming) zoomFovDelta += 6.5F;
		else zoomFovDelta -= 5F;
		zoomFovDelta = MathHelper.limit(zoomFovDelta, 0F, 45F);
		
		cursor.update();
		
		flags[IN_WALL] = worldObj.getAABBInBox(collisionBox, 0F, worldObj.gravity, 0F) != null;
		
		if(flags[IN_WALL] || posY < -100F) onAttacked(null, 20);
		
		if(health <= 0)
		{
			worldObj.player = new EntityPlayerSP(worldObj);
		}
		
		distanceMovedH = MathHelper.dist(0F, 0F, motX, motZ);
		distanceMovedT = MathHelper.dist(0F, 0F, 0F, motX, motY, motZ);
		
		Renderer3D.FOV = 75F + runFovDelta - zoomFovDelta;
		Renderer3D.setCamRot(rotYaw, rotPitch);
		Renderer3D.camPos.posX = posX;
		Renderer3D.camPos.posY = posY + eyeHeight;
		Renderer3D.camPos.posZ = posZ;
		Renderer3D.camLook.posX = posX + MathHelper.sinFromDeg(rotYaw);
		Renderer3D.camLook.posY = posY + eyeHeight + MathHelper.tanFromDeg(rotPitch);
		Renderer3D.camLook.posZ = posZ + MathHelper.cosFromDeg(rotYaw);
		
		if(hurtTimer > 0F) hurtTimer -= 0.3F;
		if(hurtTimer < 0F) hurtTimer = 0F;
		
		if(t.tick % t.TPS == 0 && health < 100) health++;
	}
	
	public void executeCommand(String s)
	{
		if(s == null || s.length() == 0) return;
		if(s.startsWith("/")) s = s.substring(1);
		if(s.length() == 0) return;
		
		int si = s.indexOf(' ');
		String cmd, argsUnsplit;
		
		if(si != -1)
		{
			cmd = s.substring(0, si);
			argsUnsplit = s.substring(si + 1, s.length());
		}
		else
		{
			cmd = s;
			argsUnsplit = "";
		}
		
		String args[] = LatCore.split(argsUnsplit, " ");
		
		String err = null;
		
		Command c = Command.commands.get(cmd);
		if(c == null) err = "Command doesn't exist";
		else
		{
			try { err = c.onCommand(worldObj, this, args, argsUnsplit); }
			catch(Exception e)
			{
				GuiIngame.printChat("Failed to execute command: " + e);
				e.printStackTrace();
			}
		}
		
		if(err != null) GuiIngame.printChat(Command.ERR.toString() + err);
	}
	
	public boolean onAttacked(Entity from, float hp)
	{
		if(hurtTimer < 5)
		{
			health -= hp;
			hurtTimer = 10;
		}
		
		if(health <= 0) return true;
		
		return false;
	}
}