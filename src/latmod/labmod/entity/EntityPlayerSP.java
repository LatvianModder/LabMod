package latmod.labmod.entity;
import latmod.core.rendering.*;
import latmod.core.util.*;
import latmod.labmod.*;
import latmod.labmod.client.entity.EntityRenderer;
import latmod.labmod.client.gui.*;
import latmod.labmod.client.gui.ingame.*;
import latmod.labmod.cmd.*;
import latmod.labmod.net.packets.PacketChat;
import latmod.labmod.world.*;

public class EntityPlayerSP extends EntityPlayer // Entity
{
	public float moveSpeed = 0.2F;
	public FastMap<KeyBinding, Long> lastKeyPressedMillis = new FastMap<KeyBinding, Long>();
	private float runFovDelta = 0F;
	private float zoomFovDelta = 0F;
	public boolean renderGui = true;
	public DebugPage debugPage = null;
	
	public EntityPlayerSP(WorldClient w)
	{
		super(w, w.client.clientID);
		posX = w.rand.nextFloat() * 200F;
		posZ = w.rand.nextFloat() * 200F;
		username = GameOptions.props.username;
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
		//super.onRender();
		
		Renderer.push();
		EntityRenderer r = EntityRenderer.renderMap.get(EntityRock.class);
		Entity e = new EntityRock(worldObj);
		e.setPos(camera);
		r.renderEntity(e);
		Renderer.pop();
	}
	
	public void onUpdate(Timer t)
	{
		GuiIngame.updateChat();
		
		boolean canMove = Main.inst.getGui().allowPlayerInput();
		
		float tarX = MathHelper.sinFromDeg(rotYaw);
		float tarZ = MathHelper.cosFromDeg(rotYaw);
		float prevYaw = rotYaw;
		float prevPitch = rotPitch;
		
		float speed = moveSpeed;
		if(!canMove || !GameOptions.KEY_MOVE_FORWARD.isPressed()) isRunning = false;
		if(isRunning) speed *= 2.1F;
		
		if(canMove)
		{
			boolean inverse = (rotPitch > -270F && rotPitch <= -90F);
			inverse = false;
			
			if(GameOptions.KEY_MOVE_FORWARD.isPressed())
			{
				float x = tarX;
				float z = tarZ;
				moveTowards(x, z, speed * (inverse ? -0.8F : 1F));
			}
			
			if(GameOptions.KEY_MOVE_BACKWARD.isPressed())
			{
				float x = tarX;
				float z = tarZ;
				moveTowards(x, z, speed * -0.8F);
			}
		}
		
		moveEntity();
		
		motX *= 0.29F;
		motY *= 0.29F;
		motZ *= 0.29F;
		
		if(Math.abs(motX) < 0.01F) motX = 0F;
		if(Math.abs(motY) < 0.01F) motY = 0F;
		if(Math.abs(motZ) < 0.01F) motZ = 0F;
		
		if(isRunning) runFovDelta += 3.5F;
		else runFovDelta -= 4F;
		runFovDelta = MathHelper.limit(runFovDelta, 0F, 20F);
		
		zoomFovDelta -= 5F;
		zoomFovDelta = MathHelper.limit(zoomFovDelta, 0F, 45F);
		
		rotYaw = rotYaw % 360F;
		rotPitch = MathHelper.limit(rotPitch, -89.995F, 89.995F);
		
		tarX = MathHelper.sinFromDeg(rotYaw);
		tarZ = MathHelper.cosFromDeg(rotYaw);
		
		camera.update();
		
		flags[IN_WALL] = worldObj.getAABBInBox(collisionBox, 0F, 0F, 0F) != null;
		
		if(flags[IN_WALL]) onAttacked(null, maxHealth / 5F);
		
		distanceMovedH = MathHelper.dist(0F, 0F, motX, motZ);
		distanceMovedT = MathHelper.dist(0F, 0F, 0F, motX, motY, motZ);
		
		Renderer3D.FOV = 75F + runFovDelta - zoomFovDelta;
		
		Renderer3D.camPos.posX = posX;
		Renderer3D.camPos.posY = posY;
		Renderer3D.camPos.posZ = posZ;
		
		Renderer3D.camLook.posX = camera.posX;
		Renderer3D.camLook.posY = camera.posY;
		Renderer3D.camLook.posZ = camera.posZ;
		
		Renderer3D.setCamRot(rotYaw, rotPitch);
		
		if(hurtTimer > 0F) hurtTimer -= 0.3F;
		if(hurtTimer < 0F) hurtTimer = 0F;
		
		if(prevYaw != rotYaw || prevPitch != rotPitch) isDirty = true;
		
		if(health <= 0)
		{
			health = maxHealth;
			isDirty = true;
			//TODO: Respawn
			return;
		}
		
		if(t.tick % t.TPS == 0)
		{
			if(health < 100)
			health++;
			
			isDirty = true;
		}
	}
	
	public void printChat(String s)
	{
		if(s == null || s.length() <= 0) return;
		
		if(s.length() > 1 && s.startsWith("/"))
		{
			s = s.substring(1);
			
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
			
			CommandSender cmdSender = new CommandSender();
			cmdSender.name = username;
			cmdSender.player = this;
			
			String err = null;
			
			Command c = Command.commands.get(cmd);
			if(c == null) err = "Command doesn't exist";
			else
			{
				if(Command.SERVER.equals(c.getCommandSide()))
				err = "Can't execute Server command on client!";
				else err = c.onCommand(worldObj, cmdSender, args, argsUnsplit);
			}
			
			if(err != null) GuiIngame.printChat(Command.ERR.toString() + err);
			
			return;
		}
		
		//GuiIngame.printChat(Command.NAME + username + ": " + Command.FINE + s);
		//LatCore.println(s, username, "Chat");
		worldObj.sendPacket(new PacketChat(Command.NAME + username + ": " + Command.FINE + s));
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