package lotr.common.block;

import java.util.Random;

public class LOTRBlockMorgulTorch extends LOTRBlockTorch
{
	public LOTRBlockMorgulTorch()
	{
		super();
	}
	
	@Override
	protected TorchParticle createTorchParticle(Random random)
	{
		return new TorchParticle("morgulPortal", 0D, 0D, 0D);
	}
}
