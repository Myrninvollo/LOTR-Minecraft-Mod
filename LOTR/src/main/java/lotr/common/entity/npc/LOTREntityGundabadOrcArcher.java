package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.entity.projectile.LOTREntityCrossbowBolt;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class LOTREntityGundabadOrcArcher extends LOTREntityGundabadOrc implements IRangedAttackMob
{
	private boolean isCrossbower;
	
	public LOTREntityGundabadOrcArcher(World world)
	{
		super(world);
	}
	
	@Override
	public EntityAIBase createOrcAttackAI()
	{
		return new EntityAIArrowAttack(this, 1.25D, 30, 60, 12F);
	}
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		
		if (rand.nextInt(4) == 0)
		{
			setCurrentItemOrArmor(0, new ItemStack(LOTRMod.ironCrossbow));
			isCrossbower = true;
		}
		else
		{
			if (rand.nextBoolean())
			{
				setCurrentItemOrArmor(0, new ItemStack(LOTRMod.orcBow));
			}
			else
			{
				setCurrentItemOrArmor(0, new ItemStack(Items.bow));
			}
			isCrossbower = false;
		}
		
		return data;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("CrossbowOrc", isCrossbower);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
		isCrossbower = nbt.getBoolean("CrossbowOrc");
	}
	
	@Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float f)
    {
		if (isCrossbower)
		{
	        LOTREntityCrossbowBolt bolt = new LOTREntityCrossbowBolt(worldObj, this, target, 1F, 1F);
	        playSound("lotr:item.crossbow", 1F, 1F / (rand.nextFloat() * 0.4F + 0.8F));
	        worldObj.spawnEntityInWorld(bolt);
		}
		else
		{
			orcArrowAttack(target, f);
		}
    }
	
	@Override
	protected void dropFewItems(boolean flag, int i)
	{
		super.dropFewItems(flag, i);
		
		if (rand.nextBoolean())
		{
			int j = rand.nextInt(3) + rand.nextInt(i + 1);
			for (int k = 0; k < j; k++)
			{
				if (isCrossbower)
				{
					dropItem(Items.arrow, 1);
				}
				else
				{
					dropItem(LOTRMod.crossbowBolt, 1);
				}
			}
		}
	}
}
