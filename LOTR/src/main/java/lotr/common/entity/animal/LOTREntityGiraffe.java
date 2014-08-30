package lotr.common.entity.animal;

import lotr.common.*;
import lotr.common.world.biome.LOTRBiomeGenShire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTREntityGiraffe extends LOTREntityHorse
{
    public LOTREntityGiraffe(World world)
    {
        super(world);
        setSize(1.7F, 3.5F);
    }
	
	@Override
	public int getHorseType()
	{
		return 0;
	}
	
	@Override
	protected void onLOTRHorseSpawn()
	{
		double jumpStrength = getEntityAttribute(LOTRReflection.getHorseJumpStrength()).getAttributeValue();
		jumpStrength *= 0.8D;
		getEntityAttribute(LOTRReflection.getHorseJumpStrength()).setBaseValue(jumpStrength);
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

        if (!worldObj.isRemote)
        {
        	if (riddenByEntity instanceof EntityPlayer && isMountSaddled())
			{
        		EntityPlayer entityplayer = (EntityPlayer)riddenByEntity;
        		BiomeGenBase biome = worldObj.getBiomeGenForCoords(MathHelper.floor_double(posX), MathHelper.floor_double(posZ));
        		if (biome instanceof LOTRBiomeGenShire)
        		{
        			LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.rideGiraffeShire);
        		}
			}
		}
	}
	
	@Override
    public boolean isBreedingItem(ItemStack itemstack)
    {
        return itemstack != null && Block.getBlockFromItem(itemstack.getItem()) instanceof BlockLeavesBase;
    }

	@Override
    protected Item getDropItem()
    {
        return Items.leather;
    }
}
