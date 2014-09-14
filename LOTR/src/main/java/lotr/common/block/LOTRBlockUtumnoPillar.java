package lotr.common.block;

import lotr.common.world.LOTRWorldProviderUtumno.UtumnoBlock;

public class LOTRBlockUtumnoPillar extends LOTRBlockPillarBase implements UtumnoBlock
{
	public LOTRBlockUtumnoPillar()
	{
		super();
		setPillarNames("fire", "ice", "obsidian");
	}
}
