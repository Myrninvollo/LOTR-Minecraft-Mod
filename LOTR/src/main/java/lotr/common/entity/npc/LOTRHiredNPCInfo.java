package lotr.common.entity.npc;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lotr.common.LOTRLevelData;
import lotr.common.inventory.LOTRInventoryNPC;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

public class LOTRHiredNPCInfo
{
	private LOTREntityNPC theEntity;
	public boolean isActive;
	public int alignmentRequiredToCommand;
	private boolean canMove = true;
	public int mobKills;
	public boolean obeyHornHaltReady = true;
	public boolean obeyHornSummon = true;
	public boolean teleportAutomatically = true;
	public boolean inCombat;
	private boolean prevInCombat;
	public boolean isGuiOpen;
	public boolean guardMode;
	public static int GUARD_RANGE_MIN = 8;
	public static int GUARD_RANGE_MAX = 64;
	private int guardRange = GUARD_RANGE_MIN;
	private Task task = Task.WARRIOR;
	private LOTRInventoryNPC hiredInventory;
	
	public enum Task
	{
		WARRIOR,
		FARMER;
		
		public static Task forID(int id)
		{
			for (Task task : values())
			{
				if (task.ordinal() == id)
				{
					return task;
				}
			}
			return WARRIOR;
		}
	}
	
	public LOTRHiredNPCInfo(LOTREntityNPC npc)
	{
		theEntity = npc;
		theEntity.getDataWatcher().addObject(28, "");
	}
	
	public String getHiringPlayerUUID()
	{
		return theEntity.getDataWatcher().getWatchableObjectString(28);
	}
	
	public void setHiringPlayerUUID(String s)
	{
		theEntity.getDataWatcher().updateObject(28, s);
	}
	
