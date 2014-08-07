package lotr.common.item;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.block.LOTRBlockMug;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTRItemMug extends Item
{
	public boolean alwaysDrinkable;
	private int foodHealAmount;
	private float foodSaturationAmount;
	private boolean curesEffects;
	
	public LOTRItemMug(boolean drinkable)
	{
		super();
		setMaxStackSize(1);
		alwaysDrinkable = drinkable;
		setCreativeTab(LOTRCreativeTabs.tabFood);
	}
	
	public LOTRItemMug setDrinkStats(int i, float f)
	{
		foodHealAmount = i;
		foodSaturationAmount = f;
		return this;
	}

	public LOTRItemMug setCuresEffects()
	{
		curesEffects = true;
		return this;
	}

	@Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float f, float f1, float f2)
    {
		i += Facing.offsetsXForSide[side];
		j += Facing.offsetsYForSide[side];
		k += Facing.offsetsZForSide[side];
		Block block = LOTRMod.mugBlock;
		
		Block block1 = world.getBlock(i, j, k);
		if (block1 != null && !block1.isReplaceable(world, i, j, k))
		{
			return false;
		}
		
		if (block1.getMaterial() == Material.water)
		{
			return false;
		}

		if (entityplayer.canPlayerEdit(i, j, k, side, itemstack))
		{
			if (!block.canPlaceBlockAt(world, i, j, k))
			{
				return false;
			}
			else
			{
				int l = MathHelper.floor_double((double)(entityplayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
				world.setBlock(i, j, k, block, l, 3);
				LOTRBlockMug.setMugItem(world, i, j, k, itemstack);
				world.markBlockForUpdate(i, j, k);
				world.playSoundEffect(i + 0.5D, j + 0.5D, k + 0.5D, block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
				itemstack.stackSize--;
				return true;
			}
		}
		else
		{
			return false;
		}
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
		if (this == LOTRMod.mug)
		{
			MovingObjectPosition m = getMovingObjectPositionFromPlayer(world, entityplayer, true);
			if (m == null)
			{
				return itemstack;
			}
			else if (m.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
			{
                int i = m.blockX;
                int j = m.blockY;
                int k = m.blockZ;

                if (!world.canMineBlock(entityplayer, i, j, k))
                {
                    return itemstack;
                }
				if (!entityplayer.canPlayerEdit(i, j, k, m.sideHit, itemstack))
				{
					return itemstack;
				}
				
				if (world.getBlock(i, j, k).getMaterial() == Material.water && world.getBlockMetadata(i, j, k) == 0)
				{
					itemstack.stackSize--;
					if (itemstack.stackSize <= 0)
					{
						world.playSoundAtEntity(entityplayer, "lotr:item.mug_fill", 0.5F, 0.8F + (world.rand.nextFloat() * 0.4F));
						return new ItemStack(LOTRMod.mugWater);
					}

					if (!entityplayer.inventory.addItemStackToInventory(new ItemStack(LOTRMod.mugWater)))
					{
						entityplayer.dropPlayerItemWithRandomChoice(new ItemStack(LOTRMod.mugWater), false);
					}
					world.playSoundAtEntity(entityplayer, "lotr:item.mug_fill", 0.5F, 0.8F + (world.rand.nextFloat() * 0.4F));
					return itemstack;
				}
			}
			return itemstack;
		}
		else
		{
			if (alwaysDrinkable || entityplayer.canEat(false))
			{
				entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
			}
			return itemstack;
		}
	}
	
	@Override
    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.drink;
    }
	
	@Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
		if (entityplayer.canEat(false))
		{
			entityplayer.getFoodStats().addStats(foodHealAmount, foodSaturationAmount);
		}

		if (!world.isRemote && curesEffects)
		{
			entityplayer.curePotionEffects(new ItemStack(Items.milk_bucket));
		}
		
		if (!world.isRemote)
		{
			if (this == LOTRMod.mugMangoJuice)
			{
				LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.drinkMangoJuice);
			}
		}

        return !entityplayer.capabilities.isCreativeMode ? new ItemStack(LOTRMod.mug) : itemstack;
    }
	
	public void applyToNPC(LOTREntityNPC npc, ItemStack itemstack)
	{
		npc.heal((float)foodHealAmount);
	}
}
