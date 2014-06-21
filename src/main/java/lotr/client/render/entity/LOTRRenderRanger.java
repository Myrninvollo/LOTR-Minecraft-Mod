package lotr.client.render.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lotr.client.model.LOTRModelBiped;
import lotr.common.entity.npc.LOTREntityRanger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class LOTRRenderRanger extends LOTRRenderBiped
{
	private static List rangerSkins;
	private static Map capes = new HashMap();
	
	public LOTRRenderRanger()
	{
		super(new LOTRModelBiped(), 0.5F);
		rangerSkins = LOTRRandomSkins.loadSkinsList("lotr:mob/ranger/ranger");
	}
	
	@Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
		return LOTRRandomSkins.getRandomSkin(rangerSkins, entity);
    }
	
	private void doRangerInvisibility()
	{
		GL11.glColor4f(1F, 1F, 1F, 0.15F);
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
	}
	
	@Override
	protected void preRenderCallback(EntityLivingBase entity, float f)
	{
		if (((LOTREntityRanger)entity).isRangerSneaking())
		{
			doRangerInvisibility();
		}
	}
	
	@Override
	protected int shouldRenderPass(EntityLivingBase entity, int i, float f)
	{
		int j = super.shouldRenderPass(entity, i, f);
		if (j > 0 && ((LOTREntityRanger)entity).isRangerSneaking())
		{
			doRangerInvisibility();
		}
		return j;
	}
	
	@Override
	protected void renderEquippedItems(EntityLivingBase entity, float f)
	{
		ResourceLocation cape = loadRangerCape((LOTREntityRanger)entity);
		setCapeTexture(cape);
		
		if (((LOTREntityRanger)entity).isRangerSneaking())
		{
			doRangerInvisibility();
		}
		
		super.renderEquippedItems(entity, f);
		
		if (((LOTREntityRanger)entity).isRangerSneaking())
		{
			doRangerInvisibility();
		}
		
		if (((LOTREntityRanger)entity).isRangerSneaking())
		{
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			GL11.glDepthMask(true);
		}
	}
	
	private ResourceLocation loadRangerCape(LOTREntityRanger ranger)
	{
		String s = ranger.getRangerCape();
		if (s != null)
		{
			ResourceLocation r = (ResourceLocation)capes.get(s);
			if (r == null)
			{
				r = new ResourceLocation("lotr:mob/ranger/" + s + ".png");
				capes.put(s, r);
			}
			bindTexture(r);
			return r;
		}
		return null;
	}
}
