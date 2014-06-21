package lotr.compatibility;

public class LOTRModChecker
{
	private static int hasNEI = -1;
	
	public static boolean hasNEI()
	{
		if (hasNEI == -1)
		{
			try
			{
				if (Class.forName("codechicken.nei.api.API") != null)
				{
					hasNEI = 1;
				}
				else
				{
					hasNEI = 0;
				}
			}
			catch (ClassNotFoundException e)
			{
				hasNEI = 0;
			}
		}
		return hasNEI == 1;
	}
}
