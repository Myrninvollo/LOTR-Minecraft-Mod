package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIOrcPlaceBomb;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTREntityUrukHaiBerserker extends LOTREntityUrukHai
{
	public LOTREntityUrukHaiBerserker(World world)
	{
		super(world);
		removeTasksOfType(EntityAIAvoidEntity.class);
		isBombardier = true;
	}
	
	@Override
	public EntityAIBase getOrcAttackAI()
	{
		tasks.addTask(4, new LOTREntityAIOrcPlaceBomb(this, 1.5D, LOTRMod.scimitarUruk));
		return new LOTREntityAIAttackOnCollide(this, 2D, false);
	}
	
	@Override
	public void entityInit()
	{
		super.entityInit();
		dataWatcher.addObject(17, Byte.valueOf((byte)0));
	}
	
	@Override
	public int getBombStrength()
	{
		return dataWatcher.getWatchableObjectByte(17);
	}
	
	@Override
	public void setBombStrength(int i)
	{
		dataWatcher.updateObject(17, Byte.valueOf((byte)i));
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.orcTorchItem));
		setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetUruk));
		return data;
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if (!worldObj.isRemote && getAttackTarget() != null && (getEquipmentInSlot(0) == null || getEquipmentInSlot(0).getItem() != LOTRMod.orcTorchItem))
		{
			worldObj.setEntityState(this, (byte)15);
		}
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void handleHealthUpdate(byte b)
    {
        if (b == 15)
        {
            worldObj.spawnParticle("smoke", posX + (rand.nextDouble() - 0.5D) * (double)width, posY + rand.nextDouble() * (double)height, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0.0D, 0.0D, 0.0D);
        }
        else
        {
            super.handleHealthUpdate(b);
        }
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setByte("BombStrength", (byte)getBombStrength());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		setBombStrength(nbt.getByte("BombStrength"));
	}
}
