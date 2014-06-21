package lotr.client.render.entity;

import java.util.List;

import lotr.client.model.LOTRModelOrc;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityOrc;
import lotr.common.entity.npc.LOTREntityUrukHai;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderOrc extends LOTRRenderBiped
{
	private static List orcSkins;
	private static List urukSkins;
	private static ResourceLocation staffTexture = new ResourceLocation("lotr:mob/orc/mercenaryCaptain_staff.png");
	
	public LOTRRenderOrc()
	{
		super(new LOTRModelOrc(), 0.5F);
		orcSkins = LOTRRandomSkins.loadSkinsList("lotr:mob/orc/orc");
		urukSkins = LOTRRandomSkins.loadSkinsList("lotr:mob/orc/urukHai");
	}
	
	@Override
	protected void func_82421_b()
    {
        field_82423_g = new LOTRModelOrc(1.0F);
        field_82425_h = new LOTRModelOrc(0.5F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        if (entity instanceof LOTREntityUrukHai)
		{
			return LOTRRandomSkins.getRandomSkin(urukSkins, entity);
		}
        return LOTRRandomSkins.getRandomSkin(orcSkins, entity);
    }
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		if (!((LOTREntityOrc)entity).isWeakOrc)
		{
			return;
		}
		GL11.glScalef(0.85F, 0.85F, 0.85F);
	}
	
	@Override
    protected void renderEquippedItems(EntityLivingBase entity, float f)
    {
        GL11.glColor3f(1F, 1F, 1F);
        super.renderEquippedItems(entity, f);
		
		if (((LOTREntityOrc)entity).renderOrcSkullStaff())
		{
			GL11.glPushMatrix();
			bindTexture(staffTexture);
			((LOTRModelOrc)mainModel).renderMercenaryCaptainStaff(0.0625F);
			GL11.glPopMatrix();
		}
    }
	
	@Override
	public ItemStack getHeldItemLeft(EntityLivingBase entity)
	{
        if (((LOTREntityOrc)entity).isBombardier && entity.getHeldItem() != null && entity.getHeldItem().getItem() == LOTRMod.orcTorchItem)
        {
			return new ItemStack(LOTRMod.orcBomb, 1, ((LOTREntityOrc)entity).getBombStrength());
		}
		return super.getHeldItemLeft(entity);
	}
}
