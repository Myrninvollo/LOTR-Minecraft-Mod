package lotr.common.item;

import java.util.List;

import lotr.common.LOTRCreativeTabs;
import lotr.common.entity.item.LOTREntityWargskinRug;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRItemWargskinRug extends Item
{
	@SideOnly(Side.CLIENT)
	private IIcon[] rugIcons;
	
	public LOTRItemWargskinRug()
	{
		super();
		setCreativeTab(LOTRCreativeTabs.tabDeco);
		setMaxStackSize(1);
		setMaxDamage(0);
		setHasSubtypes(true);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int i)
    {
        if (i >= rugIcons.length)
		{
			i = 0;
		}
		return rugIcons[i];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconregister)
    {
        rugIcons = new IIcon[4];
        for (int i = 0; i < rugIcons.length; i++)
        {
            rugIcons[i] = iconregister.registerIcon(getIconString() + "_" + i);
        }
    }
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float f, float f1, float f2)
	{
        Block block = world.getBlock(i, j, k);
        if (block == Blocks.snow_layer)
        {
            l = 1;
        }
        else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, i, j, k))
        {
            if (l == 0)
            {
                j--;
            }
            if (l == 1)
            {
                j++;
            }
            if (l == 2)
            {
                k--;
            }
            if (l == 3)
            {
                k++;
            }
            if (l == 4)
            {
                i--;
            }
            if (l == 5)
            {
                i++;
            }
        }
		if (!entityplayer.canPlayerEdit(i, j, k, l, itemstack))
        {
            return false;
        }
		if (world.getBlock(i, j - 1, k).isSideSolid(world, i, j - 1, k, ForgeDirection.UP))
		{
			if (!world.isRemote)
			{
				LOTREntityWargskinRug rug = new LOTREntityWargskinRug(world);
				rug.setLocationAndAngles((double)i + f, (double)j, (double)k + f2, 180F - (entityplayer.rotationYaw % 360F), 0F);
				if (world.checkNoEntityCollision(rug.boundingBox) && world.getCollidingBoundingBoxes(rug, rug.boundingBox).size() == 0 && !world.isAnyLiquid(rug.boundingBox))
				{
					rug.setRugType(itemstack.getItemDamage());
					world.spawnEntityInWorld(rug);
					world.playSoundAtEntity(rug, Blocks.wool.stepSound.func_150496_b(), (Blocks.wool.stepSound.getVolume() + 1F) / 2F, Blocks.wool.stepSound.getPitch() * 0.8F);
					itemstack.stackSize--;
					return true;
				}
				rug.setDead();
			}
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (int j = 0; j <= 3; j++)
        {
            list.add(new ItemStack(item, 1, j));
        }
    }
}
