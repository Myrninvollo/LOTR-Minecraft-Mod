package lotr.common.item;

import static lotr.common.LOTRFaction.*;

import java.util.List;

import lotr.common.LOTRAlignmentValues;
import lotr.common.LOTRCreativeTabs;
import lotr.common.LOTREventHandler;
import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.entity.item.LOTREntityBanner;
import lotr.common.entity.item.LOTREntityBannerWall;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemBanner extends Item
{
	public static String[] bannerTypes = {"gondor", "rohan", "mordor", "lothlorien", "mirkwood", "dunland", "urukHai", "durin", "angmar", "nearHarad", "highElf", "blueMountains", "ranger"};
	public static LOTRFaction[] factions = {GONDOR, ROHAN, MORDOR, GALADHRIM, WOOD_ELF, DUNLAND, URUK_HAI, DWARF, ANGMAR, NEAR_HARAD, HIGH_ELF, BLUE_MOUNTAINS, RANGER_NORTH};
	@SideOnly(Side.CLIENT)
	private IIcon[] bannerIcons;
	
	public LOTRItemBanner()
	{
		super();
		setCreativeTab(LOTRCreativeTabs.tabDeco);
		setMaxStackSize(16);
		setMaxDamage(0);
		setHasSubtypes(true);
		setFull3D();
	}
	
	public static LOTRFaction getFaction(ItemStack itemstack)
	{
		if (itemstack.getItem() instanceof LOTRItemBanner)
		{
			int i = itemstack.getItemDamage();
			if (i < 0 || i >= factions.length)
			{
				i = 0;
			}
			return factions[i];
		}
		return LOTRFaction.UNALIGNED;
	}
	
	public static int getSubtypeForFaction(LOTRFaction faction)
	{
		for (int i = 0; i < factions.length; i++)
		{
			if (factions[i] == faction)
			{
				return i;
			}
		}
		return 0;
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int i)
    {
        if (i >= bannerIcons.length)
		{
			i = 0;
		}
		return bannerIcons[i];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconregister)
    {
        bannerIcons = new IIcon[bannerTypes.length];
        for (int i = 0; i < bannerIcons.length; i++)
        {
            bannerIcons[i] = iconregister.registerIcon(getIconString() + "_" + bannerTypes[i]);
        }
    }
	
	@Override
    public String getUnlocalizedName(ItemStack itemstack)
    {
        return super.getUnlocalizedName() + "." + itemstack.getItemDamage();
    }
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float f, float f1, float f2)
	{
		if (world.getBlock(i, j, k).isReplaceable(world, i, j, k))
		{
			side = 1;
		}
		else if (side == 1)
		{
			j++;
		}
		
		if (side == 0)
		{
			return false;
		}
		else if (side == 1)
		{
			if (!entityplayer.canPlayerEdit(i, j, k, side, itemstack))
	        {
	            return false;
	        }
			
			if (world.getBlock(i, j - 1, k).isSideSolid(world, i, j - 1, k, ForgeDirection.UP))
			{
				if (!entityplayer.capabilities.isCreativeMode && world.getBlock(i, j - 1, k) == Blocks.gold_block)
				{
					if (LOTRLevelData.getAlignment(entityplayer, getFaction(itemstack)) < 1)
					{
						if (!world.isRemote)
						{
							LOTRAlignmentValues.notifyAlignmentNotHighEnough(entityplayer, 1, getFaction(itemstack));
						}
						return false;
					}
					else
					{
						if (!world.isRemote && LOTREventHandler.isProtectedByBanner(world, i, j, k, entityplayer, false, LOTREntityBanner.PROTECTION_RANGE * 2))
						{
							entityplayer.addChatMessage(new ChatComponentTranslation("chat.lotr.protectedLandBanner"));
							return false;
						}
					}
				}
				
				if (!world.isRemote)
				{
					LOTREntityBanner banner = new LOTREntityBanner(world);
					banner.setLocationAndAngles((double)i + 0.5F, (double)j, (double)k + 0.5F, 90F * (MathHelper.floor_double((double)(entityplayer.rotationYaw * 4F / 360F) + 0.5D) & 3), 0F);
					if (world.checkNoEntityCollision(banner.boundingBox) && world.getCollidingBoundingBoxes(banner, banner.boundingBox).size() == 0 && !world.isAnyLiquid(banner.boundingBox))
					{
						banner.setBannerType(itemstack.getItemDamage());
						banner.allowedPlayers[0] = entityplayer.getUniqueID();
						world.spawnEntityInWorld(banner);
						world.playSoundAtEntity(banner, Blocks.planks.stepSound.func_150496_b(), (Blocks.planks.stepSound.getVolume() + 1F) / 2F, Blocks.planks.stepSound.getPitch() * 0.8F);
						itemstack.stackSize--;
						return true;
					}
					banner.setDead();
				}
			}
		}
		else
		{
			if (!entityplayer.canPlayerEdit(i, j, k, side, itemstack))
            {
                return false;
            }

			int l = Direction.facingToDirection[side];
			LOTREntityBannerWall banner = new LOTREntityBannerWall(world, i, j, k, l);
            if (world.checkNoEntityCollision(banner.boundingBox) && !world.isAnyLiquid(banner.boundingBox) && banner.onValidSurface())
            {
				banner.setBannerType(itemstack.getItemDamage());
                if (!world.isRemote)
                {
                    world.spawnEntityInWorld(banner);
                }
				world.playSoundAtEntity(banner, Blocks.planks.stepSound.func_150496_b(), (Blocks.planks.stepSound.getVolume() + 1F) / 2F, Blocks.planks.stepSound.getPitch() * 0.8F);
                itemstack.stackSize--;
				return true;
            }
			banner.setDead();
			return false;
    
		}
		
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (int j = 0; j < bannerTypes.length; j++)
        {
            list.add(new ItemStack(item, 1, j));
        }
    }
}
