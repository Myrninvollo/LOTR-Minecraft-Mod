package lotr.common.entity.npc;

public interface LOTRNPCMount
{
	public abstract boolean getBelongsToNPC();
	
	public abstract void setBelongsToNPC(boolean flag);
	
	public abstract void setNavigatorRangeFrom(LOTREntityNPC npc);
}
