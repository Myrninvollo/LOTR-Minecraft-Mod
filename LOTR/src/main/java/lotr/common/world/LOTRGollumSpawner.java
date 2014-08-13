package lotr.common.world;

import lotr.common.LOTRLevelData;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.npc.LOTREntityGollum;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTRGollumSpawner
{
	public static void performSpawning(World world)
	{
		if (LOTRLevelData.gollumSpawned)
		{
			return;
		}
		
		LOTRWaypoint home = LOTRWaypoint.HIGH_PASS;
		
		int x = home.getXCoord();
		int z = home.getZCoord();
		int homeRange = 128;
		
		int i = MathHelper.getRandomIntegerInRange(world.rand, x - homeRange, x + homeRange);
		int j = MathHelper.getRandomIntegerInRange(world.rand, 16, 32);
		int k = MathHelper.getRandomIntegerInRange(world.rand, z - homeRange, z + homeRange);
		
		int checkRange = 16;
		if (world.checkChunksExist(i - checkRange, j - checkRange, k - checkRange, i + checkRange, j + checkRange, k + checkRange))
		{
			AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(i, j, k, i + 1, j + 1, k + 1);
			aabb = aabb.expand(checkRange, checkRange, checkRange);
			if (world.getEntitiesWithinAABB(EntityPlayer.class, aabb).isEmpty())
			{
				Block block = world.getBlock(i, j, k);
				Block below = world.getBlock(i, j - 1, k);
				Block above = world.getBlock(i, j + 1, k);
				
				if (below.isNormalCube() && !block.isNormalCube() && !above.isNormalCube())
				{
					LOTREntityGollum gollum = new LOTREntityGollum(world);
					gollum.setLocationAndAngles(i + 0.5D, j, k + 0.5D, 0F, 0F);
					gollum.isNPCPersistent = true;
					if (gollum.getCanSpawnHere())
					{
						gollum.onSpawnWithEgg(null);
						world.spawnEntityInWorld(gollum);

						LOTRLevelData.gollumSpawned = true;
						LOTRLevelData.markDirty();
					}
				}
			}
		}
	}
}
