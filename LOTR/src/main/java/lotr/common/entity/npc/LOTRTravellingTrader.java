package lotr.common.entity.npc;

import net.minecraft.entity.player.EntityPlayer;
import lotr.common.world.biome.LOTRBiome;

public interface LOTRTravellingTrader
{
	public abstract void startTraderVisiting(EntityPlayer entityplayer);
	
	public abstract LOTREntityNPC createTravellingEscort();
	
	public abstract String getDepartureSpeech();
}
