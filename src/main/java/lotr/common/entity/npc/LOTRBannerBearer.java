package lotr.common.entity.npc;

import lotr.common.LOTRFaction;
import net.minecraft.item.Item;

public interface LOTRBannerBearer
{
	public abstract LOTRFaction getFaction();
	
	public abstract Item getWeaponItem();
}
