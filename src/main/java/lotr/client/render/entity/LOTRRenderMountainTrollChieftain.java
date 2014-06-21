package lotr.client.render.entity;

import lotr.client.model.LOTRModelTroll;
import lotr.common.entity.npc.LOTREntityMountainTrollChieftain;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderMountainTrollChieftain extends LOTRRenderMountainTroll
{
	private static ResourceLocation armorTexture = new ResourceLocation("lotr:mob/troll/mountainTrollChieftain_armor.png");
	private LOTRModelTroll helmetModel = new LOTRModelTroll(1.5F, 2);
	private LOTRModelTroll chestplateModel = new LOTRModelTroll(1.5F, 3);
	
    public LOTRRenderMountainTrollChieftain()
    {
        super();
    }
	
	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
	{
		super.doRender(entity, d, d1, d2, f, f1);
		
		LOTREntityMountainTrollChieftain troll = (LOTREntityMountainTrollChieftain)entity;
		if (Minecraft.getMinecraft().theWorld.loadedEntityList.contains(troll))
		{
			BossStatus.setBossStatus(troll, false);
		}
	}
	
	@Override
    protected int shouldRenderPass(EntityLivingBase entity, int i, float f)
    {
		LOTREntityMountainTrollChieftain troll = (LOTREntityMountainTrollChieftain)entity;
		bindTexture(armorTexture);
		if (i == 2 && troll.getTrollArmorLevel() >= 2)
		{
			helmetModel.onGround = mainModel.onGround;
			setRenderPassModel(helmetModel);
			return 1;
		}
		else if (i == 3 && troll.getTrollArmorLevel() >= 1)
		{
			chestplateModel.onGround = mainModel.onGround;
			setRenderPassModel(chestplateModel);
			return 1;
		}
		else
		{
			return super.shouldRenderPass(entity, i, f);
		}
    }

	@Override
	protected float getMountainTrollScale()
	{
		return 2F;
	}
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		super.preRenderCallback(entity, f);
		GL11.glTranslatef(0F, -((LOTREntityMountainTrollChieftain)entity).getSpawningOffset(f), 0F);
	}
}
