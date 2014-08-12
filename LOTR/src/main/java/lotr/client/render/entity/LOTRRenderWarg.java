package lotr.client.render.entity;

import java.util.HashMap;
import java.util.Map;

import lotr.client.LOTRClientProxy;
import lotr.client.model.LOTRModelWarg;
import lotr.common.LOTRMod;
import lotr.common.entity.item.LOTREntityOrcBomb;
import lotr.common.entity.npc.LOTREntityWarg;
import lotr.common.entity.npc.LOTREntityWargBombardier;
import lotr.common.item.LOTRItemMountArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LOTRRenderWarg extends RenderLiving
{
	private static Map wargSkins = new HashMap();
	private static Map wargArmorSkins = new HashMap();
	private static ResourceLocation wargSaddle = new ResourceLocation("lotr:mob/warg/saddle.png");
	private LOTRModelWarg saddleModel = new LOTRModelWarg(0.5F);
	private LOTRModelWarg armorModel = new LOTRModelWarg(0.2F);
	
    public LOTRRenderWarg()
    {
        super(new LOTRModelWarg(), 0.5F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        LOTREntityWarg warg = (LOTREntityWarg)entity;
		String s = String.valueOf(warg.getWargType());
		ResourceLocation r = (ResourceLocation)wargSkins.get(s);
		if (r == null)
		{
			r = new ResourceLocation("lotr:mob/warg/" + s + ".png");
			wargSkins.put(s, r);
		}
		return r;
    }
	
	@Override
	public void doRender(EntityLiving entity, double d, double d1, double d2, float f, float f1)
	{
		if (entity instanceof LOTREntityWargBombardier)
		{
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glPushMatrix();
			GL11.glTranslatef((float)d, (float)d1 + 1.7F, (float)d2);
			GL11.glRotatef(-f, 0F, 1F, 0F);
            int i = entity.getBrightnessForRender(f1);
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1F, (float)k / 1F);
            GL11.glColor4f(1F, 1F, 1F, 1F);
            GL11.glColor4f(1F, 1F, 1F, 1F);
			LOTRRenderEntityOrcBomb bombRenderer = (LOTRRenderEntityOrcBomb)RenderManager.instance.getEntityClassRenderObject(LOTREntityOrcBomb.class);
			bombRenderer.renderBomb(entity, 0D, 0D, 0D, f1, ((LOTREntityWargBombardier)entity).getBombFuse(), ((LOTREntityWargBombardier)entity).getBombStrengthLevel(), 0.75F, 1F);
			GL11.glPopMatrix();
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glColor4f(1F, 1F, 1F, 1F);
		}
		super.doRender(entity, d, d1, d2, f, f1);
		if (Minecraft.isGuiEnabled() && ((LOTREntityWarg)entity).hiredNPCInfo.getHiringPlayer() == renderManager.livingPlayer)
		{
			if (entity.riddenByEntity == null)
			{
				func_147906_a(entity, "Hired", d, d1 + 0.5D, d2, 64);
			}
			LOTRClientProxy.renderHealthBar(entity, d, d1 + 0.5D, d2, 64, renderManager);
		}
	}

	@Override
    protected int shouldRenderPass(EntityLivingBase entity, int pass, float f)
    {
		if (pass == 3)
		{
			if (((LOTREntityWarg)entity).isMountSaddled())
			{
				bindTexture(wargSaddle);
				setRenderPassModel(saddleModel);
				return 1;
			}
			return super.shouldRenderPass(entity, pass, f);
		}
		else if (pass == 2)
		{
			ItemStack itemstack = ((LOTREntityWarg)entity).getEquipmentInSlot(4);
			if (itemstack != null)
			{
				Item item = itemstack.getItem();
				if (item instanceof LOTRItemMountArmor)
				{
					String s = ((LOTRItemMountArmor)item).getArmorTexture();
					ResourceLocation r = (ResourceLocation)wargArmorSkins.get(s);
					if (r == null)
					{
						r = new ResourceLocation(s);
						wargArmorSkins.put(s, r);
					}
					bindTexture(r);
					
					setRenderPassModel(armorModel);

					GL11.glColor3f(1F, 1F, 1F);

					if (itemstack.isItemEnchanted())
					{
						return 15;
					}
					else
					{
						return 1;
					}
				}
			}
		}
		return super.shouldRenderPass(entity, pass, f);
    }
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		if (LOTRMod.isAprilFools())
		{
			GL11.glRotatef(45F, 0F, 0F, 1F);
			GL11.glRotatef(-30F, 1F, 0F, 0F);
		}
	}
}
