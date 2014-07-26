package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRWaypoint;
import lotr.common.entity.npc.LOTREntityMordorOrc;
import lotr.common.entity.npc.LOTREntityMordorOrcArcher;
import lotr.common.entity.npc.LOTREntityMordorOrcBombardier;
import lotr.common.entity.npc.LOTREntityMordorSpider;
import lotr.common.world.feature.LOTRWorldGenWebOfUngoliant;
import lotr.common.world.structure.LOTRWorldGenMordorSpiderPit;
import net.minecraft.world.World;

public class LOTRBiomeGenNanUngol extends LOTRBiomeGenMordor
{
	public LOTRBiomeGenNanUngol(int i)
	{
		super(i);

		spawnableEvilList.clear();
		spawnableEvilList.add(new SpawnListEntry(LOTREntityMordorOrc.class, 20, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityMordorOrcArcher.class, 10, 4, 6));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityMordorOrcBombardier.class, 3, 1, 2));
		spawnableEvilList.add(new SpawnListEntry(LOTREntityMordorSpider.class, 100, 4, 4));
		
		decorator.generateCobwebs = false;
		
		decorator.clearRandomStructures();
		decorator.addRandomStructure(new LOTRWorldGenMordorSpiderPit(false), 40);
	}
	
	@Override
	public LOTRAchievement getBiomeAchievement()
	{
		return LOTRAchievement.enterNanUngol;
	}
	
	@Override
	public LOTRWaypoint.Region getBiomeWaypoints()
	{
		return LOTRWaypoint.Region.NAN_UNGOL;
	}
	
	@Override
    public void decorate(World world, Random random, int i, int k)
    {
		super.decorate(world, random, i, k);
		
		for (int l = 0; l < 4; l++)
		{
			int i1 = i + random.nextInt(16) + 8;
			int j1 = random.nextInt(128);
			int k1 = k + random.nextInt(16) + 8;
			new LOTRWorldGenWebOfUngoliant(false, 64).generate(world, random, i1, j1, k1);
		}
	}
	
	@Override
	public float getTreeIncreaseChance()
	{
		return 0.75F;
	}
}