	public EntityPlayer getHiringPlayer()
	{
		try
		{
			UUID uuid = UUID.fromString(getHiringPlayerUUID());
			return uuid == null ? null : theEntity.worldObj.func_152378_a(uuid);
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
	}
	
	public Task getTask()
	{
		return task;
	}
	
	public void setTask(Task t)
	{
		task = t;
		
		if (task == Task.FARMER)
		{
			hiredInventory = new LOTRInventoryNPC("HiredInventory", theEntity, 3);
		}
	}
	
	public LOTRInventoryNPC getHiredInventory()
	{
		return hiredInventory;
	}
	
	public void onUpdate()
	{
		if (!theEntity.worldObj.isRemote)
		{
			if (isActive && getHiringPlayer() != null)
			{
				int alignment = LOTRLevelData.getData(getHiringPlayer()).getAlignment(theEntity.getFaction());
				if ((alignmentRequiredToCommand < 0 && alignment > alignmentRequiredToCommand) || (alignmentRequiredToCommand >= 0 && alignment < alignmentRequiredToCommand))
				{
					dismissUnit();
				}
			}
			
			inCombat = theEntity.getAttackTarget() != null;
			if (inCombat != prevInCombat)
			{
				sendClientPacket(false);
			}
			prevInCombat = inCombat;
		}
	}
	
	public void dismissUnit()
	{
		getHiringPlayer().addChatMessage(new ChatComponentTranslation("lotr.hiredNPC.desert", new Object[] {theEntity.getCommandSenderName()}));
		isActive = false;
		setHiringPlayerUUID("");
		canMove = true;
	}
	
	public void onDeath(DamageSource damagesource)
	{
		if (!theEntity.worldObj.isRemote && isActive && getHiringPlayer() != null)
		{
			EntityPlayer hiringPlayer = getHiringPlayer();
			if (LOTRLevelData.getData(hiringPlayer).getEnableHiredDeathMessages())
			{
				hiringPlayer.addChatMessage(new ChatComponentTranslation("lotr.hiredNPC.death", new Object[] {theEntity.func_110142_aN().func_151521_b()}));
			}
		}
		
		if (!theEntity.worldObj.isRemote && hiredInventory != null)
		{
			hiredInventory.dropAllItems();
		}
	}
	
	public void halt()
	{
		canMove = false;
		theEntity.setAttackTarget(null);
		sendClientPacket(false);
	}
	
	public void ready()
	{
		canMove = true;
		sendClientPacket(false);
	}
	
	public boolean isHalted()
	{
		return !guardMode && !canMove;
	}
	
	public boolean shouldFollowPlayer()
	{
		return !guardMode && canMove;
	}
	
	public boolean getObeyHornHaltReady()
	{
		if (task != Task.WARRIOR)
		{
			return false;
		}
		return !guardMode && obeyHornHaltReady;
	}
	
	public boolean getObeyHornSummon()
	{
		if (task != Task.WARRIOR)
		{
			return false;
		}
		return !guardMode && obeyHornSummon;
	}
	
	public boolean getObeyCommandSword()
	{
		if (task != Task.WARRIOR)
		{
			return false;
		}
		return !guardMode && canMove;
	}
	
	public boolean isGuardMode()
	{
		return guardMode;
	}
	
	public void setGuardMode(boolean flag)
	{
		guardMode = flag;
		if (flag)
		{
			int i = MathHelper.floor_double(theEntity.posX);
			int j = MathHelper.floor_double(theEntity.posY);
			int k = MathHelper.floor_double(theEntity.posZ);
			theEntity.setHomeArea(i, j, k, guardRange);
		}
		else
		{
			theEntity.detachHome();
		}
	}
	
	public int getGuardRange()
	{
		return guardRange;
	}
	
	public void setGuardRange(int range)
	{
		guardRange = MathHelper.clamp_int(range, GUARD_RANGE_MIN, GUARD_RANGE_MAX);
		if (guardMode)
		{
			int i = MathHelper.floor_double(theEntity.posX);
			int j = MathHelper.floor_double(theEntity.posY);
			int k = MathHelper.floor_double(theEntity.posZ);
			theEntity.setHomeArea(i, j, k, guardRange);
		}
	}
	
	public String getStatusString()
	{
		String s = StatCollector.translateToLocal("lotr.hiredNPC.status") + ": ";
		
		if (task == Task.WARRIOR)
		{
			if (inCombat)
			{
				s = s + StatCollector.translateToLocal("lotr.hiredNPC.status.combat");
			}
			else if (isHalted())
			{
				s = s + StatCollector.translateToLocal("lotr.hiredNPC.status.halted");
			}
			else if (guardMode)
			{
				s = s + StatCollector.translateToLocal("lotr.hiredNPC.status.guard");
			}
			else
			{
				s = s + StatCollector.translateToLocal("lotr.hiredNPC.status.ready");
			}
		}
		else if (task == Task.FARMER)
		{
			if (guardMode)
			{
				s = s + StatCollector.translateToLocal("lotr.hiredNPC.status.farming");
			}
			else
			{
				s = s + StatCollector.translateToLocal("lotr.hiredNPC.status.following");
			}
		}
		
		return s;
	}
	
	public void onKillEntity(EntityLivingBase entity)
	{
		if (!theEntity.worldObj.isRemote && isActive)
		{
			mobKills++;
			sendClientPacket(false);
		}
	}
	
	public boolean tryTeleportToHiringPlayer()
	{
		if (!theEntity.worldObj.isRemote)
		{
			EntityPlayer entityplayer = getHiringPlayer();
			if (isActive && entityplayer != null && theEntity.riddenByEntity == null)
			{
				int i = MathHelper.floor_double(entityplayer.posX);
				int j = MathHelper.floor_double(entityplayer.boundingBox.minY);
				int k = MathHelper.floor_double(entityplayer.posZ);
				
				float f = theEntity.width / 2F;
				float f1 = theEntity.height;
				AxisAlignedBB npcBoundingBox = AxisAlignedBB.getBoundingBox(entityplayer.posX - (double)f, entityplayer.posY - (double)theEntity.yOffset + (double)theEntity.ySize, entityplayer.posZ - (double)f, entityplayer.posX + (double)f, entityplayer.posY - (double)theEntity.yOffset + (double)theEntity.ySize + (double)f1, entityplayer.posZ + (double)f);
				
				if (theEntity.worldObj.func_147461_a(npcBoundingBox).isEmpty() && theEntity.worldObj.getBlock(i, j - 1, k).isSideSolid(theEntity.worldObj, i, j - 1, k, ForgeDirection.UP))
				{
					if (theEntity.ridingEntity != null && theEntity.ridingEntity instanceof EntityLiving)
					{
						EntityLiving mount = (EntityLiving)theEntity.ridingEntity;
						float f2 = mount.width / 2F;
						float f3 = mount.height;
						AxisAlignedBB mountBoundingBox = AxisAlignedBB.getBoundingBox(entityplayer.posX - (double)f2, entityplayer.posY - (double)mount.yOffset + (double)mount.ySize, entityplayer.posZ - (double)f2, entityplayer.posX + (double)f2, entityplayer.posY - (double)mount.yOffset + (double)mount.ySize + (double)f3, entityplayer.posZ + (double)f2);
						
						if (theEntity.worldObj.func_147461_a(mountBoundingBox).isEmpty() && theEntity.worldObj.getBlock(i, j - 1, k).isSideSolid(theEntity.worldObj, i, j - 1, k, ForgeDirection.UP))
						{
							mount.setLocationAndAngles(entityplayer.posX, entityplayer.boundingBox.minY, entityplayer.posZ, theEntity.rotationYaw, theEntity.rotationPitch);
							mount.fallDistance = 0F;
							mount.getNavigator().clearPathEntity();
							mount.setAttackTarget(null);
							theEntity.fallDistance = 0F;
							theEntity.getNavigator().clearPathEntity();
							theEntity.setAttackTarget(null);
							return true;
						}
					}
					else
					{
						theEntity.setLocationAndAngles(entityplayer.posX, entityplayer.boundingBox.minY, entityplayer.posZ, theEntity.rotationYaw, theEntity.rotationPitch);
						theEntity.fallDistance = 0F;
						theEntity.getNavigator().clearPathEntity();
						theEntity.setAttackTarget(null);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagCompound data = new NBTTagCompound();
		data.setBoolean("IsActive", isActive);
		data.setString("HiringPlayerUUID", getHiringPlayerUUID());
		data.setInteger("AlignmentRequired", alignmentRequiredToCommand);
		data.setBoolean("CanMove", canMove);
		data.setBoolean("ObeyHornHaltReady", obeyHornHaltReady);
		data.setBoolean("ObeyHornSummon", obeyHornSummon);
		data.setBoolean("TeleportAutomatically", teleportAutomatically);
		data.setInteger("MobKills", mobKills);
		data.setBoolean("GuardMode", guardMode);
		data.setInteger("GuardRange", guardRange);
		data.setInteger("Task", task.ordinal());
		if (hiredInventory != null)
		{
			hiredInventory.writeToNBT(data);
		}
		nbt.setTag("HiredNPCInfo", data);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagCompound data = nbt.getCompoundTag("HiredNPCInfo");
		if (data != null)
		{
			if (data.hasKey("HiringPlayerName"))
			{
				String name = data.getString("HiringPlayerName");
				setHiringPlayerUUID(PreYggdrasilConverter.func_152719_a(name));
			}
			else
			{
				setHiringPlayerUUID(data.getString("HiringPlayerUUID"));
			}
			isActive = data.getBoolean("IsActive");
			alignmentRequiredToCommand = data.getInteger("AlignmentRequired");
			canMove = data.getBoolean("CanMove");
			if (data.hasKey("ObeyHornHaltReady"))
			{
				obeyHornHaltReady = data.getBoolean("ObeyHornHaltReady");
				obeyHornSummon = data.getBoolean("ObeyHornSummon");
				teleportAutomatically = data.getBoolean("TeleportAutomatically");
				mobKills = data.getInteger("MobKills");
				setGuardMode(data.getBoolean("GuardMode"));
				setGuardRange(data.getInteger("GuardRange"));
			}
			setTask(Task.forID(data.getInteger("Task")));
			if (hiredInventory != null)
			{
				hiredInventory.readFromNBT(data);
			}
		}
	}
	
	public void sendClientPacket(boolean shouldOpenGui)
	{
		if (theEntity.worldObj.isRemote || getHiringPlayer() == null)
		{
			return;
		}
		
		ByteBuf data = Unpooled.buffer();
		
		data.writeInt(theEntity.getEntityId());
		data.writeBoolean(shouldOpenGui);
		data.writeBoolean(isActive);
		data.writeBoolean(canMove);
		data.writeBoolean(obeyHornHaltReady);
		data.writeBoolean(obeyHornSummon);
		data.writeBoolean(teleportAutomatically);
		data.writeInt(mobKills);
		data.writeInt(alignmentRequiredToCommand);
		data.writeBoolean(inCombat);
		data.writeBoolean(guardMode);
		data.writeInt(guardRange);
		data.writeByte((byte)task.ordinal());

		Packet packet = new S3FPacketCustomPayload("lotr.hiredGui", data);
		((EntityPlayerMP)getHiringPlayer()).playerNetServerHandler.sendPacket(packet);
		
		if (shouldOpenGui)
		{
			isGuiOpen = true;
		}
	}
	
	public void receiveClientPacket(ByteBuf data)
	{
		isActive = data.readBoolean();
		canMove = data.readBoolean();
		obeyHornHaltReady = data.readBoolean();
		obeyHornSummon = data.readBoolean();
		teleportAutomatically = data.readBoolean();
		mobKills = data.readInt();
		alignmentRequiredToCommand = data.readInt();
		inCombat = data.readBoolean();
		guardMode = data.readBoolean();
		guardRange = data.readInt();
		setTask(Task.forID(data.readByte()));
	}
}
