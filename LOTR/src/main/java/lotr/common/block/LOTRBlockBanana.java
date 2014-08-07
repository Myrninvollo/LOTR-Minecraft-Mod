package lotr.common.block;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LOTRBlockBanana extends LOTRBlockHangingFruit
{
	public LOTRBlockBanana()
	{
		super();
	}

	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k)
    {
        int dir = world.getBlockMetadata(i, j, k);
        switch (dir)
        {
            case 0:
                setBlockBounds(0.375F, 0.1875F, 0F, 0.625F, 0.9375F, 0.25F);
                break;
            case 1:
                setBlockBounds(0.375F, 0.1875F, 0.75F, 0.625F, 0.9375F, 1F);
                break;
            case 2:
                setBlockBounds(0F, 0.1875F, 0.375F, 0.25F, 0.9375F, 0.625F);
                break;
            case 3:
                setBlockBounds(0.75F, 0.1875F, 0.375F, 1F, 0.9375F, 0.625F);
        }
    }

	@Override
    public Item getItemDropped(int i, Random random, int j)
    {
        return LOTRMod.banana;
    }
	
	@Override
    public boolean removedByPlayer(World world, EntityPlayer entityplayer, int i, int j, int k)
    {
        boolean flag = super.removedByPlayer(world, entityplayer, i, j, k);
        if (flag && !world.isRemote)
        {
        	LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.pickBanana);
        }
        return flag;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public Item getItem(World world, int i, int j, int k)
    {
        return LOTRMod.banana;
    }
}
