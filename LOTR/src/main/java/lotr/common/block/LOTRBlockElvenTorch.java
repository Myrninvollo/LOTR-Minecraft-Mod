package lotr.common.block;

import java.util.Random;

public class LOTRBlockElvenTorch extends LOTRBlockTorch
{
	public LOTRBlockElvenTorch()
	{
		super();
	}
	
	@Override
	protected TorchParticle createTorchParticle(Random random)
	{
		if (random.nextInt(3) == 0)
		{
			return new TorchParticle("elvenGlow", 0D, 0D, 0D);
		}
		return null;
	}
}
