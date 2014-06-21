package lotr.common.world;

import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.item.LOTREntityPortal;
import lotr.common.item.LOTRItemPouch;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class LOTRTeleporter extends Teleporter
{
	private WorldServer world;
	
    public LOTRTeleporter(WorldServer worldserver)
    {
		super(worldserver);
		world = worldserver;
    }

	@Override
    public void placeInPortal(Entity entity, double d, double d1, double d2, float f)
    {
		int i;
		int j;
		int k;
		
		if (world.provider.dimensionId == LOTRMod.idDimension)
		{
			i = 0;
			k = 0;
			j = world.getTopSolidOrLiquidBlock(i, k);
		}
		else
		{
			i = LOTRLevelData.overworldPortalX;
			k = LOTRLevelData.overworldPortalZ;
			j = LOTRLevelData.overworldPortalY;
		}
		
		entity.setLocationAndAngles((double)i + 0.5D, (double)j + 1D, (double)k + 0.5D, entity.rotationYaw, 0.0F);

		if (world.provider.dimensionId == LOTRMod.idDimension && LOTRLevelData.madeMiddleEarthPortal == 0)
		{
			LOTRLevelData.setMadeMiddleEarthPortal(1);
			world.provider.setSpawnPoint(i, j, k);
			Entity portal = new LOTREntityPortal(world);
			portal.setLocationAndAngles(i + 0.5D, j + 3.5D, k + 0.5D, 0F, 0F);
			world.spawnEntityInWorld(portal);
			
			int i1 = i - world.rand.nextInt(8) + world.rand.nextInt(8);
			int k1 = k - world.rand.nextInt(8) + world.rand.nextInt(8);
			int j1 = world.getTopSolidOrLiquidBlock(i1, k1);
			
			world.setBlock(i1, j1, k1, Blocks.chest, 0, 3);
			TileEntityChest chest = (TileEntityChest)world.getTileEntity(i1, j1, k1);
			if (chest != null)
			{
				for (int l = 0; l < 3; l++)
				{
					ItemStack item = new ItemStack(LOTRMod.pouch, 1, LOTRItemPouch.getRandomPouchSize(world.rand));
					chest.setInventorySlotContents(world.rand.nextInt(chest.getSizeInventory()), item);
				}
			}
		}
    }
}
