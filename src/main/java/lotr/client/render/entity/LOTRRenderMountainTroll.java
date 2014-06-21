package lotr.client.render.entity;

import lotr.client.model.LOTRModelTroll;
import lotr.common.entity.npc.LOTREntityMountainTroll;
import lotr.common.entity.projectile.LOTREntityThrownRock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderMountainTroll extends LOTRRenderTroll
{
	private static ResourceLocation skin = new ResourceLocation("lotr:mob/troll/mountainTroll.png");
	private LOTREntityThrownRock heldRock;
	
    public LOTRRenderMountainTroll()
    {
        super();
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return skin;
    }
	
	@Override
	protected void renderTrollWeapon(EntityLivingBase entity, float f)
	{
		if (((LOTREntityMountainTroll)entity).isThrowingRocks())
		{
			if (((LOTRModelTroll)mainModel).onGround <= 0F)
			{
				if (heldRock == null)
				{
					heldRock = new LOTREntityThrownRock(entity.worldObj);
				}
				heldRock.setWorld(entity.worldObj);
				heldRock.setPosition(entity.posX, entity.posY, entity.posZ);
				((LOTRModelTroll)mainModel).rightArm.postRender(0.0625F);
				GL11.glTranslatef(0.375F, 1.5F, 0F);
				GL11.glRotatef(45F, 0F, 1F, 0F);
				preRenderCallback(entity, f, true);
				renderManager.renderEntityWithPosYaw(heldRock, 0D, 0D, 0D, 0F, f);
			}
		}
		else
		{
			((LOTRModelTroll)mainModel).renderWoodenClubWithSpikes(0.0625F);
		}
	}
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		preRenderCallback(entity, f, false);
	}
	
	private void preRenderCallback(EntityLivingBase entity, float f, boolean inverse)
	{
		float scale = getMountainTrollScale();
		if (inverse)
		{
			scale = 1F / scale;
		}
		GL11.glScalef(scale, scale, scale);
	}
	
	protected float getMountainTrollScale()
	{
		return 1.6F;
	}
}
