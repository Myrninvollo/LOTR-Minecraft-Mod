package lotr.client.render.entity;

import lotr.client.LOTRClientProxy;
import lotr.client.model.LOTRModelTroll;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTREntityTroll;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderTroll extends RenderLiving
{
	private static ResourceLocation trollSkin = new ResourceLocation("lotr:mob/troll/troll.png");
	public static ResourceLocation[] trollOutfits = new ResourceLocation[]
	{
		new ResourceLocation("lotr:mob/troll/outfit_0.png"),
		new ResourceLocation("lotr:mob/troll/outfit_1.png"),
		new ResourceLocation("lotr:mob/troll/outfit_2.png")
	};
	private static ResourceLocation weaponsTexture = new ResourceLocation("lotr:mob/troll/weapons.png");
	private LOTRModelTroll shirtModel = new LOTRModelTroll(1F, 0);
	private LOTRModelTroll trousersModel = new LOTRModelTroll(0.75F, 1);
	
    public LOTRRenderTroll()
    {
        super(new LOTRModelTroll(), 0.5F);
    }
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return trollSkin;
    }
	
	@Override
	public void doRender(EntityLiving entity, double d, double d1, double d2, float f, float f1)
	{
		super.doRender(entity, d, d1, d2, f, f1);
		if (Minecraft.isGuiEnabled() && ((LOTREntityNPC)entity).hiredNPCInfo.getHiringPlayer() == renderManager.livingPlayer)
		{
			if (entity.riddenByEntity == null)
			{
				func_147906_a(entity, "Hired", d, d1 + 1D, d2, 64);
			}
			LOTRClientProxy.renderHealthBar(entity, d, d1 + 1D, d2, 64, renderManager);
		}
	}
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		if (LOTRMod.isAprilFools() || ((LOTREntityTroll)entity).getTrollName().toLowerCase().equals("shrek"))
		{
			GL11.glColor3f(0F, 1F, 0F);
		}
	}
	
	@Override
    protected void renderEquippedItems(EntityLivingBase entity, float f)
    {
        GL11.glColor3f(1F, 1F, 1F);
        super.renderEquippedItems(entity, f);
		GL11.glPushMatrix();
		bindTexture(weaponsTexture);
		renderTrollWeapon(entity, f);
		GL11.glPopMatrix();
    }
	
	protected void renderTrollWeapon(EntityLivingBase entity, float f)
	{
		((LOTRModelTroll)mainModel).renderWoodenClub(0.0625F);
	}

	@Override
    protected int shouldRenderPass(EntityLivingBase entity, int i, float f)
    {
		bindTrollOutfitTexture(entity);
		
        if (i == 0)
		{
			shirtModel.onGround = mainModel.onGround;
			setRenderPassModel(shirtModel);
			return 1;
		}
		else if (i == 1)
		{
			trousersModel.onGround = trousersModel.onGround;
			setRenderPassModel(trousersModel);
			return 1;
		}
		else
		{
			return -1;
		}
    }
	
	protected void bindTrollOutfitTexture(EntityLivingBase entity)
	{
		int j = ((LOTREntityTroll)entity).getTrollOutfit();
		if (j < 0 || j >= trollOutfits.length)
		{
			j = 0;
		}
		bindTexture(trollOutfits[j]);
	}
	
	@Override
	protected void rotateCorpse(EntityLivingBase entity, float f, float f1, float f2)
	{
        if (((LOTREntityTroll)entity).getSneezingTime() > 0)
        {
            f1 += (float)(Math.cos((double)entity.ticksExisted * 3.25D) * Math.PI);
        }

        super.rotateCorpse(entity, f, f1, f2);
	}
}
