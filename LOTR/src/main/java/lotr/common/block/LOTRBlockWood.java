package lotr.common.block;

import lotr.common.LOTRFaction;
import lotr.common.LOTRLevelData;
import lotr.common.entity.npc.LOTREntityElfWarrior;
import lotr.common.entity.npc.LOTRSpeech;
import lotr.common.world.biome.LOTRBiomeGenLothlorien;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class LOTRBlockWood extends LOTRBlockWoodBase
{
    public LOTRBlockWood()
    {
        super();
		setWoodNames("shirePine", "mallorn", "mirkOak", "charred");
    }
	
	@Override
    public boolean removedByPlayer(World world, EntityPlayer entityplayer, int i, int j, int k, boolean willHarvest)
    {
        if (!world.isRemote && (world.getBlockMetadata(i, j, k) & 3) == 1 && world.rand.nextInt(3) == 0 && world.getBiomeGenForCoords(i, k) instanceof LOTRBiomeGenLothlorien && LOTRLevelData.getAlignment(entityplayer, LOTRFaction.GALADHRIM) < 0 && !entityplayer.capabilities.isCreativeMode)
		{
			int elves = 4 + world.rand.nextInt(3);
			boolean sentMessage = false;
			for (int l = 0; l < elves; l++)
			{
				LOTREntityElfWarrior elfWarrior = new LOTREntityElfWarrior(world);
				int i1 = MathHelper.floor_double(entityplayer.posX) - 6 + world.rand.nextInt(12);
				int k1 = MathHelper.floor_double(entityplayer.posZ) - 6 + world.rand.nextInt(12);
				int j1 = world.getTopSolidOrLiquidBlock(i1, k1);
				if (world.getBlock(i1, j1 - 1, k1).isSideSolid(world, i1, j1 - 1, k1, ForgeDirection.UP) && !world.getBlock(i1, j1, k1).isNormalCube() && !world.getBlock(i1, j1 + 1, k1).isNormalCube())
				{
					elfWarrior.setLocationAndAngles(i1 + 0.5D, j1, k1 + 0.5D, 0F, 0F);
					if (elfWarrior.getCanSpawnHere())
					{
						elfWarrior.spawnRidingHorse = false;
						elfWarrior.onSpawnWithEgg(null);
						world.spawnEntityInWorld(elfWarrior);
						elfWarrior.isDefendingTree = true;
						elfWarrior.setAttackTarget(entityplayer);
						if (!sentMessage)
						{
							elfWarrior.sendSpeechBank(entityplayer, "elfWarrior_defendTrees");
							sentMessage = true;
						}
					}
				}
			}
		}
		return super.removedByPlayer(world, entityplayer, i, j, k, willHarvest);
    }
}
