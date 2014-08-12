package latmod.labmod.entity;
import org.lwjgl.input.Mouse;

import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.client.gui.*;
import latmod.labmod.client.gui.ingame.*;
import latmod.labmod.cmd.*;

public class EntityPlayerSP extends EntityPlayer // Entity
{
	public static final Color DOT_GREEN = Color.get(50, 220, 50);
	public static final Color DOT_RED = Color.get(220, 50, 50);
	
	public double moveSpeed = 0.05D;
	public FastMap<KeyBinding, Long> lastKeyPressedMillis = new FastMap<KeyBinding, Long>();
	private double runFovDelta = 0D;
	private double zoomFovDelta = 0D;
	public boolean renderGui = true;
	public DebugPage debugPage = null;
	public boolean isZooming = false;
	
	public EntityPlayerSP(World w)
	{
		super(w);
		setPos(w.spawnPoint);
		collisionBox = new AABB.BottomCentred(posX, posY, posZ, sizeH, sizeV, sizeH);
		
		for(KeyBinding k : GameOptions.keyBindings)
		lastKeyPressedMillis.put(k, Time.millis());
		
		debugPage = DebugPage.debugPages.get(0);
	}
	
	public void renderGui()
	{
		Renderer.enableTexture();
		
		boolean chatOpened = Main.inst.getGui() instanceof GuiChat;
		Main.inst.font.alpha = chatOpened ? 40 : 200;
		
		if(debugPage != null)
		{
			FastList<String> al = new FastList<String>();
			al.add(TextColor.BOLD + debugPage.name + TextColor.BOLD);
			
			debugPage.addInfo(worldObj, this, al);
			
			for(int i = 0; i < al.size(); i++)
				Main.inst.font.drawShadedText(4, 4 + i * 20, al.get(i));
			
			if(debugPage != null)
			debugPage.onCustom2DRender(worldObj, this);
		}
		
		Main.inst.font.alpha = 255;
		
		{
			if(cursor.lookEntity != null && cursor.lookEntity != null && cursor.lookEntity.isGreenDot(this))
				Color.set(DOT_GREEN); else Color.set(DOT_RED);
			
			Renderer.disableTexture();
			Renderer.lineWidth(4F);
			Renderer.enableSmooth();
			Renderer.point(Main.inst.width / 2F, Main.inst.height / 2F);
			Renderer.disableSmooth();
			Renderer.lineWidth(1F);
		}
	}
	
	public void onRender()
	{
		super.onRender();
		
		if(debugPage instanceof DPPlayer)
		{
			Color.reset();
			Renderer.disableTexture();
			Renderer.translate(cursor, 1D);
			Renderer.scale(0.1D);
			Renderer3D.sphere(Renderer3D.LINES, 10, 10);
			Renderer.enableTexture();
		}
	}
	
	public void onUpdate(Timer t)
	{
		GuiIngame.updateChat();
		collisionBox.set(posX, posY, posZ, sizeH, sizeV, sizeH);
		
		boolean canMove = Main.inst.getGui().allowPlayerInput();
		
		double tarX = MathHelper.sinFromDeg(rotYaw);
		double tarZ = MathHelper.cosFromDeg(rotYaw);
		
		double speed = moveSpeed;
		if(!canMove || !GameOptions.KEY_MOVE_FORWARD.isPressed()) isRunning = false;
		if(isRunning) speed *= 2.1D;
		
		if(canMove)
		{
			if(GameOptions.KEY_MOVE_FORWARD.isPressed())
			{
				double x = tarX;
				double z = tarZ;
				moveTowards(x, z, speed * 1F);
			}
			
			if(GameOptions.KEY_MOVE_BACKWARD.isPressed())
			{
				double x = tarX;
				double z = tarZ;
				moveTowards(x, z, speed * -0.8F);
			}
			
			if(GameOptions.KEY_MOVE_LEFT.isPressed())
			{
				double x = MathHelper.sinFromDeg(rotYaw + 90F);
				double z = MathHelper.cosFromDeg(rotYaw + 90F);
				moveTowards(x, z, speed * 0.6F);
			}
			
			if(GameOptions.KEY_MOVE_RIGHT.isPressed())
			{
				double x = MathHelper.sinFromDeg(rotYaw - 90F);
				double z = MathHelper.cosFromDeg(rotYaw - 90F);
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
		
		distanceMovedH = MathHelper.dist(0D, 0F, motX, motZ);
		distanceMovedT = MathHelper.dist(0F, 0F, 0F, motX, motY, motZ);
		
		Renderer3D.camera.fov = 75F + runFovDelta - zoomFovDelta;
		Renderer3D.camera.yaw = rotYaw;
		Renderer3D.camera.pitch = rotPitch;
		Renderer3D.camera.posX = posX;
		Renderer3D.camera.posY = posY + eyeHeight;
		Renderer3D.camera.posZ = posZ;
		Renderer3D.camera.look.posX = posX + MathHelper.sinFromDeg(rotYaw);
		Renderer3D.camera.look.posY = posY + eyeHeight + MathHelper.tanFromDeg(rotPitch);
		Renderer3D.camera.look.posZ = posZ + MathHelper.cosFromDeg(rotYaw);
		
		if(hurtTimer > 0F) hurtTimer -= 0.3F;
		if(hurtTimer < 0F) hurtTimer = 0F;
		
		if(t.tick % t.TPS == 0 && health < 100) health++;
		
		if(Mouse.isButtonDown(1) && t.tick % 3 == 0)
		{
			executeCommand("spawn box");
		}
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
		
		String args[] = LMCommon.split(argsUnsplit, " ");
		
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