package lotr.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class LOTRBlockGoran extends Block
{
	public LOTRBlockGoran()
	{
		super(Material.rock);
		setCreativeTab(null);
	}
	
	@Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float f, float f1, float f2)
    {
		if (!world.isRemote)
		{
			if (!MinecraftServer.getServer().getConfigurationManager().func_152596_g(entityplayer.getGameProfile()))
			{
				return false;
			}
			
			for (int i1 = i - 32; i1 <= i + 32; i1++)
			{
				for (int j1 = j - 32; j1 <= j + 32; j1++)
				{
					for (int k1 = k - 32; k1 <= k + 32; k1++)
					{
						if (world.blockExists(i1, j1, k1) && world.isAirBlock(i1, j1, k1))
						{
							world.setBlock(i1, j1, k1, Blocks.water);
						}
					}
				}
			}
		}
		return true;
    }
}
