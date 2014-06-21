package lotr.common;

import net.minecraft.entity.player.EntityPlayer;

public interface LOTRAbstractWaypoint
{
	public abstract int getX();
	public abstract int getY();
	public abstract int getXCoord();
	public abstract int getZCoord();
	public abstract String getDisplayName();
	public abstract boolean hasPlayerUnlocked(EntityPlayer entityplayer);
	public abstract boolean isUnlockable(EntityPlayer entityplayer);
	public abstract int getID();
}