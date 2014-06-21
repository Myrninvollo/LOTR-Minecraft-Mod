package lotr.common.world.biome;


public class LOTRBiomeGenBlueMountainsFoothills extends LOTRBiomeGenBlueMountains
{
	public LOTRBiomeGenBlueMountainsFoothills(int i)
	{
		super(i);

		spawnableGoodList.clear();
		
		setGoodEvilWeight(0, 100);
	}
}
