package lotr.common.entity.npc;

import net.minecraft.item.ItemStack;

public interface LOTRNPCMount
{
	public abstract boolean isMountSaddled();
	
	public abstract boolean getBelongsToNPC();
	
	public abstract void setBelongsToNPC(boolean flag);
	
	public abstract void super_moveEntityWithHeading(float strafe, float forward);

	public abstract String getMountArmorTexture();
	
	public abstract boolean isMountArmorValid(ItemStack itemstack);
}
