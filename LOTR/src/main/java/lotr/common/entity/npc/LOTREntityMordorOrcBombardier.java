package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIOrcPlaceBomb;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class LOTREntityMordorOrcBombardier extends LOTREntityMordorOrc
{
	public LOTREntityMordorOrcBombardier(World world)
	{
		super(world);
		isBombardier = true;
	}
	
	@Override
	public EntityAIBase createOrcAttackAI()
	{
		tasks.addTask(4, new LOTREntityAIOrcPlaceBomb(this, 1.4D, LOTRMod.daggerOrc));
		return new LOTREntityAIAttackOnCollide(this, 1.4D, false);
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
		setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetOrc));
		return data;
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
