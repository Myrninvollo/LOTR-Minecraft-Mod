package lotr.common.world;

import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraft.world.storage.WorldInfo;

public class LOTRDerivedWorldInfo extends DerivedWorldInfo
{
	private WorldInfo parentWorldInfo;
	
	public LOTRDerivedWorldInfo(WorldInfo worldinfo)
	{
		super(worldinfo);
		parentWorldInfo = worldinfo;
	}
	
	@Override
	public void setWorldTime(long time)
	{
		if (time - getWorldTime() < 20)
		{
			return;
		}
		parentWorldInfo.setWorldTime(time);
		System.out.println("Set LOTR world time to " + time);
	}
}
