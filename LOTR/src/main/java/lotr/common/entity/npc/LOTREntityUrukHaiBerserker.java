package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityUrukHaiBerserker extends LOTREntityUrukHai
{
	public static float BERSERKER_SCALE = 1.25F;
	
	public LOTREntityUrukHaiBerserker(World world)
	{
		super(world);
		setSize(width * BERSERKER_SCALE, height * BERSERKER_SCALE);
	}
	
	@Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(32D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.28D);
    }
	
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data)
    {
		data = super.onSpawnWithEgg(data);
		setCurrentItemOrArmor(0, new ItemStack(LOTRMod.scimitarUruk));
		setCurrentItemOrArmor(4, null);
		return data;
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		worldObj.spawnParticle("largesmoke", posX + (rand.nextDouble() - 0.5D) * (double)width, posY + rand.nextDouble() * (double)height, posZ + (rand.nextDouble() - 0.5D) * (double)width, 0D, 0D, 0D);
	}
	
	@Override
	protected float getSoundPitch()
	{
		return super.getSoundPitch() * 0.8F;
	}
}
