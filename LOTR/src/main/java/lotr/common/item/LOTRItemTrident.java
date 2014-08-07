package lotr.common.item;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRLevelData;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTRItemTrident extends LOTRItemSword
{
	public LOTRItemTrident(ToolMaterial material)
	{
		super(material);
	}
	
	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		entityplayer.swingItem();
        MovingObjectPosition m = getMovingObjectPositionFromPlayer(world, entityplayer, true);
        if (m != null && m.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
			int i = m.blockX;
			int j = m.blockY;
			int k = m.blockZ;

			if (canFishAt(world, i, j, k))
			{
				for (int l = 0; l < 20; l++)
				{
					double d = (double)i + (double)world.rand.nextFloat();
					double d1 = (double)j + (double)world.rand.nextFloat();
					double d2 = (double)k + (double)world.rand.nextFloat();
					String s = world.rand.nextBoolean() ? "bubble" : "splash";
					world.spawnParticle(s, d, d1, d2, 0D, (double)(world.rand.nextFloat() * 0.2F), 0D);
				}
				
				if (!world.isRemote)
				{
					entityplayer.addExhaustion(0.03F);
					
					if (world.rand.nextInt(5) == 0)
					{
						world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "random.splash", 0.5F, 1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.4F);
						itemstack.damageItem(1, entityplayer);

						if (world.rand.nextInt(4) == 0)
						{
							EntityItem fish = new EntityItem(world, (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, new ItemStack(Items.fish));
							double d = entityplayer.posX - fish.posX;
							double d1 = entityplayer.posY - fish.posY;
							double d2 = entityplayer.posZ - fish.posZ;
							double d3 = (double)MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
							double d4 = 0.1D;
							fish.motionX = d * d4;
							fish.motionY = d1 * d4 + (double)MathHelper.sqrt_double(d3) * 0.08D;
							fish.motionZ = d2 * d4;
							world.spawnEntityInWorld(fish);
							entityplayer.addStat(StatList.fishCaughtStat, 1);
							world.spawnEntityInWorld(new EntityXPOrb(world, fish.posX, fish.posY, fish.posZ, world.rand.nextInt(3) + 1));
							LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.useDunlendingTrident);
						}
					}
				}
			}
		}
		
		return itemstack;
    }
	
	private boolean canFishAt(World world, int i, int j, int k)
	{
		double d = i + 0.5D;
		double d1 = j + 0.5D;
		double d2 = k + 0.5D;
		double d3 = 0.125D;
		AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(d - d3, d1 - d3, d2 - d3, d + d3, d1 + d3, d2 + d3);
		byte range = 5;
		for (int l = 0; l < range; l++)
		{
			double d5 = boundingBox.minY + (boundingBox.maxY - boundingBox.minY) * (l + 0) / range - d3 + d3;
			double d6 = boundingBox.minY + (boundingBox.maxY - boundingBox.minY) * (l + 1) / range - d3 + d3;
			AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(boundingBox.minX, d5, boundingBox.minZ, boundingBox.maxX, d6, boundingBox.maxZ);
			if (world.isAABBInMaterial(aabb, Material.water))
			{
				return true;
			}
		}
		return false;
	}
	
    @Override
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.none;
    }
}
