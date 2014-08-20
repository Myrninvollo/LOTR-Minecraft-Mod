package lotr.common;

import net.minecraft.util.ResourceLocation;

public class LOTRCapes
{
	public static ResourceLocation GONDOR = forName("gondor");
	public static ResourceLocation TOWER_GUARD = forName("gondorTowerGuard");
	public static ResourceLocation RANGER = forName("ranger");
	public static ResourceLocation ROHAN = forName("rohan");
	public static ResourceLocation GALADHRIM = forName("galadhrim");
	public static ResourceLocation ELVEN_TRADER = forName("galadhrimTrader");
	public static ResourceLocation WOOD_ELF = forName("woodElf");
	public static ResourceLocation HIGH_ELF = forName("highElf");
	public static ResourceLocation NEAR_HARAD = forName("nearHarad");
	
	private static ResourceLocation forName(String s)
	{
		return new ResourceLocation("lotr:cape/" + s + ".png");
	}
}
